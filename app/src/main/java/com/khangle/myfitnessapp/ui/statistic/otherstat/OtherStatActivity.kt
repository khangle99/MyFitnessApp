package com.khangle.myfitnessapp.ui.statistic.otherstat

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.SwipeToDeleteCallback
import com.khangle.myfitnessapp.model.AppBodyStat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherStatActivity : AppCompatActivity() {

    private val viewmodel: OtherStatViewModel by viewModels()
    private lateinit var statSpinner: Spinner
    private lateinit var bodyRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: OtherStatAdapter
    private var selectStatId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_stat)
        statSpinner = findViewById(R.id.statSpinner)
        bodyRecyclerView = findViewById(R.id.bodyRecyclerview)
        progressBar = findViewById(R.id.other_stat_progress)
        fab = findViewById(R.id.other_stat_fab)
        fab.setOnClickListener {
            // mo fragment add
            AddBodyStatDialogFragment(viewmodel).show(supportFragmentManager, "AddStat")
        }
        setupSpinner()
        viewmodel.getAppBodyStatList()
        viewmodel.getStatHistory()

        viewmodel.bodyStatList.observe(this) {
            if (!selectStatId.equals("")) {
                val list =  it.filter { it.statId.equals(selectStatId) }
                adapter.submitList(list)
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupRecyclerview(bodyList: List<AppBodyStat>) {
        adapter = OtherStatAdapter(bodyList)
        bodyRecyclerView.adapter = adapter
        bodyRecyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun setupSpinner() {
        viewmodel.appBodyStatList.observe(this) {
            setupRecyclerview(it)
            val array = it.map {
                it.name
            }
            val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_item, array)
            statSpinner.adapter = adapter
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
                    selectStatId = it.get(position).id
                    val appBodylist = viewmodel.bodyStatList.value
                   if (appBodylist != null) {
                      val list =  appBodylist.filter { it.statId.equals(selectStatId) }
                       adapter.submitList(list)
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