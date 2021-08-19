package com.khangle.myfitnessapp.ui.base

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.UseState


abstract class BaseComposableFragment : Fragment() {
    lateinit var state: UseState
    abstract fun onAdded()
    abstract fun onUpdated()
    abstract fun onDeleted()
    abstract fun getManageObjectName(): String
    abstract fun invalidateView()

    fun changeState(state: UseState) {
        this.state = state
        invalidateView()
        invalidateOptionsMenu(requireActivity())
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        invalidateMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun invalidateMenu(menu: Menu) {
        when (state) {
            UseState.ADD -> {
                menu.findItem(R.id.save).isVisible = true
                menu.findItem(R.id.edit).isVisible = false
                menu.findItem(R.id.cancel).isVisible = true
                menu.findItem(R.id.delete).isVisible = false
            }
            UseState.EDIT -> {
                menu.findItem(R.id.save).isVisible = true
                menu.findItem(R.id.edit).isVisible = false
                menu.findItem(R.id.cancel).isVisible = true
                menu.findItem(R.id.delete).isVisible = false
            }
            UseState.VIEW -> {
                menu.findItem(R.id.save).isVisible = false
                menu.findItem(R.id.edit).isVisible = true
                menu.findItem(R.id.cancel).isVisible = false
                menu.findItem(R.id.delete).isVisible = true
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (state === UseState.ADD) {
                    onAdded()
                } else { // state Edit
                    onUpdated()
                }
                return true
            }
            R.id.edit -> {
                changeState(UseState.EDIT)
                return true
            }
            R.id.cancel -> {
                if (state === UseState.ADD) {
                    parentFragmentManager.popBackStack()
                } else if (state === UseState.EDIT) {
                    changeState(UseState.VIEW)
                }
                return true
            }
            R.id.delete -> AlertDialog.Builder(requireContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remove ${getManageObjectName()}")
                .setMessage("Are you sure you want to remove this ${getManageObjectName()}?")
                .setPositiveButton("Yes") { _, _ ->
                    onDeleted()
                }
                .setNegativeButton("No", null)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

}