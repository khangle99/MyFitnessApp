/*
 * MIT License
 *
 * Copyright (c) 2017 Rishi Raj
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.khangle.myfitnessapp.ui.userexc.workout;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.khangle.myfitnessapp.R;
import com.khangle.myfitnessapp.customview.ExerciseModificationListener;
import com.khangle.myfitnessapp.model.user.UserExcTuple;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Locale;


/**
 * @author Rishi Raj
 */

public class UpcomingExercisesAdapter extends RecyclerView.Adapter<UpcomingExercisesAdapter.ExerciseViewHolder>
        implements ListChangeAdapter {

    private ArrayList<UserExcTuple> exercises;
    private List<ExerciseModificationListener> exerciseModificationListeners;
    private int beg;

    public UpcomingExercisesAdapter(ArrayList<UserExcTuple> exercises, int index, List<ExerciseModificationListener> exerciseModificationListeners) {
        this.exercises = exercises;
        this.exerciseModificationListeners = exerciseModificationListeners;
        beg = index + 1;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View exerciseView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.exercise_layout, parent, false);
        return new ExerciseViewHolder(exerciseView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        holder.name.setText(exercises.get(beg + position).getExcercise().getName());
        holder.reps.setText(String.format(Locale.ENGLISH, "X %d", exercises.get(beg + position).getTimeInfo().getNoTurn()));
    }

    @Override
    public int getItemCount() {
        return exercises.size() - beg;
    }

    public void setBeg(int index) {
        beg = index + 1;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(exercises, i + beg, i + beg + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(exercises, i + beg, i + beg - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        for (ExerciseModificationListener exerciseModificationListener :
                exerciseModificationListeners) {
            exerciseModificationListener.onChange(false);
        }
        return true;
    }

    @Override
    public boolean onItemDismiss(int position) {
        exercises.remove(position + beg);
        notifyItemRemoved(position);
        for (ExerciseModificationListener exerciseModificationListener :
                exerciseModificationListeners) {
            exerciseModificationListener.onChange(true);
        }
        return true;
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView reps;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
            reps = itemView.findViewById(R.id.exercise_reps);
        }
    }
}
