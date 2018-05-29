package com.ybe.arview2;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

public class SignNode extends Node {
    private Node infoCard;
    private Context context;
    private Location location;
    private MathHelper helper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    SignNode(Context context, Location location, MathHelper helper) {
        this.context = context;
        this.location = location;
        this.helper = helper;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivate() {
        super.onActivate();

        if (infoCard == null) {
            infoCard = new Node();
            infoCard.setParent(this);
            infoCard.setLocalPosition(new Vector3(0.0f, 2f, 0.0f));

            ViewRenderable.builder()
                    .setView(context, R.layout.ar_card_distance)
                    .build()
                    .thenAccept(
                            (renderable) -> {
                                infoCard.setRenderable(renderable);
                                TextView textView = (TextView) renderable.getView();
                                textView.setText(String.format("Afstand tot %s: %s m", location.getProvider(), (int) helper.getDistanceToPoi()));
                            })
                    .exceptionally(
                            (throwable) -> {
                                throw new AssertionError("Could not load plane card view.", throwable);
                            });
        }
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        if (infoCard == null || getScene() == null) {
            return;
        }

        Vector3 cameraPosition = getScene().getCamera().getWorldPosition();
        Vector3 cardPosition = infoCard.getWorldPosition();
        Vector3 direction = Vector3.subtract(cameraPosition, cardPosition);
        Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
        infoCard.setWorldRotation(lookRotation);
    }
}
