package com.example.muneebahmad.edwbqfgb;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class MyService extends Service {
    ListView listView;
    ArrayList<OrderDetails> order = new ArrayList<>();
    OrderAdapter orderAdapter;
    ProgressDialog loading;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String url = "http://localhost:3000/provider_product/list.json";
        loading = ProgressDialog.show(MyService.this, "Please wait", "Getting data from server", false, false);
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

                        order.add(orderDetails);
                    }
                    orderAdapter = new OrderAdapter(MyService.this, R.layout.order_list_layout, order);
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
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyService.this);
        requestQueue.add(request);
        return super.onStartCommand(intent, flags, startId);
    }
}
