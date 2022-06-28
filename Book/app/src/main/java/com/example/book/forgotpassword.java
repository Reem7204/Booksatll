package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class forgotpassword extends AppCompatActivity {
    SharedPreferences sh;
    Button b1;
    EditText e1;
    String email, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        b1 = (Button) findViewById(R.id.fpsubmit);
        e1 = (EditText) findViewById(R.id.fp2);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = e1.getText().toString();
                if(email.equalsIgnoreCase("")){
                    e1.setError("Enter email address");
                }
else{
                    RequestQueue queue = Volley.newRequestQueue(forgotpassword.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/forgotpassword1";

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

                                    Intent ik = new Intent(getApplicationContext(), Login.class);
                                    startActivity(ik);
                                    Toast.makeText(forgotpassword.this, "Sented", Toast.LENGTH_SHORT).show();


                                } else {

                                    Toast.makeText(forgotpassword.this, "Invalid mailid", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERR", "", error);
                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("textfield", email);


                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });


    }
}