package com.nagaja.the330.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.view.applycompany.ApplyCompanyRepo
import com.nagaja.the330.view.applycompany.ApplyCompanyVM
import com.nagaja.the330.view.edit_profile.EditProfileRepo
import com.nagaja.the330.view.edit_profile.EditProfileVM
import com.nagaja.the330.view.favoritecompany.FavCompanyRepo
import com.nagaja.the330.view.favoritecompany.FavCompanyVM
import com.nagaja.the330.view.general.GeneralRepository
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.home.HomeScreenRepo
import com.nagaja.the330.view.home.HomeScreenVM
import com.nagaja.the330.view.login.LoginRepository
import com.nagaja.the330.view.login.LoginViewModel
import com.nagaja.the330.view.mypage.MyPageScreenRepo
import com.nagaja.the330.view.mypage.MyPageScreenVM
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
        creators[HomeScreenVM::class.java] =
            HomeScreenVM(HomeScreenRepo(apiService))
        creators[MyPageScreenVM::class.java] =
            MyPageScreenVM(MyPageScreenRepo(apiService))
        creators[EditProfileVM::class.java] =
            EditProfileVM(EditProfileRepo(apiService))
        creators[FavCompanyVM::class.java] =
            FavCompanyVM(FavCompanyRepo(apiService))
        creators[ApplyCompanyVM::class.java] =
            ApplyCompanyVM(ApplyCompanyRepo(apiService))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        for (a in creators.entries) {
            if (modelClass.isAssignableFrom(a.key)) {
                return a.value as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }

}