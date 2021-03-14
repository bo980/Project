package com.school.project.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.school.project.base.BaseDao;
import com.school.project.bean.MessageEntity;

import io.reactivex.Flowable;

@Dao
public interface MessageDao extends BaseDao<MessageEntity> {

    //必须重写此方法
    @Override
    @Query("SELECT * FROM message")
    DataSource.Factory<Integer, MessageEntity> queryAll();

    //自定义查询
    @Query("select * from message where mid = :mid")
    Flowable<MessageEntity> queryMessage(int mid);

    //自定义查询
    @Query("select * from message where inUid = :uid")
    DataSource.Factory<Integer, MessageEntity> queryUserMessage(int uid);
}
