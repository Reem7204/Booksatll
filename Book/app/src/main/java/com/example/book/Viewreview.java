package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class Viewreview extends AppCompatActivity {
    String url,bid,r;
    ListView lv;
    EditText e1;
    SharedPreferences sh;
    ArrayList<String> name,date,review;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewreview);
        lv = (ListView) findViewById(R.id.lvrv);
        b1 = findViewById(R.id.button9);
        e1 = findViewById(R.id.etreview);
        bid=getIntent().getStringExtra("bid");
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/viewreview";
        RequestQueue queue = Volley.newRequestQueue(Viewreview.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    name = new ArrayList<>();
                    date = new ArrayList<>();
                    review = new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        name.add(jo.getString("fname"));
                        date.add(jo.getString("date"));
                        review.add(jo.getString("review"));
//                        image.add(jo.getString("pic1"));
//                        image2.add(jo.getString("pic2"));
//                        isbn.add(jo.getString("isbn"));
//                        description.add(jo.getString("description"));
//                        publishdate.add(jo.getString("publishdate"));
//                        genre.add(jo.getString("genre"));
//                        pageno.add(jo.getString("pageno"));
//                        id.add(jo.getString("id"));

                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(p_viewbook.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    lv.setAdapter(new Custom3(Viewreview.this,name,review,date));
//                    lv.setOnItemClickListener(Viewreview.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Viewreview.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pid",sh.getString("lid",""));
                params.put("bid",bid);

                return params;
            }
        };
        queue.add(stringRequest);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r = e1.getText().toString();
                if (r.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter your review");
                }
                else {


                    RequestQueue queue = Volley.newRequestQueue(Viewreview.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/addreview";

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
                                    Toast.makeText(Viewreview.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                }
                                else if (res.equalsIgnoreCase("exist")) {
//                                    Toast.makeText(register1.this, "Already exists", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    Toast.makeText(Viewreview.this, "Unable to register", Toast.LENGTH_SHORT).show();

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
                            params.put("bid",bid);
                            params.put("review", r);



                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });
    }


}