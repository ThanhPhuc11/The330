package com.nagaja.the330.view.applycompany

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.gson.Gson
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.CategoryModel
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.*
import com.nagaja.the330.view.*
import com.nagaja.the330.view.applycompanyproduct.ProductCompanyFragment
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.io.File

class ApplyCompanyFragment : BaseFragment() {
    private lateinit var viewModel: ApplyCompanyVM
    private var onClickRemove: ((Int) -> Unit)? = null
    private var onClickChoose: ((Int) -> Unit)? = null
    private var callbackListImage: ((Uri?) -> Unit)? = null
    private var callbackAttachFile: ((Uri?) -> Unit)? = null

    private var userDetail: UserDetail? = null

    companion object {
        fun newInstance() = ApplyCompanyFragment()
    }

    @Composable
    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ApplyCompanyVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        viewModel.getCategory(accessToken!!)
                        CoroutineScope(Dispatchers.IO).launch {
                            requireContext().dataStore.data.map { get ->
                                get[DataStorePref.USER_DETAIL] ?: ""
                            }.collect {
                                userDetail = Gson().fromJson(it, UserDetail::class.java)
                                this@launch.coroutineContext.job.cancel()
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

                Row {
                    val listImage = remember {
                        mutableStateListOf<FileModel>()
                    }
                    callbackListImage = { uri ->
                        listImage.add(FileModel(url = uri.toString()))
                    }
                    val count = remember { mutableStateOf(listImage.size) }
                    LaunchedEffect(listImage.size) {
                        count.value = listImage.size
                    }
                    RepresentativeImage(count) {
                        if (count.value == 5) return@RepresentativeImage
                        checkPermissBeforeAttachFile(this@ApplyCompanyFragment.requireContext()) {
                            val intent = Intent(Intent.ACTION_PICK).apply {
                                setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*"
                                )
                            }
                            startResultRepresentativeCallback.launch(intent)
                        }
                    }
                    onClickRemove = { index ->
                        listImage.removeAt(index)
                    }
                    LazyRow {
                        itemsIndexed(listImage) { index, obj ->
                            ItemImagePicked(index, obj)
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
                    textStateId = viewModel.textStateAdress
                )
                CompanyDescriptionInput()
                InfoPersonInCharge()
                ChooseService()
                OpenTimeUI()
                ReservationTime()

                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(ColorUtils.gray_F5F5F5)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 14.dp, top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.button_selection), style = text14_222)
                    ItemTime(
                        index = -1,
                        time = TimeReservation(
                            time = stringResource(R.string.selectable),
                            isSelected = false
                        ),
                        modifier = Modifier.width(82.dp)
                    )
                    ItemTime(
                        index = -1,
                        time = TimeReservation(
                            time = stringResource(R.string.select),
                            isSelected = true
                        ),
                        modifier = Modifier.width(82.dp)
                    )
                }

                //TODO: Number Reservation
                Text(
                    stringResource(R.string.number_reservation_per_hour),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp)
                )
                TextFieldCustom(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .height(40.dp),
                    hint = stringResource(R.string.please_enter_number_people_who_can_make_reservation),
                    textStateId = viewModel.textStateNumReservation
                )

                //TODO: Payment method
                Text(
                    stringResource(R.string.payment_method),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp)
                )
                TextFieldCustom(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .height(40.dp),
                    hint = stringResource(R.string.please_enter_payment_method),
                    textStateId = viewModel.textStateNumReservation
                )

                //TODO: attach Ducument
                Text(
                    stringResource(R.string.attach_document),
                    style = text14_222,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    stringResource(R.string.business_registration),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp)
                )
                val fileName = remember { mutableStateOf("") }
                callbackAttachFile = { uri ->
                    val file = File(
                        RealPathUtil.getPath(
                            this@ApplyCompanyFragment.requireContext(),
                            uri
                        )
                    )
                    fileName.value = file.name
                    viewModel.fileName =
                        NameUtils.setFileName(userDetail?.id, file)
                    viewModel.filePath = file.path
                }
                Row(
                    Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp)
                        .height(40.dp)
                        .border(
                            width = 1.dp,
                            color = ColorUtils.gray_E1E1E1,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 9.dp)
                        .noRippleClickable {
                            checkPermissBeforeAttachFile(this@ApplyCompanyFragment.requireContext()) {
                                val intent = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                                intent.type = "image/*"
                                startForResultCallback.launch(intent)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        fileName.value,
                        style = text14_222,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_attach_ghim),
                        contentDescription = null
                    )
                }

                Text(
                    stringResource(R.string.attach_file_note_1),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 12.sp
                )
                Text(
                    stringResource(R.string.attach_file_note_2),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 12.sp
                )
                Text(
                    stringResource(R.string.attach_file_note_3),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 12.sp
                )
                Text(
                    "(.ppt, .pptx, .doc, .docx, .xls, ,xlsx, .pdf, .hwp, .txt .jpg, .png, .gif, . svg, .bmp, zip, 7z)",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 12.sp
                )

                Row(
                    Modifier
                        .padding(top = 77.dp)
                        .height(52.dp)
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(ColorUtils.blue_2177E4)
                            .noRippleClickable {
                                viewModel.makeCompany(accessToken!!)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.modifier_completed),
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(ColorUtils.gray_222222)
                            .noRippleClickable {
                                viewController?.pushFragment(
                                    ScreenId.SCREEN_APPLY_COMPANY_PRODUCT_INFO,
                                    ProductCompanyFragment.newInstance()
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.edit_prod_info),
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    private val startResultRepresentativeCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = copyFileOnlyAndroid10(data?.data)
                callbackListImage?.invoke(uri)
            }
        }

    private val startForResultCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = copyFileOnlyAndroid10(data?.data)
                callbackAttachFile?.invoke(uri)
            }
        }

    @Composable
    private fun CategorySeletion() {
        val options = viewModel.listCategoryState
        var expanded by remember { mutableStateOf(false) }
        LaunchedEffect(viewModel.listCategoryState.size) {
            viewModel.selectedOptionCategory.value =
                if (options.size > 0) options[0] else CategoryModel()
        }
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
                viewModel.selectedOptionCategory.value.name ?: "",
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
                            viewModel.selectedOptionCategory.value = selectionOption
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption.name!!)
                    }
                }
            }
        }
    }

    @Composable
    private fun RepresentativeImage(count: MutableState<Int>, onClick: () -> Unit) {
        Column(
            Modifier
                .padding(start = 16.dp, top = 4.dp, end = 8.dp)
                .size(72.dp)
                .border(width = 1.dp, color = ColorUtils.blue_2177E4.copy(alpha = 0.3f))
                .noRippleClickable {
                    onClick.invoke()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(R.drawable.ic_choose_image), contentDescription = null)
            Text(
                "${count.value}/5",
                color = ColorUtils.blue_2177E4,
                modifier = Modifier
                    .padding(top = 3.dp)
                    .alpha(0.3f),
                fontSize = 11.sp
            )
        }

    }

    @Composable
    private fun ItemImagePicked(position: Int, obj: FileModel) {
        ConstraintLayout(
            Modifier
                .padding(end = 4.dp)
                .size(76.dp)
        ) {
            val (content, close) = createRefs()
            GlideImage(
                imageModel = obj.url,
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
                hint = stringResource(R.string.please_enter_company_name_en),
                textStateId = viewModel.textStateNameEng
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_name_ph),
                textStateId = viewModel.textStateNamePhi
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_name_kr),
                textStateId = viewModel.textStateNameKr
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_name_cn),
                textStateId = viewModel.textStateNameCN
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_name_jp),
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
                    .weight(1f),
                list = GetDummyData.getSortFavoriteCompany(LocalContext.current),
                initValue = GetDummyData.getSortFavoriteCompany(LocalContext.current)[0]
            )
            BaseSeletion(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp),
                list = GetDummyData.getSortFavoriteCompany(LocalContext.current),
                initValue = GetDummyData.getSortFavoriteCompany(LocalContext.current)[0]
            )
            BaseSeletion(
                modifier = Modifier.weight(1f),
                list = GetDummyData.getSortFavoriteCompany(LocalContext.current),
                initValue = GetDummyData.getSortFavoriteCompany(LocalContext.current)[0]
            )
        }
    }

    @Composable
    private fun BaseSeletion(
        modifier: Modifier = Modifier,
        list: MutableList<KeyValueModel>,
        initValue: KeyValueModel,
        callback: ((KeyValueModel) -> Unit)? = null
    ) {
        val options = list
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(initValue) }
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
                            callback?.invoke(selectionOption)
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
                hint = stringResource(R.string.please_enter_company_description_en),
                textStateId = viewModel.textStateDesEng
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_description_ph),
                textStateId = viewModel.textStateDesPhi
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_description_kr),
                textStateId = viewModel.textStateDesKr
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_description_cn),
                textStateId = viewModel.textStateDesCN
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_company_description_jp),
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
            Text(
                stringResource(R.string.choose_service),
                style = text14_222,
                fontWeight = FontWeight.Bold
            )
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

    @Preview
    @Composable
    private fun OpenTimeUI() {
        Column(
            Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                stringResource(R.string.open_time_label),
                style = text14_222,
                fontWeight = FontWeight.Bold
            )
            Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                BaseSeletion(
                    modifier = Modifier.width(110.dp),
                    list = GetDummyData.getTime1Hour(),
                    initValue = KeyValueModel("null", "오픈")
                ) {
                    viewModel.textStateOpenTime.value = it.id!!.toInt()
                }
                Text("~", modifier = Modifier.padding(horizontal = 4.dp))
                BaseSeletion(
                    modifier = Modifier.width(110.dp),
                    list = GetDummyData.getTime1Hour(),
                    initValue = KeyValueModel("null", "마감")
                ) {
                    viewModel.textStateCloseTime.value = it.id!!.toInt()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Preview
    @Composable
    private fun ReservationTime() {
        Column(
            Modifier
                .padding(top = 20.dp, bottom = 12.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text("예약 가능 시간", style = text14_222, fontWeight = FontWeight.Bold)
            Text(
                "예약 가능한 시간을 선택해 주세요.",
                color = ColorUtils.gray_222222,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            val listTime =
                remember {
                    mutableStateListOf(
                        TimeReservation("00:00"), TimeReservation("00:30"),
                        TimeReservation("01:00"), TimeReservation("01:30"),
                        TimeReservation("02:00"), TimeReservation("02:30"),
                        TimeReservation("03:00"), TimeReservation("03:30"),
                        TimeReservation("04:00"), TimeReservation("04:30"),
                        TimeReservation("05:00"), TimeReservation("05:30"),
                        TimeReservation("06:00"), TimeReservation("06:30"),
                        TimeReservation("07:00"), TimeReservation("07:30"),
                        TimeReservation("08:00"), TimeReservation("08:30"),
                        TimeReservation("09:00"), TimeReservation("09:30"),
                        TimeReservation("10:00"), TimeReservation("10:30"),
                        TimeReservation("11:00"), TimeReservation("11:30"),
                        TimeReservation("12:00"), TimeReservation("12:30"),
                        TimeReservation("13:00"), TimeReservation("13:30"),
                        TimeReservation("14:00"), TimeReservation("14:30"),
                        TimeReservation("15:00"), TimeReservation("15:30"),
                        TimeReservation("16:00"), TimeReservation("16:30"),
                        TimeReservation("17:00"), TimeReservation("17:30"),
                        TimeReservation("18:00"), TimeReservation("18:30"),
                        TimeReservation("19:00"), TimeReservation("19:30"),
                        TimeReservation("20:00"), TimeReservation("20:30"),
                        TimeReservation("21:00"), TimeReservation("21:30"),
                        TimeReservation("22:00"), TimeReservation("22:30"),
                        TimeReservation("23:00"), TimeReservation("23:30"),
                    )
                }

            onClickChoose = { index ->
                if (index >= 0) {
                    val temp = listTime[index].apply {
                        isSelected = !isSelected
                    }
                    listTime.removeAt(index)
                    listTime.add(index, temp)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, 1000.dp)
                    .wrapContentHeight()
            ) {
                LazyVerticalGrid(
//                    state = rememberLazyListState(),
                    cells = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(listTime) { index, time ->
                        ItemTime(index, time)
                    }
                }
            }
        }
    }

    class TimeReservation(val time: String, var isSelected: Boolean = false)

    @Composable
    private fun ItemTime(index: Int, time: TimeReservation, modifier: Modifier = Modifier) {
        Box(
            modifier
                .padding(top = 8.dp)
                .height(32.dp)
                .border(
                    width = 1.dp,
                    color = if (time.isSelected) ColorUtils.blue_2177E4 else ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(if (time.isSelected) ColorUtils.blue_E9F1FC else ColorUtils.white_FFFFFF)
                .noRippleClickable {
                    onClickChoose?.invoke(index)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                time.time,
                color = if (time.isSelected) ColorUtils.blue_2177E4 else ColorUtils.gray_626262,
                fontSize = 14.sp
            )
        }
    }
}