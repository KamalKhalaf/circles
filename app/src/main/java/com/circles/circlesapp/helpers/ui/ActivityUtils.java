package com.circles.circlesapp.helpers.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId, boolean addToBackStack) {
        if (fragment == null || fragmentManager == null)
            return;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, String tag) {
        if (fragment == null || fragmentManager == null)
            return;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commit();
    }

    /**
     * @param activity
     * @param mClassDestination
     * @param clearBackStack
     */
    public static void goToActivity(Activity activity, Class mClassDestination, boolean clearBackStack) {
        Intent myIntent = new Intent(activity, mClassDestination);
        activity.startActivity(myIntent);
        if (clearBackStack) {
            activity.finishAffinity();
        }
    }

    public static void goToActivity(Activity activity, Intent myIntent, boolean clearBackStack) {
        activity.startActivity(myIntent);
        if (clearBackStack) {
            activity.finishAffinity();
        }
    }


    /**
     * Helper method to hide the keyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager != null)
                inputManager.hideSoftInputFromWindow((null == activity.getCurrentFocus()) ? null : activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Helper method to check if a given fragment is the last fragment is back stack entries
     *
     * @param activity Current activity that holds the stack
     * @param fragment Fragment to check
     * @return true if it's the last fragment, false if it's not.
     */
    public static boolean isLastFragmentInStack(AppCompatActivity activity, Fragment fragment) {
        int fragmentsCount = activity.getSupportFragmentManager().getBackStackEntryCount();
        return activity.getSupportFragmentManager().getBackStackEntryAt
                (fragmentsCount - 1).getName().equals(fragment.getClass().getSimpleName());
    }


    public static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            hideKeyboards((Activity) context);
        }
    }

    public static void hideKeyboards(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken() != null)
                Objects.requireNonNull(inputManager).hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
