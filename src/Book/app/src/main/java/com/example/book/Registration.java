package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6, e7, e8, e9;
    Button b;
    String name, place, post, pin, phoneno, emailid, username, password, cpassword, url;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        e1 = (EditText) findViewById(R.id.name);
        e2 = (EditText) findViewById(R.id.place);
        e3 = (EditText) findViewById(R.id.post);
        e4 = (EditText) findViewById(R.id.pin);
        e5 = (EditText) findViewById(R.id.emailid);
        e6 = (EditText) findViewById(R.id.phoneno);
        e7 = (EditText) findViewById(R.id.username);
        e8 = (EditText) findViewById(R.id.password);
        e9 = (EditText) findViewById(R.id.cpassword);
        b = (Button) findViewById(R.id.button3);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = e1.getText().toString();
                place = e2.getText().toString();
                post = e3.getText().toString();
                pin = e4.getText().toString();
                emailid = e5.getText().toString();
                phoneno = e6.getText().toString();
                username = e7.getText().toString();
                password = e8.getText().toString();
                cpassword = e9.getText().toString();

                if (name.equalsIgnoreCase(""))
                {
                    e1.setError("Enter Name");
                }
                else if (place.equalsIgnoreCase(""))
                {
                    e2.setError("Enter place");
                }
                else if (post.equalsIgnoreCase(""))
                {
                    e3.setError("Enter post");
                }
                else if (pin.equalsIgnoreCase(""))
                {
                    e4.setError("Enter pin");
                }
                else if(pin.length()!=6)
                {
                    e4.setError("Pin must be a 6 digit number");
                    e4.requestFocus();
                }
                else if(emailid.equalsIgnoreCase(""))
                {
                    e5.setError("Enter Your Email");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches())
                {
                    e5.setError("Enter Valid Email");
                    e5.requestFocus();
                }
                else if(phoneno.equalsIgnoreCase(""))
                {
                    e6.setError("Enter Your Phone No");
                }
                else if(phoneno.length()<10)
                {
                    e6.setError("Must be a 10 digit number");
                    e6.requestFocus();
                }
                else if(username.equalsIgnoreCase(""))
                {
                    e7.setError("Enter Your username");
                }
                else if(password.equalsIgnoreCase(""))
                {
                    e8.setError("Enter Your password");
                }


                else {


                    RequestQueue queue = Volley.newRequestQueue(Registration.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/registrationcode";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");


                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(Registration.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(Registration.this, "Unable to register", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("uname", username);
                            params.put("pass", password);
                            params.put("name", name);
                            params.put("place", place);
                            params.put("post", post);
                            params.put("pin", pin);
                            params.put("emailid", emailid);
                            params.put("phoneno", phoneno);


                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });
    }
}