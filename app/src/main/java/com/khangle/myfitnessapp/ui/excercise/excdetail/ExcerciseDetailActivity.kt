package com.khangle.myfitnessadmin.excercise.excdetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.khangle.myfitnessadmin.BaseActivity
import com.khangle.myfitnessadmin.ComposableBaseActivity
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.RELOAD_RS
import com.khangle.myfitnessadmin.common.UseState
import com.khangle.myfitnessadmin.extension.setReadOnly
import com.khangle.myfitnessadmin.extension.toBitmap
import com.khangle.myfitnessadmin.model.Excercise
import dagger.hilt.android.AndroidEntryPoint
import pereira.agnaldo.previewimgcol.ImageCollectionView

@AndroidEntryPoint
class ExcerciseDetailActivity : ComposableBaseActivity() {
    val viewmodel: ExcerciseDetailVM by viewModels()
    lateinit var nameEditText: EditText
    lateinit var difficultyEditText: EditText
    lateinit var equipmentEditText: EditText
    lateinit var tutorialEditText: EditText
    lateinit var imageRecyclerView: RecyclerView
    lateinit var imageListAdapter: ImageRecyclerviewAdapter
    lateinit var catId: String
    lateinit var pickImage: Button
    var pickedUriStringList: List<String>? = null
    var excercise: Excercise? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_detail)
        setupUI()
        val excerciserReceive = intent.extras?.getParcelable<Excercise>("excercise")
        if (excerciserReceive != null) {
            excercise = excerciserReceive
            loadExcercise(excerciserReceive)
        }
        catId = intent.extras?.getString("catId") ?: ""
        val stateRaw = intent.extras?.getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)
    }

    private fun setupUI() {
        nameEditText = findViewById(R.id.excName)
        difficultyEditText = findViewById(R.id.excDifficulty)
        equipmentEditText = findViewById(R.id.excEquipment)
        tutorialEditText = findViewById(R.id.excTutorial)
        imageRecyclerView = findViewById(R.id.imageList)
        pickImage = findViewById<Button>(R.id.pickImage)
        pickImage.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "select pictures"),
                    99
                )
            }
        imageListAdapter = ImageRecyclerviewAdapter()
        imageRecyclerView.adapter = imageListAdapter
        val flexboxLayoutManager = FlexboxLayoutManager(baseContext)
        imageRecyclerView.layoutManager = flexboxLayoutManager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                val uriList = getUriListFromData(data)
                pickedUriStringList = uriList.map { it.toString()  }
                imageListAdapter.applyUriList(uriList)
            } else {
                Toast.makeText(baseContext, "Cancel Pick Image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getUriListFromData(data: Intent?): List<Uri> {
        val list = mutableListOf<Uri>()
        if(data!!.clipData != null) {
            val count = data.clipData!!.itemCount
            for (index in 0..count - 1) {
                data.clipData?.getItemAt(index)?.uri?.let { list.add(it) }
            }
        } else {
            val uri = data.data!!
            list.add(uri)
        }
        return list
    }

    private fun loadExcercise(excercise: Excercise) {
        nameEditText.setText(excercise.name)
        difficultyEditText.setText(excercise.difficulty)
        equipmentEditText.setText(excercise.equipment)
        tutorialEditText.setText(excercise.tutorial)
        imageListAdapter.applyUrlList(excercise.picSteps)
    }

    private fun validateInput(): Boolean { // false khi fail
        if (nameEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            nameEditText.setError("Không được để trống tên")
            return false
        }
        if (difficultyEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            difficultyEditText.setError("Không được để trống difficulty")
            return false
        }
        if (equipmentEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            equipmentEditText.setError("Không được để trống equipment")
            return false
        }
        if (tutorialEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            tutorialEditText.setError("Không được để trống tutorial")
            return false
        }
        if (state == UseState.ADD && pickedUriStringList == null) {
            Toast.makeText(baseContext, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onAdded() {
        if (!validateInput()) return
        val name = nameEditText.text.toString()
        val equip = equipmentEditText.text.toString()
        val diff = difficultyEditText.text.toString()
        val tutor = tutorialEditText.text.toString()
        val excercise = Excercise("",name,diff,equip,tutor, listOf(), 0)
        viewmodel.createExcercise(
            catId,
            excercise,
            pickedUriStringList!!
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onUpdated() {
        if (!validateInput()) return
        excercise!!.name = nameEditText.text.toString()
        excercise!!.equipment = equipmentEditText.text.toString()
        excercise!!.difficulty = difficultyEditText.text.toString()
        excercise!!.tutorial = tutorialEditText.text.toString()
        viewmodel.updateExcercise(
            catId,
            excercise!!.id,
            excercise!!,
            pickedUriStringList
        ) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Update thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi update error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDeleted() {
        viewmodel.deleteExcercise(catId, excercise!!.id) { message ->
            if (message.id != null) {
                Toast.makeText(
                    baseContext,
                    "Delete thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RELOAD_RS)
                finish()
            } else {
                Toast.makeText(
                    baseContext,
                    "Lỗi khi delete error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getManageObjectName(): String {
        return "Excercise"
    }

    override fun invalidateView() {
        when (state) {
            UseState.EDIT, UseState.ADD -> {
                nameEditText.setReadOnly(false)
                difficultyEditText.setReadOnly(false)
                equipmentEditText.setReadOnly(false)
                tutorialEditText.setReadOnly(false)
                pickImage.visibility = View.VISIBLE
            }
            else -> {
                nameEditText.setReadOnly(true)
                difficultyEditText.setReadOnly(true)
                equipmentEditText.setReadOnly(true)
                tutorialEditText.setReadOnly(true)
                pickImage.visibility = View.INVISIBLE
            }
        }
    }
}