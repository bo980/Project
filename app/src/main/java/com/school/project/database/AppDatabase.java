package com.school.project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.school.project.bean.MessageEntity;
import com.school.project.bean.PositionEntity;
import com.school.project.bean.ResumeEntity;
import com.school.project.bean.UserEntity;

@Database(entities = {UserEntity.class, ResumeEntity.class,
        PositionEntity.class, MessageEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sAppDatabase;

    public abstract UserDao userDao();

    public abstract ResumeDao resumeDao();

    public abstract PositionDao positionDao();

    public abstract MessageDao messageDao();

    public static void init(Context context) {
        sAppDatabase = Room
                .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database.db")
                .build();
    }

    public static AppDatabase get() {
        if (sAppDatabase == null) {
            throw new NullPointerException("AppDatabase is null.");
        }
        return sAppDatabase;
    }
}
