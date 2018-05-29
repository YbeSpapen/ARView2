package com.ybe.arview2demo;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.ArFragment;
import com.ybe.arview2.NodeFactory;
import com.ybe.arview2.models.Animation;
import com.ybe.arview2.models.Sign;


public class NodeFactoryExample extends AppCompatActivity {
    private ArFragment arFragment;
    private NodeFactory factory;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sceneform);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        Location location = getIntent().getParcelableExtra("location");

        Animation animation = new Animation("balloon.sfb", 4000);
        Sign sign = new Sign("Wegwijzer", "sign.sfb", "sign", new Vector3(0.40f, 0.40f, 0.40f), animation, true);

        factory = new NodeFactory(this, arFragment, location);

        factory.drawArrow("arrow.sfb");

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    factory.drawSign(sign, anchorNode);
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPause() {
        super.onPause();
        factory.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        factory.resume();
    }
}