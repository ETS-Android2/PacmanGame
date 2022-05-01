package com.omer.mypackman;
import android.hardware.Sensor;
import android.hardware.SensorManager;
public class Sesnors_Manager {
    private SensorManager sensorManager;
    private Sensor accSensor;

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public Sesnors_Manager() {
    }

    public Sesnors_Manager setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        return this;
    }

    public Sensor getAccSensor() {
        return accSensor;
    }

    public Sesnors_Manager setAccSensor(Sensor accSensor) {
        this.accSensor = accSensor;
        return this;
    }

    public void initSensor() {
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

}
