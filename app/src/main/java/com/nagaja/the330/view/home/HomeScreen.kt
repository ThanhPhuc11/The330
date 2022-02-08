package com.nagaja.the330.view.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.google.gson.Gson
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Preview
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
    val homeVM = ViewModelProvider(
        viewModelStoreOwner,
        ViewModelFactory(
            RetrofitBuilder.getInstance(context)
                ?.create(ApiService::class.java)!!
        )
    )[HomeScreenVM::class.java]

    LayoutTheme330 {
        DisposableEffect(Unit) {
            var accessToken: String
            CoroutineScope(Dispatchers.IO).launch {
                context.dataStore.data.map { get ->
                    get[DataStorePref.AUTH_TOKEN] ?: ""
                }.collect {
                    val tokenModel = Gson().fromJson(it, AuthTokenModel::class.java)
                    accessToken = "Bearer ${tokenModel?.accessToken}"
                    homeVM.getCategory(accessToken, "MAIN")
                }
            }
            onDispose { }
        }

        LogoAndSearch()
        SearchFilter()
        CategoryMain(homeVM)
    }
}

@Composable
private fun LogoAndSearch() {
    Row(
        Modifier
            .padding(vertical = 7.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash_png),
            contentDescription = "",
            colorFilter = ColorFilter.tint(ColorUtils.blue_2177E4),
            modifier = Modifier.height(16.dp)
        )
        Row(
            Modifier
                .padding(start = 18.dp)
                .height(32.dp)
                .weight(1f)
//                .clip(RoundedCornerShape(4.dp))
                .background(ColorUtils.blue_2177E4_opacity_5, RoundedCornerShape(4.dp))
                .border(1.dp, ColorUtils.blue_2177E4_opacity_10, RoundedCornerShape(4.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "",
                Modifier.padding(horizontal = 10.dp)
            )
            Text(
                "검색어를 입력해 보세요.",
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun SearchFilter() {
    Row(
        Modifier.padding(horizontal = 16.dp)
    ) {
        BoxSearch(
            modifier = Modifier
                .weight(1f)
                .padding(end = 7.dp),
            options = GetDummyData.getCoutryAdrressSignup()
        )
        BoxSearch(
            modifier = Modifier
                .weight(1f)
                .padding(end = 7.dp),
            options = GetDummyData.getCoutryAdrressSignup()
        )
        BoxSearch(modifier = Modifier.weight(1f), options = GetDummyData.getCoutryAdrressSignup())
    }
}

@Composable
private fun BoxSearch(modifier: Modifier = Modifier, options: MutableList<KeyValueModel>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    Row(
        modifier = modifier
            .noRippleClickable {
                expanded = !expanded
            }
            .border(
                width = 1.dp,
                color = ColorUtils.blue_2177E4_opacity_10,
                shape = RoundedCornerShape(4.dp)
            )
            .height(44.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            selectedOptionText.name!!,
            style = text14_222,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Image(
            painter = painterResource(R.drawable.ic_arrow_filter),
            contentDescription = "",
            Modifier.rotate(if (expanded) 180f else 0f)
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

@Composable
fun IconCategory(url: String, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        GlideImage(
            imageModel = "${BuildConfig.BASE_S3}$url",
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.Fit,
//            circularReveal = CircularReveal(duration = 250),
            placeHolder = painterResource(R.drawable.ic_arrow_filter),
            error = painterResource(R.drawable.ic_arrow_filter),
            modifier = Modifier.size(36.dp)
        )
        Text(
            title,
            fontSize = 12.sp,
            color = ColorUtils.gray_222222,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(top = 10.dp),
            maxLines = 2
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoryMain(homeVM: HomeScreenVM) {
    LazyVerticalGrid(
//            state = rememberLazyListState(),
        cells = GridCells.Fixed(5),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(homeVM.listCategoryState.value) { obj ->
            IconCategory(url = obj.image ?: "", title = obj.name ?: "")
        }
    }
}