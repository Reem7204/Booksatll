package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ip extends AppCompatActivity {
EditText e;
Button b;
String i;
SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        e = (EditText) findViewById(R.id.ip);
        b = (Button) findViewById(R.id.ipid);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = e.getText().toString();
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("ip",i);
                ed.commit();
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);


            }
        });

    }
}