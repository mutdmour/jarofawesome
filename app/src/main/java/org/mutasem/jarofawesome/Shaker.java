package org.mutasem.jarofawesome;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.provider.Settings;

/**
 * Created by mutasemdmour on 6/6/16.
 * https://stackoverflow.com/questions/9934324/implementing-a-shake-event
 */

public class Shaker {
    private SensorManager sensorManager = null;
    private double threshold = 1.0d;
    private long lastShakeTimestamp = 0;
    private long gap = 0;
    private int repeat = 1;
    private int count = 0;
    private Shaker.Callback cb = null;
    public Shaker(Context context, double threshold, long gap, int repeat, Shaker.Callback cb){
        this.threshold = threshold * threshold;
        this.threshold = this.threshold * SensorManager.GRAVITY_EARTH
                * SensorManager.GRAVITY_EARTH;
        this.gap = gap;
        this.cb = cb;
        this.repeat = repeat;

        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        startListening();
    }

    public void startListening(){
        this.sensorManager.registerListener(listener,
                this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void stopListening(){
        this.sensorManager.unregisterListener(listener);
    }

    public interface Callback {
        void shakingStarted();
        void shakingStopped();
        void shakingNotEnough();
    }

    private final SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                double netForce = event.values[0] * event.values[0];

                netForce += event.values[1] * event.values[1];
                netForce += event.values[2] * event.values[2];

                if (threshold < netForce) {
                    isShaking();
                } else {
                    isNotShaking();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void isShaking(){
        long now = SystemClock.uptimeMillis();
        try {
            if (lastShakeTimestamp == 0){
                lastShakeTimestamp = now;

                if (cb != null){
                    cb.shakingStarted();
                }
            } else if (now - lastShakeTimestamp > gap){
                count++;
                lastShakeTimestamp = now;
            }
        } catch (NullPointerException e){

        }
    }

    private void isNotShaking(){
        long now = SystemClock.uptimeMillis();
        if (lastShakeTimestamp > 0){
            if (now - lastShakeTimestamp > gap){
                lastShakeTimestamp = 0;
                if ((count+1) > repeat) {
                    cb.shakingStopped();
                } else {
                    cb.shakingNotEnough();
                }
                count = 0;
            }
        }
    }

}
