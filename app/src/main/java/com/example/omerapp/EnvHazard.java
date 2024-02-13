package com.example.omerapp;

public class EnvHazard {
    private String id;
    private String title;
    private String type;
    private String urgency;
    private String time;
    private Double x;
    private Double y;
    private String userID;
    private String name;
    private String phone;
    private String img1;
    private String img2;
    private String img3;
    private Integer counter;

    public EnvHazard(String id, String title, String type, String urgency, String time, Double x, Double y, String userID, String name, String phone, String img1, String img2, String img3, Integer counter) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.urgency = urgency;
        this.time = time;
        this.x = x;
        this.y = y;
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.counter = counter;
    }

    public EnvHazard() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
