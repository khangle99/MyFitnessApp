package com.khangle.myfitnessadmin.suggestpack.planlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessadmin.suggestpack.plandetail.PlanDetailActivity
import com.khangle.myfitnessapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuggestActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PlanAdapter
    lateinit var progressBar: ProgressBar
    val viewmodel: PlanListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggest)
        setupUI()

        viewmodel.planList.observe(this) {
            progressBar.visibility = View.INVISIBLE
            if(it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }
        viewmodel.getPlanList()
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.suggestRecyclerview)
        progressBar = findViewById(R.id.excCatProgress)

        adapter = PlanAdapter{
            val intent = Intent(this, PlanDetailActivity::class.java)
            val bundle = bundleOf("plan" to it)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

}