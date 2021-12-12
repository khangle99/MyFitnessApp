package com.khangle.myfitnessapp.ui.exclog.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.dateFormat
import com.khangle.myfitnessapp.common.isSameMonth
import com.khangle.myfitnessapp.common.toFormatDate
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.user.ExcLog
import com.khangle.myfitnessapp.ui.exclog.NutritionViewModel
import com.khangle.myfitnessapp.ui.exclog.RatingDialogFragment
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthScrollListener
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.abs

@AndroidEntryPoint
class NutritionCategoryFragment : Fragment() {

    private val viewModel: NutritionViewModel by activityViewModels()
    private lateinit var calendarView: CalendarView
    private lateinit var yearTextView: TextView
    private lateinit var monthTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var selectDayInfoTV: TextView
    private lateinit var summarizeTV: TextView
    private lateinit var ratingBtn: Button
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val today = LocalDate.now()
    private var selectMonthStr = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_log, container, false)
        calendarView = view.findViewById(R.id.exOneCalendar)
        yearTextView = view.findViewById(R.id.exOneYearText)
        monthTextView = view.findViewById(R.id.exOneMonthText)
        progressBar = view.findViewById(R.id.excLogProgress)
        ratingBtn = view.findViewById(R.id.ratingChip)
        selectDayInfoTV = view.findViewById(R.id.selectDayInfo)
        summarizeTV = view.findViewById(R.id.excerciseSumTV)
        setupCalendar()
        viewModel.fetchWHStat()
        viewModel.excercises.observe(viewLifecycleOwner) { list ->
            var str = ""
            list.groupBy { it.id }
                .forEach {
                    val btName = it.value.firstOrNull()?.name ?: "Exercise has been delete"
                    str += "- ${btName}: ${it.value.size} ngày \n"
                }
            summarizeTV.setText(str)
        }
        viewModel.getStatHistory()
        viewModel.getAppBodyStatList()


        setupRatingBtn()

        return view
    }
    private fun setupRatingBtn() {
        ratingBtn.setOnClickListener { // when has data will clickable, else null will skipe function
            // compose report message
            var reportMessage = ""
            if (viewModel.excercises.value == null && viewModel.bodyStatList.value == null && viewModel.appBodyStatList.value == null && viewModel.excLogOfMonth.value != null) {
                return@setOnClickListener
            }

            /* giai thuat :
                group exclog cho nhung bt nao duoc tap hon 4 tuan
                tu exclog -> excer(if exist) -> statEnsure map -> duyet tung stat
                lay ra lastest record (lay tu list body or tu weight neu chon weight) ve stat trong statEnsure yeu cau (for statensure)
                so voi first record of the month (querry trong thang thoi)
             */

            lifecycleScope.launch(Dispatchers.Default) {
                viewModel.excLogOfMonth.value?.groupBy { it.excId }?.forEach { excId, list -> // group thanh 1 list nhom cac bt

                    val firstExcercise = viewModel.excercises.value?.find {
                        it.id == excId
                    } ?: return@forEach

                    if (list.size >= 4) {
                        reportMessage += "--- ${firstExcercise.name}: \n"

                        val fromJson = Gson().fromJson(firstExcercise.achieveEnsure, JsonObject::class.java)

                        for ((statName, value) in fromJson.entrySet()) { // each ensure of excercise
                            val appBodyStat = viewModel.appBodyStatList.value?.find { it.name == statName  } // reference body stat id
                            if (appBodyStat != null) {

                                if (appBodyStat.equals("Weight")) {
                                    val lastestOfMonth = viewModel.userStat.value?.find { it.dateString.isSameMonth("10/${selectMonthStr}/2021") }
                                    val firstOfMonth = viewModel.userStat.value?.findLast { it.dateString.isSameMonth("10/${selectMonthStr}/2021") }
                                    if (firstOfMonth != null && lastestOfMonth != null) {
                                        reportMessage += compareCurrentStatWithEnsure(firstOfMonth.weight.toString(),lastestOfMonth.weight.toString(),value.asString,"Weight", "Kg")
                                    }
                                } else {
                                    // lastest record is first (ordered at vm)
                                    val lastestRecord = viewModel.bodyStatList.value?.find { it.statId == appBodyStat.id && it.dateString.isSameMonth("10/${selectMonthStr}/2021") }

                                    if (lastestRecord != null) {
                                        val firstRecordValue = viewModel.bodyStatList.value?.findLast {
                                            it.statId == appBodyStat.id && it.dateString.isSameMonth(lastestRecord.dateString)
                                        }?.value ?: ""
                                        reportMessage += compareCurrentStatWithEnsure(firstRecordValue, lastestRecord.value, value.asString, statName, appBodyStat.unit)
                                    } else {
                                        reportMessage += "+ ${statName}: Chưa có record nào được ghi về chỉ số này \n"
                                    }
                                }




                            } else {
                                reportMessage += "+ ${statName}: Tên stat đã bị thay đổi, admin sẽ cập nhật sau \n"
                            }

                        }

                    } else {
                        reportMessage += "- ${firstExcercise.name}: Không đủ số lượng buổi tập trong tháng \n"
                    }


                }
                // use message
                withContext(Dispatchers.Main) {
                    RatingDialogFragment( reportMessage).show(childFragmentManager, "rating")
                }

            }
        }
    }

    private fun compareCurrentStatWithEnsure(monthBegin: String, lastest: String, ensure: String, statName: String, unit: String): String {
        val beginFloat = monthBegin.toFloatOrNull() ?: return ""
        val lastesFloat = lastest.toFloatOrNull() ?: return ""
        val ensureFloat = ensure.toFloatOrNull() ?: return ""

        val deltaMonth = lastesFloat - beginFloat
        val percentRating = (deltaMonth/ensureFloat - 1)*100
        return "+ ${statName}: Phần trăm gia tăng: ${percentRating}%  [đạt được: ${deltaMonth}  - mục tiêu: ${ensureFloat}] (đơn vị: $unit)\n"
    }

    private fun setupCalendar() {
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view) { excLog ->
                excLog?.let {  log ->
                    val exc = viewModel.excercises.value?.find { exc ->
                        if (exc.categoryId != null) {
                            exc.categoryId.equals(log.categoryId) && exc.id.equals(log.excId)
                        } else false
                    }
                    if (exc != null) {
                        selectDayInfoTV.setText(exc.name)
                    } else {
                        selectDayInfoTV.setText("Excercise has been deleted")
                    }
                }
            }

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
                            container.excLog= find
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
                selectMonthStr = if (month < 10) "0${month}" else month.toString()
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

class DayViewContainer(view: View,var onClick: (ExcLog?) -> Unit) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.exOneDayText)
    lateinit var day: CalendarDay
    var excLog: ExcLog? = null
    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                onClick(excLog)
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