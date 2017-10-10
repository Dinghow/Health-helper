package com.example.healthhelper.function;

/**
 * Created by user on 2017/10/5.
 */

public class Function {
    private String name;
    private int imageId;

    public Function(String name, int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
