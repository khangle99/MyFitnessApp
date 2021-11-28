package com.khangle.myfitnessapp.ui.exclog.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.exclog.NutritionViewModel
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthScrollListener
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class NutritionCategoryFragment : Fragment() {

    private val viewModel: NutritionViewModel by activityViewModels()
    private lateinit var calendarView: CalendarView
    private lateinit var yearTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var progressBar: ProgressBar
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val today = LocalDate.now()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_log, container, false)
        calendarView = view.findViewById(R.id.exOneCalendar)
        yearTextView = view.findViewById(R.id.exOneYearText)
        monthTextView = view.findViewById(R.id.exOneMonthText)
        progressBar = view.findViewById(R.id.excLogProgress)
        setupCalendar()

        return view
    }

    private fun setupCalendar() {
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        today == day.date -> {
                            textView.setTextColorRes(R.color.desgin_tomator)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                        }
                    }
                    viewModel.excLogOfMonth.value?.let {
                        val find = it.find {
                            it.dateInMonth.equals(day.day.toString())
                        }
                        if (find != null) {
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        } else {
                            textView.background = null
                        }
                    }

                } else {
                    textView.setTextColorRes(R.color.gray)
                    textView.background = null
                }
            }
        }
        calendarView.monthScrollListener = object : MonthScrollListener {
            override fun invoke(p1: CalendarMonth) {
                yearTextView.text = p1.yearMonth.year.toString()
                monthTextView.text = monthTitleFormatter.format(p1.yearMonth)

                val year = p1.year % 100
                val month = p1.month
                val myStr = month.toString() + year.toString()
                progressBar.visibility = View.VISIBLE
                viewModel.loadExcLogOfMonth(myStr)
            }
        }

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.excLogOfMonth.observe(viewLifecycleOwner) {
            progressBar.visibility = View.INVISIBLE
            calendarView.notifyCalendarChanged()
        }

    }

}

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.exOneDayText)
    lateinit var day: CalendarDay
    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
            // kiem tra ngay dc chon co phai ngay da log
            Toast.makeText(view.context,"Select",Toast.LENGTH_SHORT).show()
            // neu co thi navigate hien detail ngay hom do
            }
        }
    }
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))
internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)