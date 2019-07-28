package com.circles.circlesapp.vaildation;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Is {
    public static boolean nn(String s) {
        return (s != null && (!s.isEmpty()) && !(s.equals("null")) && !s.equals("") && !s.equals(" "));
    }


    public static boolean isValidContentNote(String note, EditText editText, String errorMsg) {

        if (nn(note) & note.length() > 2 & note.length() < 1000) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public static boolean nn(Object o) {
        return o != null;
    }

    public static boolean nn(int num) {
        return num > 0;
    }

    public static boolean nn(float num) {
        return num > 0;
    }

    public static boolean nn(CharSequence s) {
        return (s != null && (s.length() > 0) && !(s.equals("null")) && !s.equals("") && !s.equals(" "));
    }

    public static boolean isValidWebSite(String webSite) {

        String ePattern = "^[a-zA-Z0-9\\-\\.]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$";

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(webSite);
        return m.matches();
    }

    public static boolean isValidShort(String s, EditText editText, String errorMsg) {
        if (isValidShort(s)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }

    }

    public static boolean isValidShort(String s) {
        return s != null && s.length() > 3 && s.length() < 25;
    }

    public static void txt(TextView textView, String value) {
        if (nn(value)) {
            textView.setText(value);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static boolean nn(ArrayList arrayList) {
        return arrayList != null && arrayList.size() != 0;
    }

    public boolean isPasswordValid(String password) {
        return !password.isEmpty() && password.length() > 4;
    }

    public boolean isPasswordValid(String password, EditText editText, String errorMsg) {
        if (isUserNameValid(password)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isValidWebSite(String webSite, EditText editText, String errorMsg) {

        if (isValidWebSite(webSite)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isAddressValid(String address) {
        return !address.isEmpty() && address.length() > 11;
    }

    public boolean isAddressValid(String address, EditText editText, String errorMsg) {

        if (isAddressValid(address)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isValidShort(String s, int min) {
        return s != null && s.length() > min;
    }

    public boolean isNameValid(String fullName) {
        return java.util.regex.Pattern.compile("^[a-zA-Z\u0621-\u064A ]{3,50}$").matcher(fullName).matches();
    }

    public boolean isNameValid(String fullName, EditText editText, String errorMsg) {
        if (isNameValid(fullName)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isUserNameValid(String userName) {
        String USERNAME_PATTERN = "^[A-Za-z0-9_-]{3,20}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(USERNAME_PATTERN);
        java.util.regex.Matcher m = p.matcher(userName);
        return m.matches();
    }

    public boolean isUserNameValid(String userName, EditText editText, String errorMsg) {
        if (isUserNameValid(userName)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isPlaceNameValid(String fullName) {
        return java.util.regex.Pattern.compile("^[a-zA-Z\u0621-\u064A ]{4,100}$").matcher(fullName).matches();
    }

    public boolean isPlaceNameValid(String placeName, EditText editText, String errorMsg) {
        if (isPlaceNameValid(placeName)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isPhoneValid(String phone) {
        return !phone.isEmpty() && phone.length() == 11;
    }

    public boolean isPhoneValid(String phone, EditText editText, String errorMsg) {

        if (isPhoneValid(phone)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }

    }

    public boolean isEmailValid(String email, EditText editText, String errorMsg) {
        if (isEmailValid(email)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }
}
