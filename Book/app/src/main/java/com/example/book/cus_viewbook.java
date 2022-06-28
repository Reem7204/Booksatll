package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

public class cus_viewbook extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {
    String url,sid="";
    ListView lv;
    SharedPreferences sh;
    Button b1;
    Spinner sp;
    EditText e1;
    String bookname;

    ArrayList<String> genre,id,image,title,author,price,image2,isbn,description,pageno,publishdate,g,bid,rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_viewbook);
        lv = findViewById(R.id.lvbook);
        sp = findViewById(R.id.spnrbook);
        b1 = findViewById(R.id.vbookbutton);
        e1=findViewById(R.id.editTextTextPersonName2);
        sp.setOnItemSelectedListener(this);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url ="http://"+sh.getString("ip", "") + ":5000/genreselect";
        RequestQueue queue = Volley.newRequestQueue(cus_viewbook.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    genre = new ArrayList<>();
                    id = new ArrayList<>();

                    genre.add("ALL");
                    id.add("0");


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        genre.add(jo.getString("name"));
                        id.add(jo.getString("s_id"));

                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<>(cus_viewbook.this,android.R.layout.simple_list_item_1,genre);
                    sp.setAdapter(ad);

//                    lv.setAdapter(new Custom(cus_viewbook.this,title,author,price,image));
//                    lv.setOnItemClickListener(cus_viewbook.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(cus_viewbook.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };
        queue.add(stringRequest);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookname=e1.getText().toString();

                url ="http://"+sh.getString("ip", "") + ":5000/custviewbook";
                RequestQueue queue = Volley.newRequestQueue(cus_viewbook.this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.d("+++++++++++++++++",response);
                        try {

                            JSONArray ar=new JSONArray(response);
                            image = new ArrayList<>();
                            title = new ArrayList<>();
                            author = new ArrayList<>();
                            price = new ArrayList<>();
                            image2 = new ArrayList<>();
                            isbn = new ArrayList<>();
                            description = new ArrayList<>();
                            g = new ArrayList<>();
                            pageno = new ArrayList<>();
                            publishdate = new ArrayList<>();
                            bid = new ArrayList<>();
                            rating = new ArrayList<>();





                            for(int i=0;i<ar.length();i++)
                            {
                                JSONObject jo=ar.getJSONObject(i);
                                image.add(jo.getString("pic1"));
                                title.add(jo.getString("title"));
                                author.add(jo.getString("author"));
                                price.add(jo.getString("price"));
                                image2.add(jo.getString("pic2"));
                                isbn.add(jo.getString("isbn"));
                                description.add(jo.getString("description"));
                                pageno.add(jo.getString("pageno"));
                                publishdate.add(jo.getString("p_year"));
                                g.add(jo.getString("name"));
                                bid.add(jo.getString("b_id"));
                                rating.add(jo.getString("r"));


                            }

//                            ArrayAdapter<String> ad=new ArrayAdapter<>(cus_viewbook.this,android.R.layout.simple_list_item_1,genre);
//                            sp.setAdapter(ad);

                    lv.setAdapter(new Custom4(cus_viewbook.this,title,author,price,image,rating));
                    lv.setOnItemClickListener(cus_viewbook.this);

                        } catch (Exception e) {
                            Log.d("=========", e.toString());
                        }


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(cus_viewbook.this, "err"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("sid",sid);
                        params.put("title",bookname);

                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent ik=new Intent(getApplicationContext(),cus_viewbook2.class);
        ik.putExtra("title",title.get(i));
        ik.putExtra("author",author.get(i));
        ik.putExtra("price",price.get(i));
        ik.putExtra("image",image.get(i));
        ik.putExtra("image2",image2.get(i));
        ik.putExtra("isbn",isbn.get(i));
        ik.putExtra("description",description.get(i));
        ik.putExtra("pageno",pageno.get(i));
        ik.putExtra("g",g.get(i));
        ik.putExtra("publishdate",publishdate.get(i));
        ik.putExtra("bid",bid.get(i));


        startActivity(ik);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sid=id.get(i);




    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}