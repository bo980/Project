package com.school.project.bean;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.school.project.position.PositionDetailsActivity;
import com.school.project.position.PositionEditActivity;
import com.school.project.resume.ResumeEditActivity;

@Entity(tableName = "position")
public class PositionEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long pid;
    @NonNull
    public int uid;
    //名称
    public String name;
    //经验
    public String workNum = "经验不限";
    //学历
    public String seniority = "学历不限";
    //薪资
    public String salary = "薪资面议";
    //公司名称
    public String companyName = "未知";
    //工作地址
    public String address;
    //工作详情
    public String details;

    public boolean isEnable;

    public PositionEntity() {
        pid = System.currentTimeMillis();
    }

    public String getDescText() {
        return workNum + " * " + seniority + " * " + address;
    }

    public void goEdit(View view) {
        if (isEnable) {
            view.getContext().startActivity(new Intent(view.getContext(), PositionEditActivity.class)
                    .putExtra("pid", pid));
        } else {
            view.getContext().startActivity(new Intent(view.getContext(), PositionDetailsActivity.class)
                    .putExtra("pid", pid));
        }
    }
}
