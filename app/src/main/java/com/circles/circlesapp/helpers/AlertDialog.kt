package com.circles.circlesapp.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.labters.lottiealertdialoglibrary.ClickListener


/**/

class AlertDialog(){

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var alertDialog: LottieAlertDialog;

        fun loading(context: Context, loadingTitle: String, loadingMessage: String, cancelable: Boolean) {
            alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
                    .setTitle(loadingTitle)
                    .setDescription(loadingMessage)
                    .build()
            alertDialog.setCancelable(cancelable)
            alertDialog.show()
        }

        fun warrning(context: Context, title: String, description: String, positive: String, negative: String, buildType: String,
                     cancelable: Boolean, dialogueChose: DialogClicks) {

            var TYPE = 0;

            when (buildType) {
                "loading" -> TYPE = 0;
                "success" -> TYPE = 1;
                "error" -> TYPE = 2;
                "warning" -> TYPE = 3;
                "question" -> TYPE = 4;
            }
            alertDialog = LottieAlertDialog.Builder(context, TYPE)
                    .setTitle(title)
                    .setDescription(description)
                    .setPositiveText(positive)
                    .setNegativeText(negative)
                    .setPositiveButtonColor(Color.parseColor("#187ff5"))
                    .setPositiveTextColor(Color.parseColor("#ffffff"))
                    .setNegativeButtonColor(Color.parseColor("#ffffff"))
                    .setNegativeTextColor(Color.parseColor("#187ff5"))
                    .setNoneButtonColor(Color.parseColor("#1cd3ef"))
                    .setNoneTextColor(Color.parseColor("#c407c4"))
                    .setPositiveListener(object : ClickListener {
                        override fun onClick(dialog: LottieAlertDialog) {
                            dialogueChose.onChose(0)
                        }
                    }).setNegativeListener(object : ClickListener {
                        override fun onClick(dialog: LottieAlertDialog) {
                            dialogueChose.onChose(1)
                        }
                    }).build()
            alertDialog.setCancelable(cancelable)
            alertDialog.show()
        }

        fun cancelDialog() {
            if (alertDialog.isShowing)
                alertDialog.dismiss()
        }
    }
}