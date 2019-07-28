package com.circles.circlesapp.retrofit.errors

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


 class ConfirmUserErrorrs (

    @SerializedName("code")
    @Expose
    var code: List<String>,
    @SerializedName("email")
    @Expose
    var email: List<String>

)