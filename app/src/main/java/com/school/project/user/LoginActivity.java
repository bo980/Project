package com.school.project.user;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.school.project.App;
import com.school.project.HomeActivity;
import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.UserEntity;
import com.school.project.database.DataSourceModel;
import com.school.project.databinding.ActivityLoginBinding;
import com.school.project.utils.Logger;
import com.school.project.utils.RxJavaHelper;
import com.school.project.utils.SharedPreferencesUtils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mActivityLoginBinding;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void initDataBinding() {
        mActivityLoginBinding = initDataBinding(R.layout.activity_login);
    }

    @Override
    protected void initViewOrData(Bundle bundle) {
        mLoginViewModel = DataSourceModel.getViewModel(this, LoginViewModel.class);
        mLoginViewModel.mStatusLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer userStatus) {
                checkLoginResult(userStatus);
            }
        });
        int uid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        getLastUser(uid, false);
    }

    private void checkLoginResult(int userStatus) {
        switch (userStatus) {
            case UserStatus.NONE:
                showToast("用户名不存在");
                break;
            case UserStatus.STUDENT:
            case UserStatus.ENTERPRISE:
                showToast("登录成功");
                goHome(userStatus);
                break;
            case UserStatus.PASSWORD_ERROR:
                showToast("密码错误");
                break;
        }
    }

    private void goHome(int userStatus) {
        App.sUserStatus = userStatus;
        SharedPreferencesUtils.getInstance().putInt(UserStatus.USER_STATUS_KEY, userStatus);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void goLogin(View view) {
        Logger.i("enter goLogin.");
        String account = mActivityLoginBinding.editTextTextPersonName.getText().toString();
        String password = mActivityLoginBinding.editTextTextPassword.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            showToast("用户名或密码不能为空");
            return;
        }
        mLoginViewModel.goLogin(account, password);
    }

    public void goRegister(View view) {
        Logger.i("enter goRegister.");
        startActivityForResult(new Intent(this, RegisterActivity.class), 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            String account = data.getStringExtra("account");
            getLastUser(account, true);
        }
    }

    private void getLastUser(String account, final boolean isAutoLogin) {
        mLoginViewModel.getDao().queryUserForAccount(account).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxJavaHelper.addDisposable(LoginActivity.this, d);
                    }

                    @Override
                    public void onSuccess(UserEntity userEntity) {
                        Logger.i(userEntity.toString());
                        mActivityLoginBinding.editTextTextPersonName.setText(userEntity.account);
                        mActivityLoginBinding.editTextTextPassword.setText(userEntity.password);
                        if (isAutoLogin) {
                            goLogin(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private void getLastUser(int uid, final boolean isAutoLogin) {
        mLoginViewModel.getDao().queryUserForId(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        RxJavaHelper.addDisposable(LoginActivity.this, d);
                    }

                    @Override
                    public void onSuccess(UserEntity userEntity) {
                        Logger.i(userEntity.toString());
                        mActivityLoginBinding.editTextTextPersonName.setText(userEntity.account);
                        mActivityLoginBinding.editTextTextPassword.setText(userEntity.password);
                        if (isAutoLogin) {
                            goLogin(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}