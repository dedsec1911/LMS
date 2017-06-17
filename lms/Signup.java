package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Signup extends Activity implements View.OnClickListener {

    String REGISTER_URL = "http://dedsec1911.hol.es/register_main.php";
    // String app_server_url = "http://dedsec1911.hol.es/fcm_insert.php";

   // int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    Button register;
    EditText naam,unaam,pw,email1,phone1;
    String sent = "Sent to Admin!";
    String delivered = "Sms delivered";
    String str = "\nRequest Pending for the above user!";
    PendingIntent sendpi;
    PendingIntent deliveredpi;
    BroadcastReceiver smssentreceiver,smsdeliveredreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        register = (Button) findViewById(R.id.reg);
        naam = (EditText) findViewById(R.id.name);
        unaam = (EditText) findViewById(R.id.uname);
        pw = (EditText) findViewById(R.id.pass);
        email1 = (EditText) findViewById(R.id.email);
        phone1 = (EditText)findViewById(R.id.phone);
        register.setOnClickListener(this);
        sendpi = PendingIntent.getBroadcast(this,0,new Intent(sent),0);
        deliveredpi = PendingIntent.getBroadcast(this,0,new Intent(delivered),0);
    }

            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {


                    registerUser();

                }else{
                    //no connection
                    //Toast toast = Toast.makeText(Signup.this, "No Internet Connection!", Toast.LENGTH_LONG);
                    //toast.show();

                    //View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
                }
                //startActivity(new Intent(fourth.this,fifth.class));
            }


    @Override
    protected void onResume() {
        super.onResume();
        smssentreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(Signup.this,"Sms Sent",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(Signup.this,"Network Error",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(Signup.this,"No service",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(Signup.this,"Null pdu",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(Signup.this,"Radio Off",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        smsdeliveredreceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(Signup.this,"Sms delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(Signup.this,"Sms not delivered",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(smssentreceiver,new IntentFilter(sent));
        registerReceiver(smsdeliveredreceiver,new IntentFilter(delivered));
    }


    public void sendsms() {

        String msg = naam.getText().toString();
        String st = str;
        String phone = "7771090500";

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone, null, msg+st, sendpi, deliveredpi);
        }


    private void registerUser() {
        //String name = editTextName.getText().toString().trim().toLowerCase();
        String name = naam.getText().toString().trim().toLowerCase();
        String username = unaam.getText().toString().trim().toLowerCase();
        String password = pw.getText().toString().trim().toLowerCase();
        String email = email1.getText().toString().trim().toLowerCase();
        String phone = phone1.getText().toString().trim().toLowerCase();

        if(name.equals(""))
        {
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Please Enter your Name",Snackbar.LENGTH_LONG).show();
        }
        else if(username.equals("")){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Please Enter your Username",Snackbar.LENGTH_LONG).show();
        }
        else if(password.equals("")){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Please Enter your Password",Snackbar.LENGTH_LONG).show();
        }
        else if(email.equals("")){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Please Enter your Email",Snackbar.LENGTH_LONG).show();
        }
        else if(phone.equals("")){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Please Enter your Phone",Snackbar.LENGTH_LONG).show();
        }
        else if (password.length() < 5){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Password is too Short!",Snackbar.LENGTH_LONG).show();
        }
        else if (phone.length() < 10){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,"Please Enter valid phone number!",Snackbar.LENGTH_LONG).show();
        }

        else
            register(name, username, password, email, phone);
    }

    private void register(final String name, final String username, final String password, final String email, final String phone) {
        class RegisterUser extends AsyncTask <String, Void, String> {
            ProgressDialog loading;
            Json ruc = new Json();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                String name = naam.getText().toString().trim().toLowerCase();
                String username = unaam.getText().toString().trim().toLowerCase();
                String password = pw.getText().toString().trim().toLowerCase();
                String email = email1.getText().toString().trim().toLowerCase();
                String phone = phone1.getText().toString().trim().toLowerCase();

                if(name.equals("") && username.equals("") && password.equals("") && email.equals("") && phone.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please fill all values",Toast.LENGTH_LONG).show();
                }

                loading = ProgressDialog.show(Signup.this, "Please Wait", "Processing...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                }else{
                    Intent intent = new Intent(Signup.this,Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    sendsms();
                }
                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //  startActivity(new Intent(fourth.this, fifth.class));
            }


            @Override
            protected String doInBackground (String...params){

                HashMap<String, String> data = new HashMap<String, String>();
                //data.put("name",params[1]);

                data.put("name",params[0]);
                data.put("username", params[1]);
                data.put("password", params[2]);
                data.put("email", params[3]);
                data.put("phone",params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);


                return result;
            }
        }


        RegisterUser ru = new RegisterUser();
        ru.execute(name, username, password, email, phone);
    }

}







  /*  private EditText email1;
    private EditText pw;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        email1 = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.pass);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void reg_Click(View v) {

        final ProgressDialog progressDialog = ProgressDialog.show(Signup.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(email1.getText().toString(), pw.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Registration successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Signup.this, Login.class);
                            startActivity(i);
                        }
                        else
                        {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}           */




/*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                         }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("fcm_token", token);


                        return params;
                    }
                };
                MySingleton.getmInstance(Signup.this).addToRequestqueue(stringRequest);*/