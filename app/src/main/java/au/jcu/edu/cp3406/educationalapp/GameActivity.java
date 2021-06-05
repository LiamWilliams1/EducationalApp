package au.jcu.edu.cp3406.educationalapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SensorEventListener {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewCountDown;
    private TextView textViewQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirm;
    private ArrayList<Question> questionList;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private ColorStateList textColorDefaultRb;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;


    private int score;
    private boolean answered;


    public static GameActivity gameActivity;
    private static final String NAME = "NAME";
    String name;
    //public static int score = 0;
    FrameLayout frameLayout;

    Intent mainIntent;

    private int numParts = 7;
    private int currPart;
    private int numChars;
    private int numCorr;

    //private Question currentQuestion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_game);
            textViewQuestion = findViewById(R.id.question);
            textViewScore = findViewById(R.id.score);
            textViewCountDown = findViewById(R.id.timer);
            textViewQuestionCount = findViewById(R.id.text_view_question_count);
            rbGroup = findViewById(R.id.radio_group);
            rb1 = findViewById(R.id.radio_button1);
            rb2 = findViewById(R.id.radio_button2);
            rb3 = findViewById(R.id.radio_button3);
            buttonConfirm = findViewById(R.id.confirmButton);


            textColorDefaultRb = rb1.getTextColors();



            if (savedInstanceState == null) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
            
            displayNextQuestion();
            } else {
                questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
                questionCountTotal = questionList.size();
                questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
                currentQuestion = questionList.get(questionCounter - 1);
                score = savedInstanceState.getInt(KEY_SCORE);
                timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
                answered = savedInstanceState.getBoolean(KEY_ANSWERED);
                if (!answered) {
                    startCountDown();
                } else {
                    updateCountDownText();
                    showSolution();
                }
            }
            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!answered) {
                        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                            checkAnswer();
                        } else {
                            Toast.makeText(GameActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        displayNextQuestion();
                    }
                }
            });
        }


        //this.gameActivity = gameActivity;


//        mainIntent = getIntent();
//        name = mainIntent.getStringExtra(NAME)

        }

    private void displayNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirm.setText("Confirm");
        } else {
            finishQuiz();
            countDownTimer.cancel();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();

    }

    private void updateCountDownText() {
        // formay mill to minutes and seconds
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;


        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);

    }

    private void checkAnswer() {
        answered = true;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);
        }
        showSolution();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirm.setText("Next");
        } else {
            buttonConfirm.setText("Finish");
        }
    }


    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    // cancels timer at the end of activity to stop it running in the background
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }


    @Override
        public void onClick(View v) {

        }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}