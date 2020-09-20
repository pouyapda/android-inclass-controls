package com.example.webreqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick();
            }
        });
    }
    public void onclick(){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "NetworkError: Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "AuthFailureError: Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "NoConnectionError: Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        };

        VolleyLog.DEBUG = true;
        RequestQueue queue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String requestURL = getString(R.string.base_url) + "employee/1";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.wtf(response, "utf-8");
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                Employee emp = mGson.fromJson(response, Employee.class);
                Log.d("got employee: ",emp.toString());

            }
        }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }

        };

        queue.add(stringRequest);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // check if status is success
                String data="";
                try {
                    data = response.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                VolleyLog.wtf(response.toString(), "utf-8");
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                Employee emp = mGson.fromJson(data, Employee.class);
                Log.d("got employee: ",emp.toString());

            }
        }, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        queue.add(jsonObjectRequest);

        queue.start();

    }
}