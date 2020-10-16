package com.example.webreqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

    }


    public void getEmployeeInfo(int id) {

        RequestQueue queue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        String requestURL = getString(R.string.base_url) + "employee/" + id;

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("[WEBREQUEST]:GetEmployee", " Response=" + response.toString());

                    String status = response.getString("status");

                    if (!status.equals("success")) {
                        Log.e("[WEBREQUEST]:GetEmployee","status is not success");
                    }

                    else {
                        String data = response.getString("data");
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();


                        Employee emp = mGson.fromJson(data, Employee.class);


                        // EXE: populate the information of the employee into a textbox

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestURL, null, responseListener, errorListener) {


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

    public void getAllEmployees() {

        RequestQueue queue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        String requestURL = getString(R.string.base_url) + "employees";

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(JSONObject response) {
                try {
                   Log.i("[WEBREQUEST]:GetEmployees", " Response=" + response.toString());



                    String status = response.getString("status");

                    if (!status.equals("success")) {
                        Log.e("[WEBREQUEST]:GetEmployee","status is not success");
                    }

                    else {
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();


                        Employees emps = mGson.fromJson(response.toString(), Employees.class);


                        // EXE: extract ids and names into global variables, and load the spinner
                        // hint:
                        // List<String> names = new ArrayList<>();
                        //  for (Person p : people) {
                        //    names.add(p.name);
                        //}
                        // spinner, needs string[] not List<string>. search for a way to convert List<string> to string[]

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestURL, null, responseListener, errorListener) {


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

            Log.e("[WEBREQUEST]: FAILURE ", message);
        }
    };

}