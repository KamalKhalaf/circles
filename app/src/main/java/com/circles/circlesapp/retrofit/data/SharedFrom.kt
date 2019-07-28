package com.circles.circlesapp.retrofit.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SharedFrom (

    @SerializedName("id")
    @Expose
    var id: Int= 0,
    @SerializedName("full_name")
    @Expose
    var fullName: String = "",
    @SerializedName("username")
    @Expose
    var username: String = "",
    @SerializedName("profile_image")
    @Expose
    var profileImage: Any = ""
)