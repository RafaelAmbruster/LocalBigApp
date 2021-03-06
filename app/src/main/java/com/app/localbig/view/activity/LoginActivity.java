package com.app.localbig.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.data.dao.ApplicationUserDAO;
import com.app.localbig.data.dao.IOperationDAO;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.model.AccountLoginResponse;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.account.LoginTask;
import com.app.localbig.rest.task.account.ResponseLoginCallBack;
import com.app.localbig.rest.task.account.UserApplicationTask;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements ResponseLoginCallBack,
        ResponseObjectCallBack {

    private static final int REQUEST_SIGNUP = 0;

    private EditText _emailText;
    private EditText _passwordText;
    private ActionProcessButton _loginButton;
    private TextView _signupLink;
    private TextInputLayout _emailWrapper, _passwordWrapper;
    private View parent_view;
    private ApplicationUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);

        _emailText = (EditText) findViewById(R.id.input_email);
        _emailText.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(LoginActivity.this));
        _passwordText = (EditText) findViewById(R.id.input_password);
        _passwordText.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(LoginActivity.this));
        _loginButton = (ActionProcessButton) findViewById(R.id.btn_login);
        _loginButton.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(LoginActivity.this));
        _signupLink = (TextView) findViewById(R.id.link_signup);
        _emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        _emailWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(LoginActivity.this));
        _passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        _passwordWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(LoginActivity.this));

        _loginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {

        //if (!validate()) {
        //    return;
        //}

        _loginButton.setEnabled(false);
        _emailText.setEnabled(false);
        _passwordText.setEnabled(false);
        _signupLink.setEnabled(false);
        _loginButton.setProgress(1);


        user = new ApplicationUser();
        user.setId("1");
        user.setPhoneNumber("123123123");
        user.setFirstName("Demo");
        user.setLastName("Loren");
        user.setEmail("demo@bertnis.com");
        user.setToken("asdasdasdasdasdsad");
        user.setOwner(true);
        user.setActive(true);
        new ApplicationUserDAO(AppDatabaseManager.getInstance().getHelper()).Create(user, IOperationDAO.OPERATION_INSERT_OR_UPDATE);

        _loginButton.setEnabled(true);
        _emailText.setEnabled(true);
        _passwordText.setEnabled(true);
        _signupLink.setEnabled(true);
        _loginButton.setProgress(100);

        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        Gson gSon = new Gson();
        b.putString(MainActivity.EXTRA_OBJCT_USER, gSon.toJson(user));
        intent.putExtras(b);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        }, 500);

        //new LoginTask(this).CallService(_emailText.getText().toString(), _passwordText.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }*/

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailWrapper.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailWrapper.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordWrapper.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordWrapper.setError(null);
        }

        return valid;
    }

    @Override
    public void onResponseLoginCallBack(AccountLoginResponse response) {
        if (response instanceof AccountLoginResponse) {
            new UserApplicationTask(this).CallService(2, response.getUserId());
        }
    }

    @Override
    public void onError(String message, Integer code) {
        _loginButton.setProgress(-1);
        _loginButton.setText("Error!");

        showMessage(message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _loginButton.setEnabled(true);
                _emailText.setEnabled(true);
                _passwordText.setEnabled(true);
                _signupLink.setEnabled(true);
                _loginButton.setProgress(0);
            }
        }, 1000);
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        if (object instanceof ApplicationUser) {

            user = (ApplicationUser) object;
            user.setActive(true);
            new ApplicationUserDAO(AppDatabaseManager.getInstance().getHelper()).Create(user, IOperationDAO.OPERATION_INSERT_OR_UPDATE);

            _loginButton.setEnabled(true);
            _emailText.setEnabled(true);
            _passwordText.setEnabled(true);
            _signupLink.setEnabled(true);
            _loginButton.setProgress(100);

            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //final Intent intent = new Intent();
            Bundle b = new Bundle();
            Gson gSon = new Gson();
            b.putString(MainActivity.EXTRA_OBJCT_USER, gSon.toJson(user));
            intent.putExtras(b);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    //setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }, 500);
        }
    }

    private void showMessage(String message) {
        Snackbar snack = Snackbar.make(parent_view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }
}
