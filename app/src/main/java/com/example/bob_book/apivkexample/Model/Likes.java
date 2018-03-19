
package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Likes implements Serializable {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("user_likes")
    @Expose
    public Integer userLikes;
    @SerializedName("can_like")
    @Expose
    public Integer canLike;
    @SerializedName("can_publish")
    @Expose
    public Integer canPublish;

}
