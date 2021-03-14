package com.school.project.resume;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.MessageEntity;
import com.school.project.bean.ResumeEntity;
import com.school.project.bean.UserEntity;
import com.school.project.database.DataSourceModel;
import com.school.project.databinding.ActivityResumeDetailsBinding;
import com.school.project.message.MessageListViewModel;
import com.school.project.resume.ResumeObserveAdapter;
import com.school.project.resume.ResumeViewModel;
import com.school.project.user.UserStatus;
import com.school.project.utils.AppContext;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.Logger;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class ResumeDetailsActivity extends BaseActivity {
    private ActivityResumeDetailsBinding mBinding;
    private int mTagUid;
    private int mCurrentUid;
    private ResumeViewModel mResumeViewModel;
    private ResumeObserveAdapter mStillAdapter;
    private ResumeObserveAdapter mJobAdapter;
    private ResumeObserveAdapter mObjAdapter;
    private MessageListViewModel mMessageViewModel;
    private MutableLiveData<UserEntity> mEntityMutableLiveData = new MutableLiveData<>();

    @Override
    protected void initDataBinding() {
        mBinding = initDataBinding(R.layout.activity_resume_details);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle(getIntent().getStringExtra("title"));
        setActionBtn("发送Offer", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSendOffer();
            }
        });
        mTagUid = getIntent().getIntExtra("uid", 0);
        mCurrentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        mResumeViewModel = DataSourceModel.getViewModel(this, ResumeViewModel.class);
        mBinding.skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBinding.jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.ojbRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStillAdapter = new ResumeObserveAdapter(false);
        mJobAdapter = new ResumeObserveAdapter(false);
        mObjAdapter = new ResumeObserveAdapter(false);
        mBinding.skillsRecyclerView.setAdapter(mStillAdapter);
        mBinding.jobRecyclerView.setAdapter(mJobAdapter);
        mBinding.ojbRecyclerView.setAdapter(mObjAdapter);
        mResumeViewModel.getListSkillLiveData(mTagUid).observe(this, new Observer<PagedList<ResumeEntity>>() {
            @Override
            public void onChanged(PagedList<ResumeEntity> resumeEntities) {
                Logger.i("getListSkillLiveData : " + resumeEntities.size());
                mStillAdapter.submitList(resumeEntities);
            }
        });

        mResumeViewModel.getListJobLiveData(mTagUid).observe(this, new Observer<PagedList<ResumeEntity>>() {
            @Override
            public void onChanged(PagedList<ResumeEntity> resumeEntities) {
                Logger.i("getListJobLiveData : " + resumeEntities.size());
                mJobAdapter.submitList(resumeEntities);
            }
        });

        mResumeViewModel.getListObjLiveData(mTagUid).observe(this, new Observer<PagedList<ResumeEntity>>() {
            @Override
            public void onChanged(PagedList<ResumeEntity> resumeEntities) {
                Logger.i("getListObjLiveData : " + resumeEntities.size());
                mObjAdapter.submitList(resumeEntities);
            }
        });
        mMessageViewModel = MessageListViewModel.getViewModel(this, MessageListViewModel.class);
        AppContext.getUserInfo(mCurrentUid, mEntityMutableLiveData);
    }

    private void goSendOffer() {
        DialogUtils.showInputDialog(this, "请输入Offer内容：", InputType.TYPE_CLASS_TEXT, new DialogUtils.OnAppDialogListener() {
            @Override
            public void onEditTextInput(String text) {
                if (TextUtils.isEmpty(text) || mEntityMutableLiveData.getValue() == null) {
                    return;
                }
                MessageEntity msg = new MessageEntity();
                msg.inUid = mTagUid;
                msg.outUid = mCurrentUid;
                msg.title = "来自" + mEntityMutableLiveData.getValue().name + "的Offer通知";
                msg.content = text;
                msg.time = System.currentTimeMillis();
                msg.mode = MessageEntity.MODE_OFFER;
                mMessageViewModel.insert(msg);
                showToast("发送成功");
            }
        });
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }
}