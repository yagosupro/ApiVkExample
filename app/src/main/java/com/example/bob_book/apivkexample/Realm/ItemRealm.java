package com.example.bob_book.apivkexample.Realm;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Roman Parkhomenko on 3/14/2018.
 * Sibers company
 * yagosupro@gmail.com
 */

public class ItemRealm extends RealmObject implements Serializable {

    private String text;
    private int date;
    private String photoUrl604;
    private String photoUrl807;

//    public ItemRealm(int date, String text, String photoUrl604, String photoUrl807) {
//        this.date=date;
//        this.text=text;
//        this.photoUrl604=photoUrl604;
//        this.photoUrl807=photoUrl807;
//    }


    public String getPhotoUrl604() {
        return photoUrl604;
    }

    public void setPhotoUrl604(String photoUrl604) {
        this.photoUrl604 = photoUrl604;
    }

    public String getPhotoUrl807() {
        return photoUrl807;
    }

    public void setPhotoUrl807(String photoUrl807) {
        this.photoUrl807 = photoUrl807;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

}
