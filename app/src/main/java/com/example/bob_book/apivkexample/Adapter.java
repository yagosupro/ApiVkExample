package com.example.bob_book.apivkexample;


import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bob_book.apivkexample.Model.EventBusModel.EventDate;
import com.example.bob_book.apivkexample.Model.EventBusModel.EventDialog;
import com.example.bob_book.apivkexample.Model.EventBusModel.EventMessage;
import com.example.bob_book.apivkexample.Realm.ItemRealm;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Roman Parkhomenko on 3/12/2018.
 * Sibers company
 * yagosupro@gmail.com
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Serializable {


    public List<ItemRealm> itemRealmList = new ArrayList<>();

    public Adapter(RealmResults<ItemRealm> itemRealmList) {
        this.itemRealmList = itemRealmList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        TextView timeView;
        TextView textView;
        ImageView imageView;
        CardView rv;
        Dialog dialog;

        public ViewHolder(final View itemView) {
            super(itemView);

            rv = (CardView) itemView.findViewById(R.id.rv);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            timeView = (TextView) itemView.findViewById(R.id.timeView);

        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static String returnDateString(Integer date) {
        Long longDate = Long.valueOf(date) * 1000;
        Date dateTmp = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM ' в' HH:mm:ss");
        return String.valueOf(dateFormat.format(dateTmp));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final int f = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int date = itemRealmList.get(f).getDate();
                EventBus.getDefault().post(new EventDate(date));
            }
        });

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("share").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String message = itemRealmList.get(position).getUrl();
                        EventBus.getDefault().post(new EventMessage(message));
                        return false;
                    }
                });
                menu.add("dialog custom").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println("dialog custom");

                        EventBus.getDefault().post(new EventDialog("TEST"));


                        return false;
                    }
                });
            }
        });
        holder.timeView.setText(returnDateString(itemRealmList.get(position).getDate()));
        holder.textView.setText(itemRealmList.get(position).getText());

        if (itemRealmList.get(position).getPhotoUrl807() != null) {
            Picasso.with(holder.imageView.getContext()).load(itemRealmList.get(position).getPhotoUrl807()).into(holder.imageView);
            return;
        }

        if (itemRealmList.get(position).getPhotoUrl604() != null) {
            Picasso.with(holder.imageView.getContext()).load(itemRealmList.get(position).getPhotoUrl604()).into(holder.imageView);
            return;
        } else
            System.out.println("NO IMAGE");

    }


    @Override
    public int getItemCount() {
        return itemRealmList.size();
    }}

