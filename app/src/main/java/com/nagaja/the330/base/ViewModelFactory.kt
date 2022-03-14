package com.nagaja.the330.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.view.applycompany.ApplyCompanyRepo
import com.nagaja.the330.view.applycompany.ApplyCompanyVM
import com.nagaja.the330.view.applycompanyproduct.ProductCompanyRepo
import com.nagaja.the330.view.applycompanyproduct.ProductCompanyVM
import com.nagaja.the330.view.companydetail.CompanyDetailRepo
import com.nagaja.the330.view.companydetail.CompanyDetailVM
import com.nagaja.the330.view.companylist.CompanyListRepo
import com.nagaja.the330.view.companylist.CompanyListVM
import com.nagaja.the330.view.edit_profile.EditProfileRepo
import com.nagaja.the330.view.edit_profile.EditProfileVM
import com.nagaja.the330.view.favoritecompany.FavCompanyRepo
import com.nagaja.the330.view.favoritecompany.FavCompanyVM
import com.nagaja.the330.view.findId.FindIdRepo
import com.nagaja.the330.view.findId.FindIdVM
import com.nagaja.the330.view.fqa.FQARepo
import com.nagaja.the330.view.fqa.FQAViewModel
import com.nagaja.the330.view.freenoticeboard.FreeNoticeRepo
import com.nagaja.the330.view.freenoticeboard.FreeNoticeVM
import com.nagaja.the330.view.freenoticedetail.FreeNoticeDetailRepo
import com.nagaja.the330.view.freenoticedetail.FreeNoticeDetailVM
import com.nagaja.the330.view.freenoticeregis.FreeNoticeRegisRepo
import com.nagaja.the330.view.freenoticeregis.FreeNoticeRegisVM
import com.nagaja.the330.view.general.GeneralRepository
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.home.HomeScreenRepo
import com.nagaja.the330.view.home.HomeScreenVM
import com.nagaja.the330.view.localnews.LocalNewsRepo
import com.nagaja.the330.view.localnews.LocalNewsVM
import com.nagaja.the330.view.localnewsdetail.LocalNewsDetailRepo
import com.nagaja.the330.view.localnewsdetail.LocalNewsDetailVM
import com.nagaja.the330.view.login.LoginRepository
import com.nagaja.the330.view.login.LoginViewModel
import com.nagaja.the330.view.mypage.MyPageScreenRepo
import com.nagaja.the330.view.mypage.MyPageScreenVM
import com.nagaja.the330.view.othersetting.OtherSettingRepo
import com.nagaja.the330.view.othersetting.OtherSettingVM
import com.nagaja.the330.view.recruitment.RecruitmentJobSearchRepo
import com.nagaja.the330.view.recruitment.RecruitmentJobSearchVM
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailRepo
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailVM
import com.nagaja.the330.view.recruitmentregis.RecruitJobRegisRepo
import com.nagaja.the330.view.recruitmentregis.RecruitJobRegisVM
import com.nagaja.the330.view.reportmissing.ReportMissingRepo
import com.nagaja.the330.view.reportmissing.ReportMissingVM
import com.nagaja.the330.view.reportmissingdetail.ReportMissingDetailRepo
import com.nagaja.the330.view.reportmissingdetail.ReportMissingDetailVM
import com.nagaja.the330.view.reportmissingregis.ReportMissingRegisRepo
import com.nagaja.the330.view.reportmissingregis.ReportMissingRegisVM
import com.nagaja.the330.view.reservation.ReservationRepo
import com.nagaja.the330.view.reservation.ReservationVM
import com.nagaja.the330.view.reservationcompany.ReservationCompanyRepo
import com.nagaja.the330.view.reservationcompany.ReservationCompanyVM
import com.nagaja.the330.view.reservationregis.ReservationRegisRepo
import com.nagaja.the330.view.reservationregis.ReservationRegisVM
import com.nagaja.the330.view.resetpassword.ResetPwRepo
import com.nagaja.the330.view.resetpassword.ResetPwVM
import com.nagaja.the330.view.secondhanddetail.SecondHandDetailRepo
import com.nagaja.the330.view.secondhanddetail.SecondHandDetailVM
import com.nagaja.the330.view.secondhandmarket.SecondHandMarketRepo
import com.nagaja.the330.view.secondhandmarket.SecondHandMarketVM
import com.nagaja.the330.view.secondhandmypage.SecondHandMypageRepo
import com.nagaja.the330.view.secondhandmypage.SecondHandMypageVM
import com.nagaja.the330.view.secondhandregis.SecondHandRegisRepo
import com.nagaja.the330.view.secondhandregis.SecondHandRegisVM
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
        creators[SecondHandMypageVM::class.java] =
            SecondHandMypageVM(SecondHandMypageRepo(apiService))
        creators[SecondHandMarketVM::class.java] =
            SecondHandMarketVM(SecondHandMarketRepo(apiService))
        creators[SecondHandRegisVM::class.java] =
            SecondHandRegisVM(SecondHandRegisRepo(apiService))
        creators[SecondHandDetailVM::class.java] =
            SecondHandDetailVM(SecondHandDetailRepo(apiService))
        creators[OtherSettingVM::class.java] =
            OtherSettingVM(OtherSettingRepo(apiService))
        creators[ApplyCompanyVM::class.java] =
            ApplyCompanyVM(ApplyCompanyRepo(apiService))
        creators[ProductCompanyVM::class.java] =
            ProductCompanyVM(ProductCompanyRepo(apiService))
        creators[LocalNewsVM::class.java] =
            LocalNewsVM(LocalNewsRepo(apiService))
        creators[LocalNewsDetailVM::class.java] =
            LocalNewsDetailVM(LocalNewsDetailRepo(apiService))
        creators[FreeNoticeVM::class.java] =
            FreeNoticeVM(FreeNoticeRepo(apiService))
        creators[FreeNoticeRegisVM::class.java] =
            FreeNoticeRegisVM(FreeNoticeRegisRepo(apiService))
        creators[FreeNoticeDetailVM::class.java] =
            FreeNoticeDetailVM(FreeNoticeDetailRepo(apiService))
        creators[ReportMissingVM::class.java] =
            ReportMissingVM(ReportMissingRepo(apiService))
        creators[ReportMissingDetailVM::class.java] =
            ReportMissingDetailVM(ReportMissingDetailRepo(apiService))
        creators[ReportMissingRegisVM::class.java] =
            ReportMissingRegisVM(ReportMissingRegisRepo(apiService))
        creators[RecruitmentJobSearchVM::class.java] =
            RecruitmentJobSearchVM(RecruitmentJobSearchRepo(apiService))
        creators[RecruitJobsDetailVM::class.java] =
            RecruitJobsDetailVM(RecruitJobsDetailRepo(apiService))
        creators[RecruitJobRegisVM::class.java] =
            RecruitJobRegisVM(RecruitJobRegisRepo(apiService))
        creators[ReservationVM::class.java] =
            ReservationVM(ReservationRepo(apiService))
        creators[ReservationCompanyVM::class.java] =
            ReservationCompanyVM(ReservationCompanyRepo(apiService))
        creators[ReservationRegisVM::class.java] =
            ReservationRegisVM(ReservationRegisRepo(apiService))
        creators[CompanyListVM::class.java] =
            CompanyListVM(CompanyListRepo(apiService))
        creators[CompanyDetailVM::class.java] =
            CompanyDetailVM(CompanyDetailRepo(apiService))
        creators[FQAViewModel::class.java] =
            FQAViewModel(FQARepo(apiService))
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