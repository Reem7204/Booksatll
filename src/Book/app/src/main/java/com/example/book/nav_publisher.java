package com.example.book;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class nav_publisher extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{



    ImageButton B1,B2,B3,B4,B5;
    ImageView I1;
    TextView T1,t2;
    SharedPreferences sh;
    String url,name,photo;
    ListView L1;

    ArrayList<String>heading,content;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_publisher);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View linearLayout=navigationView.inflateHeaderView(R.layout.nav_header_home);
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


//        T1.setText("xcxcvv");

//T1.setText("ffffffff");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(this);



    }














    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_publisher, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.nav_addbook)
        {
//            Intent i=new Intent(getApplicationContext(),Addbook.class);
//            startActivity(i);

        }
        if(id==R.id.nav_viewbook)
        {
//            Intent i=new Intent(getApplicationContext(),Addbook.class);
//            startActivity(i);

        }
        if(id==R.id.nav_vieworder)
        {
//            Intent i=new Intent(getApplicationContext(),Addbook.class);
//            startActivity(i);

        }
        if(id==R.id.nav_viewprofile)
        {
//            Intent i=new Intent(getApplicationContext(),Addbook.class);
//            startActivity(i);

        }



        return false;
    }
}