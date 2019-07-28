package com.circles.circlesapp.retrofit.errors

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SignUpErros (

        @SerializedName("first_name")
    @Expose
    var firstName: List<String> = ArrayList<String>(),
        @SerializedName("last_name")
    @Expose
    var lastName: List<String> = ArrayList<String>(),
        @SerializedName("email")
    @Expose
    var email: List<String> = ArrayList<String>(),
        @SerializedName("username")
    @Expose
    var username: List<String> = ArrayList<String>(),
        @SerializedName("password")
    @Expose
    var password: List<String> = ArrayList<String>(),
        @SerializedName("phone")
    @Expose
    var phone: List<String> = ArrayList<String>(),
        @SerializedName("gender")
    @Expose
    var gender: List<String> = ArrayList<String>()
)