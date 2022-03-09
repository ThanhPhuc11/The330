package com.nagaja.the330.view.reservation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.CompanyFavoriteModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage

class ReservationFragment : BaseFragment() {
    private lateinit var viewModel: ReservationVM

    companion object {
        fun newInstance() = ReservationFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ReservationVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {

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
            Header(stringResource(R.string.option_reservation_status)) {
                viewController?.popFragment()
            }
            Box(
                Modifier
                    .padding(horizontal = 9.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    Modifier
                        .size(100.dp, 36.dp)
                        .border(width = 1.dp, color = ColorUtils.gray_E1E1E1)
                        .padding(9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "일주일",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_down),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ColorUtils.black_000000),
                        modifier = Modifier.size(10.dp, 6.dp)
                    )
                }
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BoxStatus(Modifier.weight(1f), text = "총 예약\n30건") {}
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(ColorUtils.gray_E1E1E1)
                )

                BoxStatus(Modifier.weight(1f), text = "총 예약\n30건") {}
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(ColorUtils.gray_E1E1E1)
                )

                BoxStatus(Modifier.weight(1f), text = "총 예약\n30건") {}
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(ColorUtils.gray_E1E1E1)
                )

                BoxStatus(Modifier.weight(1f), text = "총 예약\n30건") {}
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(ColorUtils.gray_E1E1E1)
                )
            }
            Divider(color = ColorUtils.gray_E1E1E1)

            val listData = mutableListOf<KeyValueModel>().apply {
                add(KeyValueModel())
                add(KeyValueModel())
                add(KeyValueModel())
            }
            LazyColumn(state = rememberLazyListState()) {
                itemsIndexed(listData) {index, obj ->
                    ItemReservation()
                    Divider(color = ColorUtils.gray_E1E1E1)
                }
            }
        }
    }

    @Composable
    private fun BoxStatus(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
        Box(
            modifier
                .fillMaxHeight()
                .noRippleClickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = ColorUtils.black_000000,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    @Preview
    @Composable
    private fun ItemReservation(obj: CompanyFavoriteModel = CompanyFavoriteModel()) {
        Row(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = "",
                Modifier.size(96.dp),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja)
            )
            Column(Modifier.padding(start = 9.dp)) {
                Text(
                    obj.target?.name?.get(0)?.name ?: "",
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    "예약일시: 2021년 10월 26일",
                    modifier = Modifier.padding(top = 3.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp
                )
                Text(
                    "사용인원: ${obj.usageCount ?: 0}인",
                    modifier = Modifier.padding(top = 3.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp
                )
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        Modifier
                            .width(76.dp)
                            .height(32.dp)
                            .background(ColorUtils.gray_222222, shape = RoundedCornerShape(99.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("예약취소", color = ColorUtils.white_FFFFFF, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}