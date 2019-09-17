package com.forecaster_app.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DreamerData implements Parcelable {
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("birthPlace")
    @Expose
    private String birthPlace;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("maritalStatus")
    @Expose
    private String maritalStatus;

    protected DreamerData(Parcel in) {
        profilePic = in.readString();
        name = in.readString();
        birthPlace = in.readString();
        dob = in.readString();
        gender = in.readString();
        maritalStatus = in.readString();
    }

    public static final Creator<DreamerData> CREATOR = new Creator<DreamerData>() {
        @Override
        public DreamerData createFromParcel(Parcel in) {
            return new DreamerData(in);
        }

        @Override
        public DreamerData[] newArray(int size) {
            return new DreamerData[size];
        }
    };

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.profilePic);
        dest.writeString(this.name);
        dest.writeString(this.birthPlace);
        dest.writeString(this.dob);
        dest.writeString(this.gender);
        dest.writeString(this.maritalStatus);
    }
}
