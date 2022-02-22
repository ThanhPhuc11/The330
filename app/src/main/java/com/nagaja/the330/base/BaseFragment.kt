package com.nagaja.the330.base

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.gson.Gson
import com.nagaja.the330.data.DataStorePref.Companion.AUTH_TOKEN
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.CommonUtils
import com.nagaja.the330.utils.RealPathUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

abstract class BaseFragment : Fragment() {
    private var mProgressDialog: ProgressDialog? = null
    var accessToken: String? = null

    private var mActivity: BaseActivity? = null
    var viewController: ViewController? = null
    var childViewController: ViewController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
//            context.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAccessToken()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SetupViewModel()
                UIData()
            }
        }
    }

    @Composable
    abstract fun SetupViewModel()

    @Composable
    abstract fun UIData()

    fun getViewModelProvider(owner: ViewModelStoreOwner): ViewModelProvider {
        return ViewModelProvider(
            owner,
            ViewModelFactory(
                RetrofitBuilder.getInstance(requireContext())
                    ?.create(ApiService::class.java)!!
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
    }

    open fun hideKeyboard() {
        if (mActivity != null) {
            mActivity!!.hideKeyboard()
        }
    }

    open fun showKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    open fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this.context)
    }

    open fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    open fun showMess(str: String?) {
        Toast.makeText(context, str ?: "", Toast.LENGTH_LONG).show()
    }

    open fun showMessDEBUG(str: String?) {
        Toast.makeText(context, str ?: "", Toast.LENGTH_LONG).show()
    }

    open fun getAccessToken() {
        CoroutineScope(Dispatchers.IO).launch {
            requireContext().dataStore.data.map { get ->
                get[AUTH_TOKEN] ?: ""
            }.collect {
                val tokenModel = Gson().fromJson(it, AuthTokenModel::class.java)
                accessToken = formatToken(tokenModel?.accessToken)
                Log.e("TOKEN", accessToken!!)
            }
        }
    }

    open fun formatToken(token: String?): String {
        if (TextUtils.isEmpty(token)) {
//            showMess("Token Empty!")
            return ""
        }
        return "Bearer $token"
    }

    open fun checkPermissBeforeAttachFile(context: Context, content: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!CommonUtils.hasPermissions(context, CommonUtils.takePicturePermission)) {
                callbackPermission.launch(CommonUtils.takePicturePermission)
                return
            }
            content.invoke()
        }
    }

    open fun copyFileOnlyAndroid10(fileUri: Uri?): Uri? {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            val f1 = File(RealPathUtil.getPath(activity, fileUri))
            val dir = requireContext().getExternalFilesDir("MY_MEDIA")
            if (!dir!!.exists()) {
                dir.mkdirs()
            }
            val savePath = dir.absolutePath
            val tempFile = File(savePath, "copy_${f1.name}")
            fileUri?.let { returnUri ->
                val parcelFileDescriptor =
                    context?.contentResolver?.openFileDescriptor(returnUri, "r", null)

                parcelFileDescriptor?.let {
                    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                    val outputStream = FileOutputStream(tempFile)
                    IOUtils.copy(inputStream, outputStream)
                }
            }
            return Uri.fromFile(tempFile)
        } else {
            return fileUri
        }
    }

    fun backSystemHandler(handle: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handle.invoke()
                }
            }
        )
    }

    val callbackPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.e("DEBUG", "${it.key} = ${it.value}")
            }
        }
}