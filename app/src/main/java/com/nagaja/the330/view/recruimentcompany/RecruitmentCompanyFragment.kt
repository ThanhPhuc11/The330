package com.nagaja.the330.view.recruimentcompany

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.HandleSortUI
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.point.PointFragment

class RecruitmentCompanyFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = RecruitmentCompanyFragment(

        )
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    val onItemClick: () -> Unit = {
       Toast.makeText(requireContext(), "Click item", Toast.LENGTH_LONG).show()
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Preview
    @Composable
    override fun UIData() {
        // TODO replace dummy data
        val data = GetDummyData.getRecruitmentList()

        LayoutTheme330 {
            Header(stringResource(R.string.option_regular_list)) {
                viewController?.popFragment()
            }

            Column(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 16.dp)
                        .height(IntrinsicSize.Max)
                ) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .noRippleClickable {
                                   Toast.makeText(requireContext(), "click", Toast.LENGTH_LONG).show()
                        },
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(text = "상담중 ${data.size}건",
                            color = ColorUtils.black_000000,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Start
                        )
                    }

                    Box(Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        HandleSortUI(context = requireContext(), GetDummyData.getSortReservationRoleCompany(context = requireContext()))
                    }
                }
                Divider(color = ColorUtils.gray_E1E1E1)
                LazyColumn(
                    Modifier.padding(top = 16.dp),
                    state = rememberLazyListState()
                ) {
                    itemsIndexed(data) {_, item ->
                        ItemSingleColumn(item, onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSingleColumn(
    item: RecruitmentJobsModel =  RecruitmentJobsModel().apply {
        id = 1
        title = "title"
        body = "설명내용 일부 (등록된 글의 앞부분만 2줄까지 표기, 이후 ...) 설명내용 일부 (등록된 글의 앞부분만 2줄까지 표기, 이후 ...)"
        createdOn = "2022/03/18"
    },
    onClick: () -> Unit
) {
    Column(Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        .height(IntrinsicSize.Max)
        .clickable {
            onClick.invoke()
        }
    ) {
        Text(text = "${item.title}",
            modifier = Modifier.fillMaxWidth(),
            color = ColorUtils.gray_222222,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "${item.createdOn}",
            modifier = Modifier.fillMaxWidth()
                .padding(top = 5.dp),
            color = ColorUtils.gray_9F9F9F,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "${item.body}",
            modifier = Modifier.fillMaxWidth()
                .padding(top = 5.dp, bottom = 16.dp),
            color = ColorUtils.gray_626262,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Divider(Modifier.height(1.dp)
            .background(ColorUtils.gray_E1E1E1),
        )
    }
}