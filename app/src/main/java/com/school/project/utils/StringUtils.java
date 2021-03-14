package com.school.project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 判断电话号码是否符合格式.
     *
     * @param phoneNumber the input text
     * @return true, if is phone
     */
    public static boolean isPhone(String phoneNumber) {
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    //6-16位数字字母混合,不能全为数字,不能全为字母,首位不能为数字
    public static boolean isPassword(String password) {
        String regex = "^(?![0-9])(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean isMatch = m.matches();
        return isMatch;
    }
}
