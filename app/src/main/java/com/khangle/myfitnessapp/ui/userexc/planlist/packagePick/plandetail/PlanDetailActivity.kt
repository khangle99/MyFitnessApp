package com.khangle.myfitnessadmin.suggestpack.plandetail

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.Plan
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanDetailActivity : AppCompatActivity() {
    lateinit var excerciseList: RecyclerView
    lateinit var categoryName: EditText
    val viewmodel: PlanDetailViewModel by viewModels()
    lateinit var currentCategoryId: String
    lateinit var adapter: PlanDayAdapter
    lateinit var progressBar: ProgressBar
    lateinit var applyBtn: Chip
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_detail)

        setupUI()
        val plan = intent.extras?.getParcelable<Plan>("plan")
        if (plan != null) {
            currentCategoryId = plan.id
            loadListForCategory(plan)
        } else {
            progressBar.visibility = View.INVISIBLE
        }
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        viewmodel.dayList.observe(this) {
            progressBar.visibility = View.INVISIBLE
            if (it.isEmpty()) {
                Toast.makeText(baseContext, "Empty List", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }

    }

    private fun setupUI() {
        progressBar = findViewById(R.id.excListProgress)
        excerciseList = findViewById(R.id.excerciseRecycler)
        categoryName = findViewById(R.id.categoryName)
        adapter = PlanDayAdapter {
        }
        excerciseList.adapter = adapter
        excerciseList.layoutManager = LinearLayoutManager(this)
        applyBtn = findViewById(R.id.applyBtn)
        applyBtn.setOnClickListener {
            viewmodel.applyPackage(currentCategoryId) { message ->
                if (message.id != null) {
                    Toast.makeText(
                        this,
                        "Đã apply thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Lỗi khi thêm error: ${message.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    private fun loadListForCategory(plan: Plan) {
        viewmodel.loadList(plan.id)
        categoryName.setText(plan.description)
    }

    private fun validateInput(): Boolean { // false khi fail
        if (categoryName.getText().toString().trim { it <= ' ' }.length == 0) {
            categoryName.setError("Không được để trống tên")
            return false
        }
//        if (state == UseState.ADD && pickedUri == null) {
//            Toast.makeText(baseContext, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show()
//            return false
//        }
        return true
    }

}