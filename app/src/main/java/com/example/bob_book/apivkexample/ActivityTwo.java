package com.example.bob_book.apivkexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bob_book.apivkexample.Realm.ItemRealm;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class ActivityTwo extends AppCompatActivity {

    public static final String ITEM_KEY="item";

    int date;
    LinearLayout ll;
    ItemRealm itemRealm;
    TextView textView;
    ImageView imageView;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OnCreate activityTwo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Intent intent=getIntent();
        date= Integer.parseInt((intent.getStringExtra(ITEM_KEY)));

//
        Realm.init(this);
        realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        RealmResults<ItemRealm> itemRealms = realm.where(ItemRealm.class).equalTo("date",date).findAll();
        ItemRealm itemRealm=realm.where(ItemRealm.class).equalTo("date",date).findFirst();
        System.out.println(itemRealm.getDate());
//        realm.commitTransaction();
        ll= (LinearLayout) findViewById(R.id.ll);

        textView=new TextView(this);
        imageView=new ImageView(this);

        ll.addView(textView);
        ll.addView(imageView);

        imageView.setScaleType(CENTER_CROP);
        imageView.setImageResource(R.color.vk_black);
        imageView.setMinimumHeight(540);
        textView.setText(itemRealm.getText());
//        textView.setText(itemRealm.getText());
//
//
//
                if (itemRealm.getPhotoUrl807() != null) {
                    Picasso.with(imageView.getContext()).load(itemRealm.getPhotoUrl807()).into(imageView);
                    return;
                }

                if (itemRealm.getPhotoUrl604() != null) {
                    Picasso.with(imageView.getContext()).load(itemRealm.getPhotoUrl604()).into(imageView);
                    return;
                } else
                    System.out.println("NO IMAGE");









    }
}
