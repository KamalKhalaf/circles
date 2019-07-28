package com.circles.circlesapp.retrofit.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(

        @SerializedName("message")
        @Expose
        var message: String,

        @SerializedName("isVerified")
        @Expose
        var isVerified: Boolean,

        @SerializedName("is_celebrity")
        @Expose
        var is_celebrity: Boolean,

        @SerializedName("access_token")
        @Expose
        var accessToken: String,

        @SerializedName("token_type")
        @Expose
        var tokenType: String,

        @SerializedName("profile_image")
        @Expose
        var profile_image: String,

        @SerializedName("expires_at")
        @Expose
        var expiresAt: String,
        @SerializedName("id")
        @Expose
        var id: Int

)