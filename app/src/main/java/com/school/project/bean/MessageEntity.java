package com.school.project.bean;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.school.project.database.AppDatabase;
import com.school.project.message.MessageDetailsActivity;
import com.school.project.resume.ResumeDetailsActivity;
import com.school.project.resume.ResumeEditActivity;
import com.school.project.utils.DateUtils;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "message")
public class MessageEntity {
    public static final int TYPE_NONE = 0;//未读
    public static final int TYPE_OPEN = 1;//已读
    public static final int MODE_OFFER = 0;//offer通知
    public static final int MODE_APPLY = 1;//职位申请
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int mid;

    @NonNull
    public int outUid;
    @NonNull
    public int inUid;
    //消息标题
    public String title;
    //时间
    public long time;
    //类别
    public int type;
    //消息内容
    public String content;

    public int mode;

    public String getTimeText() {
        return DateUtils.timeStamp2Date(time, "yyyy年MM月dd日 HH:mm");
    }

    public int getVisible() {
        return type == TYPE_OPEN ? View.GONE : View.VISIBLE;
    }

    public void goDetails(View view) {
        if (mode == MODE_APPLY) {
            view.getContext().startActivity(new Intent(view.getContext(), ResumeDetailsActivity.class)
                    .putExtra("uid", outUid).putExtra("title", title));
        } else {
            view.getContext().startActivity(new Intent(view.getContext(), MessageDetailsActivity.class)
                    .putExtra("content", content).putExtra("title", title));
        }
        if (type == TYPE_OPEN) {
            return;
        }
        type = TYPE_OPEN;
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                AppDatabase.get().messageDao().update(MessageEntity.this);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void delete() {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                AppDatabase.get().messageDao().delete(MessageEntity.this);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
