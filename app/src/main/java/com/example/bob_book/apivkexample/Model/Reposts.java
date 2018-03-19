
package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reposts implements Serializable {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("user_reposted")
    @Expose
    public Integer userReposted;

}
