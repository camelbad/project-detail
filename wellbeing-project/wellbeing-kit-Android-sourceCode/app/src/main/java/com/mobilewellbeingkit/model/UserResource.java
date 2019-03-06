package com.mobilewellbeingkit.model;

public class UserResource {
    private int resourceId;
    private String text;


    // Constructors
    public UserResource(){
    }

    public UserResource(String text){ this.text = text;}

    public UserResource(String text, int id) {
        this.text = text;
        this.resourceId = id;
    }


    // Getters
    public String getResourceText() {
        return text;
    }

    public int getResourceId() {
        return resourceId;
    }

    // Setters
    public void setResourceText(String text) {
        this.text = text;
    }

    public void setResourceId(int id) {
        this.resourceId = id;
    }
}
