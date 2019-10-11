package com.forecaster.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("notificationStatus")
    @Expose
    private Boolean notificationStatus;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("totalPoints")
    @Expose
    private Integer totalPoints;

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("socialId")
    @Expose
    private String socialId;
    @SerializedName("socialType")
    @Expose
    private String socialType;
    @SerializedName("jwtToken")
    @Expose
    private String jwtToken;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;


    @SerializedName("deviceType")
    @Expose
    private String deviceType;

    @SerializedName("forecasterId")
    @Expose
    private String forecasterId;




   @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("birthPlace")
    @Expose
    private String birthPlace;

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("maritalStatus")
    @Expose
    private String maritalStatus;

    @SerializedName("profileSetup")
    @Expose
    private Boolean profileSetup;
    @SerializedName("totalRating")
    @Expose
    private Float totalRating;
    @SerializedName("avgRating")
    @Expose
    private Float avgRating;
    @SerializedName("onlineStatus")
    @Expose
    private Boolean onlineStatus;
    @SerializedName("responseTime")
    @Expose
    private String responseTime;
    @SerializedName("pendingQueue")
    @Expose
    private Integer pendingQueue;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("profileComplete")
    @Expose
    private Boolean profileComplete;


    @SerializedName("bookingCount")
    @Expose
    private Integer bookingCount;


    @SerializedName("aboutUs")
    @Expose
    private String aboutUs;
    @SerializedName("accountHolderName")
    @Expose
    private String accountHolderName;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("pricePerQues")
    @Expose
    private String pricePerQues;
    @SerializedName("uploadedVideo")
    @Expose
    private String uploadedVideo;

    @SerializedName("voiceRecording")
    @Expose
    private String voiceRecording;


    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("roomId")
    @Expose
    private String roomId;
    @SerializedName("dreamerData")
    @Expose
    private DreamerData dreamerData;

    @SerializedName("voiceNote")
    @Expose
    private String voiceNote;

    @SerializedName("notiTo")
    @Expose
    private String notiTo;
    @SerializedName("notiTitle")
    @Expose
    private String notiTitle;
    @SerializedName("notiMessage")
    @Expose
    private String notiMessage;
    @SerializedName("userType")
    @Expose
    private String userType;

    @SerializedName("requestStatus")
    @Expose
    private boolean requestStatus;
    @SerializedName("chatCloseByDreamerStatus")
    @Expose
    private boolean chatCloseByDreamerStatus;
    @SerializedName("chatCloseStatus")
    @Expose
    private boolean chatCloseStatus;

    @SerializedName("isSeen")
    @Expose
    private boolean isSeen;

    @SerializedName("recieverId")
    @Expose
    private String recieverId;
    @SerializedName("lastMessage")
    @Expose
    private String lastMessage;

    @SerializedName("event name")
    @Expose
    private String event_name;



    @SerializedName("requestAcceptStatus")
    @Expose
    private boolean requestAcceptStatus;

    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("receiverId")
    @Expose
    private String receiverId;

    @SerializedName("requestAcceptTime")
    @Expose
    private Object requestAcceptTime;
    @SerializedName("requestSendTime")
    @Expose
    private Object requestSendTime;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("messageType")
    @Expose
    private String messageType;

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("localTime")
    @Expose
    private String localTime;
    @SerializedName("isDelete")
    @Expose
    private boolean isDelete;

    @SerializedName("media")
    @Expose
    private String media;

    @SerializedName("Data")
    @Expose
    private  String Data;

    @SerializedName("chatList")
    @Expose
    private ChatList chatList;

    public String getForecasterId() {
        return forecasterId;
    }

    public void setForecasterId(String forecasterId) {
        this.forecasterId = forecasterId;
    }

    public ChatList getChatList() {
        return chatList;
    }

    public void setChatList(ChatList chatList) {
        this.chatList = chatList;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public boolean isRequestAcceptStatus() {
        return requestAcceptStatus;
    }

    public void setRequestAcceptStatus(boolean requestAcceptStatus) {
        this.requestAcceptStatus = requestAcceptStatus;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Object getRequestAcceptTime() {
        return requestAcceptTime;
    }

    public void setRequestAcceptTime(Object requestAcceptTime) {
        this.requestAcceptTime = requestAcceptTime;
    }

    public Object getRequestSendTime() {
        return requestSendTime;
    }

    public void setRequestSendTime(Object requestSendTime) {
        this.requestSendTime = requestSendTime;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public static Creator<Data> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public boolean isRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isChatCloseByDreamerStatus() {
        return chatCloseByDreamerStatus;
    }

    public void setChatCloseByDreamerStatus(boolean chatCloseByDreamerStatus) {
        this.chatCloseByDreamerStatus = chatCloseByDreamerStatus;
    }

    public boolean isChatCloseStatus() {
        return chatCloseStatus;
    }

    public void setChatCloseStatus(boolean chatCloseStatus) {
        this.chatCloseStatus = chatCloseStatus;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getNotiTo() {
        return notiTo;
    }

    public void setNotiTo(String notiTo) {
        this.notiTo = notiTo;
    }

    public String getNotiTitle() {
        return notiTitle;
    }

    public void setNotiTitle(String notiTitle) {
        this.notiTitle = notiTitle;
    }

    public String getNotiMessage() {
        return notiMessage;
    }

    public void setNotiMessage(String notiMessage) {
        this.notiMessage = notiMessage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getVoiceNote() {
        return voiceNote;
    }

    public void setVoiceNote(String voiceNote) {
        this.voiceNote = voiceNote;
    }

    protected Data(Parcel in) {
        id = in.readString();
        userId = in.readString();
        question = in.readString();
        categoryName = in.readString();
        createdAt = in.readString();
        voiceNote = in.readString();
        dreamerData = in.readParcelable(DreamerData.class.getClassLoader());
        roomId=in.readString();
        senderId=in.readString();
        receiverId=in.readString();
        chatList=in.readParcelable(Chatlist.class.getClassLoader());
        forecasterId=in.readString();
    }

    public Data(String roomId, String senderId, String receiverId,String message,String messageType,String createdAt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message=message;
        this.messageType=messageType;
        this.createdAt=createdAt;
    }

    public Data(String roomId, String senderId, String receiverId,String message,String messageType,String createdAt,String media) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message=message;
        this.messageType=messageType;
        this.createdAt=createdAt;
        this.media=media;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public DreamerData getDreamerData() {
        return dreamerData;
    }

    public void setDreamerData(DreamerData dreamerData) {
        this.dreamerData = dreamerData;
    }

    public String getVoiceRecording() {
        return voiceRecording;
    }

    public void setVoiceRecording(String voiceRecording) {
        this.voiceRecording = voiceRecording;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPricePerQues() {
        return pricePerQues;
    }

    public void setPricePerQues(String pricePerQues) {
        this.pricePerQues = pricePerQues;
    }

    public String getUploadedVideo() {
        return uploadedVideo;
    }

    public void setUploadedVideo(String uploadedVideo) {
        this.uploadedVideo = uploadedVideo;
    }

    public Integer getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(Integer bookingCount) {
        this.bookingCount = bookingCount;
    }

    public Boolean getProfileComplete() {
        return profileComplete;
    }

    public void setProfileComplete(Boolean profileComplete) {
        this.profileComplete = profileComplete;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getProfileSetup() {
        return profileSetup;
    }

    public void setProfileSetup(Boolean profileSetup) {
        this.profileSetup = profileSetup;
    }

    public Float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Float totalRating) {
        this.totalRating = totalRating;
    }

    public Float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getPendingQueue() {
        return pendingQueue;
    }

    public void setPendingQueue(Integer pendingQueue) {
        this.pendingQueue = pendingQueue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }



    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }




    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return this.forecasterId+""+"Sender id:"+this.chatList.getSenderId()+"Room id:"+this.chatList.getRoomId();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.question);
        dest.writeString(this.categoryName);
        dest.writeString(this.createdAt);
        dest.writeString(this.voiceNote);
        dest.writeParcelable(this.dreamerData,flags);
        dest.writeString(this.roomId);
        dest.writeString(this.senderId);
        dest.writeString(this.receiverId);
        dest.writeParcelable(this.chatList,flags);
        dest.writeString(this.forecasterId);

    }
}
