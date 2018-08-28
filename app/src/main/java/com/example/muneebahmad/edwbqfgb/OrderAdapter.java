package com.example.muneebahmad.edwbqfgb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter {

    private TextView user_name,address,price,status;
    Context context;
    int resource;
    ArrayList<OrderDetails> order;
    LayoutInflater layoutInflater;

    public OrderAdapter(@NonNull Context context, int resource, ArrayList<OrderDetails> order) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
        this.order = order;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {return order.size();}


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = layoutInflater.inflate(resource,parent,false);
        user_name = v.findViewById(R.id.tv_user_name);
        address = v.findViewById(R.id.tv_addr);
        price = v.findViewById(R.id.tv_price);
        status = v.findViewById(R.id.tv_status);

        OrderDetails orderDetails = order.get(position);

        user_name.setText(String.valueOf(orderDetails.getUser_id()));
        address.setText("ADDRESS");
        price.setText("Total Bill");
        status.setText(orderDetails.getStatus());

//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//              Toast.makeText(context,"position"+position,Toast.LENGTH_SHORT).show();
//                OrderDetails orderDetails = order.get(position);
//                int product_id = product.getProduct_id();
//                String product_name = product.getProduct_name();
//                int product_barcode = product.getProduc_barcode();
//                int poduct_price = product.getProduct_price();
//                String poduct_weight = product.getProduct_size();
//                String poduct_type = product.getProduct_type();
//
//                Intent intent = new Intent(context,SingleProductLayout.class);
//                intent.putExtra("product_id",product_id);
//                intent.putExtra("product_name",product_name);
//                intent.putExtra("product_barcode",product_barcode);
//                intent.putExtra("product_price",poduct_price);
//                intent.putExtra("product_weight",poduct_weight);
//                intent.putExtra("product_type",poduct_type);
//                context.startActivity(intent);
//            }
//        });
        return v;
    }
}
