package com.nagaja.the330.view.applycompanyproduct

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.ProductModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.NameUtils
import com.nagaja.the330.utils.RealPathUtil
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.applycompany.ShareApplyCompanyVM
import com.nagaja.the330.view.applycompanyresult.ApplyResultFragment
import com.skydoves.landscapist.glide.GlideImage
import java.io.File

class ProductCompanyFragment : BaseFragment() {
    private lateinit var viewModel: ProductCompanyVM
    private lateinit var shareViewModel: ShareApplyCompanyVM
    private var onClickRemove: ((Int) -> Unit)? = null
    private var onClickChoose: ((Int) -> Unit)? = null
    private var callbackListImage: ((Uri?) -> Unit)? = null

    companion object {
        fun newInstance() = ProductCompanyFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ProductCompanyVM::class.java]
        shareViewModel =
            ViewModelProvider(activity?.supportFragmentManager?.findFragmentByTag(ScreenId.SCREEN_APPLY_COMPANY)!!)[ShareApplyCompanyVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.callbackMakeSuccess.observe(viewLifecycleOwner) {
            hideLoading()
            viewController?.pushFragment(
                ScreenId.SCREEN_APPLY_COMPANY_RESULT,
                ApplyResultFragment.newInstance()
            )
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

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
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
                Row(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.product_info),
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        Modifier
                            .width(76.dp)
                            .height(32.dp)
                            .background(ColorUtils.gray_222222, shape = RoundedCornerShape(99.dp))
                            .noRippleClickable {
                                viewModel.addProductToList()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.add),
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    stringResource(R.string.product_image),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 13.dp, bottom = 20.dp, start = 16.dp)
                )

                Row {
                    val listImage = viewModel.listImageProduct
                    val count = remember { mutableStateOf(listImage.size) }
                    callbackListImage = { uri ->
                        val fileTemp = File(
                            RealPathUtil.getPath(
                                this@ProductCompanyFragment.requireContext(),
                                uri
                            )
                        )
                        val fileNameTemp = NameUtils.setFileName(userDetailBase?.id, fileTemp)
                        val fileModelTemp = FileModel(
                            uri = uri.toString(),
                            fileName = fileNameTemp,
                            url = fileTemp.path
                        )
                        listImage.add(fileModelTemp)
                        viewModel.mapTotalImage[fileNameTemp] = fileModelTemp
//                        Log.e("VCL_IMAGE", fileNameTemp)
                    }
                    LaunchedEffect(listImage.size) {
                        count.value = listImage.size
                    }
                    RepresentativeImage(count) {
                        if (count.value == 5) return@RepresentativeImage
                        checkPermissBeforeAttachFile(this@ProductCompanyFragment.requireContext()) {
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

                ProductNameInput()

                Text(
                    stringResource(R.string.price),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.market_price),
                        style = text14_222,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    val check = remember { mutableStateOf(false) }
                    Checkbox(
                        checked = check.value,
                        onCheckedChange = {
                            check.value = !check.value
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ColorUtils.black_000000,
//                        checkmarkColor = ColorUtils.white_FFFFFF
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                }

                PriceInput()

                Text(
                    stringResource(R.string.product_descript_remark),
                    style = text14_222,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp, top = 20.dp)
                )

                ProductDescriptionInput()
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                val listProductValidateDraft = viewModel.listProductValidate
                Column {
                    listProductValidateDraft.forEachIndexed { index, obj ->
                        ItemProductValidate(obj) {
                            listProductValidateDraft.removeAt(index)
                        }
                    }
                }

                //TODO: Button complete
                Box(
                    Modifier
                        .padding(top = 100.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(ColorUtils.blue_2177E4)
                        .noRippleClickable {
                            viewModel.makeCompany(
                                accessToken!!,
                                shareViewModel.companyInfoState.value
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.register_request),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
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
    private fun ProductNameInput() {
        Column(Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_name_en),
                textStateId = viewModel.textStateNameEng
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_name_ph),
                textStateId = viewModel.textStateNamePhi
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_name_kr),
                textStateId = viewModel.textStateNameKr
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_name_cn),
                textStateId = viewModel.textStateNameCN
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_name_jp),
                textStateId = viewModel.textStateNameJP
            )
        }
    }

    @Preview
    @Composable
    private fun ProductDescriptionInput() {
        Column(Modifier.padding(horizontal = 16.dp)) {
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_description_en),
                textStateId = viewModel.textStateDesEng
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_description_ph),
                textStateId = viewModel.textStateDesPhi
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_description_kr),
                textStateId = viewModel.textStateDesKr
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_description_cn),
                textStateId = viewModel.textStateDesCN
            )
            TextFieldCustom(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(40.dp),
                hint = stringResource(R.string.please_enter_product_description_jp),
                textStateId = viewModel.textStateDesJP
            )
        }
    }

    @Preview
    @Composable
    private fun PriceInput() {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {
            PriceUI(
                symbol = "₱",
                hint = stringResource(R.string.please_enter_price),
                viewModel.textStatePeso,
            )
            PriceUI(
                symbol = "$",
                hint = stringResource(R.string.please_enter_price),
                viewModel.textStateDollar
            )
            PriceUI(
                symbol = "₩",
                hint = stringResource(R.string.please_enter_price),
                viewModel.textStateWon
            )
        }
    }

    @Composable
    private fun PriceUI(symbol: String, hint: String, textStateId: MutableState<TextFieldValue>) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
        ) {
            Text(
                symbol,
                color = ColorUtils.gray_222222,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(12.dp),
                textAlign = TextAlign.Center
            )
            TextFieldCustom(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                hint = hint,
                textStateId = textStateId,
                inputType = KeyboardType.Number
            )
        }
    }

    @Composable
    private fun ItemProductValidate(obj: ProductModel = ProductModel(), onClick: () -> Unit) {
        Row(
            Modifier
                .padding(bottom = 7.dp)
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier
                    .padding(end = 10.dp)
                    .fillMaxHeight()
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${obj.name?.getOrNull(0)?.name}",
                    color = ColorUtils.gray_222222,
                    fontSize = 12.sp
                )
            }
            Box(
                Modifier
                    .size(78.dp, 40.dp)
                    .background(color = ColorUtils.gray_9F9F9F, shape = RoundedCornerShape(4.dp))
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("삭제", color = ColorUtils.white_FFFFFF)
            }
        }
    }
}