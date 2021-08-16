package com.khangle.myfitnessapp.ui.excercise.excdetail

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.Excercise
import com.stfalcon.imageviewer.StfalconImageViewer

class ExcDetailFragment : Fragment() {

//    private lateinit var tutorialRecycleview: RecyclerView
//    private lateinit var adapter: ImageRecyclerviewAdapter
    private lateinit var titleTextView: TextView
    private lateinit var difficultyTextView: TextView
    private lateinit var equipmentTextView: TextView
    private lateinit var tutorialTextView: TextView
    private lateinit var viewStepsButton: Button
    private lateinit var excercise: Excercise
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exc_detail, container, false)
//        tutorialRecycleview = view.findViewById(R.id.tutoriaRecycle)
//        adapter = ImageRecyclerviewAdapter()
//        tutorialRecycleview.adapter = adapter
//        tutorialRecycleview.layoutManager = GridLayoutManager(context,2)
        titleTextView = view.findViewById(R.id.titleTV)
        difficultyTextView = view.findViewById(R.id.difficultyTv)
        equipmentTextView = view.findViewById(R.id.equipmentTv)
        tutorialTextView = view.findViewById(R.id.tutorialTV)
        viewStepsButton = view.findViewById(R.id.viewStepBtn)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        excercise = arguments?.getParcelable("excercise")!!
        titleTextView.text = excercise.name
        difficultyTextView.text = excercise.difficulty
        equipmentTextView.text = excercise.equipment
        tutorialTextView.text = excercise.tutorial
        viewStepsButton.setOnClickListener {
            StfalconImageViewer.Builder(context, excercise.picSteps) { view, image ->
                view.load(image) {
                    crossfade(true)
                    placeholder(R.drawable.ic_menu_gallery)
                }
            }.show()
        }
       // adapter.applyUrlList(excercise.picSteps)
    }

}