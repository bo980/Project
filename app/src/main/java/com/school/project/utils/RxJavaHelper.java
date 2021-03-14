package com.school.project.utils;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaHelper {
    private static Map<String, CompositeDisposable> disposableMap = new HashMap<>();

    public static void addDisposable(Activity activity, Disposable disposable) {
        String key = activity.getClass().getSimpleName();
        if (hasDisposables(activity)) {
            disposableMap.get(key).add(disposable);
            return;
        }
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(disposable);
        disposableMap.put(key, disposables);
    }

    public static void removeDisposable(Activity activity) {
        String key = activity.getClass().getSimpleName();
        if (hasDisposables(activity)) {
            disposableMap.get(key).clear();
            disposableMap.remove(key);
        }
    }

    private static boolean hasDisposables(Activity activity) {
        return disposableMap.containsKey(activity.getClass().getSimpleName());
    }


    public static <T> ObservableTransformer<T, T> observableTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> flyableTransformer() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> flowable) {
                return flowable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
