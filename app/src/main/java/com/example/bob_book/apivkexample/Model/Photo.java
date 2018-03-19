
package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("album_id")
    @Expose
    public Integer albumId;
    @SerializedName("owner_id")
    @Expose
    public Integer ownerId;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("photo_75")
    @Expose
    public String photo75;
    @SerializedName("photo_130")
    @Expose
    public String photo130;
    @SerializedName("photo_604")
    @Expose
    public String photo604;
    @SerializedName("photo_807")
    @Expose
    public String photo807;
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("date")
    @Expose
    public Integer date;
    @SerializedName("post_id")
    @Expose
    public Integer postId;
    @SerializedName("access_key")
    @Expose
    public String accessKey;

    public Photo(String photoUrl604, String photoUrl807) {
        this.photo604=photoUrl604;
        this.photo807=photoUrl807;

    }
}
