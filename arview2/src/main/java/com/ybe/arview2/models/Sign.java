package com.ybe.arview2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.ar.sceneform.math.Vector3;

public class Sign implements Parcelable {
    private String name;
    private String sfbPath;
    private String imagePath;
    private Vector3 scale;
    private Animation animation;
    private SignText signText;

    public Sign(String name, String sfbPath, String imagePath, Vector3 scale) {
        this.name = name;
        this.sfbPath = sfbPath;
        this.imagePath = imagePath;
        this.scale = scale;
    }

    protected Sign(Parcel in) {
        name = in.readString();
        sfbPath = in.readString();
        imagePath = in.readString();
    }

    public static final Creator<Sign> CREATOR = new Creator<Sign>() {
        @Override
        public Sign createFromParcel(Parcel in) {
            return new Sign(in);
        }

        @Override
        public Sign[] newArray(int size) {
            return new Sign[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfbPath() {
        return sfbPath;
    }

    public void setSfbPath(String sfbPath) {
        this.sfbPath = sfbPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public SignText getSignText() {
        return signText;
    }

    public void setSignText(SignText signText) {
        this.signText = signText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(sfbPath);
        parcel.writeString(imagePath);
    }
}
