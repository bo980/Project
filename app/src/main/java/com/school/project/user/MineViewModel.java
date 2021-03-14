package com.school.project.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.utils.SharedPreferencesUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MineViewModel extends ViewModel {
    public MutableLiveData<UserEntity> mUserLiveData = new MutableLiveData<>();

    public Disposable getUserInfo() {
        int uid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        return AppDatabase.get().userDao().queryUser(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        mUserLiveData.setValue(userEntity);
                    }
                });
    }

    public void goLogout() {
        SharedPreferencesUtils.getInstance().putInt(UserStatus.USER_STATUS_KEY, 0);
    }
}