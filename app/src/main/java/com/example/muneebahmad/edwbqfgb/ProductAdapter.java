package com.example.muneebahmad.edwbqfgb;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Context;
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

public class ProductAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<Product> item;
    LayoutInflater layoutInflater;


    //contructor
    public ProductAdapter(@NonNull Context context, int resource, ArrayList<Product> Item) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
        this.item = Item;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {return item.size();}


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = layoutInflater.inflate(resource,parent,false);
        ImageView imageView = v.findViewById(R.id.iv_product_image);
        TextView name = v.findViewById(R.id.tv_product_name);
        TextView category = v.findViewById(R.id.tv_product_category);
        TextView prize = v.findViewById(R.id.tv_product_prize);

        Product product = item.get(position);

        imageView.setImageResource(R.drawable.target_logo);
        name.setText(product.getProduct_name());
        category.setText(product.getProduct_type());
        prize.setText(product.getProduct_prize());

        return v;
    }
}
