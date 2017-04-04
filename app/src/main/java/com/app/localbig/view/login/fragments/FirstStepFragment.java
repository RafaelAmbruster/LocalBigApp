package com.app.localbig.view.login.fragments;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.view.login.TwoStepsLoginListener;
import com.app.localbig.view.login.view.MaterialTwoStepsLogin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class FirstStepFragment extends Fragment {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 123;

    private TwoStepsLoginListener mListener;
    private EditText email;
    private MaterialTwoStepsLogin mtsl;
    private Button next;
    private ProgressBar progressBarFirst;
    private LinearLayout layoutFirst;
    private FloatingActionButton register;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.login_first_view, null);

        email = (EditText) view.findViewById(R.id.email);
        next = (Button) view.findViewById(R.id.buttonNext);
        progressBarFirst = (ProgressBar) view.findViewById(R.id.progressBarFirst);
        layoutFirst = (LinearLayout) view.findViewById(R.id.layoutFirst);
        register = (FloatingActionButton) view.findViewById(R.id.fab);
        progressBarFirst.setVisibility(View.GONE);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    next.performClick();
                }
                return false;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarFirst.setVisibility(View.VISIBLE);
                layoutFirst.setVisibility(View.GONE);
                mListener.onNextClicked(email.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        return view;
    }

    public void setListener(MaterialTwoStepsLogin mtsl, TwoStepsLoginListener listener) {
        this.mtsl = mtsl;
        mListener = listener;
    }

    public void emailVerified() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mtsl.getSecondStepFragment().setListener(mtsl, mListener);
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragmentView, mtsl.getSecondStepFragment()).addToBackStack("secondStepLogin")
                .commit();
    }

    public void notVerified() {
        progressBarFirst.setVisibility(View.GONE);
        layoutFirst.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }
}
