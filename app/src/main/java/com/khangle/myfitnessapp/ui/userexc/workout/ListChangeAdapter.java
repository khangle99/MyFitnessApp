package com.khangle.myfitnessapp.ui.userexc.workout;

public interface ListChangeAdapter {

    /**
     * Calls all the listeners to make changes for the movement of required item
     *
     * @param fromPosition The position from which item is moved
     * @param toPosition   The position to which the item is moved
     * @return If the clickListener was able to successfully make changes required for this move.
     * Never return false from a silent observer as it will abort the whole operation
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * Calls all the listeners to make changes for the dismissal of required item
     *
     * @param position The position of item to be dismissed
     * @return If the clickListener was able to successfully make changes required for this dismissal.
     * Never return false from a silent observer as it will abort the whole operation
     */
    boolean onItemDismiss(int position);
}