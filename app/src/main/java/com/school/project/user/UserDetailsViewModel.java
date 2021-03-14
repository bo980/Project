package com.school.project.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.UserDao;
import com.school.project.utils.SharedPreferencesUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserDetailsViewModel extends DataSourceModel<UserEntity, UserDao> {

    public ObservableField<UserEntity> mUserLiveData = new ObservableField<>();

    public UserDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected UserDao setDao() {
        return AppDatabase.get().userDao();
    }

    public Disposable getUserInfo() {
        int uid = SharedPreferencesUtils.getInstance().getInt(UserStatus.USER_ID_KEY);
        return AppDatabase.get().userDao().queryUser(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        mUserLiveData.set(userEntity);
                    }
                });
    }

    public void update() {
        update(mUserLiveData.get());
    }
}
