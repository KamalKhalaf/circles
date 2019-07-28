package com.circles.circlesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefHelper {
    private String loginPrefName = "login";
    private String privacyPrefname = "priv";
    private SharedPreferences preferences;
    private Context c;

    public SharedPrefHelper(Context c) {

        this.c = c;
        preferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public SharedPrefHelper() {
    }

    public void saveLogin(Context c, String email, String password) {
        SharedPreferences.Editor editor = c.getSharedPreferences(loginPrefName, MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    public void addMutedId(int id) {
        SharedPreferences.Editor editor = c.getSharedPreferences("MutePrefName", MODE_PRIVATE).edit();
        getMutedSet().add(id + "");
        editor.putStringSet("id", getMutedSet());
        editor.apply();
        Log.d("removeMutedId", getMutedSet().toString());
    }

    public void removeMutedId(int id) {
        Set<String> mutedSet = getMutedSet();
        mutedSet.remove(id + "");
        SharedPreferences.Editor editor = c.getSharedPreferences("MutePrefName", MODE_PRIVATE).edit();
        editor.putStringSet("id", mutedSet);
        editor.apply();
        Log.d("removeMutedId", mutedSet.toString());
    }

    public Set<String> getMutedSet() {
        SharedPreferences prefs = c.getSharedPreferences("MutePrefName", MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("id", new HashSet<>());
        Log.d(getClass().getName(), "getMutedSet: " + set);
        return set;
    }


    public String getUserEmail(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(loginPrefName, MODE_PRIVATE);
        String restoredText = prefs.getString("email", null);
        if (restoredText != null) {
            return restoredText;
        }
        return null;
    }

    public String getUserPassword(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(loginPrefName, MODE_PRIVATE);
        String restoredText = prefs.getString("password", null);
        if (restoredText != null) {
            return restoredText;
        }
        return null;
    }

    public void clearAll() {
        SharedPreferences.Editor editor = c.getSharedPreferences(loginPrefName, MODE_PRIVATE).edit();
        editor.clear().apply();
        SharedPreferences.Editor editor2 = c.getSharedPreferences("tokenPref", MODE_PRIVATE).edit();
        editor2.clear().apply();
    }

    public boolean shouldShowProfile() {
        SharedPreferences prefs = c.getSharedPreferences(privacyPrefname, MODE_PRIVATE);
        return prefs.getBoolean("profile", true);
    }

    public boolean shouldShowAge() {
        SharedPreferences prefs = c.getSharedPreferences(privacyPrefname, MODE_PRIVATE);
        return prefs.getBoolean("age", true);
    }

    public boolean shouldShowFollowing() {
        SharedPreferences prefs = c.getSharedPreferences(privacyPrefname, MODE_PRIVATE);
        return prefs.getBoolean("following", true);
    }

    public void setShouldShowProfile(boolean b) {
        SharedPreferences prefs = c.getSharedPreferences(privacyPrefname, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("profile", b).apply();
    }

    public void setShouldShowAge_Location(boolean b) {
        SharedPreferences prefs = c.getSharedPreferences(privacyPrefname, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("age", b).apply();
    }

    public void setShouldShowFollowing(boolean b) {
        SharedPreferences prefs = c.getSharedPreferences(privacyPrefname, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("following", b).apply();
    }

    public void setDeviceToken(String token) {
        SharedPreferences prefs = c.getSharedPreferences("deviceTokenPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token).apply();
        Log.d("token", "setDeviceToken: " + token);
    }

    public String getDeviceToken() {
        SharedPreferences prefs = c.getSharedPreferences("deviceTokenPref", MODE_PRIVATE);
        String token = prefs.getString("token", "kk");
        Log.d("token", "getDeviceToken: " + token);
        return (token==null)?"":token;

    }

    public void setUserToken(String token) {
        SharedPreferences prefs = c.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_token", token).apply();
    }

    public String getUserToken() {
        SharedPreferences prefs = c.getSharedPreferences("tokenPref", MODE_PRIVATE);
        return prefs.getString("user_token", "");
    }

    @NotNull
    public int getUserId() {
        SharedPreferences prefs = c.getSharedPreferences("tokenPref", MODE_PRIVATE);
        return prefs.getInt("user_id", 0);
    }

    public void saveUserId(int userId) {
        SharedPreferences prefs = c.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", userId).apply();
    }



    public String getProfileImage() {
        SharedPreferences prefs = c.getSharedPreferences("tokenPref", MODE_PRIVATE);
        String restoredText =  prefs.getString("Profile_Image" , null);
        if (restoredText != null) {
            return restoredText;
        }
        return null;

    }

    public void setProfileImage(String profile) {
        SharedPreferences prefs = c.getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Profile_Image", profile).apply();
    }


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
