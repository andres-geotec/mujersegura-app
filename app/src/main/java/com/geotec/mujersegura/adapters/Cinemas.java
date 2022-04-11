package com.geotec.mujersegura.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Cinemas {
    private long id;
    private String cve;
    private String name;
    private String desc;
    private String image;
    private String timestamp;

    public Cinemas() {}

    public Cinemas(long id, String cve, String name, String desc, String image, String timestamp) {
        this.id = id;
        this.cve = cve;
        this.name = name;
        this.desc = desc;
        this.image = image;
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
