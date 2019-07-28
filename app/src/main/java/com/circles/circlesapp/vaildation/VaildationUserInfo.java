package com.circles.circlesapp.vaildation;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

//import jvax.inject.Inject;

public class VaildationUserInfo {

//    @Inject
//    public VaildationUserInfo() {
//    }

    public boolean isEmailValid(String email) {
        // String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if (email == null) return false;
        String ePattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{3,6}$";
        Pattern p = Pattern.compile(ePattern, Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = p.matcher(email.trim());
        Log.d("VaildationUserInfo", m.matches() + "isEmailValid" + email);
        return m.matches();
    }

    public boolean isEmailValid(String email, EditText editText, String errorMsg) {
        if (isEmailValid(email)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isText(String txt, EditText editText, String msg) {
        if (TextUtils.isEmpty(txt)) {
            editText.setError(msg);
            return false;
        } else {
            return true;
        }
    }

    public boolean isArabicText(String text, EditText editText, String msg) {
        String ePattern = "^[\\u0621-\\u064A\\u0660-\\u0669 ]+$";
        Pattern p = Pattern.compile(ePattern, Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = p.matcher(text);
        if (!TextUtils.isEmpty(text) && m.matches()) {
            return true;
        } else {
            editText.setError(msg);
            return false;
        }
    }


    public boolean isPasswordValid(String password) {
        if (password == null) return false;
//        Log.d("VaildationUserInfo", (!password.isEmpty() && password.length() >= 6)+"isPasswordValid");
//
//        return !password.isEmpty() && password.length() >= 6;
        // String pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    public boolean isPasswordValid(String password, EditText editText, String errorMsg) {
        if (isPasswordValid(password)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isPhoneValid(String phone) {
        // return PhoneNumberUtils.isGlobalPhoneNumber(phone) && phone.length() >= 11;
        // return !phone.isEmpty() && phone.length() >= 11 ;
        //String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        if (phone == null) return false;
//        String pattern = "^(01)(1|2|5|0)[0-9]{8}";
//        Pattern p = Pattern.compile(pattern);
//        java.util.regex.Matcher m = p.matcher(phone.trim());
//        Log.d("VaildationUserInfo",m.matches()+"");
//        return m.matches() && phone.length()==11;
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }

    public boolean isPhoneValid(String phone, EditText editText, String errorMsg) {
        if (isPhoneValid(phone)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }

    }

    public boolean isBirthDateVaild(String birthdate, TextView textView, String errorMsg) {
        if (isBirthDateVaild(birthdate)) {
            return true;
        } else {
            textView.setError(errorMsg);
            return false;
        }

    }

    public boolean isBirthDateVaild(String birthdate) {
        return !birthdate.isEmpty();
    }

    public boolean isAddressValid(String address) {
        return !address.isEmpty();
    }

    public boolean isAddressValid(String address, EditText editText, String errorMsg) {

        if (isAddressValid(address)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }

    }


    public boolean checkIdNumber(String s) {
        return (s != null &&
                (!s.isEmpty()) &&
                !(s.equals("null")) &&
                !s.equals("") &&
                !s.equals(" "))
                && s.length() == 10;
    }


    public boolean checkIdNumber(String IdNumber, EditText editText, String errorMsg) {
        if (checkIdNumber(IdNumber)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }


    public boolean check(String s) {
        return (s != null &&
                (!s.isEmpty()) &&
                !(s.equals("null")) &&
                !s.equals("") &&
                !s.equals(" "));
    }


    public boolean checkComment(String comment, EditText editText, String errorMsg) {

        if (check(comment)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }


    public boolean isNameValid(String fullName) {
        if (fullName == null) return false;
        return Pattern.compile("^[a-zA-Z\u0621-\u064A ]{3,25}$").matcher(fullName.trim()).matches();
    }

    public boolean isNameValid(String fullName, EditText editText, String errorMsg) {
        if (isNameValid(fullName)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }


    public boolean isUserNameValid(String userName, EditText editText, String errorMsg) {

        if (isNameValid(userName)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public boolean isUserAge(String age, EditText editText, String errorMsg) {
        if (age != null && !TextUtils.isEmpty(age)) {
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }


    public boolean isNameValidWithEmpty(String s, EditText editText, String error) {
        return (!Is.nn(s)) || isNameValid(s, editText, error);

    }


    public boolean isAddressValidWithEmpty(String s, EditText editText, String error) {
        if (Is.nn(s))
            return isAddressValid(s, editText, error);
        return true;
    }

    public boolean isPhoneValidWithEmpty(String s, EditText editText, String error) {
        if (Is.nn(s))
            return isPhoneValid(s, editText, error);
        return true;
    }

    public boolean isEmailValidWithEmpty(String s, EditText editText, String error) {
        if (Is.nn(s)) {
            return isEmailValid(s, editText, error);
        }
        return true;
    }
}
