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

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khangle.myfitnessapp.R;
import com.khangle.myfitnessapp.customview.ExerciseModificationListener;
import com.khangle.myfitnessapp.customview.IntegerChangeListener;
import com.khangle.myfitnessapp.customview.PlayingView;
import com.khangle.myfitnessapp.model.user.Session;
import com.khangle.myfitnessapp.model.user.UserExcTuple;

import java.util.ArrayList;
import java.util.LinkedList;


public class WorkoutActivity extends AppCompatActivity implements IntegerChangeListener, ExerciseModificationListener {

    private static final String TAG = "EXACTIVITY";
    public static Session session;
    PlayingView playingView;
    RecyclerView recyclerView;
    UpcomingExercisesAdapter adapter;
    ArrayList<UserExcTuple> prev, temp;
    ArrayList<UserExcTuple> exercises;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exercises = getIntent().getParcelableArrayListExtra("tupleList");

        setContentView(R.layout.activity_workout);
        setupView();
        playingView.addIntegerChangeListener(this);

        LinkedList<ExerciseModificationListener> exerciseModificationListeners = new LinkedList<>();
        exerciseModificationListeners.add(this);
        exerciseModificationListeners.add(playingView);
        adapter = new UpcomingExercisesAdapter(exercises, 0, exerciseModificationListeners);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        playingView.setGapBetweenExercises(5);
        playingView.setExercises(exercises);

        adapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();

        prev = new ArrayList<>(exercises);
        temp = new ArrayList<>(exercises);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setTaskDescription(new ActivityManager.TaskDescription("Continue " + session.getName()));
//        }

    }

    private void setupView() {
        playingView = (PlayingView) findViewById(R.id.playing_view);
        recyclerView = (RecyclerView) findViewById(R.id.upcoming_exercises);
    }

    @Override
    public void onChange(int index) { // khi doi bai
        this.index = index;
        adapter.setBeg(index);
        adapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();
//        if (index >= exercises.size() && SettingsActivity.isLog()) {
//            logSession();
//        }
    }

    @Override
    public void onChange(boolean wasRemoval) { // khi xoa item
        if (exercises.size() < prev.size() - 1)
            prev = new ArrayList<>(temp);

        if (wasRemoval)
            Toast.makeText(getBaseContext(), "Remove", Toast.LENGTH_SHORT).show();
           // Snackbar.make(recyclerView, "R.string.undo_message", Snackbar.LENGTH_LONG).setAction("R.string.undo_text", WorkoutActivity.this).setActionTextColor(Color.RED).show();
        else prev = new ArrayList<>(exercises);
        temp = new ArrayList<>(exercises);
    }

//    @Override
//    public void onClick(View v) {
//        exercises.add(new Exercise());
//        Collections.copy(exercises, prev);
//
//        adapter.notifyDataSetChanged();
//        recyclerView.refreshDrawableState();
//        playingView.onChange(false);
//    }

    @Override
    public void onBackPressed() {
        if (index >= exercises.size()) {
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Abort current workout");
        alertBuilder.setTitle("Abort");
        alertBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WorkoutActivity.super.onBackPressed();
            }
        });
        alertBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("exercises", exercises);
        outState.putParcelable("session_exercise", session);
    }




}
