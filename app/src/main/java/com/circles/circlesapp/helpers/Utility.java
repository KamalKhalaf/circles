package com.circles.circlesapp.helpers;
/**/

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.circles.circlesapp.R;

public class Utility {


    public static void showShakeError(Context context, View viewToAnimate) {
        try {
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
