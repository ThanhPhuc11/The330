package com.nagaja.the330.view.signupinfo

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*

class SignupInfoFragment : BaseFragment() {
    private lateinit var viewModel: SignupInfoVM
    private var countDownTimer: CountDownTimer? = null

    companion object {
        @JvmStatic
        fun newInstance() = SignupInfoFragment()
    }

    @Composable
    override fun SetupViewModel() {
        val viewModelStoreOwner: ViewModelStoreOwner =
            checkNotNull(LocalViewModelStoreOwner.current) {
                "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
            }
        viewModel = getViewModelProvider(viewModelStoreOwner)[SignupInfoVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview(showBackground = true)
    @Composable
    override fun UIData() {
        DisposableEffect(key1 = Unit, effect = {
            Log.e("PHUC", "onStart")
            onDispose {
                Log.e("PHUC", "onStop")
            }
        })
        val textStateId = remember { mutableStateOf(TextFieldValue("")) }

        val checkedAll = remember { mutableStateOf(false) }
        val check1 = remember { mutableStateOf(false) }
        val check2 = remember { mutableStateOf(false) }
        val check3 = remember { mutableStateOf(false) }
        val check4 = remember { mutableStateOf(false) }
        val check5 = remember { mutableStateOf(false) }
        val check6 = remember { mutableStateOf(false) }

        val btnSignupState = remember { mutableStateOf(false) }

        LaunchedEffect(
            check1.value,
            check2.value,
            check3.value,
            check4.value,
            check5.value,
            check6.value
        ) {
            checkedAll.value =
                check1.value && check2.value && check3.value && check4.value && check5.value && check6.value
        }

        LaunchedEffect(viewModel.stateEdtPhone.value.text.length) {
            viewModel.checkPhone()
        }
        LaunchedEffect(viewModel.stateEdtOTP.value.text.length) {
            viewModel.checkOTP()
        }

        LaunchedEffect(
            viewModel.validateId.value,
            viewModel.validatePass.value,
            viewModel.validatePhone.value,
            viewModel.validateAddress.value,
            checkedAll.value
        ) {
            btnSignupState.value =
                viewModel.validateId.value &&
                        viewModel.validatePass.value &&
                        viewModel.validatePhone.value &&
                        viewModel.validateAddress.value &&
                        checkedAll.value
        }
        countDownTimer = object : android.os.CountDownTimer(20000, 1000) {
            override fun onFinish() {
                viewModel.stateBtnSendPhone.value = true
                viewModel.stateEnableFocusPhone.value = true
                viewModel.cbActivePhoneButtonTimer.value = false
                viewModel.cbNumberCoundown.value = getString(R.string.authen_request)
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
        LaunchedEffect(viewModel.cbActivePhoneButtonTimer.value) {
            if (viewModel.cbActivePhoneButtonTimer.value) {
                countDownTimer?.start()
            }
        }
        LayoutTheme330 {
            //TODO: Header
            Header(title = "", clickBack = {
                viewController?.popFragment()
            })
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(
                        state = rememberScrollState()
                    ),
//                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()

                ) {
                    Text(
                        stringResource(R.string.signup),
                        color = ColorUtils.gray_222222,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 20.dp)
                    )
                    Text(
                        stringResource(R.string.enter_member_infomation),
                        fontSize = 16.sp,
                        color = ColorUtils.gray_9F9F9F,
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                    )

                    //TODO: Name
                    Text(
                        stringResource(R.string.name),
                        style = text14_222,
                        fontWeight = FontWeight.Black,
                    )
                    TextFieldCustom(
                        hint = stringResource(R.string.input_your_name_please),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    //TODO: ID
                    Text(
                        stringResource(R.string.user_id),
                        fontWeight = FontWeight.Black,
                        style = text14_222,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text(
                        stringResource(R.string.guide_input_id),
                        fontSize = 10.sp,
                        color = ColorUtils.gray_222222,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    HandleInputId(textStateId)
                    AnimatedVisibility(
                        visible = (viewModel.stateErrorId.value != null &&
                                viewModel.stateErrorId.value!!.isNotEmpty())
                    ) {
                        Text(
                            text = viewModel.stateErrorId.value ?: "",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 12.sp,
                            color = ColorUtils.pink_FF1E54
                        )
                    }

                    //TODO: Password
                    Text(
                        stringResource(R.string.password),
                        fontWeight = FontWeight.Black,
                        style = text14_222,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text(
                        stringResource(R.string.guide_input_password),
                        fontSize = 10.sp,
                        color = ColorUtils.gray_222222,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    val textStatePw = remember { viewModel.pw }
                    HandleInputPassword(textStatePw)
                    AnimatedVisibility(
                        visible = (viewModel.stateErrorPw.value != null &&
                                viewModel.stateErrorPw.value!!.isNotEmpty())
                    ) {
                        Text(
                            text = viewModel.stateErrorPw.value ?: "",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 12.sp,
                            color = ColorUtils.pink_FF1E54
                        )
                    }

                    //TODO: Re-password
                    Text(
                        stringResource(R.string.confirm_password),
                        style = text14_222,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    val textStateRePw = remember { mutableStateOf(TextFieldValue("")) }
                    HandleInputConfirmPassword(textStateRePw)
                    AnimatedVisibility(
                        visible = (viewModel.stateErrorRePw.value != null &&
                                viewModel.stateErrorRePw.value!!.isNotEmpty())
                    ) {
                        Text(
                            text = viewModel.stateErrorRePw.value ?: "",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 12.sp,
                            color = ColorUtils.pink_FF1E54
                        )
                    }

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
                        viewModel.checkExistByPhone()
                    }

                    //TODO: OTP
                    Text(
                        stringResource(R.string.cer_number_input),
                        style = text14_222,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(top = 24.dp)
                    )

                    TextFieldSignUp(
                        hint = stringResource(R.string.hint_input_otp),
                        textStateId = viewModel.stateEdtOTP,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLength = 6,
                        focusable = viewModel.stateEnableFocusOTP.value
                    )

                    DrawRequestOTPButton(viewModel.stateBtnSendOTP.value) {
                        viewModel.sendOTP()
                    }

                    //TODO: Address
                    HandleChooseAddress()

                    //term
                    Text(
                        stringResource(R.string.label_agree_term_to_use),
                        fontSize = 16.sp,
                        color = ColorUtils.gray_222222,
                        modifier = Modifier.padding(top = 40.dp, bottom = 17.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .padding(top = 20.dp)
                    ) {
                        Checkbox(
                            checked = checkedAll.value,
                            onCheckedChange = {
                                checkedAll.value = !checkedAll.value
                                if (checkedAll.value) {
                                    check1.value = true
                                    check2.value = true
                                    check3.value = true
                                    check4.value = true
                                    check5.value = true
                                    check6.value = true
                                } else {
                                    check1.value = false
                                    check2.value = false
                                    check3.value = false
                                    check4.value = false
                                    check5.value = false
                                    check6.value = false
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ColorUtils.blue_2177E4,
//                        checkmarkColor = ColorUtils.white_FFFFFF
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            stringResource(R.string.agree_all_term),
                            fontSize = 14.sp,
                            color = ColorUtils.gray_222222,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .padding(start = 10.5.dp)
                                .weight(1f)
                        )
                    }

                    Box(
                        Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ColorUtils.gray_F5F5F5)
                    )

                    val collapsed1 = remember { mutableStateOf(true) }
                    val collapsed2 = remember { mutableStateOf(true) }
                    val collapsed3 = remember { mutableStateOf(true) }
                    val collapsed4 = remember { mutableStateOf(true) }
                    val collapsed5 = remember { mutableStateOf(true) }
                    val collapsed6 = remember { mutableStateOf(true) }
                    CheckPolicy(
                        text = stringResource(R.string.term_line_1_accept_term_of_use),
                        check1,
                        collapsed1
                    )
                    AnimatedVisibility(visible = !collapsed1.value) {
                        ContentPolicy(content = "content1")
                    }

                    CheckPolicy(
                        text = stringResource(R.string.term_line_2_privacy_policy),
                        check2,
                        collapsed2
                    )
                    AnimatedVisibility(visible = !collapsed2.value) {
                        ContentPolicy(content = "content2")
                    }

                    CheckPolicy(text = stringResource(R.string.term_line_3), check3, collapsed3)
                    AnimatedVisibility(visible = !collapsed3.value) {
                        ContentPolicy(content = "content3")
                    }

                    CheckPolicy(
                        text = stringResource(R.string.term_line_4_location),
                        check4,
                        collapsed4
                    )
                    AnimatedVisibility(visible = !collapsed4.value) {
                        ContentPolicy(content = "content4")
                    }

                    CheckPolicy(
                        text = stringResource(R.string.term_line_5_event_benefit),
                        check5,
                        collapsed5
                    )
                    AnimatedVisibility(visible = !collapsed5.value) {
                        ContentPolicy(content = "content5")
                    }

                    CheckPolicy(
                        text = stringResource(R.string.term_line_6_notification),
                        check6,
                        collapsed6
                    )
                    AnimatedVisibility(visible = !collapsed6.value) {
                        ContentPolicy(content = "content6")
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    )
                }
                Button(
                    onClick = {
                        viewModel.authWithId(UserDetail().apply {
                            name = textStateId.value.text
                        })
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(if (btnSignupState.value) ColorUtils.blue_2177E4 else ColorUtils.gray_E1E1E1),
                    enabled = btnSignupState.value
                ) {
                    Text(
                        stringResource(R.string.btn_register),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (btnSignupState.value) ColorUtils.white_FFFFFF else ColorUtils.gray_C4C4C4
                    )
                }
            }

        }
    }

    @Composable
    fun CheckPolicy(
        text: String,
        checked: MutableState<Boolean>,
        collapsed: MutableState<Boolean>
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .noRippleClickable {
                    collapsed.value = !collapsed.value
                }
        ) {
            Checkbox(
                checked = checked.value,
                onCheckedChange = {
                    checked.value = !checked.value
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorUtils.black_000000,
                ),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text,
                fontSize = 14.sp,
                color = ColorUtils.gray_626262,
                modifier = Modifier
                    .padding(start = 10.5.dp)
                    .weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = "",
                Modifier
                    .rotate(if (collapsed.value) 0f else 180f)
            )
        }
    }

    @Composable
    fun ContentPolicy(content: String) {
        Column(
            Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .background(ColorUtils.gray_F5F5F5)
                .fillMaxWidth()
                .height(200.dp)
                .padding(9.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Text(
                content,
                color = ColorUtils.gray_767676,
                fontSize = 12.sp
            )
        }
    }

    @Composable
    fun TextFieldSignUp(
        modifier: Modifier = Modifier,
        textStateId: MutableState<TextFieldValue> = remember {
            mutableStateOf(TextFieldValue(""))
        },
        hint: String = "",
        maxLength: Int = 1000,
        inputType: KeyboardType = KeyboardType.Text,
        isPw: Boolean = false,
        focusable: Boolean = true
    ) {
        Box(
            modifier = modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .height(44.dp)
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = textStateId.value,
                onValueChange = {
                    if (it.text.length <= maxLength) textStateId.value = it
                },
                Modifier
                    .fillMaxWidth(),
                singleLine = true,
                enabled = focusable,
//            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                visualTransformation = if (isPw) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = inputType),
                textStyle = TextStyle(
                    color = ColorUtils.black_000000
                ),
                decorationBox = { innerTextField ->
                    Row {
                        if (textStateId.value.text.isEmpty()) {
                            Text(
                                text = hint,
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

    @Composable
    private fun HandleInputId(textStateId: MutableState<TextFieldValue>) {
        val focused = remember { mutableStateOf(false) }
        TextFieldSignUp(
            hint = stringResource(R.string.please_enter_id),
            textStateId = textStateId,
            modifier = Modifier
                .padding(top = 4.dp)
                .onFocusChanged {
                    if (!it.hasFocus && focused.value) {
                        viewModel.checkInputId(textStateId.value.text)
                    } else {
                        focused.value = true
                    }
                },
            maxLength = 20
        )
    }

    @Composable
    private fun HandleInputPassword(textFieldValue: MutableState<TextFieldValue>) {
        val focused = remember { mutableStateOf(false) }
        TextFieldSignUp(
            hint = stringResource(R.string.hint_input_password),
            textStateId = textFieldValue,
            modifier = Modifier
                .padding(top = 4.dp)
                .onFocusChanged {
                    if (!it.hasFocus && focused.value) {
                        viewModel.checkInputPw(textFieldValue.value.text)
                        viewModel.checkInputRePw()
                    } else {
                        focused.value = true
                    }
                },
            maxLength = 15,
            isPw = true
        )
    }

    @Composable
    private fun HandleInputConfirmPassword(
        textFieldValue: MutableState<TextFieldValue>
    ) {
        val focused = remember { mutableStateOf(false) }
        TextFieldSignUp(
            hint = stringResource(R.string.hint_input_confirm_password),
            textStateId = textFieldValue,
            modifier = Modifier
                .padding(top = 4.dp)
                .onFocusChanged {
                    if (!it.hasFocus && focused.value) {
                        viewModel.confirmPw = textFieldValue.value.text
                        viewModel.checkInputRePw()
                    } else {
                        focused.value = true
                    }
                },
            maxLength = 15,
            isPw = true
        )
    }

    @Composable
    private fun HandleInputPhoneNumber(
        textFieldValue: MutableState<TextFieldValue>,
        modifier: Modifier = Modifier
    ) {
        TextFieldSignUp(
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

    @Composable
    private fun HandleChooseAddress() {
        Text(
            stringResource(R.string.address),
            style = text14_222,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(top = 20.dp)
        )
        val edtAddress = remember { mutableStateOf(TextFieldValue("")) }
        val edtFullAddress = remember { mutableStateOf(TextFieldValue("")) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val options = GetDummyData.getCoutryAdrressSignup()
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }
            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .noRippleClickable {
                        expanded = !expanded
                    }
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .weight(2f)
                    .height(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    selectedOptionText.name!!,
                    style = text14_222
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }


            Row(modifier = Modifier.weight(5f), verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(
                    visible = selectedOptionText.id == "KOREA",
                    modifier = Modifier.weight(1f)
                ) {
                    TextFieldCustom(
                        hint = stringResource(R.string.hint_input_address),
                    )
                }
                Image(
                    painter = painterResource(R.drawable.ic_mark),
                    contentDescription = "",
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 8.dp)
                )
            }

        }
        TextFieldSignUp(
            textStateId = edtAddress,
            hint = stringResource(R.string.hint_input_full_address),
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }

    fun numberFormat2Digit(number: Int): String {
        return if (number < 10) "0$number" else number.toString() + ""
    }
}