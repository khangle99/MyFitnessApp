package com.khangle.myfitnessapp.ui.excercise.excdetail

import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.Difficulty
import com.khangle.myfitnessapp.common.UseState
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.ui.excercise.exclist.ExcerciseListViewModel
import com.khangle.myfitnessapp.ui.userexc.excerciseDetail.UserExcDetailFragment
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcDetailFragment : Fragment() {
    lateinit var linearLayout: LinearLayout
    private lateinit var titleTextView: TextView
    private lateinit var difficultyTextView: TextView
    private lateinit var equipmentTextView: TextView
    private lateinit var viewStepsButton: Button
    lateinit var selectedDifficulty: Difficulty
    private lateinit var excercise: Excercise
    private lateinit var addExcerciseBtn: Button
    lateinit var noTurnEditText: TextView
    lateinit var noGapEditText: TextView
    lateinit var noSecEditText: TextView
    lateinit var caloFactorEditText: TextView
    private val viewmodel: ExcDetailViewModel by viewModels()

    private var achievementCount = 0
    private val stepTicketList = mutableListOf<View>()
    private val stepEditTextList = mutableListOf<TextView>()
    private val stepPicImageViewList = mutableListOf<ImageView>() // uri dc luu thong qua tag
    private var achievementTicketList = mutableListOf<View>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exc_detail, container, false)
        linearLayout = view.findViewById(R.id.container)
        titleTextView = view.findViewById(R.id.titleTV)
       noTurnEditText = view.findViewById(R.id.excNoTurn)
       noGapEditText = view.findViewById(R.id.excNoGap)
        noSecEditText = view.findViewById(R.id.excNoSec)

        caloFactorEditText = view.findViewById(R.id.caloFactor)
        difficultyTextView = view.findViewById(R.id.difficultyTv)
        equipmentTextView = view.findViewById(R.id.equipmentTv)
      //  viewStepsButton = view.findViewById(R.id.viewStepBtn)
        addExcerciseBtn = view.findViewById(R.id.addFavourite)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     //   viewmodel.getBodyStatList()
        excercise = arguments?.getParcelable("excercise")!!
        loadExcercise(excercise)
//        viewStepsButton.setOnClickListener {
//            StfalconImageViewer.Builder(context, excercise.picSteps) { view, image ->
//                view.load(image) {
//                    crossfade(true)
//                    placeholder(R.drawable.ic_menu_gallery)
//                }
//            }.show()
//        }

        if (requireArguments().getBoolean("isViewOnly",false)) {
            addExcerciseBtn.visibility = View.INVISIBLE
        }

        addExcerciseBtn.setOnClickListener {
            // code lai
//            val frag = UserExcDetailFragment()
//            frag.arguments = bundleOf("state" to UseState.ADD.raw, "categoryId" to excercise.categoryId, "excId" to excercise.id, "excName" to excercise.name)
//            parentFragmentManager.commitAnimate {
//                addToBackStack(null)
//                replace(R.id.excerciseContainer, frag)
//
//            }
        }
    }

    private fun loadExcercise(excercise: Excercise) {
        titleTextView.setText(excercise.name)

        selectedDifficulty = Difficulty.fromInt(excercise.difficulty)
        difficultyTextView.setText(Difficulty.fromInt(excercise.difficulty).name)

        equipmentTextView.setText(excercise.equipment)

        noTurnEditText.setText(excercise.noTurn.toString() + " Turns")
        noGapEditText.setText(excercise.noGap.toString() + " Break Sec")
        noSecEditText.setText(excercise.noSec.toString()+ " Set Sec")
        caloFactorEditText.setText(excercise.caloFactor.toString()+ " Kcal/Kg/Minute")

        // load step tu cac array tutorial for de new view voi moi picurl
        var count = 1
        excercise.tutorialWithPic.forEach { tutoText, urlString ->
            val view = onAddMoreStepClick()
            view.findViewById<TextView>(R.id.stepNumber).setText("- B${count++}: ")
            view.findViewById<TextView>(R.id.stepTutorial).setText(tutoText)
            view.findViewById<ImageView>(R.id.stepPic)?.let {
                it.load(urlString) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                }
                it.tag = urlString
            }
        }
        //load achievement tu json string map -> sang view voi moi entries
        val fromJson = Gson().fromJson(excercise.achieveEnsure, JsonObject::class.java)
        for ((key, value) in fromJson.entrySet()) {
            val view = onAddAchievement()
            view.findViewById<TextView>(R.id.promiseValue).setText(value.asString)
            view.findViewById<TextView>(R.id.bodyStatTextView).setText(key.toString())
        }

    }

    private fun onAddAchievement(): View {
        achievementCount++
        val achieveTicket = layoutInflater.inflate(R.layout.view_achievement_ticket, null)
        achievementTicketList.add(achieveTicket)
        linearLayout.addView(achieveTicket,linearLayout.childCount - 1)
        return achieveTicket
    }

    private fun onAddMoreStepClick(): View {
        val stepTicket = layoutInflater.inflate(R.layout.view_tutorial_ticket, null)
        stepTicketList.add(stepTicket)
        val editText = stepTicket.findViewById<TextView>(R.id.stepTutorial)
        stepEditTextList.add(editText)
        val imageView = stepTicket.findViewById<ImageView>(R.id.stepPic)
        stepPicImageViewList.add(imageView)
        imageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select picture"),
                99
            )
        }
        imageView.tag = ""

        linearLayout.addView(stepTicket,linearLayout.childCount - achievementCount - 1)
        return stepTicket
    }

}