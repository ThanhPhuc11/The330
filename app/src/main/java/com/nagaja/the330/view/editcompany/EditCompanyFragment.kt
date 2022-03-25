package com.nagaja.the330.view.editcompany

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.CategoryModel
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.TimeReservation
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.NameUtils
import com.nagaja.the330.utils.RealPathUtil
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.applycompany.ShareApplyCompanyVM
import com.nagaja.the330.view.applycompanyproduct.ProductCompanyFragment
import com.nagaja.the330.view.editcompanyproduct.EditProductCompanyFragment
import com.skydoves.landscapist.glide.GlideImage
import java.io.File

class EditCompanyFragment : BaseFragment() {
    private lateinit var viewModel: EditCompanyVM
    private lateinit var shareViewModel: ShareApplyCompanyVM
    private var onClickRemove: ((Int) -> Unit)? = null
    private var onClickChoose: ((Int) -> Unit)? = null
    private var callbackListImage: ((Uri?) -> Unit)? = null
    private var callbackAttachFile: ((Uri?) -> Unit)? = null

    companion object {
        fun newInstance() = EditCompanyFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[EditCompanyVM::class.java]
        shareViewModel = ViewModelProvider(this)[ShareApplyCompanyVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.callbackEditSuccess.observe(viewLifecycleOwner) {
            viewController?.popFragment()
        }
        viewModel.callbackStart.observe(viewLifecycleOwner) {
            showLoading()
        }
        viewModel.callbackSuccess.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.callbackFail.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.showMessCallback.observe(viewLifecycleOwner) {
            showMess(it)
        }
    }

    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getCompanyDetail(
                            accessToken!!,
                            userDetailBase?.companyRequest?.id ?: 0
                        )
                        viewModel.getCategory(accessToken!!)
                        viewModel.getPopularAreas(accessToken!!)
                        viewModel.getCity(accessToken!!)
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
        val fileName = remember { mutableStateOf("") }
        LaunchedEffect(viewModel.companyDetail.value) {
            val obj = viewModel.companyDetail.value
            viewModel.selectedOptionCategory.value = CategoryModel(ctype = obj.ctype)
            obj.images?.let { viewModel.listImageRepresentative.addAll(it) }
            obj.name?.associate { it -> it.lang to it.name }?.let {
                viewModel.textStateNameEng.value = TextFieldValue(it["en"] ?: "")
                viewModel.textStateNamePhi.value = TextFieldValue(it["ph"] ?: "")
                viewModel.textStateNameKr.value = TextFieldValue(it["kr"] ?: "")
                viewModel.textStateNameCN.value = TextFieldValue(it["cn"] ?: "")
                viewModel.textStateNameJP.value = TextFieldValue(it["jp"] ?: "")
            }
            viewModel.textStateAdress.value = TextFieldValue(obj.address ?: "")
            obj.description?.associate { it -> it.lang to it.name }?.let {
                viewModel.textStateDesEng.value = TextFieldValue(it["en"] ?: "")
                viewModel.textStateDesPhi.value = TextFieldValue(it["ph"] ?: "")
                viewModel.textStateDesKr.value = TextFieldValue(it["kr"] ?: "")
                viewModel.textStateDesCN.value = TextFieldValue(it["cn"] ?: "")
                viewModel.textStateDesJP.value = TextFieldValue(it["jp"] ?: "")
            }
            viewModel.textStateName.value = TextFieldValue(obj.chargeName ?: "")
            viewModel.textStatePhone.value = TextFieldValue(obj.chargePhone ?: "")
            viewModel.textStateMailAddress.value = TextFieldValue(obj.chargeEmail ?: "")
            viewModel.textStateFb.value = TextFieldValue(obj.chargeFacebook ?: "")
            viewModel.textStateKakao.value = TextFieldValue(obj.chargeKakao ?: "")
            viewModel.textStateLine.value = TextFieldValue(obj.chargeLine ?: "")

            viewModel.textStateOpenTime.value = obj.openHour ?: 0
            viewModel.textStateCloseTime.value = obj.closeHour ?: 0

//            viewModel.reservationTime = obj.reservationTime?.toMutableList()

            viewModel.textStateNumReservation.value =
                TextFieldValue((obj.reservationNumber ?: 0).toString())
            viewModel.textStatePaymethod.value = TextFieldValue(obj.paymentMethod ?: "")
            fileName.value = obj.file.toString()
            viewModel.fileName
            viewModel.serviceType.onEachIndexed {index, it->
                obj.serviceTypes?.onEach { iz->
                    if (it.id == iz) {
                        viewModel.serviceType[index] = it.copy(isSelected = true)
                    }
                }
            }
            obj.reservationTime?.onEach {
                viewModel.stateListTime[it] = viewModel.stateListTime[it].copy(isSelected = true)
                viewModel.reservationTime!!.add(it)
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
                        val fileTemp = File(
                            RealPathUtil.getPath(
                                this@EditCompanyFragment.requireContext(),
                                uri
                            )
                        )
                        listImage.add(FileModel(url = uri.toString()))
                        viewModel.listImageRepresentative.add(
                            FileModel(
                                fileName = NameUtils.setFileName(userDetailBase?.id, fileTemp),
                                url = fileTemp.path
                            )
                        )
                    }
                    val count = remember { mutableStateOf(listImage.size) }
                    LaunchedEffect(listImage.size) {
                        count.value = listImage.size
                    }
                    RepresentativeImage(count) {
                        if (count.value == 5) return@RepresentativeImage
                        checkPermissBeforeAttachFile(this@EditCompanyFragment.requireContext()) {
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
                        viewModel.listImageRepresentative.removeAt(index)
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
                //TODO: Area Selection
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
                    textStateId = viewModel.textStateNumReservation,
                    inputType = KeyboardType.Number
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
                    textStateId = viewModel.textStatePaymethod
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

                callbackAttachFile = { uri ->
                    val file = File(
                        RealPathUtil.getPath(
                            this@EditCompanyFragment.requireContext(),
                            uri
                        )
                    )
                    fileName.value = file.name
                    viewModel.fileName =
                        NameUtils.setFileName(userDetailBase?.id, file)
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
                            checkPermissBeforeAttachFile(this@EditCompanyFragment.requireContext()) {
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
                    //TODO: Button completed without product
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(ColorUtils.blue_2177E4)
                            .noRippleClickable {
                                viewModel.editCompany(accessToken!!)
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

                    //TODO: Button next product input
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(ColorUtils.gray_222222)
                            .noRippleClickable {
//                                if (viewModel.isValidate()) {
//                                    shareViewModel.companyInfoState.value =
//                                        viewModel.saveCompanyTransfer()
//                                    viewController?.pushFragment(
//                                        ScreenId.SCREEN_APPLY_COMPANY_PRODUCT_INFO,
//                                        EditProductCompanyFragment.newInstance()
//                                    )
//                                }
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
                requestOptions = {
                    RequestOptions()
                        .override(360, 360)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                },
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
                list = viewModel.listPopularAreas.map {
                    KeyValueModel(it.id.toString(), it.name?.getOrNull(0)?.name)
                }.toMutableList(),
                initValue = KeyValueModel(null, "인기지역")
            ) {
                viewModel.popularAreaId = it.id?.toInt()
            }
            BaseSeletion(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp),
                list = viewModel.listCity.map {
                    KeyValueModel(it.id.toString(), it.name?.getOrNull(0)?.name)
                }.toMutableList(),
                initValue = KeyValueModel(null, "시/도")
            ) {
                it.id?.let { item ->
                    viewModel.cityId = item.toInt()
                    viewModel.getDistrict(accessToken!!, item.toInt())
                }
            }
            BaseSeletion(
                modifier = Modifier.weight(1f),
                list = viewModel.listDistrict.map {
                    KeyValueModel(it.id.toString(), it.name?.getOrNull(0)?.name)
                }.toMutableList(),
                initValue = KeyValueModel("null", "구/군")
            ) {
                viewModel.popularAreaId = it.id?.toInt()
            }
        }
    }

    @Composable
    private fun BaseSeletion(
        modifier: Modifier = Modifier,
        list: MutableList<KeyValueModel>,
        initValue: KeyValueModel,
        setValue: MutableState<KeyValueModel> = mutableStateOf(initValue),
        callback: ((KeyValueModel) -> Unit)? = null
    ) {
        val options = list
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(initValue) }

        LaunchedEffect(setValue.value) {
            selectedOptionText = setValue.value
            Log.e("Side", setValue.value.id ?: "")
        }
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
                        Text(text = selectionOption.name ?: "")
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
                textStateId = viewModel.textStatePhone,
                inputType = KeyboardType.Number
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
        val isChooseTab1 = remember { mutableStateOf(viewModel.serviceType[0].isSelected) }
        val isChooseTab2 = remember { mutableStateOf(viewModel.serviceType[1].isSelected) }
        val isChooseTab3 = remember { mutableStateOf(viewModel.serviceType[2].isSelected) }
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
                    isSelected = isChooseTab1.value,
                    onClick = {
                        viewModel.serviceType[0].isSelected = !viewModel.serviceType[0].isSelected
                        isChooseTab1.value = !isChooseTab1.value
                    }
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
                    isSelected = isChooseTab2.value,
                    onClick = {
                        viewModel.serviceType[1].isSelected = !viewModel.serviceType[1].isSelected
                        isChooseTab2.value = !isChooseTab2.value
                    }
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
                    isSelected = isChooseTab3.value,
                    onClick = {
                        viewModel.serviceType[2].isSelected = !viewModel.serviceType[2].isSelected
                        isChooseTab3.value = !isChooseTab3.value
                    }
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

    @SuppressLint("UnrememberedMutableState")
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
                val mapTime1h = GetDummyData.getTime1Hour().associate { it.id to it.name }
                BaseSeletion(
                    modifier = Modifier.width(110.dp),
                    list = GetDummyData.getTime1Hour(),
                    initValue = KeyValueModel("null", "오픈"),
                    setValue = mutableStateOf(
                        KeyValueModel(
                            viewModel.textStateOpenTime.value.toString(),
                            mapTime1h[viewModel.textStateOpenTime.value.toString()]
                        )
                    )
                ) {
                    viewModel.textStateOpenTime.value = it.id!!.toInt()
                }
                Text("~", modifier = Modifier.padding(horizontal = 4.dp))
                BaseSeletion(
                    modifier = Modifier.width(110.dp),
                    list = GetDummyData.getTime1Hour(),
                    initValue = KeyValueModel("null", "마감"),
                    setValue = mutableStateOf(
                        KeyValueModel(
                            viewModel.textStateOpenTime.value.toString(),
                            mapTime1h[viewModel.textStateCloseTime.value.toString()]
                        )
                    )
                ) {
                    viewModel.textStateCloseTime.value = it.id!!.toInt()
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
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

            LaunchedEffect(viewModel.reservationTime) {
                viewModel.reservationTime?.forEach { obj ->
                    val temp = TimeReservation(viewModel.stateListTime[obj].time, true)
                    viewModel.stateListTime.removeAt(obj)
                    viewModel.stateListTime.add(temp)
                }
            }

            onClickChoose = { index ->
                val listTime = viewModel.stateListTime
                if (index >= 0) {
                    listTime[index] = listTime[index].copy(isSelected = !listTime[index].isSelected)
                    viewModel.reservationTime!!.add(index)
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
                    itemsIndexed(viewModel.stateListTime) { index, time ->
                        ItemTime(index, time)
                    }
                }
            }
        }
    }

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