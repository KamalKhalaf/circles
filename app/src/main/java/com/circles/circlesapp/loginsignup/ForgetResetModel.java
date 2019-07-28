package com.circles.circlesapp.loginsignup;
/**/

public class ForgetResetModel {


    private String email;
    private String ConfirmationCode;
    private String password;
    private String retryPassword;


    public ForgetResetModel(String email) {
        this.email = email;
    }


    public ForgetResetModel(String email, String confirmationCode, String password, String retryPassword) {
        this.email = email;
        ConfirmationCode = confirmationCode;
        this.password = password;
        this.retryPassword = retryPassword;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationCode() {
        return ConfirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        ConfirmationCode = confirmationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetryPassword() {
        return retryPassword;
    }

    public void setRetryPassword(String retryPassword) {
        this.retryPassword = retryPassword;
    }
}
