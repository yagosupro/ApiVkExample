
package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attachment implements Serializable {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("photo")
    @Expose
    public Photo photo;

    public Attachment(Photo photo) {
        this.photo=photo;
    }
//
//    @SerializedName("video")
//    @Expose
//    public String video;

}
