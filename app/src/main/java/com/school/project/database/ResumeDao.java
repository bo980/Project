package com.school.project.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.school.project.base.BaseDao;
import com.school.project.bean.ResumeEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ResumeDao extends BaseDao<ResumeEntity> {

    //必须重写此方法
    @Override
    @Query("SELECT * FROM resume")
    DataSource.Factory<Integer, ResumeEntity> queryAll();

    //自定义查询
    @Query("select * from resume where uid = :uid")
    Flowable<List<ResumeEntity>> queryResume(int uid);

    //自定义查询
    @Query("select * from resume where rid = :rid")
    Flowable<ResumeEntity> queryResume(long rid);

    //自定义查询
    @Query("select * from resume where type = 0 and uid = :uid")
    DataSource.Factory<Integer, ResumeEntity> queryResumeSkill(int uid);

    //自定义查询
    @Query("select * from resume where type = 1 and uid = :uid")
    DataSource.Factory<Integer, ResumeEntity> queryResumeJob(int uid);

    //自定义查询
    @Query("select * from resume where type = 2 and uid = :uid")
    DataSource.Factory<Integer, ResumeEntity> queryResumeObj(int uid);
}
