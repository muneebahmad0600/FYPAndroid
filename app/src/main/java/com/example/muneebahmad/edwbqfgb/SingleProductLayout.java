package com.example.muneebahmad.edwbqfgb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleProductLayout extends AppCompatActivity {

    String product_id,product_name,product_barcode,product_price,product_weight,product_type;
    ImageView product_image;
    TextView name,price,weight,category;
    Button minus_quantity,plus_quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_layout);

        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        product_name = intent.getStringExtra("product_name");
        product_barcode = intent.getStringExtra("product_barcode");
        product_price = intent.getStringExtra("product_price");
        product_weight = intent.getStringExtra("product_weight");
        product_type = intent.getStringExtra("product_type");

        product_image = findViewById(R.id.iv_product_image);
        name = findViewById(R.id.tv_product_name);
        price = findViewById(R.id.tv_product_price);
        weight = findViewById(R.id.tv_product_weight);
        category = findViewById(R.id.tv_product_category);
        minus_quantity = findViewById(R.id.btn_quantity_minus);
        plus_quantity = findViewById(R.id.btn_quantity_plus);

        name.setText(product_name);
        price.setText(product_price);
        weight.setText(product_weight);
        category.setText(product_type);
        minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        plus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}