package com.school.project.position;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.MessageEntity;
import com.school.project.bean.PositionEntity;
import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.databinding.ActivityPositionDetailsBinding;
import com.school.project.message.MessageListViewModel;
import com.school.project.user.UserStatus;
import com.school.project.utils.AppContext;
import com.school.project.utils.DialogUtils;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class PositionDetailsActivity extends BaseActivity {
    private MutableLiveData<UserEntity> mEntityMutableLiveData = new MutableLiveData<>();
    private ActivityPositionDetailsBinding mDetailsBinding;
    private PositionEditViewModel mEditViewModel;
    private MessageListViewModel mMessageViewModel;
    private long pid;
    private int mTagUid;
    private int mCurrentUid;

    @Override
    protected void initDataBinding() {
        mDetailsBinding = initDataBinding(R.layout.activity_position_details);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle("职位信息");
        setActionBtn("申请", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goApply();
            }
        });
        pid = getIntent().getLongExtra("pid", 0);
        mCurrentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        mEditViewModel = PositionEditViewModel.getViewModel(this, PositionEditViewModel.class);
        mMessageViewModel = MessageListViewModel.getViewModel(this, MessageListViewModel.class);
        mEditViewModel.mEntityMutableLiveData.observe(this, new Observer<PositionEntity>() {
            @Override
            public void onChanged(PositionEntity positionEntity) {
                mDetailsBinding.setPosition(positionEntity);
                mEditViewModel.getUserEntity(positionEntity.uid);
            }
        });
        mEditViewModel.mUserEntityMutableLiveData.observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                mDetailsBinding.setUser(userEntity);
                mTagUid = userEntity.uid;
            }
        });
        mEditViewModel.getPositionEntity(pid);
        AppContext.getUserInfo(mCurrentUid, mEntityMutableLiveData);
    }

    private void goApply() {
        DialogUtils.showNormalDialog(this, "确认要申请此职位吗？", new DialogUtils.OnAppDialogListener() {
            @Override
            public void onCommit() {
                MessageEntity msg = new MessageEntity();
                msg.inUid = mTagUid;
                msg.outUid = mCurrentUid;
                msg.title = "来自" + mEntityMutableLiveData.getValue().name + "的职位申请通知";
                msg.content = "请点击查看申请～";
                msg.time = System.currentTimeMillis();
                msg.mode = MessageEntity.MODE_APPLY;
                mMessageViewModel.insert(msg);
                showToast("申请成功");
            }
        });
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mDetailsBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }
}