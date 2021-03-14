package com.school.project;

import android.app.Application;

import com.school.project.database.AppDatabase;
import com.school.project.user.UserStatus;
import com.school.project.utils.AppContext;
import com.school.project.utils.Logger;

public class App extends Application {
    public static int sUserStatus = UserStatus.NONE;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(App.class, BuildConfig.DEBUG);
        AppContext.initApp(this);
        AppDatabase.init(this);
    }
}
