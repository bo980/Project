package com.school.project.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.UserEntity;
import com.school.project.databinding.ActivityRegisterBinding;
import com.school.project.utils.Logger;
import com.school.project.utils.RxJavaHelper;

import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding mActivityRegisterBinding;
    private RegisterViewModel mRegisterViewModel;

    @Override
    protected void initDataBinding() {
        mActivityRegisterBinding = initDataBinding(R.layout.activity_register);
    }

    @Override
    protected void initViewOrData(Bundle bundle) {
        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
    }

    public void back(View view) {
        finish();
    }

    public void goRegister(View view) {
        Logger.i("enter goRegister.");
        String account = mActivityRegisterBinding.textPersonName.getText().toString();
        String password = mActivityRegisterBinding.textPassword.getText().toString();
        String passwordConfirm = mActivityRegisterBinding.textPasswordConfirm.getText().toString();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)) {
            showToast("用户名或密码不能为空");
            return;
        }
        if (!TextUtils.equals(password, passwordConfirm)) {
            showToast("两次输入密码不相同");
            return;
        }
        boolean isEnterprise = mActivityRegisterBinding.radioGroup
                .getCheckedRadioButtonId() == mActivityRegisterBinding.enterprise.getId();
        UserEntity userEntity = new UserEntity();
        userEntity.account = account;
        userEntity.password = password;
        userEntity.channel = isEnterprise ? 1 : 0;
        Disposable insert = mRegisterViewModel.insert(userEntity);
        RxJavaHelper.addDisposable(this, insert);
        Logger.i("register result : " + userEntity.account);
        goLogin(userEntity);
    }

    private void goLogin(UserEntity userEntity) {
        Intent intent = getIntent();
        String account = userEntity.account;
        intent.putExtra("account", account);
        setResult(RESULT_OK, intent);
        finish();
    }
}