package com.circles.circlesapp.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


// Don't forget for paginating 20 posts


data class NewsFeedResponse (

    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<NewsFeedData>

)