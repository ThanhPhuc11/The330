package com.nagaja.the330.view.edit_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*

class EditProfileFragment : BaseFragment() {
    private lateinit var vm: EditProfileVM

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    @Composable
    override fun SetupViewModel() {
        vm = getViewModelProvider(this)[EditProfileVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Header(title = "") {
                viewController?.popFragment()
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "회원 정보 수정",
                    color = ColorUtils.gray_222222,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
                //TODO: ID
                Text(stringResource(R.string.user_id), style = text14_222)
                TextFieldCustom(
//                    textStateId = viewModel.stateEdtOTP,
                    modifier = Modifier.padding(top = 4.dp)
                )
                //TODO: Name
                Text(
                    stringResource(R.string.name),
                    style = text14_222,
                    modifier = Modifier.padding(top = 20.dp)
                )
                TextFieldCustom(
//                    textStateId = viewModel.stateEdtOTP,
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
                    Box(
                        Modifier
                            .padding(end = 4.dp)
                            .size(44.dp)
                            .border(
                                width = 1.dp,
                                color = ColorUtils.gray_E1E1E1,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    HandleInputPhoneNumber(
                        vm.stateEdtPhone,
                        modifier = Modifier.weight(1f)
                    )
                }

                HandleChooseAddress()
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ColorUtils.blue_2177E4),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "완료",
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 17.sp
                )
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
        TextFieldCustom(
            textStateId = edtAddress,
            hint = stringResource(R.string.hint_input_full_address),
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}