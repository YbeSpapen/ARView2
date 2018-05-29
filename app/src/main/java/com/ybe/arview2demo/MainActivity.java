package com.ybe.arview2demo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_app_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditText = findViewById(R.id.et_app_name);
                String name = nameEditText.getText().toString();

                EditText latitudeEditText = findViewById(R.id.et_app_latitude);
                String latitude = latitudeEditText.getText().toString();

                EditText longitudeEditText = findViewById(R.id.et_app_longitude);
                String longitude = longitudeEditText.getText().toString();

                Location location = new Location(name);
                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitude));

                Intent intent = new Intent(getApplicationContext(), ARActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });
    }
}
