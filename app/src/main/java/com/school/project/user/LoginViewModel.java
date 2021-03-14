package com.school.project.user;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.UserDao;
import com.school.project.utils.Logger;
import com.school.project.utils.SharedPreferencesUtils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends DataSourceModel<UserEntity, UserDao> {

    public MutableLiveData<Integer> mStatusLiveData = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected UserDao setDao() {
        return AppDatabase.get().userDao();
    }

    public Disposable goLogin(String account, final String password) {
        final Disposable[] disposable = {null};
        getDao().queryUserForAccount(account).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onSuccess(UserEntity userEntity) {
                        Logger.i(userEntity.toString());
                        if (TextUtils.equals(userEntity.password, password)) {
                            SharedPreferencesUtils.getInstance().putInt(UserStatus.USER_ID_KEY, userEntity.uid);
                            mStatusLiveData.setValue(userEntity.channel == 0 ? UserStatus.STUDENT
                                    : UserStatus.ENTERPRISE);
                        } else {
                            mStatusLiveData.setValue(UserStatus.PASSWORD_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mStatusLiveData.setValue(UserStatus.NONE);
                    }
                });
        return disposable[0];
    }
}
