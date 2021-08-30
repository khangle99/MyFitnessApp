package com.khangle.myfitnessapp.ui.nutrition.menudetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.Menu

class MenuDetailFragment : Fragment() {
    private lateinit var imageSlider: ImageSlider
    private lateinit var nameTextView: TextView
    private lateinit var breakfastTextView: TextView
    private lateinit var lunchTextView: TextView
    private lateinit var dinnerTextView: TextView
    private lateinit var snackTextView: TextView
    private lateinit var otherTextView: TextView
    private lateinit var menu: Menu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_detail, container, false)
        nameTextView = view.findViewById(R.id.menuName)
        breakfastTextView = view.findViewById(R.id.breakfast)
        lunchTextView = view.findViewById(R.id.lunch)
        dinnerTextView = view.findViewById(R.id.dinner)
        snackTextView = view.findViewById(R.id.snack)
        otherTextView = view.findViewById(R.id.other)
        imageSlider = view.findViewById(R.id.imageSlider)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menu = arguments?.getParcelable("menu")!!
        nameTextView.text = menu.name
        breakfastTextView.text = menu.breakfast
        lunchTextView.text = menu.lunch
        dinnerTextView.text = menu.dinner
        snackTextView.text = menu.snack
        otherTextView.text = menu.other

        setupSlider(menu.picUrls)
    }

    private fun setupSlider(urlList: List<String>) {
        val list = mutableListOf<SlideModel>()
        urlList.forEach {
            list.add(SlideModel(it))
        }
        imageSlider.setImageList(list)
    }

}