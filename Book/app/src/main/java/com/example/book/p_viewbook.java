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

public class p_viewbook extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
        url ="http://"+sh.getString("ip", "") + ":5000/viewbooks";
        RequestQueue queue = Volley.newRequestQueue(p_viewbook.this);

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
                        publishdate.add(jo.getString("publishdate"));
                        genre.add(jo.getString("genre"));
                        pageno.add(jo.getString("pageno"));
                        id.add(jo.getString("id"));

                    }

//                     ArrayAdapter<String> ad=new ArrayAdapter<>(p_viewbook.this,android.R.layout.simple_list_item_1,name);
//                    lv.setAdapter(ad);

                    lv.setAdapter(new Custom(p_viewbook.this,title,author,price,image));
                    lv.setOnItemClickListener(p_viewbook.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(p_viewbook.this, "err"+error, Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder ald=new AlertDialog.Builder(p_viewbook.this);
        ald.setTitle("Choose an option")
                .setPositiveButton(" View more ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                                        Intent ik=new Intent(getApplicationContext(),p_viewbook2.class);
                                        ik.putExtra("title",title.get(i));
                                        ik.putExtra("author",author.get(i));
                                        ik.putExtra("price",price.get(i));
                                        ik.putExtra("image",image.get(i));
                                        ik.putExtra("image2",image2.get(i));
                                        ik.putExtra("isbn",isbn.get(i));
                                        ik.putExtra("description",description.get(i));
                                        ik.putExtra("publishdate",publishdate.get(i));
                                        ik.putExtra("genre",genre.get(i));
                                        ik.putExtra("pageno",pageno.get(i));
                                        ik.putExtra("id",id.get(i));

                                        startActivity(ik);



                    }
                })
                .setNegativeButton(" Delete ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        RequestQueue queue = Volley.newRequestQueue(p_viewbook.this);
                        url = "http://" + sh.getString("ip","") + ":5000/deletebooks";

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
                                        Toast.makeText(p_viewbook.this, "Deleted successfully", Toast.LENGTH_SHORT).show();


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

//                        RequestQueue queue = Volley.newRequestQueue(p_viewbook.this);
//                        url = "http://" + sh.getString("ip","") + ":5000/rejectrequest";
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
//                                        Toast.makeText(p_viewbook.this, "Request rejected", Toast.LENGTH_SHORT).show();
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
////                                params.put("id", id.get(i));
//
//
//                                return params;
//                            }
//                        };
//                        queue.add(stringRequest);
//
//                    }
//                });


        AlertDialog al=ald.create();
        al.show();

    }
}