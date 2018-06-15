package com.ybe.arview2demo;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ybe.arview2.DirectionARView;
import com.ybe.arview2.DefaultConfig;
import com.ybe.arview2.models.Animation;
import com.ybe.arview2.models.Sign;
import com.ybe.arview2.models.SignText;

import java.util.ArrayList;

public class ARActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        Location location = getIntent().getParcelableExtra("location");

        Animation animation = DefaultConfig.getDefaultAnimation();
        SignText text = DefaultConfig.getDefaultText();
        ArrayList<Sign> signs = DefaultConfig.getDefaultSignList();

        //Set animation and text on sign small
        signs.get(0).setAnimation(animation);
        signs.get(0).setSignText(text);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("signs", signs);
        bundle.putParcelable("location", location);

        DirectionARView arScene = new DirectionARView();
        arScene.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_ar_container, arScene);
        ft.commit();
    }
}
