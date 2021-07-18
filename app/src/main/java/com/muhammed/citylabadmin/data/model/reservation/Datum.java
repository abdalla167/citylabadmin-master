
package com.muhammed.citylabadmin.data.model.reservation;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("age")
    private String mAge;
    @SerializedName("appartementNo")
    private String mAppartementNo;
    @SerializedName("buildingNo")
    private String  mBuildingNo;
    @SerializedName("day")
    private int mDay;
    @SerializedName("file")
    private String mFile;
    @SerializedName("floorNo")
    private String mFloorNo;
    @SerializedName("name")
    private String mName;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("reservationDate")
    private String mReservationDate;
    @SerializedName("reservationId")
    private int mReservationId;
    @SerializedName("type")
    private int mType;
    @SerializedName("userId")
    private Long mUserId;

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmAppartementNo() {
        return mAppartementNo;
    }

    public void setmAppartementNo(String mAppartementNo) {
        this.mAppartementNo = mAppartementNo;
    }

    public String getmBuildingNo() {
        return mBuildingNo;
    }

    public void setmBuildingNo(String mBuildingNo) {
        this.mBuildingNo = mBuildingNo;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public String getmFile() {
        return mFile;
    }

    public void setmFile(String mFile) {
        this.mFile = mFile;
    }

    public String getmFloorNo() {
        return mFloorNo;
    }

    public void setmFloorNo(String mFloorNo) {
        this.mFloorNo = mFloorNo;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmReservationDate() {
        return mReservationDate;
    }

    public void setmReservationDate(String mReservationDate) {
        this.mReservationDate = mReservationDate;
    }

    public int getmReservationId() {
        return mReservationId;
    }

    public void setmReservationId(int mReservationId) {
        this.mReservationId = mReservationId;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public Long getmUserId() {
        return mUserId;
    }

    public void setmUserId(Long mUserId) {
        this.mUserId = mUserId;
    }
}
