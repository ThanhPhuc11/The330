package com.nagaja.the330.view.searchmain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.*
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailFragment
import com.nagaja.the330.view.reportmissingdetail.ReportMissingDetailFragment
import com.nagaja.the330.view.text14_222
import com.nagaja.the330.view.text14_62
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ItemCompany(obj: CompanyModel, onClick: () -> Unit) {
    Row(
        Modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        GlideImage(
            imageModel = "",
            Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(4.dp)),
            placeHolder = painterResource(R.drawable.ic_default_nagaja),
            error = painterResource(R.drawable.ic_default_nagaja),
        )
        Column(Modifier.padding(start = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    obj.name?.getOrNull(0)?.name ?: "",
                    color = ColorUtils.gray_222222,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                //TODO: like button
//                    ButtonLike(obj)
            }
            Text(
                "배달 불가능/ 예약 가능 (오늘마감)",
                modifier = Modifier.padding(top = 7.dp),
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
            Text(
                "픽업 가능 / 드랍 불가능",
                modifier = Modifier.padding(top = 3.dp),
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomStart
            ) {

                Text(
                    "영업시간:  09:00~20:00  (예약10:00~18:00)",
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemDisable() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(97.dp)
            .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart
    ) {
        Text(
            stringResource(R.string.This_post_has_been_made_private_by_the_administrator),
            style = text14_222
        )
    }
}

@Composable
fun ItemFreeNotice(obj: FreeNoticeModel, onClick: () -> Unit) {
    Row(
        Modifier
            .background(
                when {
                    obj.notice == true -> {
                        ColorUtils.blue_2177E4_opacity_5
                    }
                    obj.top == true -> {
                        ColorUtils.yellow_FFB800_opacity_5
                    }
                    else -> {
                        ColorUtils.white_FFFFFF
                    }
                }
            )
            .padding(16.dp)
            .fillMaxWidth()
            .noRippleClickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                "[${obj.id}] ${
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_16,
                        AppDateUtils.FORMAT_15,
                        obj.createdOn ?: ""
                    )
                }", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp
            )
            Text(
                obj.title ?: "",
                color = ColorUtils.gray_222222,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Text(
                    obj.user?.name ?: "",
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp,
                )
                Image(
                    painter = painterResource(R.drawable.ic_dot),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(2.dp),
                    colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                )
                Text(
                    stringResource(R.string.views).plus(" ${obj.viewCount ?: 0}"),
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp,
                )
            }
        }
        Box(
            Modifier
                .padding(start = 16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(200.dp))
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(200.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("${obj.commentCount ?: 0}", color = ColorUtils.gray_9F9F9F, fontSize = 14.sp)
        }
    }
}

@Composable
fun ItemRecruitmentJobs(obj: RecruitmentJobsModel, onClick: () -> Unit) {
    Column(
        Modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(top = 16.dp)
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        Row {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${obj.images?.getOrNull(0)?.url ?: ""}",
                Modifier
                    .size(96.dp)
                    .clip(shape = RoundedCornerShape(4.dp)),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
            )
            Column(
                Modifier
                    .padding(start = 12.dp)
                    .height(96.dp)
            ) {
                Text(
                    obj.title ?: "",
                    modifier = Modifier.padding(top = 1.dp),
                    color = ColorUtils.black_000000,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700)
                )
                Text(
                    stringResource(R.string.views).plus(" ${obj.viewCount ?: 0}"),
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp,
                )
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        obj.body ?: "",
                        style = text14_62,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
        ) {
            Text(
                AppDateUtils.changeDateFormat(
                    AppDateUtils.FORMAT_16,
                    AppDateUtils.FORMAT_15,
                    obj.createdOn ?: ""
                ),
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp,
            )
            Image(
                painter = painterResource(R.drawable.ic_dot),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(2.dp),
                colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
            )
            val name = if (obj.writer?.type == "COMPANY") {
                obj.writer?.companyRequest?.name?.getOrNull(0)?.name
                    ?: ""
            } else {
                obj.writer?.name ?: ""
            }
            Text(
                name,
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun ItemSecondHand(obj: SecondHandModel, onClick: () -> Unit) {
    Column(
        Modifier
            .padding(bottom = 1.dp)
            .fillMaxWidth()
            .background(ColorUtils.white_FFFFFF)
            .padding(vertical = 20.dp)
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        Row {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${obj.images?.getOrNull(0)?.url ?: ""}",
                Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(4.dp)),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja)
            )
            Column(
                Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
                    .height(96.dp)
            ) {
                Text(
                    "[${obj.type}] ${obj.title}",
                    color = ColorUtils.black_000000,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Row {
                    Text(
                        AppDateUtils.changeDateFormat(
                            AppDateUtils.FORMAT_16,
                            AppDateUtils.FORMAT_15,
                            obj.createdOn ?: ""
                        ),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_dot),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(2.dp),
                        colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                    )
                    Text(
                        stringResource(R.string.views).plus(" ${obj.viewCount ?: 0}"),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                    )
                }
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        price(obj),
                        color = ColorUtils.black_000000,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        Text(
            obj.body ?: "",
            style = text14_62,
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun ReportMissingItem(obj: ReportMissingModel, onClick: () -> Unit) {
    Column(
        Modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(top = 16.dp)
            .noRippleClickable {

            }
    ) {
        Row {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${obj.images?.getOrNull(0)?.url ?: ""}",
                Modifier
                    .size(96.dp)
                    .clip(shape = RoundedCornerShape(4.dp)),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
            )
            Column(
                Modifier
                    .padding(start = 12.dp)
                    .height(96.dp)
            ) {
                Text(
                    obj.title ?: "",
                    modifier = Modifier.padding(top = 1.dp),
                    color = ColorUtils.black_000000,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.views).plus(" ${obj.viewCount ?: 0}"),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_dot),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(2.dp),
                        colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                    )
                    Text(
                        stringResource(R.string.comment).plus(" ${obj.commentCount ?: 0}"),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                    )
                }
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        obj.body ?: "",
                        style = text14_62,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
        ) {
            Text(
                AppDateUtils.changeDateFormat(
                    AppDateUtils.FORMAT_16,
                    AppDateUtils.FORMAT_15,
                    obj.createdOn ?: ""
                ),
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp,
            )
            Image(
                painter = painterResource(R.drawable.ic_dot),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(2.dp),
                colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
            )
            val name = if (obj.writer?.type == "COMPANY") {
                obj.writer?.companyRequest?.name?.getOrNull(0)?.name
                    ?: ""
            } else {
                obj.writer?.name ?: ""
            }
            Text(
                name,
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp,
            )
        }
    }
}

private fun price(secondhand: SecondHandModel): String {
    return if ((secondhand.dollar ?: 0.0) > 0) {
        GetDummyData.getMoneyType()[1].name!!.plus(" ").plus(secondhand.dollar)
    } else {
        GetDummyData.getMoneyType()[0].name!!.plus(" ").plus(secondhand.peso)
    }
}