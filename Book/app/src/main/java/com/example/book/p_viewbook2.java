package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class p_viewbook2 extends AppCompatActivity {
    ImageView img;
    TextView t1, t2, t3, t4, t5, t6, t7, t8;
    SharedPreferences sh;
    String img1 = "", img2 = "", url;
    String status = "f",id;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pviewbook2);
        img = findViewById(R.id.imageView);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);
        t7 = findViewById(R.id.t7);
        t8 = findViewById(R.id.t8);
        b = findViewById(R.id.button4);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(getApplicationContext(),Updatebook.class);
                i2.putExtra("title",t1.getText().toString());
                i2.putExtra("author",t2.getText().toString());
                i2.putExtra("price",t3.getText().toString());
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("image",getIntent().getStringExtra("image"));
                ed.putString("image2",getIntent().getStringExtra("image2"));
                ed.putString("id",getIntent().getStringExtra("id"));
                ed.commit();
                i2.putExtra("image",getIntent().getStringExtra("image"));
                i2.putExtra("image2",getIntent().getStringExtra("image2"));
                i2.putExtra("isbn",t5.getText().toString());
                i2.putExtra("description",t4.getText().toString());
                i2.putExtra("publishdate",t7.getText().toString());
                i2.putExtra("genre",t6.getText().toString());
                i2.putExtra("pageno",t8.getText().toString());
                i2.putExtra("id",id);
                startActivity(i2);
            }
        });


        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        id=getIntent().getStringExtra("id");
        t1.setText(getIntent().getStringExtra("title"));
        t2.setText(getIntent().getStringExtra("author"));
        t3.setText(getIntent().getStringExtra("price"));
        t4.setText(getIntent().getStringExtra("description"));
        t5.setText(getIntent().getStringExtra("isbn"));
        t6.setText(getIntent().getStringExtra("genre"));
        t7.setText(getIntent().getStringExtra("publishdate"));
        t8.setText(getIntent().getStringExtra("pageno"));
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

    }
}