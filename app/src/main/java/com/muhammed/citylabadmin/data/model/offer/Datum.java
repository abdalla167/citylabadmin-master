package com.muhammed.citylabadmin.data.model.offer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("currentPrice")
    private Long mCurrentPrice;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("endTime")
    private String mEndTime;
    @SerializedName("files")
    private List<String> mFiles;
    @SerializedName("offerId")
    private Long mOfferId;
    @SerializedName("previousPrice")
    private Long mPreviousPrice;
    @SerializedName("startTime")
    private String mStartTime;
    @SerializedName("title")
    private String mTitle;

    public Long getCurrentPrice() {
        return mCurrentPrice;
    }

    public void setCurrentPrice(Long currentPrice) {
        mCurrentPrice = currentPrice;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public List<String> getFiles() {
        return mFiles;
    }

    public void setFiles(List<String> files) {
        mFiles = files;
    }

    public Long getOfferId() {
        return mOfferId;
    }

    public void setOfferId(Long offerId) {
        mOfferId = offerId;
    }

    public Long getPreviousPrice() {
        return mPreviousPrice;
    }

    public void setPreviousPrice(Long previousPrice) {
        mPreviousPrice = previousPrice;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
