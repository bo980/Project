package com.school.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.databinding.ObservableField;

import com.school.project.base.BaseActivity;
import com.school.project.databinding.ActivityMainBinding;
import com.school.project.user.LoginActivity;
import com.school.project.user.UserStatus;
import com.school.project.utils.SharedPreferencesUtils;

public class MainActivity extends BaseActivity {
    private static final long MILLIS_IN_FUTURE = 2000L;
    private static final long COUNTDOWN_INTERVAL = 1000L;
    private CountDownTimer mCountDownTimer;
    public ObservableField<String> mCountDownFinished = new ObservableField<>();
    public ActivityMainBinding mActivityMainBinding;

    @Override
    protected void initDataBinding() {
        mActivityMainBinding = initDataBinding(R.layout.activity_main);
        mActivityMainBinding.setMainCountdown(this);
    }

    @Override
    protected void initViewOrData(Bundle bundle) {
        mCountDownTimer = new CountDownTimer(MILLIS_IN_FUTURE, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mCountDownFinished != null) {
                    mCountDownFinished.set(millisUntilFinished / COUNTDOWN_INTERVAL + "S");
                }
            }

            @Override
            public void onFinish() {
                checkLogin();
            }
        };
        mCountDownTimer.start();
    }

    private void checkLogin() {
        int status = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_STATUS_KEY);
        App.sUserStatus = status;
        if (status > 0){
            startHomePage();
        }else {
            startLoginPage();
        }
    }

    private void startHomePage() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void startLoginPage() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}