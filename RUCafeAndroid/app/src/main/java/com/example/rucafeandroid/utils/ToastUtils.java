package com.example.rucafeandroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * This is utility class to handle showing an Android Toast pop-up. This class
 * uses the Toast library.
 *
 * @author Aryan Patel
 */
public class ToastUtils {

    /**
     * Static method to create and show a Toast pop-up given a context,
     * message, and message duration.
     *
     * @param context context of the current application state.
     * @param message string containing the message to display within the Toast.
     * @param duration int containing the duration of the Toast pop-up.
     */
    public static void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }
}
