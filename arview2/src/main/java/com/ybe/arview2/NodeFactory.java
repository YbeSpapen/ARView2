package com.ybe.arview2;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.ybe.arview2.models.Sign;

public class NodeFactory {

    private final Context context;
    private final Location location;
    private ArFragment arFragment;
    private MathHelper helper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public NodeFactory(Context context, ArFragment arFragment, Location location) {
        this.context = context;
        this.arFragment = arFragment;
        this.location = location;
        this.helper = new MathHelper(context, location);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void drawArrow(String arrowSfb) {
        ModelRenderable.builder().setSource(context, Uri.parse(arrowSfb)).build().thenAccept(renderable -> {
            CompassNode arrow = new CompassNode(helper);
            arrow.setParent(arFragment.getArSceneView().getScene().getCamera());
            arrow.setLocalPosition(new Vector3(0f, -0.5f, -1f));
            arrow.setLocalScale(new Vector3(0.10f, 0.10f, 0.10f));
            arrow.setRenderable(renderable);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void drawSign(Sign sign, Node parent) {
        ModelRenderable.builder().setSource(context, Uri.parse(sign.getSfbPath())).build().thenAccept(renderable -> {
            SignNode signNode = new SignNode(context, location, helper);
            signNode.setParent(parent);
            signNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), helper.getAngle()));
            signNode.setLocalScale(new Vector3(0.40f, 0.40f, 0.40f));
            signNode.setRenderable(renderable);

            if (sign.getAnimation() != null)
                drawAnimatedObject(sign.getAnimation().getSfbPath(), sign.getAnimation().getDurationMilliseconds(), signNode);

            if (sign.isEnableSignText()) {
                drawTextOnSign(signNode);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawTextOnSign(SignNode sign) {
        ViewRenderable.builder().setView(context, R.layout.ar_sign_text).build().thenAccept(textRenderable -> {
            float angle = helper.getAngle();
            Node signText = new Node();
            signText.setParent(sign);

            if (angle <= 90 || angle >= 240) {

                float tempAngle = angle - 90;
                if (tempAngle < 0) {
                    tempAngle += 360;
                }

                signText.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), tempAngle));
                signText.setLocalPosition((new Vector3(0f, 0.62f, 0.03f)));

            } else {

                float tempAngle = angle + 90;
                if (tempAngle > 360) {
                    tempAngle -= 360;
                }

                signText.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), tempAngle));
                signText.setLocalPosition((new Vector3(0f, 0.62f, -0.03f)));
            }

            signText.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));
            signText.setRenderable(textRenderable);
            TextView textView = (TextView) textRenderable.getView();

            if (helper.getDistanceToPoi() > 20) {
                textView.setText(String.format("%s m", Math.round(helper.getDistanceToPoi())));
            } else {
                textView.setText("Gearriveerd!");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawAnimatedObject(String animatedObjectSfb, float durationMilliseconds, SignNode sign) {
        ModelRenderable.builder().setSource(context, Uri.parse(animatedObjectSfb)).build().thenAccept(renderable -> {
            Node birdRotatePoint = new Node();
            birdRotatePoint.setParent(sign);

            Node bird = new Node();
            bird.setParent(birdRotatePoint);
            bird.setLocalPosition(new Vector3(2f, 1.9f, 0.0f));
            bird.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 180));
            bird.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));
            bird.setRenderable(renderable);

            ObjectAnimator orbitAnimation = createAnimator();
            orbitAnimation.setTarget(birdRotatePoint);
            orbitAnimation.setDuration((long) durationMilliseconds);
            orbitAnimation.start();
        });
    }

    private static ObjectAnimator createAnimator() {
        Quaternion orientation1 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 0);
        Quaternion orientation2 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 120);
        Quaternion orientation3 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 240);
        Quaternion orientation4 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 360);

        ObjectAnimator orbitAnimation = new ObjectAnimator();
        orbitAnimation.setObjectValues(orientation1, orientation2, orientation3, orientation4);

        orbitAnimation.setPropertyName("localRotation");

        orbitAnimation.setEvaluator(new QuaternionEvaluator());

        orbitAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        orbitAnimation.setRepeatMode(ObjectAnimator.RESTART);
        orbitAnimation.setInterpolator(new LinearInterpolator());
        orbitAnimation.setAutoCancel(true);

        return orbitAnimation;
    }

    public void resume() {
        helper.onResume();
    }

    public void pause() {
        helper.onPause();
    }
}
