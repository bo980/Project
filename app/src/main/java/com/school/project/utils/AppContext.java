package com.school.project.utils;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AppContext {
    private static Application sApplication;
    public static Application getApplication() {
        if (sApplication == null) {
            throw new NullPointerException("AppContext not init");
        }
        return sApplication;
    }

    public static Context getContext() {
        return getApplication();
    }

    public static void initApp(Application application) {
        sApplication = application;
    }

    public static Disposable getUserInfo(final int uid, final MutableLiveData<UserEntity> entityMutableLiveData) {
        return AppDatabase.get().userDao().queryUser(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        entityMutableLiveData.setValue(userEntity);
                    }
                });
    }
}
