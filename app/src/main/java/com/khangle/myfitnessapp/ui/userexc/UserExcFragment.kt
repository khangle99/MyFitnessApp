package com.khangle.myfitnessapp.ui.userexc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.userexc.session.ExcSessionFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserExcFragment : Fragment() {

    private val viewModel: UserExcViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.invalidateExcSession()
        val view = inflater.inflate(R.layout.fragment_user_exc, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            setReorderingAllowed(true)
            val sessionFragment = ExcSessionFragment(viewModel)
            add(R.id.userexcContainer, sessionFragment)
        }
    }

}