package com.school.project.message;

import android.os.Bundle;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.databinding.ActivityMessageDetailsBinding;
import com.school.project.utils.ScreenUtils;

public class MessageDetailsActivity extends BaseActivity {
    private ActivityMessageDetailsBinding mBinding;

    @Override
    protected void initDataBinding() {
        mBinding = initDataBinding(R.layout.activity_message_details);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle(getIntent().getStringExtra("title"));
        mBinding.content.setText(getIntent().getStringExtra("content"));
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }
}