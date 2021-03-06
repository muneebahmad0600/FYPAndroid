package com.example.muneebahmad.edwbqfgb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import java.util.zip.Inflater;
import android.widget.RatingBar;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;

public class ProductAdapter extends ArrayAdapter {

    private ImageView imageView;
    private TextView name,category,price;
    Context context;
    int resource,store_id;
    ArrayList<Product> item;
    LayoutInflater layoutInflater;

    public ProductAdapter(@NonNull Context context, int resource, ArrayList<Product> Item,int store_id) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
        this.item = Item;
        this.store_id = store_id;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {return item.size();}

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = layoutInflater.inflate(resource,parent,false);
        imageView = v.findViewById(R.id.iv_product_image);
        name = v.findViewById(R.id.tv_product_name);
        category = v.findViewById(R.id.tv_product_category);
        price = v.findViewById(R.id.tv_price);


        Product product = item.get(position);

//        imageView.setImageResource(R.drawable.shopping_cart);
        name.setText(product.getProduct_name());
        category.setText(product.getProduct_type());
        price.setText(String.valueOf(product.getProduct_price()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//              Toast.makeText(context,"position"+position,Toast.LENGTH_SHORT).show();
                Product product = item.get(position);
                int product_id = product.getProduct_id();
                String product_name = product.getProduct_name();
                int product_barcode = product.getProduc_barcode();
                int poduct_price = product.getProduct_price();
                String poduct_weight = product.getProduct_size();
                String poduct_type = product.getProduct_type();

                Intent intent = new Intent(context,SingleProductLayout.class);
                intent.putExtra("product_id",product_id);
                intent.putExtra("product_name",product_name);
                intent.putExtra("product_barcode",product_barcode);
                intent.putExtra("product_price",poduct_price);
                intent.putExtra("product_weight",poduct_weight);
                intent.putExtra("product_type",poduct_type);
                context.startActivity(intent);
            }
        });
        return v;
    }
}