package com.example.c1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button button ;
    PaymentSheet paymentSheet ;
    String SECRET_KEY = "sk_test_51LvYo4CXbToS6XLGJHfYMX4rd0LfxgVwwuZZuXLgPOAqKcwpjGdvsc3Civ48ewMPXgaAwje1n8ydJmnDu0iV3uIv004HicOkvN";
    String PUBLISH_KEY="pk_test_51LvYo4CXbToS6XLGghraXW5ArLQeQx9zmfL8kDIjF6l9GJphrOilrx2PNaUd8geR8LajEqn6Eok6zufN1lSvtFl300omCOqtxg ";

    String customerID ;
    String EphericalKey;
    String Clientsecret ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.btn);



        PaymentConfiguration.init( this,PUBLISH_KEY);

        paymentSheet=new PaymentSheet(this,paymentSheetResult ->{

        } ) ;


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject (response);
                            customerID =object.getString("ID");
                            Toast.makeText(MainActivity.this, customerID, Toast.LENGTH_SHORT).show();

                            getEphericalKey(customerID);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header=new HashMap<>();
                header.put("Authorization","Bearer"+SECRET_KEY) ;
                return header;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        requestQueue.add(stringRequest) ;


    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject (response);
                            EphericalKey =object.getString("ID");
                            Toast.makeText(MainActivity.this, EphericalKey, Toast.LENGTH_SHORT).show();

                            getClientsecret(customerID,EphericalKey);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header=new HashMap<>();
                header.put("Authorization","Bearer"+SECRET_KEY) ;
                header.put("Stripe-Version","2022-08-01") ;
                return header;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =  new HashMap<>();
                params.put("customer",customerID);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        requestQueue.add(stringRequest) ;



    }

    private void getClientsecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject (response);
                            Clientsecret =object.getString("client_secret  ");
                            Toast.makeText(MainActivity.this, Clientsecret, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header=new HashMap<>();
                header.put("Authorization","Bearer"+SECRET_KEY) ;

                return header;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =  new HashMap<>();
                params.put("customer",customerID);
                params.put("amount","1000"+"00");
                params.put("currency","USD");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        requestQueue.add(stringRequest) ;


    }
}