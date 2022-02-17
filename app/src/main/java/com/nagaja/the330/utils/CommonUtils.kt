package com.nagaja.the330.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nagaja.the330.R
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object CommonUtils {
    private const val TAG = "CommonUtils"
    const val min_length_phone = 10
    const val max_length_phone = 11
    private var alertDialog: AlertDialog? = null
    val askFirstAppPermission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA
    )
    val takePicturePermission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    const val REQUEST_PERMISSION_KEY = 1

    fun getQueryMap(query: String): Map<String, String> {
        val params = query.split("&").toTypedArray()
        val map: MutableMap<String, String> = HashMap()
        for (param in params) {
            val name = param.split("=").toTypedArray()[0]
            val value = param.split("=").toTypedArray()[1]
            map[name] = value
        }
        return map
    }

    fun getHashTags(str: String?): List<String> {
        val MY_PATTERN = Pattern.compile("#(\\S+)")
        val mat = MY_PATTERN.matcher(str)
        val strs: MutableList<String> = ArrayList()
        while (mat.find()) {
            strs.add(mat.group(1))
        }
        return strs
    }

    fun checkSpecial(str: String?): Boolean {
        val MY_PATTERN = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
        val matcher = MY_PATTERN.matcher(str)
        return matcher.find()
    }

    fun changeColorTextView(context: Context?, tv: TextView, color: Int) {
        tv.setTextColor(
            ContextCompat.getColor(
                context!!,
                color
            )
        )
    }

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String?): Boolean {
        val pattern: Pattern
        val EMAIL_PATTERN = "^[a-zA-Z][a-zA-Z0-9_.]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPasswordValid(email: String?): Boolean {
        val pattern: Pattern
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,15}$"
        pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun showLoadingDialog(context: Context?): ProgressDialog? {
        try {
            val progressDialog = ProgressDialog(context, R.style.NewDialog)
            progressDialog.show()
            if (progressDialog.window != null) {
                progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            progressDialog.setContentView(R.layout.progress_dialog)
            progressDialog.isIndeterminate = true
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            return progressDialog
        } catch (e: Exception) {
        }
        return null
    }

//    fun showBottomDialog(
//        activity: Activity?,
//        list: MutableList<KeyValueModel>,
//        title: String? = null,
//        textOk: String? = null,
//        callback: ((KeyValueModel) -> Unit)?
//    ) {
//        if (activity != null && !activity.isFinishing) {
//            val binding = DialogChooseBinding.inflate(LayoutInflater.from(activity))
//            AlertDialog.Builder(activity, R.style.AlertDialogNoBG).run {
//                setView(binding.root)
//                create().also { alertDialog = it }
//            }
//
//            alertDialog?.show()
//
//            val mAdapter = DialogListAdapter(list)
//            val linearLayoutManager =
//                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//            binding.rvList.apply {
//                layoutManager = linearLayoutManager
//                adapter = mAdapter
//            }
//            mAdapter.onItemClick = {
//                alertDialog?.dismiss()
//                callback?.invoke(it)
//            }
//
//            if (!title.isNullOrEmpty()) {
//                binding.tvTitle.apply {
//                    makeVisible()
//                    text = title
//                }
//            }
//
//            if (!textOk.isNullOrEmpty()) {
//                binding.tvOk.apply {
//                    makeVisible()
//                    text = textOk
//                }
//                binding.tvOk.setOnClickListener {
//                    alertDialog?.dismiss()
//                }
//            }
//
//
//            val window = alertDialog!!.window
//            val wlp = window!!.attributes
//            wlp.gravity = Gravity.BOTTOM
//            wlp.y = 50
//            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS.inv()
//            window.attributes = wlp
//        }
//    }


    fun priceWithoutDecimal(price: Double?): String {
        val formatter = DecimalFormat(
            "###,###,###.##",
            DecimalFormatSymbols(Locale.US)
        )
        //        num = num.replace(".", ",");
        return formatter.format(price)
    }

    fun getIntFromStringDecimal(decimal: String): Int {
        val num = decimal.replace(",", "").replace(".", "")
        return num.toInt()
    }

    fun getFloatFromStringDecimal(decimal: String): Float {
        val num = decimal.replace(",", ".")
        return num.toFloat()
    }

    fun aroundRating(oldRating: Float): Float {
        return when {
            oldRating <= 0.4 -> {
                0f
            }
            oldRating <= 0.9 -> {
                0.5f
            }
            oldRating <= 1.4 -> {
                1f
            }
            oldRating <= 1.9 -> {
                1.5f
            }
            oldRating <= 2.4 -> {
                2f
            }
            oldRating <= 2.9 -> {
                2.5f
            }
            oldRating <= 3.4 -> {
                3f
            }
            oldRating <= 3.9 -> {
                3.5f
            }
            oldRating <= 4.4 -> {
                4f
            }
            oldRating <= 4.9 -> {
                4.5f
            }
            else -> 5f
        }
    }


    /***
     * Xóa .0 đằng sau dấu chấm
     * ví dụ:
     * 123.0 stripTrailingZeros >> 123
     * 123.00 stripTrailingZeros >> 123
     * 121212 stripTrailingZeros >> 121212
     * 121212.02 stripTrailingZeros >> 121212.02
     * 12. stripTrailingZeros >> 12
     */
    fun stripTrailingZero(weight: String): String {
        return try {
            val bd = BigDecimal(weight)
            bd.stripTrailingZeros().toPlainString()
        } catch (e: Exception) {
            weight
        }
    }

    private const val EOF = -1
    private const val DEFAULT_BUFFER_SIZE = 1024 * 4
    fun expand(v: View) {
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Collapse speed of 1dp/ms
        a.setDuration((initialHeight / v.context.resources.displayMetrics.density).toLong())
        v.startAnimation(a)
    }

    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}