package com.school.project.resume;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.school.project.bean.ResumeEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.ResumeDao;
import com.school.project.utils.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResumeEditViewModel extends DataSourceModel<ResumeEntity, ResumeDao> {

    public MutableLiveData<ResumeEntity> mEntityMutableLiveData = new MutableLiveData<>();

    public ResumeEditViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ResumeDao setDao() {
        return AppDatabase.get().resumeDao();
    }

    public Disposable getResumeEntity(long rid) {
        return getDao().queryResume(rid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResumeEntity>() {
                    @Override
                    public void accept(ResumeEntity resumeEntity) throws Exception {
                        Logger.i("getResumeEntity : " + resumeEntity.type);
                        mEntityMutableLiveData.setValue(resumeEntity);
                    }
                });
    }
}
