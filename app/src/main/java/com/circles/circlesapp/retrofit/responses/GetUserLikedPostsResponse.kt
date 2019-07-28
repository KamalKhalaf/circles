package com.circles.circlesapp.retrofit.responses

import com.circles.circlesapp.home.NewsFeedData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetUserLikedPostsResponse(
        @SerializedName("message")
        @Expose
        var message: String,
    @SerializedName("data")
    @Expose
    var data: NewsFeedData
)
