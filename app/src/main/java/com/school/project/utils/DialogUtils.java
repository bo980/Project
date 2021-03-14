package com.school.project.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class DialogUtils {

    private DialogUtils() {
    }

    public static void showInputDialog(final Context context, String title, int inputType, final OnAppDialogListener listener) {
        final EditText editText = new EditText(context);
        editText.setInputType(inputType);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(context);
        inputDialog.setTitle(title).setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScreenUtils.hideSoftKeyboard(editText);
                        if (listener != null) {
                            listener.onEditTextInput(editText.getText().toString());
                        }
                    }
                }).show();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScreenUtils.showSoftKeyboard(context);
            }
        }, 200);
    }

    public static void showSingleChoiceDialog(Context context, String title, final OnAppDialogListener listener, final String... items) {
        final int[] position = {0};
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(title);
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position[0] = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onSingleSelected(position[0]);
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    public static void showNormalDialog(Context context, String title, final OnAppDialogListener listener){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setMessage(title);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onCommit();
                        }
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    public abstract static class OnAppDialogListener {
        public void onEditTextInput(String text) {
        }

        public void onSingleSelected(int position) {
        }

        public void onCommit() {
        }
    }
}
