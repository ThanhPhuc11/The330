package com.nagaja.the330.view.secondhandregis

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.NameUtils
import com.nagaja.the330.utils.RealPathUtil
import com.nagaja.the330.view.HeaderOption
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_62
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

class SecondHandRegisFragment : BaseFragment() {
    private lateinit var viewModel: SecondHandRegisVM
    private var userDetail: UserDetail? = null
    private var callbackListImage: ((Uri?) -> Unit)? = null
    private var onClickRemove: ((Int) -> Unit)? = null

    companion object {
        fun newInstance() = SecondHandRegisFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[SecondHandRegisVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.callbackPostSuccess.observe(viewLifecycleOwner) {
            showMessDEBUG("Success")
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
        val listCategory = GetDummyData.getSecondHandCategory()
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.category = listCategory[0].id!!
                        accessToken?.let { viewModel.getCity(it) }
                        getUserDetailFromDataStore(requireContext())
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
            HeaderOption(
                title = "문의하기",
                clickBack = { viewController?.popFragment() },
                optionText = "등록",
                clickOption = {
                    accessToken?.let { viewModel.register(it) }
                })

            //TODO: Category dropdown
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(ColorUtils.blue_2177E4_opacity_5)
                    .padding(vertical = 6.dp, horizontal = 16.dp)
            ) {
                BaseDropDown(listData = listCategory,
                    onClick = {
                        viewModel.category = it
                    })
            }

            val stateEdtTitle = viewModel.stateEdtTitle
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ColorUtils.white_FFFFFF)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                //TODO: City
                BaseDropDown(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    listData = viewModel.listCity.map { cityModel ->
                        KeyValueModel(cityModel.id.toString(), cityModel.name?.get(0)?.name)
                    }.toMutableList(),
                    onClick = { id ->
                        viewModel.city = id
                        viewModel.getDistrict(accessToken!!, id.toInt())
                    },
                    hintText = "시/도",
                    hasDefaultFirstItem = false
                )
                //TODO: District
                LaunchedEffect(viewModel.listDistrict) {
                    viewModel.district = viewModel.listDistrict.getOrNull(0)?.id.toString()
                }
                BaseDropDown(
                    modifier = Modifier.weight(1f),
                    listData = viewModel.listDistrict.map { district ->
                        KeyValueModel(district.id.toString(), district.name?.get(0)?.name)
                    }.toMutableList(),
                    hintText = "구/군",
                    onClick = {
                        viewModel.district = it
                    }
                )
            }

            //TODO: Choose image
            Row(Modifier.padding(vertical = 8.dp)) {
                val listImage = remember {
                    mutableStateListOf<FileModel>()
                }
                callbackListImage = { uri ->
                    val fileTemp = File(
                        RealPathUtil.getPath(
                            this@SecondHandRegisFragment.requireContext(),
                            uri
                        )
                    )
                    listImage.add(FileModel(url = uri.toString()))
                    viewModel.listImage.add(
                        FileModel(
                            fileName = NameUtils.setFileName(userDetail?.id, fileTemp),
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
                    checkPermissBeforeAttachFile(this@SecondHandRegisFragment.requireContext()) {
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
                    viewModel.listImage.removeAt(index)
                }
                LazyRow {
                    itemsIndexed(listImage) { index, obj ->
                        ItemImagePicked(index, obj)
                    }
                }
            }

            //TODO: Enter title
            BasicTextField(
                value = stateEdtTitle.value,
                onValueChange = {
                    if (it.text.length <= 30) stateEdtTitle.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    color = ColorUtils.black_000000
                ),
                decorationBox = { innerTextField ->
                    Row {
                        if (stateEdtTitle.value.text.isEmpty()) {
                            Text(
                                "제목을 입력하세요.",
                                color = ColorUtils.gray_BEBEBE,
                                fontSize = 14.sp
                            )
                        }
                    }
                    innerTextField()
                }
            )
            Divider(color = ColorUtils.gray_BEBEBE, modifier = Modifier.padding(horizontal = 16.dp))
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BaseDropDown(
                    isUseOtherModifier = true,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(100.dp)
                        .height(36.dp)
                        .background(ColorUtils.white_FFFFFF)
                        .border(
                            width = 1.dp,
                            color = ColorUtils.gray_E1E1E1,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp),
                    listData = GetDummyData.getMoneyType()
                )
//                Row(
//                    Modifier
//                        .padding(end = 8.dp)
//                        .width(100.dp)
//                        .height(36.dp)
//                        .background(ColorUtils.white_FFFFFF)
//                        .border(
//                            width = 1.dp,
//                            color = ColorUtils.gray_E1E1E1,
//                            shape = RoundedCornerShape(4.dp)
//                        )
//                        .padding(horizontal = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        "P",
//                        style = text14_62,
//                        modifier = Modifier.weight(1f),
//                        textAlign = TextAlign.Start
//                    )
//                    Image(
//                        painter = painterResource(R.drawable.ic_arrow_dropdown),
//                        contentDescription = null
//                    )
//                }

                //TODO: Edt Purchase
                val stateEdtPurchase = viewModel.stateEdtPurchase
                BasicTextField(
                    value = stateEdtPurchase.value,
                    onValueChange = {
                        if (it.text.length <= 10) stateEdtPurchase.value = it
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = ColorUtils.black_000000
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Row {
                            if (stateEdtPurchase.value.text.isEmpty()) {
                                Text(
                                    "판매 금액을 입력하세요.",
                                    color = ColorUtils.gray_BEBEBE,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        innerTextField()
                    }
                )
            }
            Divider(color = ColorUtils.gray_BEBEBE, modifier = Modifier.padding(horizontal = 16.dp))

            val stateEdtBodyPost = viewModel.stateEdtBody
            BasicTextField(
                value = stateEdtBodyPost.value,
                onValueChange = {
                    if (it.text.length <= 5000) stateEdtBodyPost.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    color = ColorUtils.black_000000
                ),
                decorationBox = { innerTextField ->
                    Row {
                        if (stateEdtBodyPost.value.text.isEmpty()) {
                            Text(
                                "게시글의 본문을 입력하세요.",
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

    @Composable
    private fun BaseDropDown(
        modifier: Modifier = Modifier,
        isUseOtherModifier: Boolean = false,
        listData: MutableList<KeyValueModel>? = null,
        onClick: ((String) -> Unit)? = null,
        hintText: String? = null,
        hasDefaultFirstItem: Boolean = true
    ) {
        var expanded by remember { mutableStateOf(false) }
        val itemSelected = remember { mutableStateOf(KeyValueModel(hintText, hintText)) }
        if (hasDefaultFirstItem)
            LaunchedEffect(listData) {
                itemSelected.value =
                    if ((listData?.size ?: 0) > 0) listData!![0] else KeyValueModel(
                        hintText,
                        hintText
                    )
            }
        Row(
            modifier = if (isUseOtherModifier) modifier.noRippleClickable {
                expanded = true
            } else
                modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(ColorUtils.white_FFFFFF)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.blue_2177E4_opacity_10,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp)
                    .noRippleClickable {
                        expanded = true
                    },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                itemSelected.value.name ?: "",
                style = text14_62,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Image(
                painter = painterResource(R.drawable.ic_arrow_dropdown),
                contentDescription = null
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                listData?.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
//                            viewModel.selectedOptionCategory.value = selectionOption
                            itemSelected.value = selectionOption
                            expanded = false
                            onClick?.invoke(selectionOption.id ?: "null")
                            showMessDEBUG(itemSelected.value.id)
                        }
                    ) {
                        Text(text = selectionOption.name!!)
                    }
                }
            }
        }
    }

    private fun getUserDetailFromDataStore(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { get ->
                get[DataStorePref.USER_DETAIL] ?: ""
            }.collect {
                val userDetail = Gson().fromJson(it, UserDetail::class.java)
                userDetail?.let { this@SecondHandRegisFragment.userDetail = userDetail }
            }
        }
    }
}