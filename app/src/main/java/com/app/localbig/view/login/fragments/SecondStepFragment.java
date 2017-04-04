package com.app.localbig.view.login.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.view.login.TwoStepsLoginListener;
import com.app.localbig.view.login.view.MaterialTwoStepsLogin;

public class SecondStepFragment extends Fragment implements View.OnKeyListener {

    private TwoStepsLoginListener mListener;
    private TextView email;
    private EditText editTextPassword;
    private MaterialTwoStepsLogin mtsl;
    private Button pass_forget;
    private ProgressBar progressBarSecond;
    private Button buttonLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.login_second_view, null);
        email = (TextView) view.findViewById(R.id.email);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        pass_forget = (Button) view.findViewById(R.id.pass_forget);
        progressBarSecond = (ProgressBar) view.findViewById(R.id.progressBarSecond);
        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);

        progressBarSecond.setVisibility(View.GONE);

        if (mtsl != null) {
            email.setText(mtsl.getEmail());
            if (mtsl.getButton_login_text_color() != 0) {
                buttonLogin.setTextColor(mtsl.getButton_login_text_color());
                pass_forget.setTextColor(mtsl.getButton_login_text_color());
            }
            if (mtsl.getEdittext_password_background() != 0)
                editTextPassword.setBackgroundResource(mtsl.getEdittext_password_background());

            buttonLogin.setBackgroundResource(mtsl.getButton_login_background());
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSecond.setVisibility(View.VISIBLE);
                mListener.onLoginClicked(editTextPassword.getText().toString());
            }
        });

        pass_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecoverPasswordClicked();
            }
        });

        view.setBackgroundColor(mtsl.getSecond_step_background_color());

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(this);
    }

    @SuppressLint("NewApi")
    private void createReveal(final View myView) {

        int cx = (myView.getWidth()) / 2;
        int cy = (myView.getHeight()) / 2;
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = android.view.ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            animator.setDuration(800);
            animator.start();
        }
    }

    public void setListener(MaterialTwoStepsLogin mtsl, TwoStepsLoginListener listener) {
        mListener = listener;
        this.mtsl = mtsl;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mListener.onBackToMail();
                return false;
        }
        return false;
    }

    public void wrongPassword() {
        progressBarSecond.setVisibility(View.GONE);
    }
}
