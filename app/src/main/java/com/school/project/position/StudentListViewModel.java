package com.school.project.position;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.UserDao;

public class StudentListViewModel extends DataSourceModel<UserEntity, UserDao> {
    private PagedList.Config.Builder config;

    public StudentListViewModel(@NonNull Application application) {
        super(application);
        config = new PagedList.Config.Builder()
                .setPageSize(setPageSize())
                .setPrefetchDistance(setPrefetchDistance())
                .setInitialLoadSizeHint(setInitialLoadSizeHint())
                .setEnablePlaceholders(setEnablePlaceholders());
    }

    @Override
    protected UserDao setDao() {
        return AppDatabase.get().userDao();
    }

    public LiveData<PagedList<UserEntity>> getStudents() {
        return new LivePagedListBuilder<>(
                getDao().queryAllStudent(), config.build())
                .build();
    }
}
