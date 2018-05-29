package com.ybe.arview2;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ybe.arview2.sensors.DeviceLocation;
import com.ybe.arview2.sensors.DeviceOrientation;

public class MathHelper {
    private static final String TAG = "MathHelper";
    private Context context;
    private DeviceOrientation compassHelper;
    private Location currentLocation;
    private Location destination;

    @RequiresApi(api = Build.VERSION_CODES.M)
    MathHelper(Context context, Location destination) {
        this.context = context;
        this.destination = destination;
        compassHelper = new DeviceOrientation(context);
        registerLocationListener();
    }

    private void registerLocationListener() {
        DeviceLocation.LocationResult locationResult = new DeviceLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                currentLocation = location;
                Log.i(TAG, "gotLocation: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
            }
        };
        DeviceLocation myLocation = new DeviceLocation();
        myLocation.getLocation(context, locationResult);
    }

    public float getAngle() {
        if (currentLocation == null) return 0;

        float currentHeading = compassHelper.getAzimuth();

        GeomagneticField geoField = new GeomagneticField(
                Double.valueOf(destination.getLatitude()).floatValue(),
                Double.valueOf(destination.getLongitude()).floatValue(),
                Double.valueOf(destination.getAltitude()).floatValue(),
                System.currentTimeMillis());

        currentHeading += geoField.getDeclination();

        float bearing = getBearing();

        float angle = bearing - currentHeading;

        if (angle < 0) {
            angle += 360;
        }

        angle = 360 - angle;

        return angle;
    }

    private float getBearing() {
        float bearing = currentLocation.bearingTo(destination);

        if (bearing < 0) {
            bearing += 360;
        }

        return bearing;
    }

    public float getDistanceToPoi() {
        return currentLocation.distanceTo(destination);
    }

    public void onPause() {
        compassHelper.onPause();
    }

    public void onResume() {
        compassHelper.onResume();
    }
}
