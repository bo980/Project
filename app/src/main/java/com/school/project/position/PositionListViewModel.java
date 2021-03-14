package com.school.project.position;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.school.project.bean.PositionEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.PositionDao;

public class PositionListViewModel extends DataSourceModel<PositionEntity, PositionDao> {
    private PagedList.Config.Builder config;

    public PositionListViewModel(@NonNull Application application) {
        super(application);
        config = new PagedList.Config.Builder()
                .setPageSize(setPageSize())
                .setPrefetchDistance(setPrefetchDistance())
                .setInitialLoadSizeHint(setInitialLoadSizeHint())
                .setEnablePlaceholders(setEnablePlaceholders());
    }

    @Override
    protected PositionDao setDao() {
        return AppDatabase.get().positionDao();
    }

    public LiveData<PagedList<PositionEntity>> getPositions(int uid) {
        return new LivePagedListBuilder<>(
                getDao().queryPositions(uid), config.build())
                .build();
    }
}
