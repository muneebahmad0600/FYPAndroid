package com.example.muneebahmad.edwbqfgb;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.drm.DrmManagerClient;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaSync;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    ListView listView;
    ArrayList<Product> Item = new ArrayList<>();
    ProductAdapter productAdapter;
    ProgressDialog loading;
    TextView store, addr, starttime, endtime, deliveryfee;
    Button checkout;
    RatingBar rating;
    ImageView img;
    Context context = this;
    SharedPreferences sharedPreferences;
    JSONObject order;
    JSONArray ordered_products;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        final int store_id = Integer.parseInt(intent.getStringExtra("store_id"));
        String store_name = intent.getStringExtra("store_name");
        String address = intent.getStringExtra("store_address");
        int store_rating = Integer.parseInt(intent.getStringExtra("store_rating"));
        String start_time = intent.getStringExtra("start_time");
        String end_time = intent.getStringExtra("end_time");
        String delivery_fee = intent.getStringExtra("delivery_fee");

        listView = findViewById(R.id.lv_product_list);
        store = findViewById(R.id.tv_store_name);
        addr = findViewById(R.id.tv_address);
        starttime = findViewById(R.id.tv_starttime_value);
        endtime = findViewById(R.id.tv_endtime_value);
        deliveryfee = findViewById(R.id.tv_fee_value);
        rating = findViewById(R.id.rb_store_rating);
        img = findViewById(R.id.civ_store_logo);
        checkout = findViewById(R.id.tv_check_out);

        store.setText(store_name);
        addr.setText(address);
        rating.setRating(store_rating);
        starttime.setText(start_time);
        endtime.setText(end_time);
        deliveryfee.setText(delivery_fee);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Number,Number> map = Order.getInstance().getPqMap();
                if (map != null) {
                    Iterator iterator = map.entrySet().iterator();
                    order = new JSONObject();
                    ordered_products = new JSONArray();
                    try {
                        order.put("user_id", sharedPreferences.getInt("user_id", 0));
                        order.put("store_id", store_id);
                        for (int i = 0; iterator.hasNext(); i++) {
                            JSONObject product = new JSONObject();
                            Map.Entry pair = (Map.Entry) iterator.next();
                            product.put("product_id", pair.getKey());
                            product.put("quantity", pair.getValue());
                            iterator.remove();
                            ordered_products.put(i, product);
                    }
                        order.put("ordered_product", ordered_products);
                        Toast.makeText(context,"order set",Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String orderurl = "localhost:3000/order/new.json";
                RequestQueue queue = Volley.newRequestQueue(Main2Activity.this);
                queue.getCache().clear();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, orderurl, order
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d(TAG, "Error: " + error.getMessage());
//                        hideProgressDialog();
                    }
                });
//                {
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        headers.put("Content-Type", "application/json; charset=utf-8");
//                        return headers;
//                    }
//                };
                queue.add(jsonObjectRequest);
            }
        });
        String url = "http://localhost:3000/provider_product/list.json";
        loading = ProgressDialog.show(Main2Activity.this, "Please wait", "Getting data from server", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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
                    productAdapter = new ProductAdapter(context, R.layout.product, Item, store_id);
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
                map.put("id", String.valueOf(store_id));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Order.getInstance().clear();
    }
}
