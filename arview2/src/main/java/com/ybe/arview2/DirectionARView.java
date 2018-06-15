package com.ybe.arview2;

import android.annotation.SuppressLint;
import android.location.Location;
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

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.ybe.arview2.adapters.SignAdapter;
import com.ybe.arview2.interfaces.RecyclerViewClickListener;
import com.ybe.arview2.models.Sign;

import java.util.List;
import java.util.Objects;

public class DirectionARView extends android.support.v4.app.Fragment implements RecyclerViewClickListener {
    private ArFragment arFragment;
    private NodeFactory factory;

    private List<Sign> signs;
    private int selectedItem = 0;

    private RecyclerView signRecyclerView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sceneform, container, false);

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        signRecyclerView = view.findViewById(R.id.rv_items_items);

        Bundle args = getArguments();
        if (args != null) {
            signs = args.getParcelableArrayList("signs");
            Location location = args.getParcelable("location");

            if (signs.size() == 1) {
                signRecyclerView.setVisibility(View.GONE);
            } else {
                setupItemAdapter();
            }

            factory = new NodeFactory(getContext(), arFragment, location);
            factory.drawArrow("arrow.sfb");
        }

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    factory.drawSign(signs.get(selectedItem), anchorNode);
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
        factory.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        factory.resume();
    }
}
