package com.example.book;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Addbook extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
    Button b1,b2,b3;
    SharedPreferences sh;
    String title,author,publishdate,genre,pageno,description,price,pic1,pic2,url,isbn;
    String PathHolder="",PathHolder1="";
    byte[] filedt=null;
    byte[] filedt1=null;

    final Calendar myCalendar= Calendar.getInstance();
    private int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        e1 = (EditText) findViewById(R.id.title);
        e2 = (EditText) findViewById(R.id.author);
        e3 = (EditText) findViewById(R.id.publisherdate);
        e4 = (EditText) findViewById(R.id.genre);
        e5 = (EditText) findViewById(R.id.pageno);
        e6 = (EditText) findViewById(R.id.description);
        e7 = (EditText) findViewById(R.id.price);
        e8 = (EditText) findViewById(R.id.pic1);
        e9 = (EditText) findViewById(R.id.pic2);
        e10 = (EditText) findViewById(R.id.isbn);
        b1 = (Button) findViewById(R.id.bpic1);
        b2 = (Button) findViewById(R.id.bpic2);
        b3 = (Button) findViewById(R.id.addbook);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Addbook.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
//            intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 7);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
//            intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 8);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = e1.getText().toString();
                author = e2.getText().toString();
                publishdate = e3.getText().toString();
                genre = e4.getText().toString();
                pageno = e5.getText().toString();
                description = e6.getText().toString();
                price = e7.getText().toString();
                pic1 = e8.getText().toString();
                pic2 = e9.getText().toString();
                isbn = e10.getText().toString();
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
                    e10.setError("Enter isbn number");
                }
                else if(isbn.length()!=13)
                {
                    e10.setError("ISBN must be 13 digit number");
                    e10.requestFocus();
                }
                else if (publishdate.equalsIgnoreCase(""))
                {
                    e3.setError("Enter published date");
                }
                else if (genre.equalsIgnoreCase(""))
                {
                    e4.setError("Enter genre");
                }
                else if (pageno.equalsIgnoreCase(""))
                {
                    e5.setError("Enter page number");
                }
                else if (description.equalsIgnoreCase(""))
                {
                    e6.setError("Enter description");
                }
                else if (price.equalsIgnoreCase(""))
                {
                    e7.setError("Enter price");
                }
                else{

                    url = "http://" + sh.getString("ip", "") + ":5000/addbook";

                    uploadBitmap(title);
                }

            }
        });

    }
    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e3.setText(dateFormat.format(myCalendar.getTime()));
    }
    ProgressDialog pd;
    private void uploadBitmap(final String title) {
        pd=new ProgressDialog(Addbook.this);
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

                                Toast.makeText(Addbook.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(getApplicationContext(),Addbook.class);
                                startActivity(i);
                            }else if (obj.getString("task").equalsIgnoreCase("exist")) {
                                Toast.makeText(Addbook.this, "Already exists", Toast.LENGTH_SHORT).show();
                            } else {
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
                params.put("pic1",pic1);
                params.put("pic2",pic2);
                params.put("isbn",isbn);
                params.put("lid",sh.getString("lid",""));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(PathHolder , filedt ));
                params.put("file2", new DataPart(PathHolder1 , filedt1 ));









                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        PathHolder = FileUtils.getPathFromURI(this, uri);
//                        PathHolder = data.getData().getPath();
//                        Toast.makeText(this, PathHolder, Toast.LENGTH_SHORT).show();

                        filedt = getbyteData(PathHolder);
                        Log.d("filedataaa", filedt + "");
//                        Toast.makeText(this, filedt+"", Toast.LENGTH_SHORT).show();
                        e8.setText(PathHolder);
                    }
                    catch (Exception e){
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            case 8:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        PathHolder1 = FileUtils.getPathFromURI(this, uri);
//                        PathHolder = data.getData().getPath();
//                        Toast.makeText(this, PathHolder, Toast.LENGTH_SHORT).show();

                        filedt1 = getbyteData(PathHolder1);
                        Log.d("filedataaa1", filedt1 + "");
//                        Toast.makeText(this, filedt+"", Toast.LENGTH_SHORT).show();
                        e9.setText(PathHolder1);
                    }
                    catch (Exception e){
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

        }
    }

    private byte[] getbyteData(String pathHolder) {
        Log.d("path", pathHolder);
        File fil = new File(pathHolder);
        int fln = (int) fil.length();
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(fil);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[fln];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
            inputStream.close();
        } catch (Exception e) {
        }
        return byteArray;


    }


}