package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

public class Addtocart extends AppCompatActivity {
    EditText e1;
    Button b1;
    SharedPreferences sh;
    String q,url,bid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtocart);
        e1 = findViewById(R.id.addcart3);
        b1 = findViewById(R.id.button11);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bid=getIntent().getStringExtra("bid");
                q = e1.getText().toString();
                if (q.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter the quantity");
                }
                else{
                    RequestQueue queue = Volley.newRequestQueue(Addtocart.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/addtocart";

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
                                    Toast.makeText(Addtocart.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                }
                                else if (res.equalsIgnoreCase("exist")) {
//                                    Toast.makeText(Acceptrequest.this, "Already exists", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    Toast.makeText(Addtocart.this, "Unable to register", Toast.LENGTH_SHORT).show();

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
                            params.put("bid", bid);
                            params.put("q", q);
                            params.put("lid",sh.getString("lid",""));



                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });

    }
}