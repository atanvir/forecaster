package com.forecaster.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatList implements Parcelable {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("Data")
    @Expose
    private List<Data> data = null;

    @SerializedName("forecasterId")
    @Expose
    private String forecasterId;

    @SerializedName("roomId")
    @Expose
    private String roomId;
    @SerializedName("senderId")
    @Expose
    private String senderId;

    protected ChatList(Parcel in) {
        status = in.readString();
        responseMessage = in.readString();
        data = in.createTypedArrayList(Data.CREATOR);
        forecasterId = in.readString();
        roomId = in.readString();
        senderId = in.readString();
    }
    public ChatList()
    {

    }

    public static final Creator<ChatList> CREATOR = new Creator<ChatList>() {
        @Override
        public ChatList createFromParcel(Parcel in) {
            return new ChatList(in);
        }

        @Override
        public ChatList[] newArray(int size) {
            return new ChatList[size];
        }
    };

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getForecasterId() {
        return forecasterId;
    }

    public void setForecasterId(String forecasterId) {
        this.forecasterId = forecasterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(responseMessage);
        dest.writeTypedList(data);
        dest.writeString(forecasterId);
        dest.writeString(roomId);
        dest.writeString(senderId);
    }
}
