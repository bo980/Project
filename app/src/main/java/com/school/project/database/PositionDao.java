package com.school.project.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.school.project.base.BaseDao;
import com.school.project.bean.PositionEntity;

import io.reactivex.Flowable;

@Dao
public interface PositionDao extends BaseDao<PositionEntity> {

    //必须重写此方法
    @Override
    @Query("SELECT * FROM position")
    DataSource.Factory<Integer, PositionEntity> queryAll();

    //自定义查询
    @Query("select * from position where pid = :pid")
    Flowable<PositionEntity> queryPosition(long pid);

    //自定义查询
    @Query("select * from position where uid = :uid")
    DataSource.Factory<Integer, PositionEntity> queryPositions(int uid);
}
