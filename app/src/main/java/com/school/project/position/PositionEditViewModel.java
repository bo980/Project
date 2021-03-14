package com.school.project.position;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.school.project.bean.PositionEntity;
import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.PositionDao;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PositionEditViewModel extends DataSourceModel<PositionEntity, PositionDao> {

    public MutableLiveData<PositionEntity> mEntityMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<UserEntity> mUserEntityMutableLiveData = new MutableLiveData<>();

    public PositionEditViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected PositionDao setDao() {
        return AppDatabase.get().positionDao();
    }

    public Disposable getPositionEntity(long pid) {
        return getDao().queryPosition(pid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PositionEntity>() {
                    @Override
                    public void accept(PositionEntity positionEntity) throws Exception {
                        mEntityMutableLiveData.setValue(positionEntity);
                    }
                });
    }

    public Disposable getUserEntity(int uid) {
        return AppDatabase.get().userDao().queryUser(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        mUserEntityMutableLiveData.setValue(userEntity);
                    }
                });
    }
}
