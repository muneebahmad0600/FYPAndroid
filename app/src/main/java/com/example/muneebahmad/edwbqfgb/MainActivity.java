package com.example.muneebahmad.edwbqfgb;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.ListAdapter;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    EditText username;
    EditText password;
    Button login;
    TextView signup;
    String user;
    String pass;
    ArrayList<User> consumer = new ArrayList<>();
    ProgressDialog loading;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.tv_signup);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,consumer_signup.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();
                String url = "http://localhost:3000/users/userlogin.json";
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                loading = ProgressDialog.show(MainActivity.this, "Please wait...", "Getting Data From Server ...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try
                        {
                                JSONObject jsonObject = new JSONObject(response.trim());
                                JSONObject con = jsonObject.getJSONObject("user");

                                User userS = new User();
                                userS.setUser_id(con.optInt("id"));
                                userS.setUser_email(con.optString("email", ""));
                                userS.setUser_first_name(con.optString("first_name", ""));
                                userS.setUser_last_name(con.optString("last_name"));
                                userS.setUser_name(con.optString("user_name", ""));
                                userS.setUser_type(con.optString("user_type", ""));
                                userS.setUser_password(con.optString("crypted_password", ""));
                                userS.setProvider_id(con.optInt("provider_id"));


                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("user_id",userS.getUser_id());
                                editor.apply();

                                if(userS.getUser_type().equals("consumer"))
                                {
                                    Intent intent = new Intent(MainActivity.this, StoreList.class);
                                    startActivity(intent);
                                }
                                else if(userS.getUser_type().equals("provider")){
                                    Intent intent = new Intent(MainActivity.this, Provider.class);
                                    intent.putExtra("provider_id",userS.getProvider_id());
                                    startActivity(intent);
                                }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to the server...Please check your connection!";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                        else{
                            loading.dismiss();
                            message = "Incorrect user name or password...";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("user_name",user);
                        map.put("password",pass);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.kuchbi) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.stores) {
            Intent intent = new Intent(MainActivity.this,StoreList.class);
            startActivity(intent);
        } else if (id == R.id.orders) {

        } else if (id == R.id.location) {

        } else if (id == R.id.contactus) {

        } else if (id == R.id.settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}