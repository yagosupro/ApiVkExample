package com.example.bob_book.apivkexample;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bob_book.apivkexample.Model.EventBusModel.EventDate;
import com.example.bob_book.apivkexample.Model.EventBusModel.EventDialog;
import com.example.bob_book.apivkexample.Model.EventBusModel.EventMessage;
import com.example.bob_book.apivkexample.Model.Response;
import com.example.bob_book.apivkexample.Realm.ItemRealm;
import com.google.gson.Gson;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements Serializable {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String UrlGroup = "https://vk.com/act54_lox?w=wall-71302810_";
    private String[] scope = new String[]{VKScope.EMAIL, VKScope.FRIENDS, VKScope.WALL};
    private int item_count = 20;
    private RecyclerView rv;
    private Realm realm;
    Response.ApiClass apiClass;
    LinearLayoutManager llm;
    Button btnSave;
    Button btnLoad;
    Adapter adapter;

    private final String KEY_RECYCLER_STATE = "recycler_state";


    private Parcelable recyclerViewState;

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("onSaveInstanceState");
        recyclerViewState = rv.getLayoutManager().onSaveInstanceState();//save
        outState.putParcelable(KEY_RECYCLER_STATE,recyclerViewState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState");
        recyclerViewState=savedInstanceState.getParcelable(KEY_RECYCLER_STATE);

    }

    @Subscribe
    public void onEventClickSendDateToActivityTwo(EventDate event) {
        Intent i = new Intent(MainActivity.this, ActivityTwo.class);
        String innerDate = String.valueOf(event.date);
        i.putExtra(ActivityTwo.ITEM_KEY, innerDate);
        startActivity(i);
    }

    @Subscribe
    public void onEventLongClickShareUrl(EventMessage message) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message.message);
        startActivity(Intent.createChooser(shareIntent, "Share text")); //
    }


    @Subscribe
    public void onCreateCustomDialog(EventDialog message) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("FirstDialog");
        dialog.setContentView(R.layout.dialog_view);
        TextView textView = (TextView) dialog.findViewById(R.id.dialogTextView);
        Button buttonOk = (Button) dialog.findViewById(R.id.dialogOk);
        Button buttonCancel = (Button) dialog.findViewById(R.id.dialogCancel);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        textView.setText(message.message);

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_del_last:
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(ItemRealm.class).findAll().last(null).deleteFromRealm();
                    }
                });
                return true;
            case R.id.menu_refresh:
                callBackVk();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

//    public void draweCustom(Toolbar toolbar){
//        new Drawer()
//            .withActivity(this)
//            .withToolbar(toolbar)
//            .withActionBarDrawerToggle(true)
//            .withHeader(R.layout.drawer_header)
//            .addDrawerItems(
//                    new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("99").withIdentifier(1),
//                    new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
//                    new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("6").withIdentifier(2),
//                    new SectionDrawerItem().withName(R.string.drawer_item_settings),
//                    new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
//                    new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
//                    new DividerDrawerItem(),
//                    new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)
//            )
//            .build();
//
//    }

    ////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("start onCreate");

        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv);
        btnSave = (Button) findViewById(R.id.save);
        btnLoad = (Button) findViewById(R.id.load);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        draweCustom(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callBackVk();
                mSwipeRefreshLayout.setRefreshing(false);
                llm.scrollToPosition(0);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveAdapterToShPrefInJson(llm);
//                recyclerViewState = rv.getLayoutManager().onSaveInstanceState();//save
//                positionIndex=llm.findFirstVisibleItemPosition();
//                View startView=rv.getChildAt(0);
//                topView = (startView == null) ? 0 : (startView.getTop() - rv.getPaddingTop());

            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                lm=loadAdapterToShPrefInJson();

//                rv.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore
//                    llm.scrollToPositionWithOffset(positionIndex,topView);

            }
        });


        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.getVerticalScrollbarPosition();

        Realm.init(this);

        realm = Realm.getDefaultInstance();

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, scope);
        } else
            callBackVk();



    }

    void callBackVk() {

        final VKRequest vkRequest = new VKApiGroups().getById(VKParameters.from("group_ids", "act54_lox"));

        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                final VKList vklist = (VKList) response.parsedModel;
                VKRequest vkRequest1 = new VKApiWall().get(VKParameters.from(VKApiConst.OWNER_ID, -71302810, VKApiConst.COUNT, item_count));
                vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        System.out.println("EROOR response");

                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Gson gson = new Gson();
                        apiClass = gson.fromJson(response.responseString, Response.ApiClass.class);
                        inicializeRealmBase();
                        initializeAdapter(adapter);
                    }
                });
            }
        });

        inicializeRealmBase();
        initializeAdapter(adapter);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                    @Override
                    public void onResult(VKAccessToken res) {
                        callBackVk();
                    }

                    @Override
                    public void onError(VKError error) {
                        Toast.makeText(getApplicationContext(), "Error login", Toast.LENGTH_SHORT).show();
                    }
                }
        )) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initializeAdapter(Adapter adapter) {
        final RealmResults<ItemRealm> itemRealmList = realm.where(ItemRealm.class).findAll().sort("date", Sort.DESCENDING);
        this.adapter = new Adapter(itemRealmList);
        rv.setAdapter(this.adapter);
        //why it work only in here?
        rv.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore
    }

    private void inicializeRealmBase() {
        System.out.println("inicializeRealmBase Start");
        int tmpDate = 0;
        ItemRealm itemRealmLast = realm.where(ItemRealm.class).findAll().last(null);

        if (apiClass != null) {
            System.out.println("11111");
            for (int i = apiClass.response.items.size() - 1; i >= 0; i--) {
                tmpDate = apiClass.response.items.get(i).date;

                if (itemRealmLast != null) {

                    if (itemRealmLast.getDate() >= tmpDate) {
                        continue;
                    }
                }

                if (apiClass.response.items.get(i).attachments == null) {
                    System.out.println("Attachments=null elements: " + i);
                    continue;
                }
                if (apiClass.response.items.get(i).attachments.get(0).photo == null) {
                    System.out.println("photo is null Elements: " + i);
                    continue;
                }
                realm.beginTransaction();

                ItemRealm itemRealm = realm.createObject(ItemRealm.class);
                itemRealm.setDate(apiClass.response.items.get(i).date);
                itemRealm.setText(apiClass.response.items.get(i).text);
                itemRealm.setPhotoUrl604(apiClass.response.items.get(i).attachments.get(0).photo.photo604);
                itemRealm.setPhotoUrl807(apiClass.response.items.get(i).attachments.get(0).photo.photo807);
                System.out.println("URL_POSTS: " + UrlGroup + apiClass.response.items.get(i).id);
                itemRealm.setUrl(UrlGroup + apiClass.response.items.get(i).id);

                realm.commitTransaction();
            }
        }

        apiClass = null;

    }

}
