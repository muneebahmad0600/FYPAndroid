package com.example.muneebahmad.edwbqfgb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    ListView listView;
    ArrayList<Product> Item = new ArrayList<>();
    ProductAdapter productAdapter;
    ProgressDialog loading;
    TextView store, addr, starttime, endtime, deliveryfee;
    RatingBar rating;
    ImageView img;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = findViewById(R.id.lv_product_list);

        Intent intent = getIntent();
        final int store_id = Integer.parseInt(intent.getStringExtra("store_id"));
        String store_name = intent.getStringExtra("store_name");
        String address = intent.getStringExtra("store_address");
        int store_rating = Integer.parseInt(intent.getStringExtra("store_rating"));
        String start_time = intent.getStringExtra("start_time");
        String end_time = intent.getStringExtra("end_time");
        String delivery_fee = intent.getStringExtra("delivery_fee");

        store = findViewById(R.id.tv_store_name);
        addr = findViewById(R.id.tv_address);
        starttime = findViewById(R.id.tv_starttime_value);
        endtime = findViewById(R.id.tv_endtime_value);
        deliveryfee = findViewById(R.id.tv_fee_value);
        rating = findViewById(R.id.rb_store_rating);
        img = findViewById(R.id.civ_store_logo);

        store.setText(store_name);
        addr.setText(address);
        rating.setRating(store_rating);
        starttime.setText(start_time);
        endtime.setText(end_time);
        deliveryfee.setText(delivery_fee);

        String url = "http://localhost:3000/provider_product/list.json";
        loading = ProgressDialog.show(Main2Activity.this, "Please wait", "Getting data from server", false, false);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response.trim());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Product product = new Product();

                        product.setProduct_id(jsonObject.optInt("id"));
                        product.setProduct_name(jsonObject.optString("product_name", ""));
                        product.setProduc_barcode(jsonObject.optInt("product_barcode"));
                        product.setProduct_price(jsonObject.optInt("product_price"));
                        product.setProduct_size(jsonObject.optString("product_weight", ""));
                        product.setProduct_type(jsonObject.optString("product_type", ""));

                        Item.add(product);
                    }
                    productAdapter = new ProductAdapter(context,R.layout.product,Item);
                    listView.setAdapter((ListAdapter) productAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }


        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                    map.put("id",String.valueOf(store_id));
                return map;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        requestQueue.add(request);

    }

}