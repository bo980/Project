package com.school.project.position;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.PositionEntity;
import com.school.project.bean.UserEntity;
import com.school.project.databinding.ActivityPositionEditBinding;
import com.school.project.user.UserStatus;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class PositionEditActivity extends BaseActivity {

    private ActivityPositionEditBinding mPositionEditBinding;
    private PositionEditViewModel mEditViewModel;
    private long pid;
    private PositionEntity mPositionEntity;

    @Override
    protected void initDataBinding() {
        mPositionEditBinding = initDataBinding(R.layout.activity_position_edit);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        pid = getIntent().getLongExtra("pid", 0);
        int currentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        setTitle("编辑职位");
        mEditViewModel = PositionEditViewModel.getViewModel(this, PositionEditViewModel.class);
        mEditViewModel.mEntityMutableLiveData.observe(this, new Observer<PositionEntity>() {
            @Override
            public void onChanged(PositionEntity positionEntity) {
                mPositionEntity = positionEntity;
                mPositionEditBinding.setPosition(mPositionEntity);
            }
        });
        mEditViewModel.mUserEntityMutableLiveData.observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (mPositionEntity != null) {
                    mPositionEntity.uid = userEntity.uid;
                    mPositionEntity.address = userEntity.address;
                    mPositionEntity.companyName = userEntity.name;
                }
            }
        });
        if (pid > 0) {
            mEditViewModel.getPositionEntity(pid);
        } else {
            mPositionEntity = new PositionEntity();
            mEditViewModel.getUserEntity(currentUid);
            mPositionEditBinding.setPosition(mPositionEntity);
        }
    }

    public void goEditName(View view) {
        DialogUtils.showInputDialog(this, "请输入职位名称：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                mPositionEntity.name = text;
                mEditViewModel.insert(mPositionEntity);
                mEditViewModel.getPositionEntity(mPositionEntity.pid);
            }
        });
    }

    public void goEditWorkNum(View view) {
        DialogUtils.showInputDialog(this, "请输入经验要求：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                mPositionEntity.workNum = text;
                mEditViewModel.insert(mPositionEntity);
            }
        });
    }

    public void goEditSeniority(View view) {
        DialogUtils.showInputDialog(this, "请输入学历要求：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                mPositionEntity.seniority = text;
                mEditViewModel.insert(mPositionEntity);
            }
        });
    }

    public void goEditSalary(View view) {
        DialogUtils.showInputDialog(this, "请输入薪资范围：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                mPositionEntity.salary = text;
                mEditViewModel.insert(mPositionEntity);
            }
        });
    }

    public void goEditAddress(View view) {
        DialogUtils.showInputDialog(this, "请输入工作地址：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                mPositionEntity.address = text;
                mEditViewModel.insert(mPositionEntity);
            }
        });
    }

    public void goEditDetails(View view) {
        DialogUtils.showInputDialog(this, "请输入职位介绍：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                mPositionEntity.details = text;
                mEditViewModel.insert(mPositionEntity);
            }
        });
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mPositionEditBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }
}