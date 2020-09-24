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

        // NOTE: get access to the button in UI
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequest();
            }
        });
    }


    public void startRequest() {

        // NOTE: defining the ErrorListener (determines what to do in case request was not sent to the web server
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                // NOTE: Create an empty string that will store the reason of error
                String message = null;

                // NOTE: Fill the message variable based on the error
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

                // NOTE: log the error
                Log.e("[WEBREQUEST]: FAILURE ", message);
            }
        };


        // NOTE: Create a queue to put the requests inside it
        RequestQueue queue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();


        Integer employeeId = 1;
        // NOTE: Fill the request URL (I stored the base url inside res/values/strings)
        String requestURL = getString(R.string.base_url) + "employee/" + employeeId.toString();


        // NOTE: defining the listener (what to do with the obtained response from the web server)
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(JSONObject response) {
                // NOTE: we created a single try catch. anytime we call getString
                try {
                    // NOTE: log the full response
                    // NOTE: note that it is information, so I used Log.i
                    // NOTE: note that I use meaningful keys for logging
                    // NOTE: we checked the web server website, and we expect something like this: "{"status": "success", "data":"{...}","message":"Successfully ...."}"
                    Log.i("[WEBREQUEST]:GetEmployee", " Response=" + response.toString());


                    // NOTE: get the "status" out of the response. we expect this to be "success"
                    String status = response.getString("status");

                    // NOTE: if "status" is not "success", log it
                    if (!status.equals("success")) {
                        Log.e("[WEBREQUEST]:GetEmployee","status is not success");
                    }

                    else {
                        // NOTE: When we are here, it means status is success
                        // NOTE: now we get the "data" out of the repsonse
                        // NOTE: we expect data to be in this format: {"id":1,"employee_name":"Tiger Nixon","employee_salary":320800,"employee_age":61,"profile_image":""}
                        String data = response.getString("data");

                        // NOTE: Create an instance of GSON library
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();

                        // NOTE: parse "data" to Java Class: Employee
                        Employee emp = mGson.fromJson(data, Employee.class);

                        //NOTE: now we have access to the properties of the employee in our Java code

                        // NOTE: use Toast to show the employee name
                        Toast.makeText(getApplicationContext(), emp.employee_name, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        // NOTE: now it is time to create a JsonObjectRequest. it has 4 inputs:
        // 1) request url: the address to which you send the request
        // 2) the content you are sending accross along with the request: here we have nothing, so it is null
        // 3) response listener: what to do with the response. we created this variable before.
        // 4) error listener: what to do in case of an error (when request cannot be sent). we created this variable before.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestURL, null, responseListener, errorListener) {


            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            // NOTE: you can use getPriority whenever you want to change the priority of your request
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        // NOTE: add your request to the queue you created before
        queue.add(jsonObjectRequest);

        //NOTE: start the queue to process your requests
        queue.start();

    }
}