package com.example.muneebahmad.edwbqfgb;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class consumer_signup extends AppCompatActivity {

    TextView firstname;
    TextView lastname;
    TextView username;
    TextView password;
    TextView message;
    TextView confirmpassword;
    Button signup;
    String fname, lname, uname, pword, cnfrmpword, user_type = "consumer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_signup);

        firstname = findViewById(R.id.et_firstname);
        lastname = findViewById(R.id.et_lastname);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        confirmpassword = findViewById(R.id.et_confirmpassword);
        signup = findViewById(R.id.btn_signup);
        message = findViewById(R.id.tv_message);
        final String Url = "http://localhost:3000/users/create.json";

        signup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                fname = firstname.getText().toString();
                lname = lastname.getText().toString();
                uname = username.getText().toString();
                pword = password.getText().toString();
                cnfrmpword = confirmpassword.getText().toString();

                if (fname.isEmpty()) message.setText("Please enter you first name");
                else if (lname.isEmpty()) message.setText("Please enter you Last name");
                else if (uname.isEmpty()) message.setText("Please enter you User name");
                else if (pword.isEmpty()) message.setText("Please enter a Password");
                else if (cnfrmpword.isEmpty() || !cnfrmpword.equals(pword))
                    message.setText("Both Passwords should match");
                else {
                    Toast.makeText(consumer_signup.this, "Signing up", Toast.LENGTH_LONG).show();
                    RequestQueue requestQueue = Volley.newRequestQueue(consumer_signup.this);
                    StringRequest request = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent();
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    Notification notification = new Notification.Builder(consumer_signup.this)
                                            .setContentTitle("Grocerry Cloud")
                                            .setContentText("You have successfully signed up")
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setAutoCancel(true)
                                            .build();
                                    notificationManager.notify(4129, notification);
                                    setResult(1, intent);
                                    finish();
                                }
                            }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
                            map.put("first_name",fname);
                            map.put("last_name",lname);
                            map.put("user_name",uname);
                            map.put("password",pword);
                            map.put("user_type",user_type);
                            return map;
                        }
                    };

                    requestQueue.add(request);
                }

            }
        });
    }
}