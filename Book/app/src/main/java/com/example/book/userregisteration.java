package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class userregisteration extends AppCompatActivity {
    EditText e0, e1, e2, e3, e4, e5, e6, e7, e8, e9;
    Button b;
    String fname, lname, place, post, pin, phoneno, emailid, password, cpassword, url;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregisteration);
        e0 = (EditText) findViewById(R.id.fname);
        e1 = (EditText) findViewById(R.id.lname);
        e2 = (EditText) findViewById(R.id.place2);
        e3 = (EditText) findViewById(R.id.post2);
        e4 = (EditText) findViewById(R.id.pin2);
        e5 = (EditText) findViewById(R.id.emailid2);
        e6 = (EditText) findViewById(R.id.phoneno2);

        e8 = (EditText) findViewById(R.id.password2);
        e9 = (EditText) findViewById(R.id.cpassword2);
        b = (Button) findViewById(R.id.rbutton2);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = e0.getText().toString();
                lname = e1.getText().toString();
                place = e2.getText().toString();
                post = e3.getText().toString();
                pin = e4.getText().toString();
                emailid = e5.getText().toString();
                phoneno = e6.getText().toString();

                password = e8.getText().toString();
                cpassword = e9.getText().toString();

                if (fname.equalsIgnoreCase(""))
                {
                    e1.setError("Enter first name");
                }
                else if (lname.equalsIgnoreCase(""))
                {
                    e1.setError("Enter last name");
                }
                else if (place.equalsIgnoreCase(""))
                {
                    e2.setError("Enter place");
                }
                else if (post.equalsIgnoreCase(""))
                {
                    e3.setError("Enter post");
                }
                else if (pin.equalsIgnoreCase(""))
                {
                    e4.setError("Enter pin");
                }
                else if(pin.length()!=6)
                {
                    e4.setError("Pin must be a 6 digit number");
                    e4.requestFocus();
                }
                else if(emailid.equalsIgnoreCase(""))
                {
                    e5.setError("Enter Your Email");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches())
                {
                    e5.setError("Enter Valid Email");
                    e5.requestFocus();
                }
                else if(phoneno.equalsIgnoreCase(""))
                {
                    e6.setError("Enter Your Phone No");
                }
                else if(phoneno.length()<10)
                {
                    e6.setError("Must be a 10 digit number");
                    e6.requestFocus();
                }
                else if(password.equalsIgnoreCase(""))
                {
                    e8.setError("Enter Your password");
                }
                else if (!cpassword.equals(password)){
                    e9.setError("Confirm password doesn't match ");
                }


                else {


                    RequestQueue queue = Volley.newRequestQueue(userregisteration.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/registrationcode2";

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
                                    Toast.makeText(userregisteration.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                }
                                else if (res.equalsIgnoreCase("exist")) {
                                    Toast.makeText(userregisteration.this, "Already exists", Toast.LENGTH_SHORT).show();
                                }else {

                                    Toast.makeText(userregisteration.this, "Unable to register", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("pass", password);
                            params.put("fname", fname);
                            params.put("lname", lname);
                            params.put("place", place);
                            params.put("post", post);
                            params.put("pin", pin);
                            params.put("emailid", emailid);
                            params.put("phoneno", phoneno);


                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });
    }
}