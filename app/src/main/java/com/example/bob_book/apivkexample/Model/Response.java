
package com.example.bob_book.apivkexample.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("items")
    @Expose
    public List<Item> items = null;

    public class ApiClass{
        public Response response;

    };

}
