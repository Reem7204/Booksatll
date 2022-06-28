package com.example.book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {
    Button b1, b2;
    EditText e1, e2;
    TextView t1, t2, t3;
    String uname, pass, url;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b1 = (Button) findViewById(R.id.button2);
        b2 = (Button) findViewById(R.id.button3);
        e1 = (EditText) findViewById(R.id.et1);
        e2 = (EditText) findViewById(R.id.et2);

        t3 = (TextView) findViewById(R.id.tv3);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = e1.getText().toString();
                pass = e2.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                url = "http://" + sh.getString("ip","") + ":5000/logincode";

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
                                String lid = json.getString("id");
                                String type = json.getString("type");
                                SharedPreferences.Editor edp = sh.edit();
                                edp.putString("lid", lid);
                                edp.commit();
                                if (type.equals("Publisher")) {

                                    Intent ikk=new Intent(getApplicationContext(),Notify.class);

                                    startService(ikk);

                                    Intent ik = new Intent(getApplicationContext(), nav_publisher.class);
                                    startActivity(ik);
                                }
                                else{
                                    Intent ik2 = new Intent(getApplicationContext(),nav_customer.class);
                                    startActivity(ik2);
                                }

                            } else {

                                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

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
                        params.put("uname", uname);
                        params.put("pass", pass);

                        return params;
                    }
                };
                queue.add(stringRequest);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ald = new AlertDialog.Builder(Login.this);
                ald.setTitle("Select option")
                        .setPositiveButton("User", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent i2 = new Intent(getApplicationContext(),userregisteration.class);
                                startActivity(i2);
                            }
                        })
                        .setNegativeButton("Publisher", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent i1 = new Intent(getApplicationContext(),register1.class);
                                startActivity(i1);
                            }
                        });
                AlertDialog al = ald.create();
                al.show();
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ik = new Intent(getApplicationContext(), forgotpassword.class);
                startActivity(ik);
            }
        });


    }
}