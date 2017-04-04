package com.app.localbig.view.login;

public interface TwoStepsLoginListener {
    void onNextClicked(String email);

    void onLoginClicked(String password);

    void onRecoverPasswordClicked();

    void onBackToMail();

    void onRegisterClicked();
}
