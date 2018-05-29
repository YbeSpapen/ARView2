package com.ybe.arview2;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

public class CompassNode extends Node {

    private MathHelper helper;

    CompassNode(MathHelper helper) {
        this.helper = helper;
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        this.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), helper.getAngle()));
    }
}
