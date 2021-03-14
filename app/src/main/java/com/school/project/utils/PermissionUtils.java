package com.school.project.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class PermissionUtils {
    public static void checkPermissionRequest(@NonNull FragmentActivity activity, final OnCheckPermissionCallback checkPermissionCallback, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        RxJavaHelper.addDisposable(activity, rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Logger.e("PermissionUtils..." + aBoolean);
                if (checkPermissionCallback != null) {
                    checkPermissionCallback.result(aBoolean);
                }
            }
        }));
    }

    public interface OnCheckPermissionCallback {
        void result(@NonNull Boolean b);
    }
}
