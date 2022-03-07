package com.nagaja.the330.view.freenoticeregis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.NameUtils
import com.nagaja.the330.utils.RealPathUtil
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.freenoticedetail.FreeNoticeDetailFragment
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

class FreeNoticeRegisFragment : BaseFragment() {
    private lateinit var viewModel: FreeNoticeRegisVM
    private var userDetail: UserDetail? = null
    private var callbackListImage: ((Uri?) -> Unit)? = null
    private var onClickRemove: ((Int) -> Unit)? = null

    companion object {
        fun newInstance() = FreeNoticeRegisFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FreeNoticeRegisVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.callbackPostSuccess.observe(viewLifecycleOwner) {
            showMessDEBUG("Success")
            viewController?.pushFragment(
                ScreenId.SCREEN_FREE_NOTICE_DETAIL,
                FreeNoticeDetailFragment.newInstance(it)
            )
        }
        viewModel.callbackStart.observe(viewLifecycleOwner) {
            showLoading()
        }
        viewModel.callbackSuccess.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.callbackFail.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.showMessCallback.observe(viewLifecycleOwner) {
            showMess(it)
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        getUserDetailFromDataStore(requireContext())
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        LayoutTheme330 {
            Header(stringResource(R.string.registration)) {
                viewController?.popFragment()
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                TextFieldCustom(
                    modifier = Modifier.padding(16.dp),
                    hint = stringResource(R.string.enter_title_for_your_post),
                    textStateId = viewModel.stateEdtTitle
                )
                Text(
                    stringResource(R.string.company_basic_information),
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp)
                )

                //TODO: Company Name
                Text(
                    stringResource(R.string.company_name_2),
                    style = text14_222,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp)
                )
                TextFieldCustom(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 16.dp),
                    textStateId = viewModel.stateEdtCompanyName
                )

                //TODO: Company Address
                Text(
                    stringResource(R.string.address),
                    style = text14_222,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp)
                )
                TextFieldCustom(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 16.dp),
                    textStateId = viewModel.stateEdtCompanyAddress
                )

                //TODO: Company Contact
                Text(
                    stringResource(R.string.contact),
                    style = text14_222,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp)
                )
                TextFieldCustom(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 16.dp),
                    textStateId = viewModel.stateEdtCompanyContact
                )

                //TODO: Choose image
                Row(Modifier.padding(vertical = 8.dp)) {
                    val listImage = remember {
                        mutableStateListOf<FileModel>()
                    }
                    callbackListImage = { uri ->
                        val fileTemp = File(
                            RealPathUtil.getPath(
                                this@FreeNoticeRegisFragment.requireContext(),
                                uri
                            )
                        )
                        listImage.add(FileModel(url = uri.toString()))
                        viewModel.listImage.add(
                            FileModel(
                                fileName = NameUtils.setFileName(userDetail?.id, fileTemp),
                                url = fileTemp.path
                            )
                        )
                    }
                    val count = remember { mutableStateOf(listImage.size) }
                    LaunchedEffect(listImage.size) {
                        count.value = listImage.size
                    }
                    RepresentativeImage(count) {
                        if (count.value == 10) return@RepresentativeImage
                        checkPermissBeforeAttachFile(this@FreeNoticeRegisFragment.requireContext()) {
                            val intent = Intent(Intent.ACTION_PICK).apply {
                                setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*"
                                )
                            }
                            startResultRepresentativeCallback.launch(intent)
                        }
                    }
                    onClickRemove = { index ->
                        listImage.removeAt(index)
                        viewModel.listImage.removeAt(index)
                    }
                    LazyRow {
                        itemsIndexed(listImage) { index, obj ->
                            ItemImagePicked(index, obj)
                        }
                    }
                }

                //TODO: Body
                val stateEdtBody = viewModel.stateEdtBody
                Box(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 100.dp, start = 16.dp, end = 16.dp)
                        .height(180.dp)
                        .background(ColorUtils.white_FFFFFF)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = ColorUtils.gray_E1E1E1,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    BasicTextField(
                        value = stateEdtBody.value,
                        onValueChange = {
                            if (it.text.length <= 5000) stateEdtBody.value = it
                        },
                        Modifier
                            .fillMaxWidth(),
                        textStyle = TextStyle(
                            color = ColorUtils.black_000000
                        ),
                        decorationBox = { innerTextField ->
                            Row {
                                if (stateEdtBody.value.text.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.enter_body_of_your_post),
                                        color = ColorUtils.gray_BEBEBE,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            innerTextField()
                        }
                    )
                }
            }

            //TODO: Button post
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(ColorUtils.gray_222222)
                        .noRippleClickable {
                            viewController?.popFragment()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(ColorUtils.blue_2177E4)
                        .noRippleClickable {
                            accessToken?.let { viewModel.makePostNotice(it) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.register),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    @Composable
    private fun RepresentativeImage(count: MutableState<Int>, onClick: () -> Unit) {
        Column(
            Modifier
                .padding(start = 16.dp, top = 4.dp, end = 8.dp)
                .size(72.dp)
                .border(width = 1.dp, color = ColorUtils.blue_2177E4.copy(alpha = 0.3f))
                .noRippleClickable {
                    onClick.invoke()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(R.drawable.ic_choose_image), contentDescription = null)
            Text(
                "${count.value}/10",
                color = ColorUtils.blue_2177E4,
                modifier = Modifier
                    .padding(top = 3.dp)
                    .alpha(0.3f),
                fontSize = 11.sp
            )
        }
    }

    @Composable
    private fun ItemImagePicked(position: Int, obj: FileModel) {
        ConstraintLayout(
            Modifier
                .padding(end = 4.dp)
                .size(76.dp)
        ) {
            val (content, close) = createRefs()
            GlideImage(
                imageModel = obj.url,
                contentDescription = "",
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
                requestOptions = {
                    RequestOptions()
                        .override(360, 360)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                },
                modifier = Modifier
                    .size(72.dp)
                    .constrainAs(content) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Image(
                painter = painterResource(R.drawable.ic_close_round),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(close) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .noRippleClickable {
                        onClickRemove?.invoke(position)
                    }
            )
        }
    }

    private fun getUserDetailFromDataStore(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { get ->
                get[DataStorePref.USER_DETAIL] ?: ""
            }.collect {
                val userDetail = Gson().fromJson(it, UserDetail::class.java)
                userDetail?.let { this@FreeNoticeRegisFragment.userDetail = userDetail }
            }
        }
    }

    private val startResultRepresentativeCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = copyFileOnlyAndroid10(data?.data)
                callbackListImage?.invoke(uri)
            }
        }
}