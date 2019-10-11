package com.forecaster.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatList implements Parcelable {

    @SerializedName("roomId")
    @Expose
    private String roomId;
    @SerializedName("senderId")
    @Expose
    private String senderId;

    protected ChatList(Parcel in) {
        roomId = in.readString();
        senderId = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
        dest.writeString(senderId);
    }
}
