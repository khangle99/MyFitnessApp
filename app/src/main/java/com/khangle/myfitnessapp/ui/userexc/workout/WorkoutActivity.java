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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khangle.myfitnessapp.R;
import com.khangle.myfitnessapp.customview.ExerciseModificationListener;
import com.khangle.myfitnessapp.customview.FinishSetListener;
import com.khangle.myfitnessapp.customview.IntegerChangeListener;
import com.khangle.myfitnessapp.customview.PlayingView;
import com.khangle.myfitnessapp.customview.TurnFinishListener;
import com.khangle.myfitnessapp.model.Excercise;
import com.khangle.myfitnessapp.model.user.PlanDay;
import com.khangle.myfitnessapp.model.user.Session;
import com.khangle.myfitnessapp.model.user.UserExcTuple;

import java.util.ArrayList;
import java.util.LinkedList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutActivity extends AppCompatActivity implements IntegerChangeListener, ExerciseModificationListener, FinishSetListener, TurnFinishListener {

    private static final String TAG = "EXACTIVITY";
    public static Session session;
    PlayingView playingView;
    String selectLevelKey = "";
    TextView messageTV;
    RecyclerView recyclerView;
    UpcomingExercisesAdapter adapter;
    ArrayList<PlanDay> prev, temp;
    ArrayList<PlanDay> exercises;
    private int index;

    private WorkoutViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        exercises = getIntent().getParcelableArrayListExtra("dayList");
        selectLevelKey = getIntent().getStringExtra("selectLevel");
        setContentView(R.layout.activity_workout);
        setupView();
        playingView.selectLevel = selectLevelKey;
        playingView.addIntegerChangeListener(this);
        playingView.addFinishListener(this);
        playingView.addTurnFinishListener(this);

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
        messageTV = (TextView) findViewById(R.id.excMessageTV);
        viewModel.fetchCurrentWeight();
        viewModel.getCurrentWeight();
    }

    @Override
    public void onNextTurn(int turnPassed) {
        Excercise exc = exercises.get(0).getExc();

        try {
            int weight = viewModel.getCurrentWeight().getValue();
            if (exc.getLevelJSON().get(selectLevelKey) != null) {
                int excerciseSec = exc.getLevelJSON().get(selectLevelKey)[1];
                int totalCalories = (int) (exc.getCaloFactor() * (excerciseSec/60.0) * weight * turnPassed);
                String message = "Current calories: "+ totalCalories + "\n";
                String nutriStr = calculateNutri(exc.getNutriFactor(), totalCalories);
                messageTV.setText(message + nutriStr);
            } else {
                messageTV.setText("Level not exist");
            }


        } catch (Exception e) {
            messageTV.setText("Need at least one weight record to calculate");
        }


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

    @Override
    public void onFinish(PlanDay planDay) {
        viewModel.logDay(planDay);
    }


    private String calculateNutri(String nutriFactor, int totalCalo) {
        // 1 gram protein - 4 calo
        // 1 gram lipit - 9 calo
        // 1 gram carbon hydrat - 4 calo
        String[] split = nutriFactor.split("-");
        int dam = Integer.parseInt(split[0]);
        int beo = Integer.parseInt(split[1]);
        int tinhbot = Integer.parseInt(split[2]);
        int khoang = Integer.parseInt(split[3]);
        int base = totalCalo/(dam + beo + tinhbot + khoang);
        return "?????m: " + base*dam/4 + " gram\n" +
                "B??o: " + base*beo/9 + " gram\n" +
                "Tinh b???t: " + base*tinhbot/4 + " gram\n";
    }
}
