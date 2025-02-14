package com.Bforecaster.Retrofit;


import com.Bforecaster.Modal.ChatHistory;
import com.Bforecaster.Modal.ChatList;
import com.Bforecaster.Modal.CheckMobileNumber;
import com.Bforecaster.Modal.ContactUs;
import com.Bforecaster.Modal.ForcasterDetail;
import com.Bforecaster.Modal.ForcasterSetupProfile;
import com.Bforecaster.Modal.ForgotPassword;
import com.Bforecaster.Modal.Login;
import com.Bforecaster.Modal.Logout;
import com.Bforecaster.Modal.Notification;
import com.Bforecaster.Modal.PaymentManagement;
import com.Bforecaster.Modal.RequestManagement;
import com.Bforecaster.Modal.Setting;
import com.Bforecaster.Modal.Signup;
import com.Bforecaster.Modal.UpdateStatus;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetroInterface {

    @POST("forecasterSignup")
    Call<Signup> forecasterSignup(@Body Signup signup);

    @POST("forecasterLogin")
    Call<Login> forecasterLogin(@Body Login login);


    @POST("getForecasterSettings")
    Call<Setting> getForecasterSettings(@Body Setting setting,@Header("token") String token);


    @POST("updateForecasterSettings")
    Call<Setting> updateForecasterSettings(@Body Setting setting, @Header("token") String token);

    @POST("contactUs")
    Call<ContactUs> contactUs(@Body ContactUs contactUs,@Header("token") String token);

    @POST("checkForecasterMobileNumber")
    Call<CheckMobileNumber> checkForecasterMobileNumber(@Body CheckMobileNumber number,@Header("token") String token);


    @POST("forecasterResetPassword")
    Call<ForgotPassword> forecasterResetPassword(@Body ForgotPassword password);


    @POST("checkForecasterUsername")
    Call<Signup> checkForecasterUsername(@Body Signup signup);


    @Multipart
    @POST("forecasterSetupProfile")
    Call<ForcasterSetupProfile> forecasterSetupProfile(@Part MultipartBody.Part prfpic,@Part MultipartBody.Part attach_docx,@Part MultipartBody.Part audio,@Part MultipartBody.Part video, @PartMap Map<String, RequestBody> body);

    @POST("forecasterLogout")
    Call<Logout> forecasterLogout(@Body Logout logout);

    @POST("forecasterChangePassword")
    Call<ForgotPassword> forecasterChangePassword(@Body ForgotPassword forgotPassword,@Header("token") String token);

    @Multipart
    @POST("forecasterUpdateProfile")
    Call<ForcasterSetupProfile> forecasterUpdateProfile(@Part MultipartBody.Part prfpic,@Part MultipartBody.Part voice,@Part MultipartBody.Part video,@PartMap Map<String,RequestBody> body);

    @POST("getForecasterDetails")
    Call<ForcasterDetail> getForecasterDetails(@Body ForcasterDetail forcasterDetail,@Header("token") String token);


    @POST("getRequestList")
    Call<RequestManagement> getRequestList(@Body RequestManagement management,@Header("token") String token);

    @POST("updateOnlineStatus")
    Call<UpdateStatus> updateOnlineStatus(@Body UpdateStatus status, @Header("token") String token);

    @POST("getNotificationList")
    Call<Notification> getNotificationList(@Body Notification notification);

    @POST("chatListForForecaster")
    Call<ChatList> getchatList(@Body ChatList chatlist);

    @POST("chatHistory")
    Call<ChatHistory> getchatHistory(@Body ChatHistory history,@Header("token") String token);

    @POST("getForecasterTransactionList")
    Call<PaymentManagement> getForecasterTransactionList(@Body PaymentManagement payment, @Header("token") String token);
}
