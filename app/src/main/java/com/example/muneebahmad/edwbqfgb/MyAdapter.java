package com.example.muneebahmad.edwbqfgb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.widget.RatingBar;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class MyAdapter extends ArrayAdapter
{
    private TextView name,addr,start_time,end_time,delivery_fee ;
    RatingBar rating;
    ImageView pic;
    Context context;
    int resource;
    ArrayList<Store> shop;
    LayoutInflater layoutInflater;

    //contructor
    public MyAdapter(@NonNull Context context, int resource, ArrayList<Store> shop) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
        this.shop = shop;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {return shop.size();}

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = layoutInflater.inflate(resource,parent,false);

        ImageView imageView = v.findViewById(R.id.civ_store_logo);
        name = v.findViewById(R.id.tv_store_name);
        addr = v.findViewById(R.id.tv_address);
        rating = v.findViewById(R.id.rb_store_rating);
        pic = v.findViewById(R.id.iv_store_pic);
        start_time = v.findViewById(R.id.tv_starttime_value);
        end_time = v.findViewById(R.id.tv_endtime_value);
        delivery_fee = v.findViewById(R.id.tv_fee_value);

        Store store = shop.get(position);

        imageView.setImageResource(R.drawable.target_logo);
        name.setText(store.getStore_name());
        addr.setText(store.getAddress());
        rating.setRating(store.getRating());
        pic.setImageResource(R.drawable.store_pic);
        start_time.setText(store.getDelivery_start_time());
        end_time.setText(store.getDelivery_end_time());
        delivery_fee.setText(store.getDelivery_fee());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String store_id,store_name,store_addr,delivery_start,delivery_end,fee,store_rating;
//                Toast.makeText(context,"position"+position,Toast.LENGTH_SHORT).show();
                Store store = shop.get(position);
                store_id = String.valueOf(store.getStore_id());
                store_name = store.getStore_name();
                store_addr = store.getAddress();
                delivery_start = store.getDelivery_start_time();
                delivery_end = store.getDelivery_end_time();
                fee = store.getDelivery_fee();
                store_rating = String.valueOf(store.getRating());

                Intent intent = new Intent(context,Main2Activity.class);
                intent.putExtra("store_id",store_id);
                intent.putExtra("store_name",store_name);
                intent.putExtra("store_address",store_addr);
                intent.putExtra("store_rating",store_rating);
                intent.putExtra("start_time",delivery_start);
                intent.putExtra("end_time",delivery_end);
                intent.putExtra("delivery_fee",fee);

                context.startActivity(intent);
            }
        });
        return v;
    }
}
