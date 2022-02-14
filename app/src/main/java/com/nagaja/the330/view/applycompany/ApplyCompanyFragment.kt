package com.nagaja.the330.view.applycompany

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.glide.GlideImage

class ApplyCompanyFragment : BaseFragment() {
    companion object {
        fun newInstance() = ApplyCompanyFragment()
    }

    @Composable
    override fun SetupViewModel() {
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

                RepresentativeImage()
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
                .padding(start = 16.dp)
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

//            val list =
//            LazyRow {
//                items()
//            }
        }

    }

    @Preview
    @Composable
    private fun ItemImagePicked() {
        ConstraintLayout(Modifier.size(76.dp)) {
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
                modifier = Modifier.constrainAs(close) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            )
        }
    }
}