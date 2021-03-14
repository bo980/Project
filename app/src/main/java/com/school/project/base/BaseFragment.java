package com.school.project.base;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.school.project.R;
import com.school.project.utils.ToastUtils;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    private T mViewDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = initDataBinding(inflater, container);
        return mViewDataBinding.getRoot();
    }

    protected abstract T initDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    public T initDataBinding(@LayoutRes int layoutId, @NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return DataBindingUtil.inflate(inflater, layoutId, container, false);
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                initViewOrData(view, savedInstanceState);
                return false;
            }
        });
    }

    protected abstract void initViewOrData(View view, Bundle bundle);

    public void setTitle(String title) {
        TextView titleTv = mViewDataBinding.getRoot().findViewById(R.id.tv_title);
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    public void showToast(String msg) {
        ToastUtils.showToast(getActivity(), msg);
    }
}
