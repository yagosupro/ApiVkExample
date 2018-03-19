package com.example.bob_book.apivkexample;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bob_book.apivkexample.Model.Attachment;
import com.example.bob_book.apivkexample.Model.Item;
import com.example.bob_book.apivkexample.Model.Photo;
import com.example.bob_book.apivkexample.Realm.ItemRealm;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Roman Parkhomenko on 3/12/2018.
 * Sibers company
 * yagosupro@gmail.com
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Serializable {

    List<ItemRealm> itemRealmList=new ArrayList<>();
//    List<Item> items = new ArrayList<>();

    private OnItemClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Обьявляем поля которые будем заполнять
        TextView timeView;
        TextView textView;
        ImageView imageView;
        CardView rv;
        View fl;
        private OnItemClickListener mListener;


        public ViewHolder(final View itemView) {
            super(itemView);

            rv = (CardView) itemView.findViewById(R.id.rv);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            timeView = (TextView) itemView.findViewById(R.id.timeView);
            itemView.setOnClickListener(this);
            fl = itemView.findViewById(R.id.frameLayout);

//            fl.setOnTouchListener(new OnSwipeTouchListener(itemView.getContext()) {
//                @Override
//                public void onSwipeLeft() {
//                    super.onSwipeLeft();
//
//
//                }
//            });


        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            itemClick(position);
            if (mListener != null) {
                mListener.onItemClick(position);
            }

    }


    private void itemClick(int position) {
        System.out.println("Position");
        System.out.println(position);
        System.out.println(textView.length());
    }


    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

}

    public Adapter(List<ItemRealm> itemRealmList, OnItemClickListener listener) {
        mListener = listener;

        this.itemRealmList=itemRealmList;
//        for (int i=itemRealmList.size()-1;i>=0;i--){

//            this.itemRealmList.add(new ItemRealm(itemRealmList.get(i).getDate(),itemRealmList.get(i).getText(),itemRealmList.get(i).getPhotoUrl604(),itemRealmList.get(i).getPhotoUrl807()));
//        }

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setListener(mListener);

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
    }

public interface OnItemClickListener {
    void onItemClick(int position);
}
}
