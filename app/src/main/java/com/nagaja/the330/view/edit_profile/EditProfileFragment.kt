package com.nagaja.the330.view.edit_profile

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.gson.Gson
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.nagaja.the330.view.verify_otp.VerifyOTPFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditProfileFragment : BaseFragment() {
    private lateinit var viewModel: EditProfileVM
    private var userDetail: UserDetail? = null

    var callbackUpdate: (() -> Unit)? = null

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[EditProfileVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.cbNextScreen.observe(viewLifecycleOwner) {
            viewController?.pushFragment(
                ScreenId.SCREEN_VERIFY_OTP,
                VerifyOTPFragment.newInstance(false).apply {
                    callbackResult = { isSuccess, nationNum, phoneNum, otp ->
                        viewModel.otp = otp
                        accessToken?.let { it1 -> viewModel.edtUserWithPhone(it1) }
                    }
                }
            )
        }
        viewModel.cbUpdateSuccess.observe(viewLifecycleOwner) {
            showMess("Update info success!")
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500)
                callbackUpdate?.invoke()
                viewController?.popFragment()
            }
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        val context = LocalContext.current
        val owner = LocalLifecycleOwner.current

        LaunchedEffect(viewModel.userDetailState.value) {
            viewModel.stateEdtName.value = viewModel.userDetailState.value?.realName ?: ""
            viewModel.stateEdtNationNum.value = viewModel.userDetailState.value?.nationNumber ?: ""
            viewModel.stateEdtPhone.value = viewModel.userDetailState.value?.phone ?: ""
            viewModel.stateEdtAddress.value = viewModel.userDetailState.value?.address ?: ""
            viewModel.stateEdtDetailAddress.value =
                viewModel.userDetailState.value?.detailAddress ?: ""

            Log.e("User Local", viewModel.userDetailState.value?.id.toString())
        }


        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        getUserDetailFromDataStore(context)
                    }
                    Lifecycle.Event.ON_STOP -> {
                        Log.e("EDIT", "onStop")
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        Log.e("EDIT", "onDestroy")
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                Log.e("EDIT", "onDispose")
                owner.lifecycle.removeObserver(observer)
            }
        }

        LayoutTheme330 {
            Header(title = "") {
                viewController?.popFragment()
            }
            //TODO: Content
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(state = rememberScrollState())
            ) {
                Text(
                    stringResource(R.string.edit_info),
                    color = ColorUtils.gray_222222,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                //TODO: ID
                Text(stringResource(R.string.user_id), style = text14_222)
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(ColorUtils.gray_F5F5F5)
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(
                            width = 1.dp,
                            color = ColorUtils.gray_E1E1E1,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(viewModel.userDetailState.value?.name ?: "", style = text14_222)
                }
                //TODO: Name
                Text(
                    stringResource(R.string.name),
                    style = text14_222,
                    modifier = Modifier.padding(top = 20.dp)
                )
                TextFieldCustom(
                    textStateId = viewModel.stateEdtName,
                    modifier = Modifier.padding(top = 4.dp)
                )
                //TODO: PhoneNumber
                Text(
                    stringResource(R.string.phone_number),
                    style = text14_222,
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
                    LaunchedEffect(viewModel.userDetailState.value?.nationNumber) {
                        viewModel.userDetailState.value?.nationNumber?.let { nationNum ->
                            selectedCountryNum.value =
                                options.firstOrNull { nationNum == it.id } ?: options[0]
                            viewModel.stateEdtNationNum.value = selectedCountryNum.value.id!!
                        }
                    }
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
                                    viewModel.stateEdtNationNum.value = selectedOption.id ?: ""
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

                HandleChooseAddress()

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            //TODO: Button complete
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ColorUtils.blue_2177E4)
                    .noRippleClickable {
                        accessToken?.let { viewModel.checkChangePhone(it) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.complete),
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 17.sp
                )
            }
        }
    }

    @Composable
    private fun HandleInputPhoneNumber(
        textFieldValue: MutableState<String>,
        modifier: Modifier = Modifier
    ) {
        TextFieldCustom(
            hint = stringResource(R.string.hint_input_phone),
            textStateId = textFieldValue,
            modifier = modifier,
            maxLength = 11,
            inputType = KeyboardType.Number,
//            focusable = viewModel.stateEnableFocusPhone.value
        )
    }

    @Composable
    private fun HandleChooseAddress() {
        Text(
            stringResource(R.string.address),
            style = text14_222,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(top = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val options = GetDummyData.getCoutryAdrressSignup()
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText = remember { mutableStateOf(options[0]) }
            LaunchedEffect(viewModel.userDetailState.value?.nation) {
                viewModel.userDetailState.value?.nation?.let { nation ->
                    selectedOptionText.value =
                        options.firstOrNull { nation == it.id } ?: options[0]
                    viewModel.stateEdtNation.value = selectedOptionText.value.id!!
                }
            }
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
                    selectedOptionText.value.name!!,
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
                                selectedOptionText.value = selectionOption
                                expanded = false
                                viewModel.stateEdtNation.value = selectedOptionText.value.id ?: ""
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }


            Row(modifier = Modifier.weight(5f), verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(
                    visible = selectedOptionText.value.id == "KOREA",
                    modifier = Modifier.weight(1f)
                ) {
                    TextFieldCustom(
                        hint = stringResource(R.string.hint_input_address),
                        textStateId = viewModel.stateEdtAddress
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
        TextFieldCustom(
            textStateId = viewModel.stateEdtDetailAddress,
            hint = stringResource(R.string.hint_input_full_address),
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }

    private fun getUserDetailFromDataStore(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { get ->
                get[DataStorePref.USER_DETAIL] ?: ""
            }.collect {
                val userDetail = Gson().fromJson(it, UserDetail::class.java)
                userDetail?.let { viewModel.userDetailState.value = userDetail }
            }
        }
    }

    @Composable
    fun TextFieldCustom(
        modifier: Modifier = Modifier,
        textStateId: MutableState<String> = remember {
            mutableStateOf("")
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
                    if (it.length <= maxLength) textStateId.value = it
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
                        if (textStateId.value.isEmpty()) {
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
}