package com.school.project.message;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.school.project.R;
import com.school.project.base.BaseFragment;
import com.school.project.bean.MessageEntity;
import com.school.project.bean.UserEntity;
import com.school.project.databinding.MessageFragmentBinding;
import com.school.project.databinding.MineFragmentBinding;
import com.school.project.position.StudentListViewModel;
import com.school.project.user.MineViewModel;
import com.school.project.user.UserStatus;
import com.school.project.utils.DensityUtils;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class MessageFragment extends BaseFragment<MessageFragmentBinding> {

    private MessageListViewModel mViewModel;
    private MessageObserveAdapter mObserveAdapter;
    private int mCurrentUid;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected MessageFragmentBinding initDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return initDataBinding(R.layout.message_fragment, inflater, container);
    }

    @Override
    protected void initViewOrData(View view, Bundle bundle) {
        setTitle("消息");
        int statusBarHeight = ScreenUtils.getStatusBarHeight(getActivity());
        getViewDataBinding().actionBar.setPadding(0, statusBarHeight, 0, 0);
        mCurrentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        mViewModel = ViewModelProviders.of(this).get(MessageListViewModel.class);
        getViewDataBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getViewDataBinding().recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager == null) {
                    return;
                }
                int position = parent.getChildAdapterPosition(view);
                if (layoutManager instanceof LinearLayoutManager) {
                    if (position == 0) {
                        outRect.top = DensityUtils.dip2px(getActivity(), 5);
                    }
                    outRect.top = DensityUtils.dip2px(getActivity(), 5);
                }
            }
        });
        mObserveAdapter = new MessageObserveAdapter(mCurrentUid);
        getViewDataBinding().recyclerView.setAdapter(mObserveAdapter);
        mViewModel.getMessages(mCurrentUid).observe(this, new Observer<PagedList<MessageEntity>>() {
            @Override
            public void onChanged(PagedList<MessageEntity> messageEntities) {
                mObserveAdapter.submitList(messageEntities);
                getViewDataBinding().btnEmpty.setVisibility(messageEntities.size() > 0
                        ? View.GONE : View.VISIBLE);
            }
        });
    }


}