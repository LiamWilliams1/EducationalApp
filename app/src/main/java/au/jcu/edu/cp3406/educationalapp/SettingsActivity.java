package au.jcu.edu.cp3406.educationalapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    private Spinner difficultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        difficultySpinner = findViewById(R.id.difficultySpinner);
        String[] difficultyLevel = Question.getAllDifficultyLevels();

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLevel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setLevel();
    }

    public void setLevel() {
        String selection = difficultySpinner.getSelectedItem().toString();
        Difficulty difficulty = Difficulty.valueOf(selection.toUpperCase());
    }
}