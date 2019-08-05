package com.circles.circlesapp.retrofit;

import android.arch.lifecycle.LiveData;

import com.circles.circlesapp.chatlist.UserMessagesResModel;
import com.circles.circlesapp.comments.PostCommentResponse;
import com.circles.circlesapp.helpers.livedata.ApiResponse;
import com.circles.circlesapp.home.NewsFeedResponse;
import com.circles.circlesapp.loginsignup.CheckUserResponse;
import com.circles.circlesapp.messaging.model.MessageDetailsResponse;
import com.circles.circlesapp.messaging.model.SendMessageBody;
import com.circles.circlesapp.messaging.model.SendMsgResponse;
import com.circles.circlesapp.nearby.JoinGroupResponse;
import com.circles.circlesapp.nearby.MapDetailResponse;
import com.circles.circlesapp.notifications.NotificationModel;
import com.circles.circlesapp.profile.UserInfo;
import com.circles.circlesapp.replies.ReplayModelResponse;
import com.circles.circlesapp.retrofit.responses.ConfirmEmailResponse;
import com.circles.circlesapp.retrofit.responses.ForgetPassword;
import com.circles.circlesapp.retrofit.responses.GenericResponse;
import com.circles.circlesapp.retrofit.responses.LikedAndDislikeResponse;
import com.circles.circlesapp.retrofit.responses.LoginResponse;
import com.circles.circlesapp.retrofit.responses.ResetPassword;
import com.circles.circlesapp.retrofit.responses.SignUpResponse;
import com.circles.circlesapp.search.SearchResult;
import com.circles.circlesapp.settings.PrivacyModel;
import com.circles.circlesapp.settings.SocialMedia;
import com.circles.circlesapp.settings.SocialMediaResponse;
import com.circles.circlesapp.settings.UserModel;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Enctype: multipart/form-data"
    })
    @POST("signup")
    Call<SignUpResponse> createUser(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("city") String city,
            @Field("country") String country,
            @Field("birthdate") String birthdate,
            @Field("username") String username,
            @Field("gender") Boolean gender,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Part("profile_image") RequestBody file
    );

    @FormUrlEncoded
    @POST("forgotPassword")
    @Headers("Accept: application/json")
    Call<ForgetPassword> forgotPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("resetPassword")
    @Headers("Accept: application/json")
    Call<ResetPassword> resetPassword(
            @Field("email") String email,
            @Field("code") String code,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );


    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("firebase_token") String firebase_token
    );

//    @Multipart
//    @POST("signup")
//    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @FormUrlEncoded
    @POST("confirmUser")
    @Headers("Accept: application/json")
    Call<ConfirmEmailResponse> confirmEmail(
            @Field("email") String email,
            @Field("code") String code
    );


    @POST("getNewsFeed")
    @Headers("Accept: application/json")
    Call<NewsFeedResponse> getNewsfeed(@Header("Authorization") String accessToken_And_Type);

    @POST("getNewsFeed")
    @Headers("Accept: application/json")
//    LiveData<ApiResponse<NewsFeedResponse>> getNewsfeed2(@Header("Authorization") String accessToken_And_Type ,@Header("page") int page);
    LiveData<ApiResponse<NewsFeedResponse>> getNewsfeed2(@Header("Authorization") String accessToken_And_Type);


    @POST("getPosts")
    @Headers("Accept: application/json")
    Call<NewsFeedResponse> getUserPosts(@Header("Authorization") String accessToken_And_Type);


    @POST("getLikedPosts")
    @Headers("Accept: application/json")
    Call<NewsFeedResponse> getUserLikedPosts(@Header("Authorization") String accessToken_And_Type);

    @FormUrlEncoded
    @POST("likeOrDislikePost")
    @Headers("Accept: application/json")
    Call<LikedAndDislikeResponse> likeAndDislikePost(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);

    @FormUrlEncoded
    @POST("likeOrDislikePost")
    @Headers("Accept: application/json")
    LiveData<ApiResponse<JsonElement>> likeAndDislikePost2(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);


    @FormUrlEncoded
    @POST("deletePost")
    @Headers("Accept: application/json")
    LiveData<ApiResponse<JsonElement>> deletePost(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);


    @FormUrlEncoded
    @POST("sharePost")
    @Headers("Accept: application/json")
    Call<LikedAndDislikeResponse> sharePost(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);

    @FormUrlEncoded
    @POST("sharePost")
    @Headers("Accept: application/json")
    LiveData<ApiResponse<JsonElement>> sharePost2(@Header("Authorization") String accessToken_And_Type
            , @Field("id") String id);


    @POST("addPost")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    Call<ResponseBody> addPost(@Header("Authorization") String accessToken_And_Type,
                               @Part MultipartBody.Part file);

    @POST("addPost")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    LiveData<ApiResponse<JsonElement>> addPost2(@Header("Authorization") String accessToken_And_Type,
                                                @Part() MultipartBody.Part file, @Part("text") String text, @Part List<MultipartBody.Part> images);

    @POST("editPost")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    LiveData<ApiResponse<JsonElement>> updatePost(@Header("Authorization") String accessToken_And_Type,
                                                  @Part("id") int id, @Part("text") String text, @Part List<MultipartBody.Part> images);

    @POST("getMap")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    LiveData<ApiResponse<MapDetailResponse>> getMapDetails(@Header("Authorization") String accessToken_And_Type, @Field("longitude") double longitude, @Field("latitude") double latitude);

    @POST("getMessages")
    @Headers("Accept: application/json")
    LiveData<ApiResponse<UserMessagesResModel>> getChatList(@Header("Authorization") String auth);

    @POST("getComments")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    LiveData<ApiResponse<PostCommentResponse>> getPostComments(@Header("Authorization") String auth, @Field("id") int postId);

    @POST("getComments")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    LiveData<ApiResponse<ReplayModelResponse>> getCommentReplies(@Header("Authorization") String auth, @Field("id") int commentId);

    @POST("addComments")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    LiveData<ApiResponse<JsonElement>> addComment(@Header("Authorization") String accessToken_And_Type, @Part("id") int postId,
                                                  @Part("text") String text, @Part List<MultipartBody.Part> images);

    @POST("addComments")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    LiveData<ApiResponse<JsonElement>> addVoiceComment(@Header("Authorization") String accessToken_And_Type, @Part("id") int postId,
                                                       @Part() MultipartBody.Part file);

    @POST("getMessagesDetails")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    LiveData<ApiResponse<MessageDetailsResponse>> getChatHistoryDetails(@Header("Authorization") String auth, @Field("id") int chatId);

    @POST("sendMessage")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    LiveData<ApiResponse<JsonElement>> sendMessage(@Header("Authorization") String auth,
                                                   @Part("id") int id,
                                                   @Part("latitude") double latitude,
                                                   @Part("longitude") double longitude,
                                                   @Part("message_files_type") String messageFileType,
                                                   @Part("message_body") String message_body,
                                                   @Part("type") String type,
                                                   @Part List<MultipartBody.Part> images);


    @POST("sendMessage")
    @Headers("Accept: application/json")
    Call<SendMsgResponse> sendMessage3(@Header("Authorization") String auth, @Body SendMessageBody body);

    @GET("checkUser")
    @Headers("Accept: application/json")
    Observable<ResponseApi<CheckUserResponse>> checkUserName(@Query("username") String name);

    @POST("joinGroup")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    LiveData<ApiResponse<GenericResponse<JoinGroupResponse>>> joinGroup(@Header("Authorization") String auth, @Field("id") String id, @Field("latitude") double latitude, @Field("longitude") double longitude, @Field("password") int password);

    @POST("editProfile")
    @Headers("Accept: application/json")
    Observable<ResponseApi<UserModel>> getUserData(@Header("Authorization") String auth);

    @POST("updateProfile")
    @Multipart
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    Observable<ResponseApi<String>> updateProfile(@Header("Authorization") String auth,
                                                  @Part("user_id") int user_id,
                                                  @Part("first_name") String first_name,
                                                  @Part("last_name") String last_name,
                                                  @Part("email") String email,
                                                  @Part("phone") String phone,
                                                  @Part("city") String city,
                                                  @Part("country") String country,
                                                  @Part("birthdate") String birthdate,
                                                  @Part("username") String username,
                                                  @Part("gender") boolean gender,
                                                  @Part("password") String password,
                                                  @Part("password_confirmation") String password_confirmation);


    @POST("followOrUnFollow")
    @FormUrlEncoded
    @Headers({"Accept: application/json", "Enctype: multipart/form-data"})
    Observable<ResponseApi> followOrUnFollow(@Header("Authorization") String auth, @Field("username") String username, @Field("id") int id);

    @POST("getUserDetails")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<ResponseApi<UserInfo>> getUserDetails(@Header("Authorization") String auth, @Field("user_id") int user_id);


    @POST("getUserDetails")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<UserInfo> getUserDetails2(@Header("Authorization") String auth, @Field("user_id") int user_id);

    @POST("getPosts")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Call<NewsFeedResponse> userPosts(@Header("Authorization") String accessToken_And_Type, @Field("user_id") int userName);

    @POST("editPrivacy")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<PrivacyModel> getPrivacy(@Field("user_id") int user_id, @Header("Authorization") String auth);

    @POST("updatePrivacy")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<ResponseApi> updatePrivacy(@Field("user_id") int user_id, @Header("Authorization") String auth, @Field("profile_privacy") int profile_privacy
            , @Field("following_followers_privacy") int following_followers_privacy, @Field("age_location_privacy") int age_location_privacy);


    @POST("logout")
    @Headers("Accept: application/json")
    Observable<ResponseApi> logout(@Header("Authorization") String auth);

    @POST("search")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<ResponseApi<ArrayList<SearchResult>>> search(@Header("Authorization") String auth, @Field("search") String searchQuery);

    @POST("hearVoiceNote")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Call<ApiResponse<JsonElement>> hearVoice(@Header("Authorization") String auth, @Field("id") String postId);

    @POST("editSocialAccounts")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<ResponseApi<SocialMedia>> getSocialMedia(@Header("Authorization") String auth, @Field("user_id") int user_id);

    @POST("updateSocialAccounts")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<SocialMediaResponse> updateSocialMedia(@Header("Authorization") String auth, @Field("user_id") int user_id
            , @Field("facebook") String facebook, @Field("instagram") String instagram, @Field("twitter") String twitter, @Field("whatsapp") String whatsapp,
                                                      @Field("linkedin") String linkedin, @Field("youtube") String youtube);

    @POST("getUserNotifications")
    @Headers("Accept: application/json")
    @FormUrlEncoded
    Observable<ResponseApi<ArrayList<NotificationModel>>> getNotifications(@Field("user_id") int user_id, @Header("Authorization") String auth);


}
