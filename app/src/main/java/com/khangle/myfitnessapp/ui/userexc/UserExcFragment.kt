package com.khangle.myfitnessapp.ui.userexc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.excercise.category.ExcerciseCategoryFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserExcFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_exc, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<ExcerciseCategoryFragment>(R.id.userexcContainer)
        }
    }

}