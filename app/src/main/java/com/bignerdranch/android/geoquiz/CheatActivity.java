package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String KEY_ISCHEATER = "isCheater";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    //TextView to display Build version
    private TextView mBuildVersionTextView;
    private Button mShowAnswer;
    private boolean mIsCheater;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    private void displayAnswer() {
        if (mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        } else{
            mAnswerTextView.setText(R.string.false_button);
        }

        //adding code from api 21,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = mShowAnswer.getWidth() / 2;
            int cy = mShowAnswer.getHeight() / 2;
            float radius = mShowAnswer.getWidth() / 2;
            Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            mAnswerTextView.setVisibility(View.VISIBLE);
            mShowAnswer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        //getting the text view
        mBuildVersionTextView = (TextView) findViewById(R.id.build_version);
        //setting the text for the text view
        mBuildVersionTextView.setText("API Level "+ Build.VERSION.SDK_INT);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mIsCheater = true;
                setAnswerShownResult(mIsCheater);
                displayAnswer();
            }

        });

        if(savedInstanceState != null){
            mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEATER);
            if(mIsCheater = true){
                setAnswerShownResult(mIsCheater);
                displayAnswer();
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstaneState){
        super.onSaveInstanceState(savedInstaneState);
        savedInstaneState.putBoolean(KEY_ISCHEATER, mIsCheater);
    }
}
