package com.example.gpfduoduo.scanzoomanimation;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.example.gpfduoduo.scanzoomanimation.view.RoundImageView;


public class MainActivity extends AppCompatActivity {
    private static final String tag = MainActivity.class.getSimpleName();

    private RoundImageView mRoundView;
    private int yValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initWidget();
    }

    private void initWidget() {
        mRoundView = (RoundImageView) findViewById(R.id.img_round);
        int backW = mRoundView.getBackWidth();
        int backH = mRoundView.getBackHeight();

        Log.d(tag, "back width = " + backW);
        Log.d(tag, "back height = " + backH);

        ValueAnimator xAnimator = ValueAnimator.ofFloat(0, -backW / 2).setDuration(2 * 1000);
        xAnimator.setInterpolator(new DecelerateInterpolator());
        xAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mRoundView.setTransValue((int) value, yValue);
            }
        });

        ValueAnimator yAnimator = ValueAnimator.ofFloat(0, -backH / 2).setDuration(2 * 1000);
        yAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        yAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                yValue = (int) value;
            }
        });

        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(xAnimator).with(yAnimator);
        bouncer.start();
    }
}
