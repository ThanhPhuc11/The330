package com.nagaja.the330.view

import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.nagaja.the330.R
import com.nagaja.the330.utils.ColorUtils

@Preview
@Composable
fun CalendarUI(onChoose: ((String) -> Unit)? = null) {
    val context1 = LocalContext.current
    Box(Modifier.background(ColorUtils.white_FFFFFF)) {
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->
                CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom)).apply {
                    weekDayTextAppearance = R.style.CustomCalendarWeek

                }
            },
            update = { view ->
//            view.minDate = // contraints
//                view.maxDate = // contraints

                view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val strDate = "$year-${month + 1}-$dayOfMonth"
                    onChoose?.invoke(strDate)
                    Toast.makeText(context1, strDate, Toast.LENGTH_SHORT).show()
//                    onDateSelected(
//                        LocalDate
//                            .now()
//                            .withMonth(month + 1)
//                            .withYear(year)
//                            .withDayOfMonth(dayOfMonth)
//                    )
                }
            }
        )
    }
}