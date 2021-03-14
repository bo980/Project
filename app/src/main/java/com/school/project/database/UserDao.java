package com.school.project.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.school.project.base.BaseDao;
import com.school.project.bean.UserEntity;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface UserDao extends BaseDao<UserEntity> {

    //必须重写此方法
    @Override
    @Query("SELECT * FROM user")
    DataSource.Factory<Integer, UserEntity> queryAll();

    //自定义查询
    @Query("select * from user where uid = :uid")
    Single<UserEntity> queryUserForId(int uid);

    //自定义查询
    @Query("select * from user where uid = :uid")
    Flowable<UserEntity> queryUser(int uid);

    //自定义查询
    @Query("select * from user where account = :account")
    Single<UserEntity> queryUserForAccount(String account);

    @Query("select * from user where channel = 0")
    DataSource.Factory<Integer, UserEntity> queryAllStudent();
}
