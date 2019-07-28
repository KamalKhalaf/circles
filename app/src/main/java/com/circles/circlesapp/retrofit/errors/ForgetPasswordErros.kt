package com.circles.circlesapp.retrofit.errors

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**/

class ForgetPasswordErros {

    @SerializedName("email")
    @Expose
    var email: List<String> = ArrayList<String>()
}