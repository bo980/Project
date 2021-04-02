package com.school.project.bean;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.school.project.database.AppDatabase;
import com.school.project.resume.ResumeEditActivity;
import com.school.project.utils.DateUtils;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "resume")
public class ResumeEntity {
    public static final int TYPE_SKILL = 0;
    public static final int TYPE_JOB = 1;//公司
    public static final int TYPE_OBJ = 2;//项目
    @PrimaryKey
    @NonNull
    public long rid;
    public int type;
    @NonNull
    public int uid;
    // 名称
    public String name;
    //开始时间
    public long startTime;
    //结束时间
    public long endTime;
    //职位名称
    public String jobName;
    //工作内容
    public String desc;

    //个人介绍
    public String skill;

    public boolean isEnable;

    public ResumeEntity() {
        rid = System.currentTimeMillis();
    }

    public int getVisible() {
        return type == TYPE_JOB ? View.VISIBLE : View.GONE;
    }

    public String getStartTimeText() {
        return DateUtils.timeStamp2Date(startTime, "yyyy年MM月");
    }

    public String getEndTimeText() {
        return DateUtils.timeStamp2Date(endTime, "yyyy年MM月");
    }

    public String getTimeText() {
        return (type == TYPE_JOB ? "在职时间：" : "项目时间：") + getStartTimeText() + " -- " + getEndTimeText();
    }

    public String getNameText() {
        return (type == TYPE_JOB ? "公司名称：" : "项目名称：") + name;
    }

    public String getJobNameText() {
        return "公司岗位：" + (TextUtils.isEmpty(jobName) ? "未知" : jobName);
    }

    public String getDescText() {
        return (type == TYPE_JOB ? "工作内容：" : "项目介绍：") + desc;
    }

    public void goEdit(View view) {
        if (isEnable) {
            view.getContext().startActivity(new Intent(view.getContext(), ResumeEditActivity.class)
                    .putExtra("rid", rid).putExtra("type", ResumeEntity.TYPE_JOB));
        }
    }

    public void delete() {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                AppDatabase.get().resumeDao().delete(ResumeEntity.this);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
