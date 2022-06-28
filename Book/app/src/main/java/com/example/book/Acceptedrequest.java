package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

public class Acceptedrequest extends AppCompatActivity {
    String url;
    ListView l2;
    SharedPreferences sh;
    ArrayList<String> title,author,price,date,d_date,quantity,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptedrequest);
        l2 = (ListView) findViewById(R.id.a_lv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/a_request";
        RequestQueue queue = Volley.newRequestQueue(Acceptedrequest.this);

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
                    d_date = new ArrayList<>();
                    quantity = new ArrayList<>();
                    id = new ArrayList<>();




                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        title.add(jo.getString("title"));
                        author.add(jo.getString("author"));
                        price.add(jo.getString("price"));
                        date.add(jo.getString("date"));
                        d_date.add(jo.getString("d_date"));
                        quantity.add(jo.getString("qty"));
                        id.add(jo.getString("id"));

                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(p_viewbook.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    l2.setAdapter(new custom5(Acceptedrequest.this,title,author,price,quantity,date,d_date));



                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Acceptedrequest.this, "err"+error, Toast.LENGTH_SHORT).show();
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
}