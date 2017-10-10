package com.example.healthhelper;

/**
 * Created by user on 2017/10/7.
 */

public class medicine {
    private int id;
    private String medicineName;
    private int takenTimes;
    private String annotation;

    public medicine(int id, String medicineName, int takenTimes, String annotation){
        this.id=id;
        this.medicineName = medicineName;
        this.takenTimes = takenTimes;
        this.annotation = annotation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getTakenTimes() {
        return takenTimes;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setTakenTimes(int takenTimes) {
        this.takenTimes = takenTimes;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
