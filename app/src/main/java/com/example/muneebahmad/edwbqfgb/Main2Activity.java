package com.example.muneebahmad.edwbqfgb;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    ListView listView;
    ArrayList<Product> Item =new ArrayList<>();
    ProductAdapter myAdapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = findViewById(R.id.lv_product_list);
        getitems();
    }

    public void getitems() {
        String url = "http://localhost:3000/product/list.json";
        loading = ProgressDialog.show(Main2Activity.this,"Please wait","Getting data from server",false,false);
        StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try
                {
                    JSONArray jsonArray = new JSONArray(response.trim());
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Product product = new Product();

                        product.setProduct_id(jsonObject.optInt("id"));
                        product.setProduct_name(jsonObject.optString("product_name", ""));
                        product.setProduc_barcode(jsonObject.optInt("product_barcode"));
                        product.setProduct_prize(jsonObject.optInt("product_prize"));
                        product.setProduct_size(jsonObject.optString("product_size",""));
                        product.setProduct_type(jsonObject.optString("product_type",""));
                        product.setProduct_color(jsonObject.optString("product_color",""));


                        Item.add(product);
                    }

                    myAdapter = new ProductAdapter(Main2Activity.this,R.layout.product,Item);
                    listView.setAdapter((ListAdapter) myAdapter);


                } catch (JSONException e)
                {
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

        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        requestQueue.add(request);

    }
}
