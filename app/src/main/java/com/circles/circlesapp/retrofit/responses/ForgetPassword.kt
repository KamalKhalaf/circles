package com.circles.circlesapp.retrofit.responses

import com.circles.circlesapp.retrofit.errors.ForgetPasswordErros
import com.circles.circlesapp.retrofit.errors.SignUpErros
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**/

data class ForgetPassword(

    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("errors")
    @Expose
    var errors: ForgetPasswordErros
)