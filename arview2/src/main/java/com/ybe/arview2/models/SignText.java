package com.ybe.arview2.models;

import com.google.ar.sceneform.math.Vector3;

public class SignText {
    private String text;
    private Vector3 scale;
    private Vector3 position;
    //POSITION WHEN TEXT IS TURNED 180 DEGREE
    private Vector3 positionReflected;

    public SignText(Vector3 position, Vector3 scale) {
        this.position = position;
        this.scale = scale;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Vector3 getPositionReflected() {
        return positionReflected;
    }

    public void setPositionReflected(Vector3 positionReflected) {
        this.positionReflected = positionReflected;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }
}
