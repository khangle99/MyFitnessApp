package com.khangle.myfitnessapp.customview;


public interface ExerciseModificationListener {
    /**
     * Called to notify about modification in exercises through the Recycler view
     *
     * @param wasRemoval true: If modification was a removal and false if it was a swap
     */
    void onChange(boolean wasRemoval);
}
