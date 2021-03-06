package com.example.muneebahmad.edwbqfgb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleProductLayout extends AppCompatActivity {

    String product_name,product_weight,product_type;
    int product_id,product_barcode,product_price;
    ImageView product_image;
    TextView name,price,weight,category,order;
    Button minus_quantity,plus_quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_layout);

        Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id",0);
        product_name = intent.getStringExtra("product_name");
        product_barcode = intent.getIntExtra("product_barcode",0);
        product_price = intent.getIntExtra("product_price",0);
        product_weight = intent.getStringExtra("product_weight");
        product_type = intent.getStringExtra("product_type");

        product_image = findViewById(R.id.iv_product_image);
        name = findViewById(R.id.tv_product_name);
        price = findViewById(R.id.tv_product_price);
        weight = findViewById(R.id.tv_product_weight);
        category = findViewById(R.id.tv_product_category);
        minus_quantity = findViewById(R.id.btn_quantity_minus);
        plus_quantity = findViewById(R.id.btn_quantity_plus);
        order = findViewById(R.id.tv_quantity);
        setQuantity();

        name.setText(product_name);
        price.setText("PKR " + String.valueOf(product_price));
        weight.setText(product_weight);
        category.setText(product_type);
        minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order.getInstance().decrementQuantity(product_id);
                setQuantity();
                }
        });

        plus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order.getInstance().addOrIncrement(product_id);
                setQuantity();
            }
        });
    }

    private void setQuantity() {
        int quantity = Order.getInstance().getQuantity(product_id);
        if(quantity > 0 ) {
            order.setText(String.valueOf(quantity));
        }else {
            order.setText("ADD TO CART");
        }
    }
}