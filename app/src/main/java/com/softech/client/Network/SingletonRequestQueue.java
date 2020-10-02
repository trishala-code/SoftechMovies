package com.softech.client.Network;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class SingletonRequestQueue {

    private static SingletonRequestQueue mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String API_Key="fc167a5b";
      private SingletonRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(mContext, "No network available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    public void POSTStringAndJSONRequest(final Context context, final NetworkListner listner,
                                         JSONObject jsonObject,
                                         final String query, int currentPage) {

        RequestQueue queue = SingletonRequestQueue.getInstance((context)).getRequestQueue();
        VolleyLog.DEBUG = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(" http://www.omdbapi.com/?s="+query+"&apikey="+API_Key+"&page="+currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.wtf(response.toString());
               // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                listner.getResponse(response);
            }
        }, errorListener) {


            @Override
            public int getMethod() {
                return Method.GET;
            }
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.NORMAL;
            }
        };
        //Log.d("TechbullsQuery",jsonObjectRequest.toString());
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }


}


