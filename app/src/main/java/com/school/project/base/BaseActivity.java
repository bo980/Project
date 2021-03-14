package com.school.project.base;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.school.project.R;
import com.school.project.utils.ScreenUtils;
import com.school.project.utils.ToastUtils;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String CURRENT_FRAGMENT_KEY = "current_fragment_key";

    protected Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initStatusBar();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                initViewOrData(savedInstanceState);
                return false;
            }
        });
    }

    protected abstract void initDataBinding();

    protected void initViewOrData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentFragment = getFragment(CURRENT_FRAGMENT_KEY);
        }
    }

    protected void initStatusBar() {
        ScreenUtils.with(this).init();
    }

    public <T extends ViewDataBinding> T initDataBinding(@LayoutRes int layoutId) {
        return DataBindingUtil.setContentView(this, layoutId);
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg);
    }

    public void showToast(int resId) {
        ToastUtils.showToast(this, getString(resId));
    }

    public void setTitle(String title) {
        TextView titleTv = findViewById(R.id.tv_title);
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    public void setActionBtn(String text, View.OnClickListener listener) {
        TextView moreTv = findViewById(R.id.btn_more);
        if (moreTv != null) {
            moreTv.setText(text);
            moreTv.setOnClickListener(listener);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (ScreenUtils.isClickEditText(view, event)) {
                ScreenUtils.hideSoftKeyboard(view);
            }
            return super.dispatchTouchEvent(event);
        }
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    protected <T extends Fragment> T getFragment(Class<T> tClass) {
        return getFragment(tClass.getSimpleName());
    }

    protected <T extends Fragment> T getFragment(String tag) {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentByTag != null) {
            return (T) fragmentByTag;
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCurrentFragment != null) {
            outState.putString(CURRENT_FRAGMENT_KEY, mCurrentFragment.getClass().getSimpleName());
        }
        super.onSaveInstanceState(outState);
    }

    protected void switchFragment(Fragment targetFragment, int contentId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (targetFragment == mCurrentFragment) {
            return;
        }

        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }

        if (!targetFragment.isAdded()) {
            transaction.add(contentId, targetFragment, targetFragment.getClass().getSimpleName());
        } else {
            transaction.show(targetFragment);
        }

        transaction.commitAllowingStateLoss();
        mCurrentFragment = targetFragment;
    }
}