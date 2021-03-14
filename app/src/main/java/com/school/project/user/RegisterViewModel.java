package com.school.project.user;

import android.app.Application;

import androidx.annotation.NonNull;

import com.school.project.bean.UserEntity;
import com.school.project.database.AppDatabase;
import com.school.project.database.DataSourceModel;
import com.school.project.database.UserDao;

public class RegisterViewModel extends DataSourceModel<UserEntity, UserDao> {
    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected UserDao setDao() {
        return AppDatabase.get().userDao();
    }

}
