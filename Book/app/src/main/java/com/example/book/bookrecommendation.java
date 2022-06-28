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
import android.widget.ArrayAdapter;
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

public class bookrecommendation extends AppCompatActivity {
    String url;
    ListView lv;
    SharedPreferences sh;
    ArrayList<String> title,author,price,image,image2,isbn,description,id,publishdate,genre,pageno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pviewbook);
        lv = (ListView) findViewById(R.id.plvbook);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/View_Books_rec";
        RequestQueue queue = Volley.newRequestQueue(bookrecommendation.this);

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
                    image = new ArrayList<>();
                    image2 = new ArrayList<>();
                    isbn = new ArrayList<>();
                    description = new ArrayList<>();
                    publishdate = new ArrayList<>();
                    genre = new ArrayList<>();
                    pageno = new ArrayList<>();
                    id = new ArrayList<>();



                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        title.add(jo.getString("title"));
                        author.add(jo.getString("author"));
                        price.add(jo.getString("price"));
                        image.add(jo.getString("pic1"));
                        image2.add(jo.getString("pic2"));
                        isbn.add(jo.getString("isbn"));
                        description.add(jo.getString("description"));
                        publishdate.add(jo.getString("p_year"));
                        genre.add(jo.getString("name"));
                        pageno.add(jo.getString("pageno"));
                        id.add(jo.getString("b_id"));

                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(p_viewbook.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    lv.setAdapter(new Custom(bookrecommendation.this,title,author,price,image));


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(bookrecommendation.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid",sh.getString("lid",""));

                return params;
            }
        };
        queue.add(stringRequest);
    }


}