
package com.example.bob_book.apivkexample.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("from_id")
    @Expose
    public Integer fromId;
    @SerializedName("owner_id")
    @Expose
    public Integer ownerId;
    @SerializedName("date")
    @Expose
    public Integer date;
    @SerializedName("marked_as_ads")
    @Expose
    public Integer markedAsAds;
    @SerializedName("post_type")
    @Expose
    public String postType;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("signer_id")
    @Expose
    public Integer signerId;
    @SerializedName("attachments")
    @Expose
    public List<Attachment> attachments = null;
    @SerializedName("post_source")
    @Expose
    public PostSource postSource;
    @SerializedName("comments")
    @Expose
    public Comments comments;
    @SerializedName("likes")
    @Expose
    public Likes likes;
    @SerializedName("reposts")
    @Expose
    public Reposts reposts;


    public Item(int date, String text, Attachment attachment) {
        this.date=date;
        this.text=text;
        this.attachments= (List<Attachment>) attachment;
    }
}
