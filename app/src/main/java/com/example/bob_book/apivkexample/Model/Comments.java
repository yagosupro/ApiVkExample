
package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comments implements Serializable {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("can_post")
    @Expose
    public Integer canPost;

}
