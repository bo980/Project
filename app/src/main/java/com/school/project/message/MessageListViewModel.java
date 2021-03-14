package com.school.project.message;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.school.project.bean.MessageEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.MessageDao;

public class MessageListViewModel extends DataSourceModel<MessageEntity, MessageDao> {
    private PagedList.Config.Builder config;

    public MessageListViewModel(@NonNull Application application) {
        super(application);
        config = new PagedList.Config.Builder()
                .setPageSize(setPageSize())
                .setPrefetchDistance(setPrefetchDistance())
                .setInitialLoadSizeHint(setInitialLoadSizeHint())
                .setEnablePlaceholders(setEnablePlaceholders());
    }

    @Override
    protected MessageDao setDao() {
        return AppDatabase.get().messageDao();
    }

    public LiveData<PagedList<MessageEntity>> getMessages(int uid) {
        return new LivePagedListBuilder<>(
                getDao().queryUserMessage(uid), config.build())
                .build();
    }
}
