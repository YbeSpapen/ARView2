package com.ybe.arview2.models;

public class Animation {
    private String sfbPath;
    private float durationMilliseconds;

    public Animation(String sfbPath, float durationMilliseconds) {
        this.sfbPath = sfbPath;
        this.durationMilliseconds = durationMilliseconds;
    }

    public String getSfbPath() {
        return sfbPath;
    }

    public void setSfbPath(String sfbPath) {
        this.sfbPath = sfbPath;
    }

    public float getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public void setDurationMilliseconds(float durationMilliseconds) {
        this.durationMilliseconds = durationMilliseconds;
    }
}
