package com.school.project.position;

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

import com.school.project.App;
import com.school.project.R;
import com.school.project.base.BaseFragment;
import com.school.project.bean.PositionEntity;
import com.school.project.bean.UserEntity;
import com.school.project.databinding.MineFragmentBinding;
import com.school.project.databinding.PositionFragmentBinding;
import com.school.project.user.MineViewModel;
import com.school.project.user.UserStatus;
import com.school.project.utils.DensityUtils;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class PositionFragment extends BaseFragment<PositionFragmentBinding> {

    private StudentListViewModel mStudentListViewModel;
    private boolean mIsEnterprise;
    private StudentObserveAdapter mStudentObserveAdapter;
    private PositionListViewModel mPositionListViewModel;
    private PositionObserveAdapter mPositionObserveAdapter;
    private int mCurrentUid;

    public static PositionFragment newInstance() {
        return new PositionFragment();
    }

    @Override
    protected PositionFragmentBinding initDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return initDataBinding(R.layout.position_fragment, inflater, container);
    }

    @Override
    protected void initViewOrData(View view, Bundle bundle) {
        mIsEnterprise = App.sUserStatus == UserStatus.ENTERPRISE;
        mCurrentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        setTitle(mIsEnterprise ? "学生" : "职位");
        int statusBarHeight = ScreenUtils.getStatusBarHeight(getActivity());
        getViewDataBinding().actionBar.setPadding(0, statusBarHeight, 0, 0);
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
        if (mIsEnterprise) {
            initStudentList();
        } else {
            initPositionList();
        }

    }

    private void initStudentList() {
        mStudentObserveAdapter = new StudentObserveAdapter();
        getViewDataBinding().recyclerView.setAdapter(mStudentObserveAdapter);
        mStudentListViewModel = ViewModelProviders.of(this).get(StudentListViewModel.class);
        mStudentListViewModel.getStudents().observe(this, new Observer<PagedList<UserEntity>>() {
            @Override
            public void onChanged(PagedList<UserEntity> userEntities) {
                mStudentObserveAdapter.submitList(userEntities);
                getViewDataBinding().btnEmpty.setVisibility(mStudentObserveAdapter.getItemCount() > 0
                        ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void initPositionList() {
        mPositionObserveAdapter = new PositionObserveAdapter(false);
        getViewDataBinding().recyclerView.setAdapter(mPositionObserveAdapter);
        mPositionListViewModel = ViewModelProviders.of(this).get(PositionListViewModel.class);
        mPositionListViewModel.getDataObserve().observe(this, new Observer<PagedList<PositionEntity>>() {
            @Override
            public void onChanged(PagedList<PositionEntity> entities) {
                mPositionObserveAdapter.submitList(entities);
                getViewDataBinding().btnEmpty.setVisibility(mPositionObserveAdapter.getItemCount() > 0
                        ? View.GONE : View.VISIBLE);
            }
        });
    }

}