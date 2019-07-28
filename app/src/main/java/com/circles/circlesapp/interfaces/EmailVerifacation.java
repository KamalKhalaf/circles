package com.circles.circlesapp.interfaces;
/**/

import com.circles.circlesapp.loginsignup.ForgetResetModel;

public interface EmailVerifacation {

    void getEmailData(boolean isOk, ForgetResetModel resetModel);
}
