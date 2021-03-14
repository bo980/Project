package com.school.project.resume;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.ResumeEntity;
import com.school.project.database.DataSourceModel;
import com.school.project.databinding.ActivityResumeListBinding;
import com.school.project.user.UserStatus;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.Logger;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class ResumeListActivity extends BaseActivity {

    private ActivityResumeListBinding mBinding;
    private int mCurrentUid;
    private ResumeViewModel mResumeViewModel;
    private ResumeObserveAdapter mStillAdapter;
    private ResumeObserveAdapter mJobAdapter;
    private ResumeObserveAdapter mObjAdapter;

    @Override
    protected void initDataBinding() {
        mBinding = initDataBinding(R.layout.activity_resume_list);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle("我的简历");
        mCurrentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        mResumeViewModel = DataSourceModel.getViewModel(this, ResumeViewModel.class);
        mBinding.skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBinding.jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.ojbRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStillAdapter = new ResumeObserveAdapter(true);
        mJobAdapter = new ResumeObserveAdapter(true);
        mObjAdapter = new ResumeObserveAdapter(true);
        mBinding.skillsRecyclerView.setAdapter(mStillAdapter);
        mBinding.jobRecyclerView.setAdapter(mJobAdapter);
        mBinding.ojbRecyclerView.setAdapter(mObjAdapter);
        mResumeViewModel.getListSkillLiveData(mCurrentUid).observe(this, new Observer<PagedList<ResumeEntity>>() {
            @Override
            public void onChanged(PagedList<ResumeEntity> resumeEntities) {
                Logger.i("getListSkillLiveData : " + resumeEntities.size());
                mStillAdapter.submitList(resumeEntities);
            }
        });

        mResumeViewModel.getListJobLiveData(mCurrentUid).observe(this, new Observer<PagedList<ResumeEntity>>() {
            @Override
            public void onChanged(PagedList<ResumeEntity> resumeEntities) {
                Logger.i("getListJobLiveData : " + resumeEntities.size());
                mJobAdapter.submitList(resumeEntities);
            }
        });

        mResumeViewModel.getListObjLiveData(mCurrentUid).observe(this, new Observer<PagedList<ResumeEntity>>() {
            @Override
            public void onChanged(PagedList<ResumeEntity> resumeEntities) {
                Logger.i("getListObjLiveData : " + resumeEntities.size());
                mObjAdapter.submitList(resumeEntities);
            }
        });
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }

    public void goEditSkills(View view) {
        DialogUtils.showInputDialog(this, "请输入个人技能：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                ResumeEntity resumeEntity = new ResumeEntity();
                resumeEntity.type = ResumeEntity.TYPE_SKILL;
                resumeEntity.uid = mCurrentUid;
                resumeEntity.skill = text;
                mResumeViewModel.insert(resumeEntity);
            }
        });
    }

    public void goEditJob(View view) {
        startActivity(new Intent(this, ResumeEditActivity.class)
                .putExtra("type", ResumeEntity.TYPE_JOB));
    }

    public void goEditObj(View view) {
        startActivity(new Intent(this, ResumeEditActivity.class)
                .putExtra("type", ResumeEntity.TYPE_OBJ));
    }
}