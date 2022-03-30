package com.nagaja.the330.view.verify_otp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*

class VerifyOTPFragment : BaseFragment() {
    private lateinit var viewModel: VerifyOTPVM

    //    private lateinit var shareViewModel: ResetTemplaceShareVM
    private var countDownTimer: CountDownTimer? = null
    var callbackResult: ((Boolean, String?, String?, Int?) -> Unit)? = null

    companion object {
        fun newInstance(isCheckOldPhoneNumber: Boolean = true) = VerifyOTPFragment().apply {
            arguments = Bundle().apply {
                putBoolean(AppConstants.EXTRA_KEY1, isCheckOldPhoneNumber)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[VerifyOTPVM::class.java]
//        shareViewModel =
//            ViewModelProvider(
//                activity?.supportFragmentManager?.findFragmentByTag(
//                    ScreenId.SCREEN_FIND_RESET_TEMPLACE
//                )!!
//            )[ResetTemplaceShareVM::class.java]

        viewController = (activity as MainActivity).viewController

        viewModel.callbackVerifySuccess.observe(viewLifecycleOwner) {
            viewController?.popFragment()
            callbackResult?.invoke(
                true,
                viewModel._nationalNumber,
                viewModel.stateEdtPhone.value.text,
                viewModel._otp
            )
//            shareViewModel.userDetail = it
        }
    }

    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        countDownTimer = object : android.os.CountDownTimer(180000, 1000) {
                            override fun onFinish() {
                                viewModel.stateBtnSendPhone.value = true
                                viewModel.stateEnableFocusPhone.value = true
                                viewModel.cbActivePhoneButtonTimer.value = false
                                viewModel.cbNumberCoundown.value =
                                    getString(R.string.authen_request)
                            }

                            override fun onTick(millisUntilFinished: Long) {
                                "${numberFormat2Digit((millisUntilFinished / 60000).toInt())}:${
                                    numberFormat2Digit(
                                        ((millisUntilFinished % (60000)) / 1000).toInt()
                                    )
                                }".also {
                                    viewModel.cbNumberCoundown.value = it
                                }
                            }
                        }
                    }
                    Lifecycle.Event.ON_STOP -> {

                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(viewModel.stateEdtPhone.value.text.length) {
            viewModel.checkPhone()
        }
        LaunchedEffect(viewModel.stateEdtOTP.value.text.length) {
            viewModel.checkOTP()
        }

        LaunchedEffect(viewModel.cbActivePhoneButtonTimer.value) {
            if (viewModel.cbActivePhoneButtonTimer.value) {
                countDownTimer?.start()
            }
        }
        LayoutTheme330 {
            Header("") {
                viewController?.popFragment()
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
            ) {

                //TODO: Phone
                Text(
                    stringResource(R.string.phone_label),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    val options = GetDummyData.getCountryNumber()
                    var expanded by remember { mutableStateOf(false) }
                    val selectedCountryNum = remember { mutableStateOf(options[0]) }
                    Box(
                        Modifier
                            .padding(end = 4.dp)
                            .size(44.dp)
                            .border(
                                width = 1.dp,
                                color = ColorUtils.gray_E1E1E1,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .noRippleClickable {
                                expanded = !expanded
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedCountryNum.value.name!!, style = text14_222)
                        DropdownMenu(expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            options.forEach { selectedOption ->
                                DropdownMenuItem(onClick = {
                                    selectedCountryNum.value = selectedOption
                                    expanded = false
                                    viewModel._nationalNumber = selectedOption.id
                                }) {
                                    Text(selectedOption.name!!)
                                }
                            }
                        }
                    }
                    HandleInputPhoneNumber(
                        viewModel.stateEdtPhone,
                        modifier = Modifier.weight(1f)
                    )
                }
                DrawRequestPhoneButton(viewModel.stateBtnSendPhone.value) {
                    viewModel.checkExistByPhone(requireArguments().getBoolean(AppConstants.EXTRA_KEY1))
                }

                //TODO: OTP
                Text(
                    stringResource(R.string.cer_number_input),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 24.dp)
                )

                TextFieldCustom(
                    hint = stringResource(R.string.hint_input_otp),
                    textStateId = viewModel.stateEdtOTP,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLength = 6,
                    focusable = viewModel.stateEnableFocusOTP.value
                )

                DrawRequestOTPButton(viewModel.stateBtnSendOTP.value) {
                    viewModel.sendOTP()
                }
            }
        }
    }

    @Composable
    private fun HandleInputPhoneNumber(
        textFieldValue: MutableState<TextFieldValue>,
        modifier: Modifier = Modifier
    ) {
        TextFieldCustom(
            hint = stringResource(R.string.hint_input_phone),
            textStateId = textFieldValue,
            modifier = modifier,
            maxLength = 11,
            inputType = KeyboardType.Number,
            focusable = viewModel.stateEnableFocusPhone.value
        )
    }

    @Composable
    private fun DrawRequestPhoneButton(isActived: Boolean, onClick: () -> Unit) {
        if (isActived) {
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = ColorUtils.white_FFFFFF
                ),
                border = BorderStroke(
                    1.dp,
                    ColorUtils.gray_222222
                )
            ) {
                Text(
                    viewModel.cbNumberCoundown.value,
                    fontSize = 14.sp,
                    color = ColorUtils.gray_222222
                )
            }
        } else {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = ColorUtils.gray_E1E1E1
                ),
                enabled = false
            ) {
                Text(
                    viewModel.cbNumberCoundown.value,
                    fontSize = 14.sp,
                    color = ColorUtils.gray_BEBEBE
                )
            }
        }
    }

    @Composable
    private fun DrawRequestOTPButton(isActived: Boolean, onClick: () -> Unit) {
        if (isActived) {
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = ColorUtils.white_FFFFFF
                ),
                border = BorderStroke(
                    1.dp,
                    ColorUtils.gray_222222
                )
            ) {
                Text(
                    stringResource(R.string.certification),
                    fontSize = 14.sp,
                    color = ColorUtils.gray_222222
                )
            }
        } else {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(4.dp)),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = ColorUtils.gray_E1E1E1
                ),
                enabled = false
            ) {
                Text(
                    stringResource(R.string.certification),
                    fontSize = 14.sp,
                    color = ColorUtils.gray_BEBEBE
                )
            }
        }
    }

    fun numberFormat2Digit(number: Int): String {
        return if (number < 10) "0$number" else number.toString() + ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}