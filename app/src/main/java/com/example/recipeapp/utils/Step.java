package com.example.recipeapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Step implements Parcelable {
    private String id;
    private String shortDescription ;
    private String description ;
    private String videoURL ;
    private String thumbnailURL ;

    public Step(String id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    protected Step(Parcel in) {
        id = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Step getNextStep (Step step, List<Step> stepList) {
        Step nextStep ;
        if (Integer.valueOf(step.getId()) != stepList.size()-1) {
            nextStep = stepList.get(Integer.valueOf(step.getId()) + 1);
        } else {
            nextStep = null;
        }
        return nextStep;
    }
    public Step getPreviousStep (Step step, List<Step> stepList) {
        Step previousStep ;
        if (Integer.valueOf(step.getId()) != 0) {
            previousStep = stepList.get(Integer.valueOf(step.getId()) - 1);
        } else {
            previousStep = null;
        }
        return previousStep;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }
}
