package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;

import org.json.JSONException;



public class viewadminrequest extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String url;
    ListView l2;
    SharedPreferences sh;
    ArrayList<String> title,author,price,date,quantity,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewadminrequest);
        l2 = (ListView) findViewById(R.id.rlv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/viewrequest";
        RequestQueue queue = Volley.newRequestQueue(viewadminrequest.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    title = new ArrayList<>();
                    author = new ArrayList<>();
                    price = new ArrayList<>();
                    date = new ArrayList<>();
                    quantity = new ArrayList<>();
                    id = new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        title.add(jo.getString("title"));
                        author.add(jo.getString("author"));
                        price.add(jo.getString("price"));
                        date.add(jo.getString("date"));
                        quantity.add(jo.getString("qty"));
                        id.add(jo.getString("id"));

                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(p_viewbook.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    l2.setAdapter(new Custom2(viewadminrequest.this,title,author,price,quantity,date));
                    l2.setOnItemClickListener(viewadminrequest.this);


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(viewadminrequest.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pid",sh.getString("lid",""));

                return params;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



        AlertDialog.Builder ald=new AlertDialog.Builder(viewadminrequest.this);
        ald.setTitle("Choose an option for request")
                .setPositiveButton(" Accept ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        RequestQueue queue = Volley.newRequestQueue(viewadminrequest.this);
//                        url = "http://" + sh.getString("ip","") + ":5000/acceptrequest";
//
//                        // Request a string response from the provided URL.
//                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                // Display the response string.
//                                Log.d("+++++++++++++++++", response);
//                                try {
//                                    JSONObject json = new JSONObject(response);
//                                    String res = json.getString("task");
//
//
//                                    if (res.equalsIgnoreCase("success")) {
//                                        Toast.makeText(viewadminrequest.this, "Request accepted", Toast.LENGTH_SHORT).show();
//
//
//                                    } else {
//
////                                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e("ERR","",error);
//                                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
//                            }
//                        }) {
//                            @Override
//                            protected Map<String, String> getParams() {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("id", id.get(i));
//
//
//                                return params;
//                            }
//                        };
//                        queue.add(stringRequest);
                        Intent ik=new Intent(getApplicationContext(),Deliverydate.class);

                        ik.putExtra("id",id.get(i));

                        startActivity(ik);


                    }
                })
                .setNegativeButton(" Reject ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        RequestQueue queue = Volley.newRequestQueue(viewadminrequest.this);
                        url = "http://" + sh.getString("ip","") + ":5000/rejectrequest";

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
                                        Toast.makeText(viewadminrequest.this, "Request rejected", Toast.LENGTH_SHORT).show();


                                    } else {

//                                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERR","",error);
                                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", id.get(i));


                                return params;
                            }
                        };
                        queue.add(stringRequest);

                    }
                });

        AlertDialog al=ald.create();
        al.show();


    }
}