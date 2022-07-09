package com.example.book;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
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
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class nav_publisher extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    String count;
    ImageButton B1,B2,B3,B4,B5;
    ImageView I1;
    TextView T1,t2;
    SharedPreferences sh;
    String url,name,photo;
    ListView L1;
    TextView slideshow,gallery;
    ArrayList<String>heading,content;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_publisher);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View linearLayout=navigationView.inflateHeaderView(R.layout.nav_header_home);
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        gallery=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_cancel));
        RequestQueue queue = Volley.newRequestQueue(nav_publisher.this);
        url = "http://" + sh.getString("ip","") + ":5000/countcancel";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONObject json = new JSONObject(response);
                    String res = json.getString("task");


                    if (!res.equalsIgnoreCase("")) {
                         count = res;
                        initializeCountDrawer(count);



                    } else {

//                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

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
//                params.put("uname", uname);
                params.put("lid", sh.getString("lid",""));

                return params;
            }
        };
        queue.add(stringRequest);




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
    private void initializeCountDrawer(String c){
        //Gravity property aligns the text
        gallery.setGravity(Gravity.CENTER_VERTICAL);
        gallery.setTypeface(null, Typeface.BOLD);
        gallery.setTextColor(getResources().getColor(R.color.purple_200));
        gallery.setText(c);

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
            Intent i=new Intent(getApplicationContext(),Addbook.class);
            startActivity(i);

        }
        if(id==R.id.nav_viewbook)
        {
            Intent i=new Intent(getApplicationContext(),p_viewbook.class);
            startActivity(i);

        }
        if(id==R.id.nav_vieworder)
        {
            Intent i=new Intent(getApplicationContext(),viewadminrequest.class);
            startActivity(i);

        }
        if(id==R.id.nav_vieworder2)
        {
            Intent i=new Intent(getApplicationContext(),Acceptedrequest.class);
            startActivity(i);

        }
        if(id==R.id.nav_history)
        {
            Intent i=new Intent(getApplicationContext(),history.class);
            startActivity(i);

        }
        if(id==R.id.nav_cancel)
        {
            Intent i=new Intent(getApplicationContext(),cancelrequest.class);
            startActivity(i);
        }
        if(id==R.id.nav_viewprofile)
        {
            Intent i=new Intent(getApplicationContext(),profilepublisher.class);
            startActivity(i);
        }
        if(id==R.id.nav_logout2)
        {
            Intent i=new Intent(getApplicationContext(),Login.class);
            startActivity(i);
        }



        return false;
    }
}