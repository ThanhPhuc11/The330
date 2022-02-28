package com.nagaja.the330.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.view.applycompany.ApplyCompanyRepo
import com.nagaja.the330.view.applycompany.ApplyCompanyVM
import com.nagaja.the330.view.applycompanyproduct.ProductCompanyRepo
import com.nagaja.the330.view.applycompanyproduct.ProductCompanyVM
import com.nagaja.the330.view.edit_profile.EditProfileRepo
import com.nagaja.the330.view.edit_profile.EditProfileVM
import com.nagaja.the330.view.favoritecompany.FavCompanyRepo
import com.nagaja.the330.view.favoritecompany.FavCompanyVM
import com.nagaja.the330.view.findId.FindIdRepo
import com.nagaja.the330.view.findId.FindIdVM
import com.nagaja.the330.view.general.GeneralRepository
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.home.HomeScreenRepo
import com.nagaja.the330.view.home.HomeScreenVM
import com.nagaja.the330.view.login.LoginRepository
import com.nagaja.the330.view.login.LoginViewModel
import com.nagaja.the330.view.mypage.MyPageScreenRepo
import com.nagaja.the330.view.mypage.MyPageScreenVM
import com.nagaja.the330.view.othersetting.OtherSettingRepo
import com.nagaja.the330.view.othersetting.OtherSettingVM
import com.nagaja.the330.view.resetpassword.ResetPwRepo
import com.nagaja.the330.view.resetpassword.ResetPwVM
import com.nagaja.the330.view.secondhand.SecondHandRepo
import com.nagaja.the330.view.secondhand.SecondHandVM
import com.nagaja.the330.view.signupinfo.SignupInfoRepo
import com.nagaja.the330.view.signupinfo.SignupInfoVM
import com.nagaja.the330.view.usage.UsageRepo
import com.nagaja.the330.view.usage.UsageVM
import com.nagaja.the330.view.verify_otp.VerifyOTPRepo
import com.nagaja.the330.view.verify_otp.VerifyOTPVM


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
        creators[VerifyOTPVM::class.java] =
            VerifyOTPVM(VerifyOTPRepo(apiService))
        creators[FindIdVM::class.java] =
            FindIdVM(FindIdRepo(apiService))
        creators[ResetPwVM::class.java] =
            ResetPwVM(ResetPwRepo(apiService))
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
        creators[UsageVM::class.java] =
            UsageVM(UsageRepo(apiService))
        creators[SecondHandVM::class.java] =
            SecondHandVM(SecondHandRepo(apiService))
        creators[OtherSettingVM::class.java] =
            OtherSettingVM(OtherSettingRepo(apiService))
        creators[ApplyCompanyVM::class.java] =
            ApplyCompanyVM(ApplyCompanyRepo(apiService))
        creators[ProductCompanyVM::class.java] =
            ProductCompanyVM(ProductCompanyRepo(apiService))
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