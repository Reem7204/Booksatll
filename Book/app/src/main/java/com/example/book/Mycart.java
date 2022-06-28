package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mycart extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener  {
    String url,pmid;
    ListView l2;
    SharedPreferences sh;
    ArrayList<String> title, author, price, date, quantity, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);
        l2 = (ListView) findViewById(R.id.clv);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = "http://" + sh.getString("ip", "") + ":5000/viewmycart";
        RequestQueue queue = Volley.newRequestQueue(Mycart.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);
                    title = new ArrayList<>();
                    author = new ArrayList<>();
                    price = new ArrayList<>();
                    date = new ArrayList<>();
                    quantity = new ArrayList<>();
                    id = new ArrayList<>();


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        title.add(jo.getString("title"));
                        author.add(jo.getString("author"));
                        price.add(jo.getString("price"));
                        date.add(jo.getString("date"));
                        quantity.add(jo.getString("quantity"));
                        id.add(jo.getString("pm_id"));

                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(p_viewbook.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    l2.setAdapter(new Custom2(Mycart.this, title, author, price, quantity, date));

                    l2.setOnItemClickListener(Mycart.this);


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Mycart.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pid", sh.getString("lid", ""));

                return params;
            }
        };
        queue.add(stringRequest);


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder ald = new AlertDialog.Builder(Mycart.this);
        ald.setTitle("Do you want to cancel the order")
                .setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        RequestQueue queue = Volley.newRequestQueue(Mycart.this);
                        url = "http://" + sh.getString("ip", "") + ":5000/cancelbookorder";

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
                                        Toast.makeText(Mycart.this, "Order cancelled", Toast.LENGTH_SHORT).show();


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
                                Log.e("ERR", "", error);
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
                })
                .setNegativeButton(" No ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(Mycart.this, "Order not cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        pmid=id.get(i);




    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

