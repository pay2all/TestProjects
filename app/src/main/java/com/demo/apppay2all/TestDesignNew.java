package com.demo.apppay2all;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TestDesignNew extends AppCompatActivity {

    ImageView iv_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_design_new);

        iv_test=findViewById(R.id.iv_test);
        iv_test.setOnClickListener(listener);

    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final float fromRadius = getResources().getDimension(R.dimen.start_corner_radius);
            final float toRadius = v.getHeight() / 2;
            final GradientDrawable gd = (GradientDrawable) v.getBackground();
            final ValueAnimator animator = ValueAnimator.ofFloat(fromRadius, toRadius);
            animator.setDuration(2000)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            gd.setCornerRadius(value);
                        }
                    });
            animator.start();
        }
    };
}