package com.geotec.mujersegura.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Movies {
    private long id;
    private String cve;
    private String name;
    private String desc;
    private String image;
    private String duration;
    private String clasification;
    private String genre;
    private String timestamp;

    public Movies() {}

    public Movies(long id, String cve, String name, String desc, String image, String duration, String clasification, String genre, String timestamp) {
        this.id = id;
        this.cve = cve;
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.duration = duration;
        this.clasification = clasification;
        this.genre = genre;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getClasification() {
        return clasification;
    }

    public void setClasification(String clasification) {
        this.clasification = clasification;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Bitmap getImageBitmap() {
        String base64Image = image.split(",")[1];
        byte[] decodeString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeByte;
    }
}
