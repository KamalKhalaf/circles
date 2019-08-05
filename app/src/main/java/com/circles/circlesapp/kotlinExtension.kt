package com.circles.circlesapp

import android.view.View

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE

}