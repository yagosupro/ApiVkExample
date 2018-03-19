
package com.example.bob_book.apivkexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostSource implements Serializable {

    @SerializedName("type")
    @Expose
    public String type;

}
