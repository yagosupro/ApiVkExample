package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Roman Parkhomenko on 3/13/2018.
 * Sibers company
 * yagosupro@gmail.com
 */

public class Video implements Serializable {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("owner_id")
    @Expose
    public Integer owner_id;
    @SerializedName("title")
    @Expose
    public String title;

}
