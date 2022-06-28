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

public class Suggestions extends AppCompatActivity {
EditText e1;
Button b1;
SharedPreferences sh;
String s,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        e1 = findViewById(R.id.sugtn);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1 = findViewById(R.id.button10);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = e1.getText().toString();
                if (s.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter your suggestion");
                }
                else {


                    RequestQueue queue = Volley.newRequestQueue(Suggestions.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/addsuggestion";

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
                                    Toast.makeText(Suggestions.this, "Successfully sent", Toast.LENGTH_SHORT).show();
                                }
                                else if (res.equalsIgnoreCase("exist")) {
//                                    Toast.makeText(register1.this, "Already exists", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    Toast.makeText(Suggestions.this, "Unable to register", Toast.LENGTH_SHORT).show();

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
                            params.put("pid",sh.getString("lid",""));
                            params.put("s",s);




                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });
    }
}