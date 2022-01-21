package com.nagaja.the330.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.view.general.GeneralRepository
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.login.LoginRepository
import com.nagaja.the330.view.login.LoginViewModel
import com.nagaja.the330.view.signupinfo.SignupInfoRepo
import com.nagaja.the330.view.signupinfo.SignupInfoVM


class ViewModelFactory(apiService: ApiService) :
    ViewModelProvider.Factory {
    var creators = mutableMapOf<Class<out ViewModel>, ViewModel>()

    init {
//        creators[PermissionViewModel::class.java] =
//            PermissionViewModel(PermissionRepository(apiService))
        creators[GeneralViewModel::class.java] =
            GeneralViewModel(GeneralRepository(apiService))
        creators[LoginViewModel::class.java] =
            LoginViewModel(LoginRepository(apiService))
        creators[SignupInfoVM::class.java] =
            SignupInfoVM(SignupInfoRepo(apiService))
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        for (a in creators.entries) {
            if (modelClass.isAssignableFrom(a.key)) {
                return a.value as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }

}