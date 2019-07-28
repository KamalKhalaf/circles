package com.circles.circlesapp.helpers.kotlin

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class ActivityUtilsKotlin {



    companion object {


        fun replaceFragmentToActivity(fragmentManager: FragmentManager,
                                      fragment: Fragment, frameId: Int) {

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(frameId,fragment)
            transaction.commit()
        }
    }


}
