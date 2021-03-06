package com.example.book;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.drawerlayout.widget.DrawerLayout;




public class nav_customer extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{



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
        setContentView(R.layout.activity_nav_customer);

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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
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
        getMenuInflater().inflate(R.menu.nav_customer, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.nav_bookrecommendation2)
        {
            Intent i=new Intent(getApplicationContext(),bookrecommendation.class);
            startActivity(i);

        }
        if(id==R.id.nav_bookrecommendation)
        {
            Intent i=new Intent(getApplicationContext(),cus_viewbook.class);
            startActivity(i);

        }
        if(id==R.id.nav_mycart)
        {
            Intent i=new Intent(getApplicationContext(),Mycart.class);
            startActivity(i);

        }
        if(id==R.id.nav_myorder)
        {
            Intent i=new Intent(getApplicationContext(),history2.class);
            startActivity(i);

        }
        if(id==R.id.nav_booksuggestion)
        {
            Intent i=new Intent(getApplicationContext(),Suggestions.class);
            startActivity(i);

        }
        if(id==R.id.nav_addcomplaint)
        {
            Intent i=new Intent(getApplicationContext(),addcomplaint.class);
            startActivity(i);

        }
        if(id==R.id.nav_viewcomplaint)
        {
            Intent i=new Intent(getApplicationContext(),viewcomplaint.class);
            startActivity(i);

        }
        if(id==R.id.nav_viewprofile)
        {
            Intent i=new Intent(getApplicationContext(),profilecustomer.class);
            startActivity(i);

        }
        if(id==R.id.nav_logout1)
        {
            Intent i=new Intent(getApplicationContext(),Login.class);
            startActivity(i);

        }



        return false;
    }
}