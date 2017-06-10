package iqra.shabeer.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import iqra.shabeer.interfaces.AlertDialogCallback;
import iqra.shabeer.models.StudentAccountBO;
import iqra.shabeer.models.TeacherAccountBO;

/**
 * Created by Devprovider on 02/05/2017.
 */

public class UtilHelper {

    private static ProgressDialog waitDialog;
    private static Context staticContext;

    public static double findStdDev(double mean, ArrayList<Long> scores) {
        double total = scores.get(0) + scores.get(1) + scores.get(2) + scores.get(3) + scores.get(4);
        return Math.sqrt(((5 * 5 * scores.get(0) + 4 * 4 * scores.get(1) + 3 * 3 * scores.get(2) + 2 * 2 * scores.get(3) + scores.get(4)) / total) - (mean * mean));
    }

    public static double findMean(ArrayList<Long> scores) {
        double total = scores.get(0) + scores.get(1) + scores.get(2) + scores.get(3) + scores.get(4);
        return (5 * scores.get(0) + 4 * scores.get(1) + 3 * scores.get(2) + 2 * scores.get(3) + scores.get(4)) / total;
    }

    public static void hideActionBar(AppCompatActivity context) {
        if (context.getSupportActionBar() != null)
            context.getSupportActionBar().hide();
    }

    public static void showAlertDialog(Context context, String title, @Nullable String message) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage("" + message).setCancelable(true)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public static void showAlertDialog(Context context, @Nullable String title, @Nullable String message,
                                       String buttonText, final AlertDialogCallback callback) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage("" + message).setCancelable(true)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onClick();
                    }
                }).create();
        dialog.show();
    }

    public static void showWaitDialog(Context context, @Nullable String title, @Nullable String message) {
        try {
            if (waitDialog != null) {
                if (!waitDialog.isShowing()) {
                    waitDialog = new ProgressDialog(context);
                    waitDialog.setTitle(title);
                    waitDialog.setMessage(message);
                    waitDialog.setIndeterminate(true);
                    waitDialog.setCancelable(false);
                    waitDialog.show();
                }
            } else {
                waitDialog = new ProgressDialog(context);
                waitDialog.setTitle(title);
                waitDialog.setMessage(message);
                waitDialog.setIndeterminate(true);
                waitDialog.setCancelable(false);
                waitDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissWaitDialog() {
        try {
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static final String USER_INFO_KEY = "user_info_key";
    private static final String USER_TYPE_KEY = "user_type_key";

    public static void createStudentLoginSession(Context context, StudentAccountBO studentInfo) {
        staticContext = context;
        String json = new Gson().toJson(studentInfo);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_INFO_KEY, json).apply();
    }

    public static void createTeacherLoginSession(Context context, TeacherAccountBO teacherInfo) {
        staticContext = context;
        String json = new Gson().toJson(teacherInfo);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_INFO_KEY, json).apply();
    }

    @Nullable
    public static StudentAccountBO getLoggedInStudent() {
        String userInfo = PreferenceManager.getDefaultSharedPreferences(staticContext).getString(USER_INFO_KEY, null);
        if (userInfo == null)
            return null;
        return new Gson().fromJson(userInfo, StudentAccountBO.class);
    }

    @Nullable
    public static TeacherAccountBO getLoggedInTeahcer() {
        String userInfo = PreferenceManager.getDefaultSharedPreferences(staticContext).getString(USER_INFO_KEY, null);
        if (userInfo == null)
            return null;
        return new Gson().fromJson(userInfo, TeacherAccountBO.class);
    }

    public static void endStudentLoginSession(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static void endTeacherLoginSession(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static void setLoggedInUserType(Context context, String type) {
        staticContext = context;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_TYPE_KEY, type).apply();
    }

    public static String getLoggedInUserType(Context context) {
        staticContext = context;
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_TYPE_KEY, null);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static float findMedian(Long[] F) {
        int[] R = {1, 2, 3, 4, 5};
        float[] cF = {F[0], F[0] + F[1], F[0] + F[1] + F[2], F[0] + F[1] + F[2] + F[3],
                F[0] + F[1] + F[2] + F[3] + F[4]};
        int Mn = (int) ((F[0] + F[1] + F[2] + F[3] + F[4]) / 2);
        int MF = findGreaterOrEqueal(F, Mn);
        double l = R[MF] - 0.5;
        float f = F[MF];
        float c = cF[MF - 1];
        return (float) (l + (1 / f) * (Mn - c));
    }

    public static float findQ1(Long[] F) {
        int[] R = {1, 2, 3, 4, 5};
        float[] cF = {F[0], F[0] + F[1], F[0] + F[1] + F[2], F[0] + F[1] + F[2] + F[3],
                F[0] + F[1] + F[2] + F[3] + F[4]};
        int Mn = (int) ((F[0] + F[1] + F[2] + F[3] + F[4]) / 4);
        int MF = findGreaterOrEqueal(F, Mn);
        double l = R[MF] - 0.5;
        float f = F[MF];
        float c = cF[MF - 1];
        return (float) (l + (1 / f) * (Mn - c));
    }

    public static float findQ3(Long[] F) {
        int[] R = {1, 2, 3, 4, 5};
        float[] cF = {F[0], F[0] + F[1], F[0] + F[1] + F[2], F[0] + F[1] + F[2] + F[3],
                F[0] + F[1] + F[2] + F[3] + F[4]};
        int Mn = (int) ((F[0] + F[1] + F[2] + F[3] + F[4]) / (3.0f / 4.0f));
        int MF = findGreaterOrEqueal(F, Mn);
        double l = R[MF] - 0.5;
        float f = F[MF];
        float c = cF[MF - 1];
        return (float) (l + (1 / f) * (Mn - c));
    }

    private static int findGreaterOrEqueal(Long[] score, int mn) {
        float equalOrGreaterNumber = 0;
        for (int i = 1; i < score.length; i++) {
            if (score[i] == mn || mn > score[i]) {
                equalOrGreaterNumber = i;
                break;
            }
        }
        return (int) equalOrGreaterNumber;
    }
}
