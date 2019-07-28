package com.circles.circlesapp.retrofit.errors

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**/

class ResetPasswordErros {

    @SerializedName("email")
    @Expose
    var email: List<String> = ArrayList<String>()

    @SerializedName("code")
    @Expose
    var code: List<String> = ArrayList<String>()

    @SerializedName("password")
    @Expose
    var password: List<String> = ArrayList<String>()

    @SerializedName("password_confirmation")
    @Expose
    var password_confirmation: List<String> = ArrayList<String>()


}