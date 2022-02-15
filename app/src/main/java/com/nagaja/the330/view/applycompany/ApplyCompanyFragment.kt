package com.nagaja.the330.view.applycompany

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage

class ApplyCompanyFragment : BaseFragment() {
    private lateinit var viewModel: ApplyCompanyVM
    private var onClickRemove: ((Int) -> Unit)? = null

    companion object {
        fun newInstance() = ApplyCompanyFragment()
    }

    @Composable
    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ApplyCompanyVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Header(title = stringResource(R.string.apply_company_title)) {
                viewController?.popFragment()
            }
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    stringResource(R.string.company_info),
                    modifier = Modifier.padding(16.dp),
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp
                )
                Text(
                    stringResource(R.string.category_selection),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                    color = ColorUtils.gray_222222,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                //TODO: Select category type
                CategorySeletion()

                Text(
                    stringResource(R.string.representative_image),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp, bottom = 13.dp, start = 16.dp)
                )

                Row() {
                    RepresentativeImage()
                    val listImage = remember {
                        mutableStateListOf<KeyValueModel>().apply {
                            add(KeyValueModel())
                            add(KeyValueModel())
                            add(KeyValueModel())
                            add(KeyValueModel())
                            add(KeyValueModel())
                        }
                    }
                    onClickRemove = { index ->
                        listImage.removeAt(index)
                    }
                    LazyRow {
                        itemsIndexed(listImage) { index, obj ->
                            ItemImagePicked(index)
                        }
                    }
                }

                CompanyNameInput()

                Text(
                    stringResource(R.string.area_selection),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                AreaSelection()

                TextFieldCustom(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(40.dp),
                    hint = stringResource(R.string.input_detail_address),
                    textStateId = viewModel.textStateNameEng
                )
                CompanyDescriptionInput()
                InfoPersonInCharge()
                ChooseService()
            }
        }
    }

    @Preview
    @Composable
    private fun CategorySeletion() {
        val options = GetDummyData.getSortFavoriteCompany(LocalContext.current)
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        Row(
            modifier = Modifier
                .padding(top = 6.dp, start = 16.dp)
                .noRippleClickable {
                    expanded = !expanded
                }
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(start = 9.dp, end = 7.dp)
                .width(180.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                selectedOptionText.name ?: "",
                modifier = Modifier.weight(1f),
                style = text14_222,
                textAlign = TextAlign.Start
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .width(10.dp),
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
    }

    @Preview
    @Composable
    private fun RepresentativeImage() {
        Column(
            Modifier
                .padding(start = 16.dp, top = 4.dp, end = 8.dp)
                .size(72.dp)
                .border(width = 1.dp, color = ColorUtils.blue_2177E4.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(R.drawable.ic_choose_image), contentDescription = null)
            Text(
                "4/5",
                color = ColorUtils.blue_2177E4,
                modifier = Modifier
                    .padding(top = 3.dp)
                    .alpha(0.3f),
                fontSize = 11.sp
            )
        }

    }

    @Composable
    private fun ItemImagePicked(position: Int) {
        ConstraintLayout(
            Modifier
                .padding(end = 4.dp)
                .size(76.dp)
        ) {
            val (content, close) = createRefs()
            GlideImage(
                imageModel = "",
                contentDescription = "",
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
                modifier = Modifier
                    .size(72.dp)
                    .constrainAs(content) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Image(
                painter = painterResource(R.drawable.ic_close_round),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(close) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .noRippleClickable {
                        onClickRemove?.invoke(position)
                    }
            )
        }
    }

    @Preview
    @Composable
    private fun CompanyNameInput() {
        Column(Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_name_eng),
                textStateId = viewModel.textStateNameEng
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_name_phi),
                textStateId = viewModel.textStateNamePhi
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_name_kr),
                textStateId = viewModel.textStateNameKr
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_name_china),
                textStateId = viewModel.textStateNameCN
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_name_jp),
                textStateId = viewModel.textStateNameJP
            )
        }
    }

    @Preview
    @Composable
    private fun AreaSelection() {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 12.dp, end = 30.dp)
        ) {
            BaseSeletion(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .weight(1f)
            )
            BaseSeletion(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            )
            BaseSeletion(modifier = Modifier.weight(1f))
        }
    }

    @Composable
    private fun BaseSeletion(modifier: Modifier = Modifier) {
        val options = GetDummyData.getSortFavoriteCompany(LocalContext.current)
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        Row(
            modifier = modifier
//                .padding(top = 6.dp, start = 16.dp)
                .noRippleClickable {
                    expanded = !expanded
                }
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(start = 9.dp, end = 7.dp)
//                .width(180.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                selectedOptionText.name ?: "",
                modifier = Modifier.weight(1f),
                style = text14_222,
                textAlign = TextAlign.Start
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .width(10.dp),
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
    }

    @Preview
    @Composable
    private fun CompanyDescriptionInput() {
        Column(Modifier.padding(horizontal = 16.dp)) {
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_des_eng),
                textStateId = viewModel.textStateDesEng
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_des_phi),
                textStateId = viewModel.textStateDesPhi
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_des_kr),
                textStateId = viewModel.textStateDesKr
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_des_china),
                textStateId = viewModel.textStateDesCN
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_des_jp),
                textStateId = viewModel.textStateDesJP
            )
        }
    }

    @Preview
    @Composable
    private fun InfoPersonInCharge() {
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                stringResource(R.string.info_person_in_charge),
                style = text14_222,
                modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.input_your_name_please),
                textStateId = viewModel.textStateName
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_your_phone_number),
                textStateId = viewModel.textStatePhone
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_your_email_address),
                textStateId = viewModel.textStateMailAddress
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_your_SNS_info_FB),
                textStateId = viewModel.textStateFb
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_your_SNS_info_Kakaotalk),
                textStateId = viewModel.textStateKakao
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_your_SNS_info_Line),
                textStateId = viewModel.textStateLine
            )
        }
    }

    @Preview
    @Composable
    private fun ChooseService() {
        val listService = listOf(
            AppConstants.DELIVERY,
            AppConstants.RESERVATION,
            AppConstants.PICKUP_DROP
        )
        val chooseState = remember { mutableStateOf(listService[0]) }
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
        ) {
            Text("배달/ 예약/ 픽업,드랍", style = text14_222, fontWeight = FontWeight.Bold)
            Row(
                Modifier
                    .padding(top = 11.dp)
                    .height(32.dp)
                    .border(width = 1.dp, color = ColorUtils.gray_222222),
            ) {
                SelectedService(
                    text = "배달",
                    modifier = Modifier.weight(1f),
                    isSelected = chooseState.value == AppConstants.DELIVERY,
                    onClick = { chooseState.value = AppConstants.DELIVERY }
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(ColorUtils.gray_222222)
                )

                SelectedService(
                    text = "예약",
                    modifier = Modifier.weight(1f),
                    isSelected = chooseState.value == AppConstants.RESERVATION,
                    onClick = { chooseState.value = AppConstants.RESERVATION }
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(ColorUtils.gray_222222)
                )
                SelectedService(
                    text = "픽업/드랍",
                    modifier = Modifier.weight(1f),
                    isSelected = chooseState.value == AppConstants.PICKUP_DROP,
                    onClick = { chooseState.value = AppConstants.PICKUP_DROP }
                )
            }
        }
    }

    @Composable
    private fun SelectedService(
        text: String,
        isSelected: Boolean,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        if (isSelected) {
            Box(
                modifier
                    .fillMaxHeight()
                    .background(ColorUtils.gray_222222)
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = ColorUtils.white_FFFFFF,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Box(
                modifier
                    .fillMaxHeight()
                    .background(ColorUtils.white_FFFFFF)
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = ColorUtils.gray_222222,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}