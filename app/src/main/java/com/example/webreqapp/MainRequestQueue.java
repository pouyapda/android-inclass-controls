// CODE_START
package com.example.webreqapp;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MainRequestQueue {

    private static MainRequestQueue mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private MainRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MainRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MainRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}

// CODE_END