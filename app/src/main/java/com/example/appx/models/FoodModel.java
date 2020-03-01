package com.example.appx.models;

public class FoodModel {
    String id, name, desc, img;
    int estatus;
    double lat, lon;

    public FoodModel() {
    }

    public FoodModel(String id, String name, String desc, String img, int estatus) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.estatus = estatus;
//        this.lat = lat;
//        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
