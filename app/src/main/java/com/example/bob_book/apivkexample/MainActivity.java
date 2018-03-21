package com.example.bob_book.apivkexample;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bob_book.apivkexample.Model.Item;
import com.example.bob_book.apivkexample.Model.Response;
import com.example.bob_book.apivkexample.Realm.ItemRealm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.util.VKUtil;

import android.support.v4.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements Serializable {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActionProvider mShareActionProvider;
    private String UrlGroup = "https://vk.com/act54_lox?w=wall-71302810_";
    private String[] scope = new String[]{VKScope.EMAIL, VKScope.FRIENDS, VKScope.WALL};
    private ListView listVIew;
    private int item_count = 20;
    private RecyclerView rv;
    private Realm realm;


    Response.ApiClass apiClass;

    private ArrayList itemList = new ArrayList();

    ArrayAdapter arrayAdapter;

    //"id":-71302810
    //act54_lox


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("REFREEEEEESH");
                callBackVk();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(this);


        rv.setLayoutManager(llm);


        Realm.init(this);

        realm = Realm.getDefaultInstance();

        //or get Activity
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
                        System.out.println("Vhodim v onComplete response");
                        super.onComplete(response);
                        Gson gson = new Gson();
                        apiClass = gson.fromJson(response.responseString, Response.ApiClass.class);

//                        for (int i=0;i<apiClass.response.items.size();i++){
//                            System.out.println("iteration api class:"+i+" "+apiClass.response.items.get(i).date);
//                        }
                        inicializeRealmBase();
                        initializeAdapter();

                        //

                    }
                });
            }
        });
        Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();
        //Инциализирует Реал
        System.out.println("!!!!!!!!!!!!!");

        System.out.println("!!!!!!!!!!");
        inicializeRealmBase();
        initializeAdapter();


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
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                    }
                }
        )) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initializeAdapter() {
        final RealmResults<ItemRealm> itemRealmList = realm.where(ItemRealm.class).findAll().sort("date", Sort.DESCENDING);
//        List<ItemRealm> itemRealmList1=itemRealmList;
        Adapter adapter = new Adapter(itemRealmList,
                new Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent i = new Intent(MainActivity.this, ActivityTwo.class);
                        String innerDate = String.valueOf(itemRealmList.get(position).getDate());
                        i.putExtra(ActivityTwo.ITEM_KEY, innerDate);
// i.putExtra(ActivityTwo.ITEM_KEY,itemRealmList.get(position));

                        startActivity(i);
                        System.out.println();
                    }
                }, new Adapter.OnLongClickListener() {

            @Override
            public void onCreateMenuSelf(final int position, ContextMenu menu) {
                menu.add("delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println("we use delete context menu");
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, itemRealmList.get(position).getUrl());
                        startActivity(Intent.createChooser(shareIntent, "Share text")); //
                        return false;
                    }
                });
            }


        }
        );
        rv.setAdapter(adapter);

    }


    private void inicializeRealmBase() {
        System.out.println("inicializeRealmBase Start");

//        //ClearBase
//        realm.beginTransaction();
//        realm.deleteAll();
//        realm.commitTransaction();
        int tmpDate = 0;

        ItemRealm itemRealmLast = realm.where(ItemRealm.class).findAll().last(null);
        System.out.println("Last position:");

        if (apiClass != null) {
            System.out.println("11111");
            for (int i = apiClass.response.items.size() - 1; i >= 0; i--) {
                System.out.println("Iteration: " + i);
                //
                tmpDate = apiClass.response.items.get(i).date;

                if (itemRealmLast != null) {

                    if (itemRealmLast.getDate() >= tmpDate) {
                        System.out.println("we use BREAK " + i);
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
        RealmResults<ItemRealm> itemRealmsList = realm.where(ItemRealm.class).findAll().sort("date", Sort.DESCENDING);
        apiClass = null;
//        for (int i=0;i<itemRealmsList.size();i++){
//            System.out.println("item position: "+i+ " they id: "+itemRealmsList.get(i).getDate());
//
//        }
//
        //ClearBase
//        realm.beginTransaction();
//        realm.deleteAll();
//        realm.commitTransaction();


    }

}
