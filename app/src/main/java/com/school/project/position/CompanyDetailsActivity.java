package com.school.project.position;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.database.DataSourceModel;
import com.school.project.databinding.ActivityCompanyDetailsBinding;
import com.school.project.user.UserDetailsViewModel;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.Logger;
import com.school.project.utils.ScreenUtils;

public class CompanyDetailsActivity extends BaseActivity {
    private ActivityCompanyDetailsBinding mDetailsBinding;
    private UserDetailsViewModel mDetailsViewModel;

    @Override
    protected void initDataBinding() {
        mDetailsBinding = initDataBinding(R.layout.activity_company_details);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle("企业资料");
        mDetailsViewModel = DataSourceModel.getViewModel(this, UserDetailsViewModel.class);
        mDetailsBinding.setViewModel(mDetailsViewModel);
        mDetailsViewModel.getUserInfo();
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mDetailsBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }

    public void editName(View view) {
        DialogUtils.showInputDialog(this, "请输入企业名称：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mDetailsViewModel.mUserLiveData.get().name = text;
                Logger.i(mDetailsViewModel.mUserLiveData.get().toString());
                mDetailsViewModel.update();
            }
        });
    }

    public void editType(View view) {
        DialogUtils.showInputDialog(this, "请输入企业类别：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mDetailsViewModel.mUserLiveData.get().companyType = text;
                mDetailsViewModel.update();
            }
        });
    }

    public void editUserCount(View view) {
        DialogUtils.showInputDialog(this, "请输入企业人数：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mDetailsViewModel.mUserLiveData.get().userCount = text;
                mDetailsViewModel.update();
            }
        });
    }

    public void editAddress(View view) {
        DialogUtils.showInputDialog(this, "请输入企业地址：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mDetailsViewModel.mUserLiveData.get().address = text;
                mDetailsViewModel.update();
            }
        });
    }

    public void editDesc(View view) {
        DialogUtils.showInputDialog(this, "请输入企业介绍：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mDetailsViewModel.mUserLiveData.get().companyDesc = text;
                mDetailsViewModel.update();
            }
        });
    }
}