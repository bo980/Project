package com.school.project.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.school.project.App;
import com.school.project.R;
import com.school.project.base.BaseFragment;
import com.school.project.bean.UserEntity;
import com.school.project.databinding.MineFragmentBinding;
import com.school.project.position.CompanyDetailsActivity;
import com.school.project.position.PositionListActivity;
import com.school.project.resume.ResumeListActivity;
import com.school.project.utils.RxJavaHelper;

public class MineFragment extends BaseFragment<MineFragmentBinding> {

    private MineViewModel mViewModel;
    private boolean mIsEnterprise;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected MineFragmentBinding initDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return initDataBinding(R.layout.mine_fragment, inflater, container);
    }

    @Override
    protected void initViewOrData(View view, Bundle bundle) {
        getViewDataBinding().setMineFragment(this);
        mIsEnterprise = App.sUserStatus == UserStatus.ENTERPRISE;
        getViewDataBinding().tvResume.setText(mIsEnterprise ? "职位" : "个人简历");
        mViewModel = ViewModelProviders.of(this).get(MineViewModel.class);
        mViewModel.mUserLiveData.observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                getViewDataBinding().setUser(userEntity);
            }
        });
        RxJavaHelper.addDisposable(getActivity(), mViewModel.getUserInfo());
    }

    public void goUserDetails() {
        startActivity(new Intent(getActivity(), mIsEnterprise ? CompanyDetailsActivity.class : UserDetailsActivity.class));
    }

    public void goUserResume() {
        String name = getViewDataBinding().getUser().name;
        if (TextUtils.isEmpty(name)) {
            showToast("请完善资料～");
            return;
        }
        startActivity(new Intent(getActivity(), mIsEnterprise ? PositionListActivity.class : ResumeListActivity.class));
    }

    public void goLogout() {
        mViewModel.goLogout();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        }
    }
}