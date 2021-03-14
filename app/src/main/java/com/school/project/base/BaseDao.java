package com.school.project.base;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {

    DataSource.Factory<Integer, T> queryAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<T> data);

    @Delete
    void delete(T data);

    @Delete
    void delete(List<T> data);

    @Update
    void update(T data);

    @Update
    void update(List<T> data);

}
