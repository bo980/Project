package com.school.project.resume;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.school.project.bean.ResumeEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.ResumeDao;

public class ResumeViewModel extends DataSourceModel<ResumeEntity, ResumeDao> {

    public LiveData<PagedList<ResumeEntity>> mListSkillLiveData;
    public LiveData<PagedList<ResumeEntity>> mListJobLiveData;
    public LiveData<PagedList<ResumeEntity>> mListObjLiveData;

    private PagedList.Config.Builder config;

    public ResumeViewModel(@NonNull Application application) {
        super(application);
        config = new PagedList.Config.Builder()
                .setPageSize(setPageSize())
                .setPrefetchDistance(setPrefetchDistance())
                .setInitialLoadSizeHint(setInitialLoadSizeHint())
                .setEnablePlaceholders(setEnablePlaceholders());
    }

    @Override
    protected ResumeDao setDao() {
        return AppDatabase.get().resumeDao();
    }

    public LiveData<PagedList<ResumeEntity>> getListSkillLiveData(int uid) {
        return new LivePagedListBuilder<>(
                getDao().queryResumeSkill(uid), config.build())
                .build();
    }

    public LiveData<PagedList<ResumeEntity>> getListJobLiveData(int uid) {
        return new LivePagedListBuilder<>(
                getDao().queryResumeJob(uid), config.build())
                .build();
    }

    public LiveData<PagedList<ResumeEntity>> getListObjLiveData(int uid) {
        return new LivePagedListBuilder<>(
                getDao().queryResumeObj(uid), config.build())
                .build();
    }
}
