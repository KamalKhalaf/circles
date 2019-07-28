package com.circles.circlesapp.retrofit.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//pending
class GetUsersPostsResponse (
    @SerializedName("message")
    @Expose
    var message: String
//    @SerializedName("data")
//    @Expose
//    var data: List<Any>
    )