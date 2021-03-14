package com.school.project.user;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.database.DataSourceModel;
import com.school.project.databinding.ActivityUserDetailsBinding;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.Logger;
import com.school.project.utils.ScreenUtils;

public class UserDetailsActivity extends BaseActivity {

    private ActivityUserDetailsBinding mBinding;
    private UserDetailsViewModel mViewModel;

    @Override
    protected void initDataBinding() {
        mBinding = initDataBinding(R.layout.activity_user_details);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle("我的资料");
        mViewModel = DataSourceModel.getViewModel(this, UserDetailsViewModel.class);
        mBinding.setViewModel(mViewModel);
        mViewModel.getUserInfo();
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }

    public void editHead(View view) {
        showToast("当前版本不支持");
    }

    public void editName(View view) {
        DialogUtils.showInputDialog(this, "请输入姓名：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mViewModel.mUserLiveData.get().name = text;
                Logger.i(mViewModel.mUserLiveData.get().toString());
                mViewModel.update();
            }
        });
    }

    public void editAge(View view) {
        DialogUtils.showInputDialog(this, "请输入年龄：", InputType.TYPE_CLASS_NUMBER, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mViewModel.mUserLiveData.get().age = TextUtils.isEmpty(text) ? 0 : Integer.parseInt(text);
                mViewModel.update();
            }
        });
    }

    public void editGender(View view) {
        DialogUtils.showSingleChoiceDialog(this, "请选择性别：", new DialogUtils.OnAppDialogListener() {
            @Override
            public void onSingleSelected(int position) {
                mViewModel.mUserLiveData.get().gender = position == 0 ? 1 : 2;
                mViewModel.update();
            }
        }, "男", "女");
    }

    String[] strings = {"本科", "大专", "高中"};

    public void editSeniority(View view) {

        DialogUtils.showSingleChoiceDialog(this, "请输入学历：", new DialogUtils.OnAppDialogListener() {
            @Override
            public void onSingleSelected(int position) {
                mViewModel.mUserLiveData.get().seniority = strings[position];
                mViewModel.update();
            }
        }, strings);
    }

    public void editMajor(View view) {
        DialogUtils.showInputDialog(this, "请输入专业：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mViewModel.mUserLiveData.get().major = text;
                mViewModel.update();
            }
        });
    }

    public void editWorkNum(View view) {
        DialogUtils.showInputDialog(this, "请输入工作年限：", InputType.TYPE_CLASS_NUMBER, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mViewModel.mUserLiveData.get().workNum = TextUtils.isEmpty(text) ? 0 : Integer.parseInt(text);
                mViewModel.update();
            }
        });
    }

    public void editAutograph(View view) {
        DialogUtils.showInputDialog(this, "请输入个性签名：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                mViewModel.mUserLiveData.get().autograph = text;
                mViewModel.update();
            }
        });
    }
}