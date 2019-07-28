package com.circles.circlesapp.helpers;
/**/

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {

    private SharedPreferences preferences;
    private Context context;


    public MySharedPreferences(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * This method to return String stored in SharedPreferences
     *
     * @param key Key of stored String
     * @return stored String for this key if exists and empty String "" if not exists
     */
    public String getData(String key) {

        String language = preferences.getString(key, "");

        return language;
    }

    /**
     * This method to return boolean stored in SharedPreferences
     *
     * @param key Key of stored boolean
     * @return stored boolean for this key if exists and false if not exists
     */
    public Boolean getBoolean(String key) {

        Boolean language = preferences.getBoolean(key, false);

        return language;
    }

    /**
     * This method to return long stored in SharedPreferences
     *
     * @param key Key of stored long
     * @return stored long for this key if exists and 0 if not exists
     */
    public Long getLong(String key) {

        Long language = preferences.getLong(key, (long) 0.0);

        return language;
    }

    /**
     * This method to return float stored in SharedPreferences
     *
     * @param key Key of stored float
     * @return stored float for this key if exists and 0.0 if not exists
     */
    public Float getFloat(String key) {

        Float language = preferences.getFloat(key, (float) 0.0);

        return language;
    }

    /**
     * This method to return int stored in SharedPreferences
     *
     * @param key Key of stored int
     * @return stored int for this key if exists and 0 if not exists
     */
    public int getInt(String key) {

        int language = preferences.getInt(key, 0);

        return language;
    }

    /**
     * This method to clear all SharedPreferences
     */
    public void clearAllSharedPreferences() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();

    }

    /**
     * This method to delete specific key from SharedPreferences
     *
     * @param delete Key that will be deleted
     */
    public void clearSpecificSharedPreferences(String delete) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(delete);
        edit.commit();

    }


    /**
     * This method to add new String value to SharedPreferences
     *
     * @param key   Key for the new String
     * @param value Value of the new String
     */
    public void addData(String key, String value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * This method to add new int value to SharedPreferences
     *
     * @param key   Key for the new int
     * @param value Value of the new int
     */
    public void addData(String key, int value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * This method to add new boolean value to SharedPreferences
     *
     * @param key   Key for the new boolean
     * @param value Value of the new boolean
     */
    public void addData(String key, Boolean value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * This method to add new float value to SharedPreferences
     *
     * @param key   Key for the new float
     * @param value Value of the new float
     */
    public void addData(String key, Float value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    /**
     * This method to add new Long value to SharedPreferences
     *
     * @param key   Key for the new Long
     * @param value Value of the new Long
     */
    public void addData(String key, Long value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(key, value);
        edit.commit();
    }
}
