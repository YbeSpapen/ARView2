package com.ybe.arview2.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class DeviceOrientation implements SensorEventListener {
    private final float DECAY_RATE = 0.9f;
    private float[] accumulated = new float[3];
    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public DeviceOrientation(Context context) {
        sensorManager = context.getSystemService(SensorManager.class);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void onResume() {
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }

    public float getAzimuth() {
        return azimuth;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * sensorEvent.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * sensorEvent.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * sensorEvent.values[2];
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * sensorEvent.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * sensorEvent.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * sensorEvent.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float temp = (float) Math.toDegrees(orientation[0]);
                azimuth = (temp + 360) % 360;
            }

            if (sensorEvent.sensor.getType() != Sensor.TYPE_MAGNETIC_FIELD) return;
            float[] rotated = new float[3];

            for (int i = 0; i < 3; ++i) {
                accumulated[i] = accumulated[i] * DECAY_RATE + rotated[i];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
