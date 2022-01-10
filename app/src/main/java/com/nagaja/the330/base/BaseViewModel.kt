package com.nagaja.the330.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
//import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    val showMessCallback = MutableLiveData<String>()

    val callbackStart = MutableLiveData<Unit>()
    val callbackSuccess = MutableLiveData<Unit>()
    val callbackFail = MutableLiveData<Unit>()
    val callbackErrorCode = MutableLiveData<Int>()

//    fun handleError(it: Throwable) {
//        try {
//            callbackFail.value = Unit
//            val response = (it as HttpException).response()
//            val gson = GsonBuilder().create()
//            val mError: ErrorModel
//            try {
//                Log.e("ERROR", it.toString())
//                mError = gson.fromJson(
//                    response?.errorBody()?.string(),
//                    ErrorModel::class.java
//                )
//                if (BuildConfig.DEBUG) {
//                    showMessCallback.value = "[CODE - ${
//                        it.code()
//                    }] ${mError.status} \n" +
//                            "error code: ${mError.errorCode}\n" +
//                            "message: ${mError?.message}"
//                }
//                callbackErrorCode.value = mError?.errorCode
//                when (response?.code()) {
//                    400 -> {
//                        if (mError?.errorCode == 1007) {
//                            EventBus.getDefault().post(LockAccountEventModel())
//                        }
//                    }
//                    401 -> {
//                        Log.e("EROR", "401")
//                        EventBus.getDefault().post(ExpriteEventModel())
//                    }
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        } catch (e: Exception) {
//            EventBus.getDefault().post(NetworkCheck())
//            Log.e("Neo", e.toString())
//        }
//
//    }
//
//    fun handleError2(it: retrofit2.Response<*>?) {
//        try {
//            Log.e("ERROR", it.toString())
//            callbackFail.value = Unit
//            val gson = GsonBuilder().create()
//            val mError: ErrorModel
//            try {
//                mError = gson.fromJson(
//                    it?.errorBody()?.string(),
//                    ErrorModel::class.java
//                )
//                if (BuildConfig.DEBUG) {
//                    showMessCallback.value = "[CODE - ${
//                        it?.code().toString()
//                    }] ${mError.status} \nerror code: ${mError.errorCode}\nmessage: ${mError?.message}"
//                }
//                callbackErrorCode.value = mError?.errorCode
//                when (it?.code()) {
//                    400 -> {
//                        if (mError?.errorCode == 1007) {
//                            EventBus.getDefault().post(LockAccountEventModel())
//                        }
//                    }
//                    401 -> {
//                        EventBus.getDefault().post(ExpriteEventModel())
//                    }
//                }
//            } catch (e: IOException) {
//            }
//        } catch (e: Exception) {
//            EventBus.getDefault().post(NetworkCheck())
//            Log.e("Neo", e.toString())
//        }
//
//    }
}