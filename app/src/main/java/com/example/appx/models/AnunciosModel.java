package com.example.appx.models;

public class AnunciosModel {
    String name, desc, imgurl, id, sms;

    public AnunciosModel() {

    }

    public AnunciosModel(String name, String desc, String imgurl, String id) {
        this.name = name;
        this.desc = desc;
        this.imgurl = imgurl;
        this.id = id;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
