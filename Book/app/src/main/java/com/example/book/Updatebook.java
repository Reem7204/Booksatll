package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Updatebook extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    String id;
    String title,author,publishdate,genre,pageno,description,price,pic1,pic2,url,isbn;
    ImageView img,img2;
    Button b3,b2;
    SharedPreferences sh;
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatebook);

        e1 = findViewById(R.id.u_title);
        e2 = findViewById(R.id.u_author);
        e3 = findViewById(R.id.u_isbn);
        e4 = findViewById(R.id.u_publisherdate);
        e5 = findViewById(R.id.u_genre);
        e6 = findViewById(R.id.u_pageno);
        e7 = findViewById(R.id.u_description);
        e8 = findViewById(R.id.u_price);
        b2 = findViewById(R.id.updateimg);
        b3 = findViewById(R.id.updatebook);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        id=getIntent().getStringExtra("id");
        e1.setText(getIntent().getStringExtra("title"));
        e2.setText(getIntent().getStringExtra("author"));
        e8.setText(getIntent().getStringExtra("price"));
        e7.setText(getIntent().getStringExtra("description"));
        e3.setText(getIntent().getStringExtra("isbn"));
        e5.setText(getIntent().getStringExtra("genre"));
        e4.setText(getIntent().getStringExtra("publishdate"));
        e6.setText(getIntent().getStringExtra("pageno"));
              DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Updatebook.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ik = new Intent(getApplicationContext(), Update_image.class);
                startActivity(ik);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = e1.getText().toString();
                author = e2.getText().toString();
                publishdate = e4.getText().toString();
                genre = e5.getText().toString();
                pageno = e6.getText().toString();
                description = e7.getText().toString();
                price = e8.getText().toString();
                isbn = e3.getText().toString();
                if (title.equalsIgnoreCase(""))
                {
                    e1.setError("Enter title of the book");
                }
                else if (author.equalsIgnoreCase(""))
                {
                    e2.setError("Enter author name");
                }
                else if (isbn.equalsIgnoreCase(""))
                {
                    e3.setError("Enter isbn number");
                }
                else if(isbn.length()!=13)
                {
                    e3.setError("ISBN must be 13 digit number");
                    e3.requestFocus();
                }
                else if (publishdate.equalsIgnoreCase(""))
                {
                    e4.setError("Enter published date");
                }
                else if (genre.equalsIgnoreCase(""))
                {
                    e5.setError("Enter genre");
                }
                else if (pageno.equalsIgnoreCase(""))
                {
                    e6.setError("Enter page number");
                }
                else if (description.equalsIgnoreCase(""))
                {
                    e7.setError("Enter description");
                }
                else if (price.equalsIgnoreCase(""))
                {
                    e8.setError("Enter price");
                }
                else{

                    url = "http://" + sh.getString("ip", "") + ":5000/updatebook";

                    uploadBitmap(title);
                }

            }
        });
    }
    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e4.setText(dateFormat.format(myCalendar.getTime()));
    }
    ProgressDialog pd;
    private void uploadBitmap(final String title) {
        pd=new ProgressDialog(Updatebook.this);
        pd.setMessage("Uploading....");
        pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response1) {
                        pd.dismiss();
                        String x=new String(response1.data);
                        try {
                            JSONObject obj = new JSONObject(new String(response1.data));
//                        Toast.makeText(Upload_agreement.this, "Report Sent Successfully", Toast.LENGTH_LONG).show();
                            if (obj.getString("task").equalsIgnoreCase("success")) {

                                Toast.makeText(Updatebook.this, "Successfully updated", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), p_viewbook.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("title",title);
                params.put("author",author);
                params.put("publishdate",publishdate);
                params.put("genre",genre);
                params.put("pageno",pageno);
                params.put("price",price);
                params.put("description",description);
                params.put("isbn",isbn);
                params.put("id",id);
                params.put("lid",sh.getString("lid",""));

                return params;
            }


        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}