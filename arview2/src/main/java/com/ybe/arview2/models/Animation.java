package com.ybe.arview2.models;

import com.google.ar.sceneform.math.Vector3;

public class Animation {
    private String sfbPath;
    private float durationMilliseconds;
    private Vector3 position;
    private Vector3 scale;

    public Animation(String sfbPath, float durationMilliseconds, Vector3 position, Vector3 scale) {
        this.sfbPath = sfbPath;
        this.durationMilliseconds = durationMilliseconds;
        this.position = position;
        this.scale = scale;
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

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }
}
