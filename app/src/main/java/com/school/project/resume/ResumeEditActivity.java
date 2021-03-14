package com.school.project.resume;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.ResumeEntity;
import com.school.project.database.DataSourceModel;
import com.school.project.databinding.ActivityResumeEditBinding;
import com.school.project.user.UserStatus;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class ResumeEditActivity extends BaseActivity {

    private ActivityResumeEditBinding mBinding;
    private int type;
    private long rid;
    private ResumeEntity mResumeEntity;
    private ResumeEditViewModel mResumeEditViewModel;

    @Override
    protected void initDataBinding() {
        mBinding = initDataBinding(R.layout.activity_resume_edit);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);
        rid = getIntent().getLongExtra("rid", 0);
        int currentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        setTitle(type == ResumeEntity.TYPE_JOB ? "工作经历" : "项目经历");
        mResumeEditViewModel = DataSourceModel.getViewModel(this, ResumeEditViewModel.class);
        mResumeEditViewModel.mEntityMutableLiveData.observe(this, new Observer<ResumeEntity>() {
            @Override
            public void onChanged(ResumeEntity resumeEntity) {
                mResumeEntity = resumeEntity;
                mBinding.setResume(resumeEntity);
            }
        });
        if (rid > 0) {
            mResumeEditViewModel.getResumeEntity(rid);
        } else {
            mResumeEntity = new ResumeEntity();
            mResumeEntity.type = type;
            mResumeEntity.uid = currentUid;
            mBinding.setResume(mResumeEntity);
        }
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }

    public void goEditName(View view) {
        DialogUtils.showInputDialog(this, "请输入名称：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    showToast("名称不能为空");
                    return;
                }
                mResumeEntity.name = text;
                mResumeEditViewModel.insert(mResumeEntity);
                mResumeEditViewModel.getResumeEntity(mResumeEntity.rid);
            }
        });
    }

    public void goEditStartTime(View view) {
    }

    public void goEditEndTime(View view) {
    }

    public void goEditJobName(View view) {
        DialogUtils.showInputDialog(this, "请输入岗位名称：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mResumeEntity.jobName = text;
                mResumeEditViewModel.insert(mResumeEntity);
            }
        });
    }

    public void goEditDesc(View view) {
        DialogUtils.showInputDialog(this, "请输入内容介绍：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mResumeEntity.desc = text;
                mResumeEditViewModel.insert(mResumeEntity);
            }
        });
    }
}