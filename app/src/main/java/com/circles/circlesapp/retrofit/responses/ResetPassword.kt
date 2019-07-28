package com.circles.circlesapp.retrofit.responses

import com.circles.circlesapp.retrofit.errors.ForgetPasswordErros
import com.circles.circlesapp.retrofit.errors.ResetPasswordErros
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**/

data class ResetPassword(

        @SerializedName("message")
        @Expose
        var message: String,

        @SerializedName("access_token")
        @Expose
        var accessToken: String,

        @SerializedName("token_type")
        @Expose
        var tokenType: String,

        @SerializedName("id")
        @Expose
        var id: Int,

        @SerializedName("errors")
        @Expose
        var errors: ResetPasswordErros
)
