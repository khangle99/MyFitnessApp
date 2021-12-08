package com.khangle.myfitnessapp.ui.statistic.otherstat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.SwipeToDeleteCallback
import com.khangle.myfitnessapp.common.toFormatDate
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.ui.statistic.otherstat.report.StatReportActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherStatActivity : AppCompatActivity() {

    private val viewmodel: OtherStatViewModel by viewModels()
    private lateinit var statSpinner: Spinner
    private lateinit var bodyRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: OtherStatAdapter
    private lateinit var viewReportBtn: Chip
    private var selectStat: AppBodyStat? = null
    private var filterList = listOf<BodyStat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_stat)
        statSpinner = findViewById(R.id.statSpinner)
        bodyRecyclerView = findViewById(R.id.bodyRecyclerview)
        progressBar = findViewById(R.id.other_stat_progress)
        viewReportBtn = findViewById(R.id.viewReport)

        viewReportBtn.setOnClickListener {
            val intent = Intent(this, StatReportActivity::class.java)
            intent.putExtras(bundleOf("stat" to selectStat, "filterList" to filterList))
            startActivity(intent)
        }

        fab = findViewById(R.id.other_stat_fab)
        fab.setOnClickListener {
            // mo fragment add
            AddBodyStatDialogFragment(viewmodel).show(supportFragmentManager, "AddStat")
        }
        setupSpinner()
        viewmodel.getAppBodyStatList()
        viewmodel.getStatHistory()


    }

    private fun setupRecyclerview(bodyList: List<AppBodyStat>) {
        adapter = OtherStatAdapter(bodyList)
        bodyRecyclerView.adapter = adapter
        bodyRecyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun setupSpinner() {
        viewmodel.appBodyStatList.observe(this) {
            setupRecyclerview(it)

            viewmodel.bodyStatList.observe(this) {
                if (!selectStat?.id.equals("")) {
                    viewReportBtn.isEnabled = true
                    val list =  it.filter { it.statId.equals(selectStat?.id) }
                    filterList = list
                    adapter.submitList(list)
                    progressBar.visibility = View.INVISIBLE
                }
            }

            val array = it.map {
                it.name
            }
            val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_item, array)
            statSpinner.adapter = adapter
            statSpinner.setSelection(0)
        }
        statSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                progressBar.visibility = View.VISIBLE
                viewmodel.appBodyStatList.value?.let {
                    selectStat = it.get(position)

                    val bpdyStatList = viewmodel.bodyStatList.value
                   if (bpdyStatList != null) {
                      val list =  bpdyStatList.filter { it.statId.equals(selectStat?.id) }
                       filterList = list.sortedWith(Comparator { o1, o2 ->
                           val d1 = o1.dateString.toFormatDate()
                           val d2 = o2.dateString.toFormatDate()
                           if (d1!!.before(d2)) {
                               return@Comparator 1
                           } else if (d1.after(d2)) {
                               return@Comparator -1
                           } else {
                               return@Comparator 0
                           }
                       })

                       viewReportBtn.isEnabled = true
                       adapter.submitList(filterList)
                       progressBar.visibility = View.INVISIBLE
                   }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        setupSwipeToDelete()
        viewmodel.getAppBodyStatList()
    }

    private fun setupSwipeToDelete() {
        val callback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                progressBar.visibility = View.VISIBLE
                viewmodel.removeOtherStat(item) {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@OtherStatActivity, "Deleted Session", Toast.LENGTH_SHORT).show()
                    viewmodel.getStatHistory()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(bodyRecyclerView)
    }
}