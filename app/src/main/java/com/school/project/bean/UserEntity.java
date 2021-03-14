package com.school.project.bean;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.school.project.resume.ResumeDetailsActivity;

@Entity(tableName = "user")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int uid;
    // 姓名
    public String name;
    //年龄
    public int age;
    //性别
    public int gender;
    //学历
    public String seniority = "未知";
    //个性签名
    public String autograph = "暂无签名";
    //专业
    public String major = "未知";
    //职位
    public String position = "未知";
    //账号
    public String account;
    //密码
    public String password;
    //头像
    public String avatar;
    //用户渠道 0学生 1企业
    public int channel;
    //工作年限
    public int workNum;
    //企业人数
    public String userCount = "99-500人";
    //企业类别
    public String companyType;
    //企业介绍
    public String companyDesc = "暂无介绍～";
    //地址
    public String address;

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", seniority=" + seniority +
                ", autograph='" + autograph + '\'' +
                ", major='" + major + '\'' +
                ", position='" + position + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", channel=" + channel +
                ", workNum=" + workNum +
                '}';
    }

    public String getMainName() {
        return TextUtils.isEmpty(name) ? account : name;
    }

    public String getDesc() {
        String desc = "";
        if (channel == 0) {
            if (gender > 0) {
                desc += ("性别：" + (gender == 1 ? "男" : "女") + "  ");
            }
            if (age > 0) {
                desc += ("年龄：" + age + "  ");
            }
            if (!TextUtils.isEmpty(seniority)) {
                desc += ("学历：" + seniority);
            }
        } else {
            if (!TextUtils.isEmpty(companyType)) {
                desc += "类别：" + companyType + "  ";
            }
            if (!TextUtils.isEmpty(userCount)) {
                desc += ("人数：" + userCount);
            }
        }
        return !TextUtils.isEmpty(desc) ? desc : "请完善资料～";
    }

    public String getMainAutograph() {
        if (channel == 1) {
            return !TextUtils.isEmpty(companyDesc) ? "介绍：" + companyDesc : companyDesc;
        }
        return !TextUtils.isEmpty(autograph) ? "签名：" + autograph : autograph;
    }

    public String getAgeText() {
        return age + "";
    }

    public String getGenderText() {
        if (gender > 0) {
            return gender == 1 ? "男" : "女";
        } else {
            return "未知";
        }
    }

    public String getWorkNumText() {
        return workNum + "年";
    }

    public String getWorkText() {
        return "工作年限：" + workNum + "年";
    }

    public String getSeniorityText() {
        if (!TextUtils.isEmpty(seniority)) {
            return "学历：" + seniority;
        }
        return seniority;
    }

    public String getMajorText() {
        if (!TextUtils.isEmpty(major)) {
            return "专业：" + major;
        }
        return major;
    }

    public void goResume(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), ResumeDetailsActivity.class)
                .putExtra("uid", uid).putExtra("title", name + "的简历"));
    }

}
