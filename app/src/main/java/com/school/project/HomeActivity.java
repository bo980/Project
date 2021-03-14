package com.school.project;


import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.school.project.base.BaseActivity;
import com.school.project.databinding.ActivityHomeBinding;
import com.school.project.message.MessageFragment;
import com.school.project.position.PositionFragment;
import com.school.project.user.MineFragment;
import com.school.project.user.UserStatus;
import com.school.project.utils.Logger;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding mActivityHomeBinding;
    private boolean mIsEnterprise;
    private PositionFragment mPositionFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;

    @Override
    protected void initDataBinding() {
        mActivityHomeBinding = initDataBinding(R.layout.activity_home);
    }

    @Override
    protected void initViewOrData(Bundle bundle) {
        mIsEnterprise = App.sUserStatus == UserStatus.ENTERPRISE;
        TabLayout tabLayout = mActivityHomeBinding.tablayout;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Logger.i("onTabSelected : " + tab.getPosition());
                switchFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.addTab(tabLayout.newTab().setText(mIsEnterprise ? "学生" : "职位"));
        tabLayout.addTab(tabLayout.newTab().setText("消息"));
        tabLayout.addTab(tabLayout.newTab().setText("我的"));
    }

    private void switchFragment(int position) {
        switch (position) {
            case 0:
                switchPositionFragment();
                break;
            case 1:
                switchMessageFragment();
                break;
            case 2:
                switchMineFragment();
                break;
        }
    }

    private void switchPositionFragment() {
        if (mPositionFragment == null) {
            mPositionFragment = PositionFragment.newInstance();
        }
        switchFragment(mPositionFragment, R.id.view_content);
    }

    private void switchMessageFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = MessageFragment.newInstance();
        }
        switchFragment(mMessageFragment, R.id.view_content);
    }

    private void switchMineFragment() {
        if (mMineFragment == null) {
            mMineFragment = MineFragment.newInstance();
        }
        switchFragment(mMineFragment, R.id.view_content);
    }
}