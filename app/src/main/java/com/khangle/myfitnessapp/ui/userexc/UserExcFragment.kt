package com.khangle.myfitnessapp.ui.userexc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.userexc.excList.UserExcListFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserExcFragment : Fragment() {

    private val viewModel: UserSessionViewModel by viewModels()

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
            val excListFrag = UserExcListFragment()
            add(R.id.userexcContainer, excListFrag)
        }
    }

}