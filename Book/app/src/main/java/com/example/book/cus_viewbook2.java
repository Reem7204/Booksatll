package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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


public class cus_viewbook2 extends AppCompatActivity {
    ImageView img;
    TextView t1,t2,t3,t4,t6,t7,t8,t9;
    SharedPreferences sh;
    String img1="",img2="";
    String status="f",bid,q,url;
    Button buttonZoomIn,buttonZoomOut,vr,cart;
    EditText e1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_viewbook2);
        img =  findViewById(R.id.imageView2);
        t1 =  findViewById(R.id.c_t1);
        t2 =  findViewById(R.id.c_t2);
        t3 =  findViewById(R.id.c_t3);
        t4 =  findViewById(R.id.c_t4);
        t6 =  findViewById(R.id.c_t6);
        t7 =  findViewById(R.id.c_t7);
        t8 =  findViewById(R.id.c_t8);
        t9 =  findViewById(R.id.c_t9);
//        e1 = findViewById(R.id.c_t10);
//        buttonZoomIn = findViewById(R.id.button7);
//        buttonZoomOut = findViewById(R.id.button8);
        cart = findViewById(R.id.addcart);
        vr = findViewById(R.id.button5);

//buttonZoomIn.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        // loading the animation of
//        // zoom_in.xml file into a variable
//        Animation animZoomIn = AnimationUtils.loadAnimation(cus_viewbook2.this, R.anim.zoom_in);
//        // assigning that animation to
//        // the image and start animation
//        img.startAnimation(animZoomIn);
//    }
//});
//buttonZoomOut.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        // loading the animation of
//        // zoom_out.xml file into a variable
//        Animation animZoomOut = AnimationUtils.loadAnimation(cus_viewbook2.this, R.anim.zoom_out);
//
//        // assigning that animation to
//        // the image and start animation
//        img.startAnimation(animZoomOut);
//    }
//});

        //Enabling scrolling on TextView.
        t4.setMovementMethod(new ScrollingMovementMethod());
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        bid=getIntent().getStringExtra("bid");
        t1.setText(getIntent().getStringExtra("title"));
        t2.setText(getIntent().getStringExtra("author"));
        t3.setText(getIntent().getStringExtra("price"));
        t4.setText(getIntent().getStringExtra("description"));
        t6.setText(getIntent().getStringExtra("publishdate"));
        t7.setText(getIntent().getStringExtra("g"));
        t8.setText(getIntent().getStringExtra("pageno"));
        t9.setText(getIntent().getStringExtra("isbn"));
        img1=getIntent().getStringExtra("image");
        img2=getIntent().getStringExtra("image2");
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/bookpic/"+getIntent().getStringExtra("image"));
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            img.setImageDrawable(thumb_d);

        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equalsIgnoreCase("f"))
                {
                    status="n";
                    try {java.net.URL thumb_u;

                        //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

                        thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/bookpic/"+img2);
                        Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                        img.setImageDrawable(thumb_d);

                    }
                    catch (Exception e)
                    {
                        Log.d("errsssssssssssss",""+e);
                    }


                }
                else
                {
                    status="f";
                    try {java.net.URL thumb_u;

                        //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

                        thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/static/bookpic/"+img1);
                        Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                        img.setImageDrawable(thumb_d);

                    }
                    catch (Exception e)
                    {
                        Log.d("errsssssssssssss",""+e);
                    }
                }
            }
        });
        vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ik = new Intent(getApplicationContext(), Viewreview.class);
                ik.putExtra("bid",bid);
                startActivity(ik);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                q = e1.getText().toString();
//                if (q.equalsIgnoreCase(""))
//                {
//                    e1.setError("Please enter the quantity");
//                }
//                else{
//                    RequestQueue queue = Volley.newRequestQueue(cus_viewbook2.this);
//                    url = "http://" + sh.getString("ip", "") + ":5000/addtocart";
//
//                    // Request a string response from the provided URL.
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // Display the response string.
//                            Log.d("+++++++++++++++++", response);
//                            try {
//                                JSONObject json = new JSONObject(response);
//                                String res = json.getString("task");
//
//
//                                if (res.equalsIgnoreCase("success")) {
//                                    Toast.makeText(cus_viewbook2.this, "Successfully added", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (res.equalsIgnoreCase("exist")) {
////                                    Toast.makeText(Acceptrequest.this, "Already exists", Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//
//                                    Toast.makeText(cus_viewbook2.this, "Unable to register", Toast.LENGTH_SHORT).show();
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//
//                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("bid", bid);
//                            params.put("q", q);
//                            params.put("lid",sh.getString("lid",""));
//
//
//
//                            return params;
//                        }
//                    };
//                    queue.add(stringRequest);
//                }
                Intent ik = new Intent(getApplicationContext(), Addtocart.class);
                ik.putExtra("bid",bid);
                startActivity(ik);

            }
        });

    }
}