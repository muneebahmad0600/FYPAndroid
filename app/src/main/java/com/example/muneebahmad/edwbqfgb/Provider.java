package com.example.muneebahmad.edwbqfgb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Provider extends AppCompatActivity {
    ListView listView;
    ArrayList<OrderDetails> order = new ArrayList<>();
    OrderAdapter orderAdapter;
    ProgressDialog loading;
    TextView store_name, addr, starttime, endtime, deliveryfee;
    Button refresh;
    RatingBar rating;
    ImageView img;
    int provider;
    Context context = this;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        listView = findViewById(R.id.lv_product_list);
        store_name = findViewById(R.id.tv_store_name);
        addr = findViewById(R.id.tv_address);
        starttime = findViewById(R.id.tv_starttime_value);
        endtime = findViewById(R.id.tv_endtime_value);
        deliveryfee = findViewById(R.id.tv_fee_value);
        rating = findViewById(R.id.rb_store_rating);
        img = findViewById(R.id.civ_store_logo);
        refresh = findViewById(R.id.btn_order_update);

        final Intent intent = getIntent();
        provider = intent.getIntExtra("provider_id",0);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://localhost:3000/order/list.json";
                loading = ProgressDialog.show(Provider.this, "Please wait", "Getting data from server", false, false);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response.trim());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                OrderDetails orderDetails = new OrderDetails();

                                orderDetails.setOrder_id(jsonObject.optInt("id"));
                                orderDetails.setUser_id(jsonObject.optInt("user_id"));
                                orderDetails.setProvider_id(jsonObject.optInt("provider_id"));
                                orderDetails.setProduct_id(jsonObject.optInt("product_id"));
                                orderDetails.setQuantity(jsonObject.optInt("quantity"));
                                orderDetails.setBill(jsonObject.optInt("bill"));
                                orderDetails.setStatus(jsonObject.optString("status"));

                                order.add(orderDetails);
                            }
                            orderAdapter = new OrderAdapter(context, R.layout.order_list_layout, order);
                            listView.setAdapter((ListAdapter) orderAdapter);

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
                        map.put("id",String.valueOf(provider));
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);
            }
        });

        String Url = "http://localhost:3000/provider/show.json";
        loading = ProgressDialog.show(Provider.this, "Please wait...", "Getting Data From Server ...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try
                {
                    JSONObject jsonObject = new JSONObject(response.trim());
                    JSONObject con = jsonObject.getJSONObject("provider");

                        Store store = new Store();

                        store.setStore_id(con.optInt("id"));
                        store.setStore_name(con.optString("provider_name", ""));
                        store.setAddress(con.optString("location", ""));
                        store.setRating(con.optInt("provider_ranking"));
                        store.setDelivery_start_time(con.optString("delivery_start_time", ""));
                        store.setDelivery_end_time(con.optString("delivery_end_time", ""));
                        store.setDelivery_fee(con.optString("delivery_charges", ""));
                        store.setStore_latitude(con.optDouble("latitude"));
                        store.setStore_longitude(con.optDouble("longitude"));

                        store_name.setText(store.getStore_name());
                        addr.setText(store.getAddress());
                        starttime.setText(store.getDelivery_start_time());
                        endtime.setText(store.getDelivery_end_time());
                        rating.setRating(store.getRating());

//                        Intent service = new Intent(Provider.this,MyService.class);
//                        context.startService(service);

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id",String.valueOf(provider));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Provider.this);
        requestQueue.add(request);


    }
}
