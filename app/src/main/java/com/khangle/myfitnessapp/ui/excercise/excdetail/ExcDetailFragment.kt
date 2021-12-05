package com.khangle.myfitnessapp.ui.excercise.excdetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.Difficulty
import com.khangle.myfitnessapp.common.UseState
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.ui.userexc.plandetail.UserExcDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcDetailFragment : Fragment() {
    lateinit var stepLayout: LinearLayout
    lateinit var achievementLayout: LinearLayout
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

    private val stepTicketList = mutableListOf<View>()
    private val stepEditTextList = mutableListOf<TextView>()
    private val stepPicImageViewList = mutableListOf<ImageView>() // uri dc luu thong qua tag
    private var achievementTicketList = mutableListOf<View>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exc_detail, container, false)
        stepLayout = view.findViewById(R.id.stepLayout)
        achievementLayout = view.findViewById(R.id.achievementLayout)
        titleTextView = view.findViewById(R.id.titleTV)
       noTurnEditText = view.findViewById(R.id.excNoTurn)
       noGapEditText = view.findViewById(R.id.excNoGap)
        noSecEditText = view.findViewById(R.id.excNoSec)

        caloFactorEditText = view.findViewById(R.id.caloFactor)
        difficultyTextView = view.findViewById(R.id.difficultyTv)
        equipmentTextView = view.findViewById(R.id.equipmentTv)
        addExcerciseBtn = view.findViewById(R.id.addFavourite)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getBodyStatList()
        excercise = arguments?.getParcelable("excercise")!!
        viewmodel.appBodyStatList.observe(viewLifecycleOwner) {
            loadExcercise(excercise)
        }

        if (requireArguments().getBoolean("isViewOnly",false)) {
            addExcerciseBtn.visibility = View.INVISIBLE
        }

        addExcerciseBtn.setOnClickListener {
            val frag = UserExcDetailFragment()
            frag.arguments = bundleOf("state" to UseState.ADD.raw, "categoryId" to excercise?.categoryId, "excId" to excercise?.id, "excName" to excercise?.name)
            parentFragmentManager.commitAnimate {
                addToBackStack(null)
                replace(R.id.excerciseContainer, frag)

            }
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
        stepLayout.removeAllViews()
        achievementLayout.removeAllViews()
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
            val unitString = viewmodel.appBodyStatList.value?.find {
                it.name.equals(key.toString())
            }?.unit ?: ""
            view.findViewById<TextView>(R.id.unitTV).setText(unitString)
            view.findViewById<TextView>(R.id.bodyStatTextView).setText(key.toString())
        }

    }

    private fun onAddAchievement(): View {
        val achieveTicket = layoutInflater.inflate(R.layout.view_achievement_ticket, null)
        achievementTicketList.add(achieveTicket)
        achievementLayout.addView(achieveTicket,achievementLayout.childCount)
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

        stepLayout.addView(stepTicket,stepLayout.childCount)
        return stepTicket
    }

}