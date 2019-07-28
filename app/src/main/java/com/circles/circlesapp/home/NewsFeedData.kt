package com.circles.circlesapp.home

import com.circles.circlesapp.retrofit.data.SharedFrom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NewsFeedData(

        @SerializedName("id")
        @Expose
        var id: Int,
        @SerializedName("user_id")
        @Expose
        var userId: Int,
        @SerializedName("full_name")
        @Expose
        var fullName: String,
        @SerializedName("profile_image")
        @Expose
        var profileImage: Any,
        @SerializedName("title")
        @Expose
        var title: String,
        @SerializedName("text")
        @Expose
        var text: String,
        @SerializedName("image")
        @Expose
        var image: String,
        @SerializedName("voice_note")
        @Expose
        var voiceNote: String,
        @SerializedName("link")
        @Expose
        var link: String,
        @SerializedName("created_at")
        @Expose
        var createdAt: Long,
        @SerializedName("is_like")
        @Expose
        var isLike: Boolean,
        @SerializedName("likes")
        @Expose
        var likes: Int,
        @SerializedName("is_shared")
        @Expose
        var isShared: Boolean,
        @SerializedName("shared_at")
        @Expose
        var sharedAt: Any,
        @SerializedName("shared_from")
        @Expose
        var sharedFrom: SharedFrom,
        @SerializedName("shares_count")
        @Expose
        var shares_count: String,
        @SerializedName("heard_count")
        @Expose
        var heard_count: String,
        @SerializedName("comments_count")
        @Expose
        var comments_count: String
)