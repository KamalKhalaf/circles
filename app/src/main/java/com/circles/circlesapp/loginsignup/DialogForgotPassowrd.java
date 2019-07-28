package com.circles.circlesapp.loginsignup;
/**/

import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.Utility;
import com.circles.circlesapp.interfaces.EmailVerifacation;

public class DialogForgotPassowrd {


    private Context context;
    private Dialog forgot_dialog;
    private long mLastClickTime = 0;
    private boolean isResetPassword = false;

    DialogForgotPassowrd(Context context , boolean isResetPassword) {
        this.context = context;
        this.isResetPassword = isResetPassword;
    }

    public void Show_forgot_dialog(final EmailVerifacation verifacation) {

        forgot_dialog = new Dialog(context, R.style.Password_Dialog);
        forgot_dialog.setCancelable(true);
        forgot_dialog.setContentView(R.layout.dialog_forgot_password);
        forgot_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (!forgot_dialog.isShowing()) {
            forgot_dialog.show();
        }
        Button submit = forgot_dialog.findViewById(R.id.submit);
        Button cancel = forgot_dialog.findViewById(R.id.cancel);
        final EditText email = forgot_dialog.findViewById(R.id.et_forgot_email);
        final EditText etConfirmationCode = forgot_dialog.findViewById(R.id.etConfirmationCode);
        final EditText etPassword = forgot_dialog.findViewById(R.id.etPassword);
        final EditText etRetryPassword = forgot_dialog.findViewById(R.id.etRetryPassword);
        final LinearLayout mainLinear = forgot_dialog.findViewById(R.id.mainLinear);

        TextView tvForgetYourPassword = forgot_dialog.findViewById(R.id.tvForgetYourPassword);
        TextView tvEnterYourField = forgot_dialog.findViewById(R.id.tvEnterYourField);

        final LinearLayout layoutEmail = forgot_dialog.findViewById(R.id.layoutEmail);
        final LinearLayout layoutResetPassword = forgot_dialog.findViewById(R.id.layoutResetPassword);
        final LinearLayout layoutConfirm = forgot_dialog.findViewById(R.id.layoutConfirm);
        final LinearLayout layoutPassw = forgot_dialog.findViewById(R.id.layoutPassw);
        final LinearLayout layoutRetryPass = forgot_dialog.findViewById(R.id.layoutRetryPass);


        if(isResetPassword){

            layoutResetPassword.setVisibility(View.VISIBLE);
            tvForgetYourPassword.setText(context.getResources().getString(R.string.reset_password));
            tvEnterYourField.setText(context.getResources().getString(R.string.demand_data));
            submit.setText(context.getResources().getString(R.string.resre));
        }else {

            layoutResetPassword.setVisibility(View.GONE);
            tvForgetYourPassword.setText(context.getResources().getString(R.string.forgotyourpassword));
            tvEnterYourField.setText(context.getResources().getString(R.string.enteryouremailbelow));
            submit.setText(context.getResources().getString(R.string.submit2));
        }


        final String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+";

        submit.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();


            if(!isResetPassword) {

                if (!email.getText().toString().trim().matches(emailPattern)) {
                    Snackbar.make(mainLinear, context.getResources().getString(R.string.miss_email), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.close), view -> {
                    }).setActionTextColor(context.getResources().getColor(android.R.color.white)).show();
                    Utility.showShakeError(context, layoutEmail);
                    return;
                } else {

                    ForgetResetModel resetModel = new ForgetResetModel(email.getText().toString());
                    verifacation.getEmailData(true, resetModel);
                }

            }else {

                if (!email.getText().toString().trim().matches(emailPattern)) {
                    Snackbar.make(mainLinear, context.getResources().getString(R.string.miss_email), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.close), view -> {
                    }).setActionTextColor(context.getResources().getColor(android.R.color.white)).show();
                    Utility.showShakeError(context, layoutEmail);
                    return;
                }

                if (etConfirmationCode.getText().toString().isEmpty() || etConfirmationCode.getText().toString().length() < 3) {
                    Snackbar.make(mainLinear, context.getResources().getString(R.string.miss_ConfirmationCode), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.close), view -> {
                    }).setActionTextColor(context.getResources().getColor(android.R.color.white)).show();
                    Utility.showShakeError(context, layoutConfirm);
                    return;
                }

                if (etPassword.getText().toString().isEmpty() || etPassword.getText().toString().length() < 8) {
                    Snackbar.make(mainLinear, context.getResources().getString(R.string.miss_pass), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.close), view -> {
                    }).setActionTextColor(context.getResources().getColor(android.R.color.white)).show();
                    Utility.showShakeError(context, layoutPassw);
                    return;
                }

                if (etRetryPassword.getText().toString().isEmpty() || etRetryPassword.getText().toString().length() < 8) {
                    Snackbar.make(mainLinear, context.getResources().getString(R.string.miss_pass), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.close), view -> {
                    }).setActionTextColor(context.getResources().getColor(android.R.color.white)).show();
                    Utility.showShakeError(context, layoutRetryPass);
                    return;
                }

                if (!etRetryPassword.getText().toString().equals(etPassword.getText().toString())) {
                    Snackbar.make(mainLinear, context.getResources().getString(R.string.miss_match), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.close), view -> {
                    }).setActionTextColor(context.getResources().getColor(android.R.color.white)).show();
                    Utility.showShakeError(context, layoutRetryPass);
                    Utility.showShakeError(context, layoutPassw);
                    return;
                }

                ForgetResetModel resetModel = new ForgetResetModel(email.getText().toString() , etConfirmationCode.getText().toString(), etPassword.getText().toString() , etRetryPassword.getText().toString());
                verifacation.getEmailData(true, resetModel);
            }
        });


        cancel.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (forgot_dialog != null) {
                if (forgot_dialog.isShowing()) {
                    forgot_dialog.dismiss();
                    forgot_dialog = null;
                }
            }
        });
    }

    public void dismiss() {
        if (forgot_dialog != null) {
            if (forgot_dialog.isShowing()) {
                forgot_dialog.dismiss();
                forgot_dialog = null;
            }
        }
    }
}
