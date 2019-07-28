package com.circles.circlesapp.retrofit.responses

import com.circles.circlesapp.retrofit.errors.SignUpErros
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SignUpResponse (

    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("errors")
    @Expose
    var errors: SignUpErros

)