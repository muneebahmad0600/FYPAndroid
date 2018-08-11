package com.example.muneebahmad.edwbqfgb;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import android.app.ProgressDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import android.view.LayoutInflater;
import android.widget.Toast;

public class StoreList extends AppCompatActivity
{
    ListView listView;
    final ArrayList<Store> shop = new ArrayList<>();
    ArrayList<Store> filtered_stores = new ArrayList<>();
    MyAdapter myAdapter;
    ProgressDialog loading;
    double longitude , latitude ;
    private LocationManager manager;
    final Looper looper = null;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        listView = findViewById(R.id.lv_store_list);
        manager = (LocationManager) getSystemService(context.LOCATION_SERVICE);

            String Url = "http://localhost:3000/provider/list.json";
            loading = ProgressDialog.show(StoreList.this, "Please wait...", "Getting Data From Server ...", false, false);
            StringRequest request = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    try {
                        JSONArray jsonArray = new JSONArray(response.trim());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Store store = new Store();

                            store.setStore_id(jsonObject.optInt("id"));
                            store.setStore_name(jsonObject.optString("provider_name", ""));
                            store.setAddress(jsonObject.optString("location", ""));
                            store.setRating(jsonObject.optInt("provider_ranking"));
                            store.setDelivery_start_time(jsonObject.optString("delivery_start_time", ""));
                            store.setDelivery_end_time(jsonObject.optString("delivery_end_time", ""));
                            store.setDelivery_fee(jsonObject.optString("delivery_charges", ""));
                            store.setStore_latitude(jsonObject.optDouble("latitude"));
                            store.setStore_longitude(jsonObject.optDouble("longitude"));
                            shop.add(store);
                        }
                        if (ActivityCompat.checkSelfPermission(StoreList.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(StoreList.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(StoreList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.INTERNET}, 1);
                            return;
                        }
                        final Criteria criteria = new Criteria();
                        criteria.setAccuracy(criteria.ACCURACY_COARSE);
                        criteria.setPowerRequirement(criteria.POWER_LOW);
                        criteria.setAltitudeRequired(false);
                        criteria.setBearingRequired(false);
                        criteria.setSpeedRequired(false);
                        criteria.setCostAllowed(true);
                        criteria.setHorizontalAccuracy(criteria.ACCURACY_HIGH);
                        criteria.setVerticalAccuracy(criteria.ACCURACY_HIGH);
                        final LocationListener locationListener = new LocationListener() {
                            public void onLocationChanged(Location location) {
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                                for (int i = 0; i < shop.size(); i++) {
                                    Store store1 = shop.get(i);
                                    double meters = distance_in_meter(latitude, longitude, store1.getStore_latitude(), store1.getStore_longitude());
                                    if (meters / 1000 <= 5) filtered_stores.add(store1);
                                }
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                            }

                            @Override
                            public void onProviderEnabled(String s) {
                            }

                            @Override
                            public void onProviderDisabled(String s) {
                            }
                        };
                        manager.requestSingleUpdate(criteria, locationListener, looper);
                        myAdapter = new MyAdapter(StoreList.this, R.layout.row, filtered_stores);
                        listView.setAdapter((ListAdapter) myAdapter);

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
                    Map<String, String> map = new HashMap<String, String>();
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(StoreList.this);
            requestQueue.add(request);
    }
    private static double distance_in_meter ( double lat1, double lon1, double lat2, double lon2)
    {
        double R = 6371000f; // Radius of the earth in m
        double dLat = (lat1 - lat2) * Math.PI / 180f;
        double dLon = (lon1 - lon2) * Math.PI / 180f;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180f) * Math.cos(lat2 * Math.PI / 180f) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2f * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }
}