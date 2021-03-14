package com.school.project.position;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.school.project.R;
import com.school.project.base.BaseActivity;
import com.school.project.bean.PositionEntity;
import com.school.project.databinding.ActivityPositionListBinding;
import com.school.project.user.UserStatus;
import com.school.project.utils.DensityUtils;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.SharedPreferencesUtils;

public class PositionListActivity extends BaseActivity {

    private ActivityPositionListBinding mPositionListBinding;
    private PositionListViewModel mPositionListViewModel;
    private int mCurrentUid;
    private PositionObserveAdapter mObserveAdapter;

    @Override
    protected void initDataBinding() {
        mPositionListBinding = initDataBinding(R.layout.activity_position_list);
    }

    @Override
    protected void initViewOrData(Bundle savedInstanceState) {
        setTitle("职位");
        setActionBtn("添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PositionListActivity.this, PositionEditActivity.class));
            }
        });
        mCurrentUid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        mPositionListBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPositionListBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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
                        outRect.top = DensityUtils.dip2px(PositionListActivity.this, 5);
                    }
                    outRect.top = DensityUtils.dip2px(PositionListActivity.this, 5);
                }
            }
        });
        mObserveAdapter = new PositionObserveAdapter(true);
        mPositionListBinding.recyclerView.setAdapter(mObserveAdapter);
        mPositionListViewModel = PositionListViewModel.getViewModel(this, PositionListViewModel.class);
        mPositionListViewModel.getPositions(mCurrentUid).observe(this, new Observer<PagedList<PositionEntity>>() {
            @Override
            public void onChanged(PagedList<PositionEntity> positionEntities) {
                mObserveAdapter.submitList(positionEntities);
                mPositionListBinding.btnEmpty.setVisibility(positionEntities.size() > 0
                        ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        mPositionListBinding.actionBar.setPadding(0, statusBarHeight, 0, 0);
    }
}