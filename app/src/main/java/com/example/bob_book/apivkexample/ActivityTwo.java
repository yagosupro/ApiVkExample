package com.example.bob_book.apivkexample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bob_book.apivkexample.Model.EventBusModel.EventMessage;
import com.example.bob_book.apivkexample.Model.EventBusModel.FirstTest;
import com.example.bob_book.apivkexample.Realm.ItemRealm;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class ActivityTwo extends AppCompatActivity implements View.OnClickListener {

    public static final String ITEM_KEY = "item";
    private ActionProvider mShareActionProvider;
    int date;
    LinearLayout ll;
    ItemRealm itemRealm;
    TextView textView;
    ImageView imageView;
    String URL;

    private Realm realm;
    private Context activity;


//    public void onEvent(EventMessage event) {
////        Toast.makeText(getActivity(), "Получено число:" + event, Toast.LENGTH_SHORT).show();
//        System.out.println("What we take this");
//        System.out.println(event);
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//
//
//        try {
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    //
//    @Override
//    protected void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_share:
                System.out.println("SHARE press");
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, URL); // Вместо My message упаковываете текст, который необходимо передать
                startActivity(Intent.createChooser(shareIntent, "Share text")); //
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    public void setShareIntent(Intent shareIntent) {
//        if (mShareActionProvider != null) {
//    mShareActionProvider.setShareIntent(shareIntent);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OnCreate activityTwo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Intent intent = getIntent();
        date = Integer.parseInt((intent.getStringExtra(ITEM_KEY)));

//
        Realm.init(this);
        realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        RealmResults<ItemRealm> itemRealms = realm.where(ItemRealm.class).equalTo("date",date).findAll();
        ItemRealm itemRealm = realm.where(ItemRealm.class).equalTo("date", date).findFirst();

//        realm.commitTransaction();
        ll = (LinearLayout) findViewById(R.id.ll);

        textView = new TextView(this);
        imageView = new ImageView(this);

        ll.addView(textView);
        ll.addView(imageView);

        imageView.setScaleType(CENTER_CROP);
        imageView.setImageResource(R.color.vk_black);
        imageView.setMinimumHeight(540);
        textView.setText(itemRealm.getText());
        URL = itemRealm.getUrl();

        if (itemRealm.getPhotoUrl807() != null) {
            Picasso.with(imageView.getContext()).load(itemRealm.getPhotoUrl807()).into(imageView);
            return;
        }

        if (itemRealm.getPhotoUrl604() != null) {
            Picasso.with(imageView.getContext()).load(itemRealm.getPhotoUrl604()).into(imageView);
            return;
        } else
            System.out.println("NO IMAGE");
        imageView.setOnClickListener(this);
    }


    public Context getActivity() {
        return activity;
    }

    @Override
    public void onClick(View v) {


    }
}
