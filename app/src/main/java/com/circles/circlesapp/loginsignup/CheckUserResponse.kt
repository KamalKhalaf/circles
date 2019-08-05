package com.circles.circlesapp.loginsignup

import com.google.gson.annotations.SerializedName

data class CheckUserResponse(@SerializedName("success") var isSuccess: Boolean)