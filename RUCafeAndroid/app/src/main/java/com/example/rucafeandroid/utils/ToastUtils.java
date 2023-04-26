package com.example.rucafeandroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 *
 */
public class ToastUtils {

    /**
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }
}
