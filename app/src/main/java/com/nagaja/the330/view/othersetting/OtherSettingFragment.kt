package com.nagaja.the330.view.othersetting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.login.LoginFragment

class OtherSettingFragment : BaseFragment() {
    private lateinit var viewModel: OtherSettingVM

    companion object {
        fun newInstance() = OtherSettingFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[OtherSettingVM::class.java]
        viewController = (activity as MainActivity).viewController
        backSystemHandler {
            viewController?.popFragment()
        }
        viewModel.callbackLogoutSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(null)
            showMess("로그아웃이 완료되었습니다")
            viewController?.popAllFragment()
            viewController?.pushFragment(
                ScreenId.SCREEN_LOGIN,
                LoginFragment.newInstance()
            )
        }

        viewModel.callbackWithDrawSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(null)
            showMess("탈퇴가 완료되었습니다.")
            viewController?.popAllFragment()
            viewController?.pushFragment(
                ScreenId.SCREEN_LOGIN,
                LoginFragment.newInstance()
            )
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Header("기타설정") {
                viewController?.popFragment()
            }
            Column(Modifier.padding(horizontal = 16.dp)) {
                Divider(color = ColorUtils.gray_E1E1E1, modifier = Modifier.padding(top = 16.dp))
                Row(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "이벤트 및 혜택 알림 수신 동의",
                        style = text16_black,
                        modifier = Modifier.weight(1f)
                    )
                    val checkedState = remember { mutableStateOf(true) }
                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "예약 및 서비스 알림 수신 동의",
                        style = text16_black,
                        modifier = Modifier.weight(1f)
                    )
                    val checkedState = remember { mutableStateOf(true) }
                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                Divider(color = ColorUtils.gray_E1E1E1, modifier = Modifier.padding(top = 20.dp))

                Text(
                    "서비스 이용 약관",
                    color = ColorUtils.black_000000,
                    style = text16_black,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    "개인정보처리방침",
                    color = ColorUtils.black_000000,
                    style = text16_black,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    "위치정보 동의 약관",
                    color = ColorUtils.black_000000,
                    style = text16_black,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Divider(color = ColorUtils.gray_E1E1E1, modifier = Modifier.padding(top = 20.dp))

                Row(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "버전 정보",
                        style = text16_black,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        BuildConfig.VERSION_NAME,
                        style = text16_black,
                    )
                }

                Divider(color = ColorUtils.gray_E1E1E1, modifier = Modifier.padding(top = 20.dp))

                val state = remember {
                    mutableStateOf(false)
                }
                if (state.value) {
                    Dialog2Button(
                        state = state,
                        title = "회원 탈퇴 하시겠습니까?",
                        content = "회원 탈퇴 시 모든 정보가 초기화 되며\n 재가입 및 복구가 불가능합니다.",
                        leftText = "취소",
                        rightText = "확인",
                        onClick = {
                            if (it) {
                                accessToken?.let { it1 -> viewModel.withdraw(it1) }
                            }
                        }
                    )
                }
                Text(
                    "회원탈퇴",
                    color = ColorUtils.black_000000,
                    style = text16_black,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .noRippleClickable {
                            state.value = true
                        }
                )

                Text(
                    "로그아웃",
                    color = ColorUtils.black_000000,
                    style = text16_black,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .noRippleClickable {
                            accessToken?.let { viewModel.logout(it) }
                        }
                )
            }
        }
    }
}