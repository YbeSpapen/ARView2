package com.ybe.arview2;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.ybe.arview2.adapters.SignAdapter;
import com.ybe.arview2.interfaces.RecyclerViewClickListener;
import com.ybe.arview2.models.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ARScene extends android.support.v4.app.Fragment implements RecyclerViewClickListener {
    private ArFragment arFragment;

    private ModelRenderable balloonRenderable;
    private ModelRenderable arrowRenderable;
    private ViewRenderable signTextRenderable;

    private float durationMilliseconds = 8000;

    private MathHelper helper;

    private List<Sign> signs;
    private List<ModelRenderable> renderables;
    private int selectedItem = 0;
    private Location location;

    private RecyclerView signRecyclerView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sceneform, container, false);

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        signRecyclerView = view.findViewById(R.id.rv_items_items);

        renderables = new ArrayList<>();

        Bundle args = getArguments();
        if (args != null) {
            signs = args.getParcelableArrayList("itemList");
            location = args.getParcelable("location");

            helper = new MathHelper(getContext(), location);
        }

        setupItemAdapter();

        CompletableFuture<ModelRenderable> balloonStage =
                ModelRenderable.builder().setSource(getContext(), Uri.parse("balloon.sfb")).build();
        CompletableFuture<ModelRenderable> arrowStage =
                ModelRenderable.builder().setSource(getContext(), Uri.parse("arrow.sfb")).build();
        CompletableFuture<ViewRenderable> signTextStage =
                ViewRenderable.builder().setView(getContext(), R.layout.ar_sign_text).build();

        for (Sign sign : signs) {
            ModelRenderable.builder().setSource(getContext(), Uri.parse(sign.getSfbPath())).build().thenAccept(renderable -> renderables.add(renderable));
        }

        CompletableFuture.allOf(
                balloonStage,
                arrowStage,
                signTextStage
        ).handle(
                (notUsed, throwable) -> {
                    try {
                        balloonRenderable = balloonStage.get();
                        arrowRenderable = arrowStage.get();
                        signTextRenderable = signTextStage.get();

                        //Arrow
                        CompassNode arrow = new CompassNode(helper);
                        arrow.setParent(arFragment.getArSceneView().getScene().getCamera());
                        arrow.setLocalPosition(new Vector3(0f, -0.5f, -1f));
                        arrow.setLocalScale(new Vector3(0.10f, 0.10f, 0.10f));
                        arrow.setRenderable(arrowRenderable);

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (balloonRenderable == null || renderables.size() == 0) {
                        return;
                    }

                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    float angle = helper.getAngle();

                    //Sign
                    SignNode sign = new SignNode(getContext(), location, helper);
                    sign.setParent(anchorNode);
                    sign.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), angle));
                    sign.setLocalScale(signs.get(selectedItem).getScale());
                    sign.setRenderable(renderables.get(selectedItem));

                    //Signtext
                    Node signText = new Node();
                    signText.setParent(anchorNode);

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
                    signText.setRenderable(signTextRenderable);
                    TextView textView = (TextView) signTextRenderable.getView();

                    if (helper.getDistanceToPoi() > 20) {
                        textView.setText(String.format("%s m", Math.round(helper.getDistanceToPoi())));
                    } else {
                        textView.setText("Gearriveerd!");
                    }

                    //Point bird rotation
                    Node birdRotatePoint = new Node();
                    birdRotatePoint.setParent(sign);

                    //Balloon
                    Node bird = new Node();
                    bird.setParent(birdRotatePoint);
                    bird.setLocalPosition(new Vector3(2f, 1.9f, 0.0f));
                    bird.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 180));
                    bird.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));
                    bird.setRenderable(balloonRenderable);

                    //Animator
                    ObjectAnimator orbitAnimation = createAnimator();
                    orbitAnimation.setTarget(birdRotatePoint);
                    orbitAnimation.setDuration((long) durationMilliseconds);
                    orbitAnimation.start();
                });

        Texture.Sampler sampler =
                Texture.Sampler.builder()
                        .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                        .build();

        Texture.builder()
                .setSource(Objects.requireNonNull(getContext()), R.drawable.grass)
                .setSampler(sampler)
                .build()
                .thenAccept(texture -> arFragment.getArSceneView().getPlaneRenderer().getMaterial().thenAccept(material -> material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture)));

        return view;
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

    private void setupItemAdapter() {
        SignAdapter adapter = new SignAdapter(signs, this);
        signRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        signRecyclerView.setLayoutManager(layoutManager);
        signRecyclerView.setAdapter(adapter);
    }


    @Override
    public void recyclerViewListClicked(View v) {
        selectedItem = signRecyclerView.indexOfChild(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPause() {
        super.onPause();
        helper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        helper.onResume();
    }
}
