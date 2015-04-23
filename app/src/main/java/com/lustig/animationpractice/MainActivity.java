package com.lustig.animationpractice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.Toast;

/**
 * Practicing with the Android Material Animations following the guide here:
 * https://developer.android.com/training/material/animations.html
 */

public class MainActivity extends Activity {

    View mView;
    Animator mCardAnimator;

    // X and Y center of view
    int mCX;
    int mCY;

    int mFinalRadius;
    int mInitialRadius;

    Boolean isCardVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        // All manipulations are going to be done on this cool card
        mView = findViewById(R.id.my_cool_card);
    }

    public void revealOrHideCard(View v) {

        mCX = (mView.getLeft() + mView.getRight()) / 2;
        mCY = (mView.getTop() + mView.getBottom()) / 2;

        mFinalRadius = Math.max(mView.getWidth(), mView.getHeight());

        // If card is invisible, show card with animation
        if (!isCardVisible) {

            mCardAnimator = ViewAnimationUtils
                    .createCircularReveal(mView, mCX, mCY, 0, mFinalRadius);

            mView.setVisibility(View.VISIBLE);

            // Duration of animation in milliseconds
            mCardAnimator.setDuration(750);

            mCardAnimator.start();

            // After the animation, card is now visible
            isCardVisible = true;

            ((Button) v).setText("Hide Card");

        } else {

            /**
             * We're doing this on the same view, do the calculations need to
             * be performed twice?
             */

            // If card is visible, hide card with animation
            mInitialRadius = mView.getWidth();

            mCardAnimator = ViewAnimationUtils
                    .createCircularReveal(mView, mCX, mCY, mInitialRadius, 0);

            // We want to set the visibility of the button to invisible /after/ anim
            mCardAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mView.setVisibility(View.INVISIBLE);
                }
            });

            mCardAnimator.start();

            isCardVisible = false;

            ((Button) v).setText("Reveal Card");

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Main", "Landscape");
        } else {
            Log.d("Main", "Portrait");
        }

        super.onConfigurationChanged(newConfig);
    }

    public void animateCard(View v) {

        // If card is visible, animate to next activity
        if (isCardVisible) {

            Intent intent = new Intent(this, Activity2.class);

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, mView, "animatedCard");

            startActivity(intent, options.toBundle());

//            // Right now, we're just starting the activity. Let's attempt to animate the card
//            startActivity(new Intent(this, Activity2.class));

        // If card is invisible, we have nothing to animate... =\
        } else {

            Toast.makeText(
                    this,
                    "Where is the card?",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
}
