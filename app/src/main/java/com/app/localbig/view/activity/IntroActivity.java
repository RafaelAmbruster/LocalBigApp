package com.app.localbig.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import com.app.localbig.R;
import com.app.localbig.helper.manager.PreferenceManager;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {
    PreferenceManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PreferenceManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        enableLastSlideAlphaExitTransition(true);

        getNextButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new IntroSlideOne());
        addSlide(new IntroSlideTwo());
        addSlide(new IntroSlideThree());
        addSlide(new IntroSlideFour());
        addSlide(new IntroSlideFive());
        addSlide(new IntroSlideSix());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.bg_screen4)
                .buttonsColor(R.color.first_slide_buttons)
                .title("That's it")
                .description("Would you join us?")
                .build());

    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onFinish() {
        launchHomeScreen();
        super.onFinish();
    }
}