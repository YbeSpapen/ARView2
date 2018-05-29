package com.ybe.arview2demo;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.ar.sceneform.math.Vector3;
import com.ybe.arview2.ARScene;
import com.ybe.arview2.models.Sign;

import java.util.ArrayList;

public class ARActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        Location location = getIntent().getParcelableExtra("location");

        ArrayList<Sign> signs = new ArrayList<>();
        signs.add(new Sign("Wegwijzer", "sign.sfb", "sign", new Vector3(0.40f, 0.40f, 0.40f), true));
        signs.add(new Sign("Wegwijzer groot", "sign.sfb", "sign", new Vector3(0.60f, 0.60f, 0.60f), true));

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("itemList", signs);
        bundle.putParcelable("location", location);

        ARScene arScene = new ARScene();
        arScene.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_ar_container, arScene);
        ft.commit();
    }
}
