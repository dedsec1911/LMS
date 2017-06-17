package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.HashMap;

public class AdminLogin extends Activity implements View.OnClickListener {

    EditText logu,logp;
    Button btn;
    String username,password;
    SharedPreferences sp;

    public static final String USER_NAME = "USER_NAME";
    private static final String LOGIN_URL = "http://dedsec1911.hol.es/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.admin_login);

            btn = (Button) findViewById(R.id.adlogin);
            logu = (EditText) findViewById(R.id.addusr);
            logp = (EditText) findViewById(R.id.adpass);
            btn.setOnClickListener(this);
            sp=getSharedPreferences("login",MODE_PRIVATE);

        if(sp.contains("username") && sp.contains("password")){
            startActivity(new Intent(this,Admin.class));
            finish();   //finish current activity
        }


            new AlertDialog.Builder(AdminLogin.this)
                    .setTitle("WARNING")
                    .setMessage(R.string.action_warning)
                    .setNegativeButton("OK,Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.mipmap.warning)
                    .show();
        }

    @Override
    public void onClick(View v) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {

            //loginCheck();
            login();

        } else{
            //no connection
            //Toast toast = Toast.makeText(AdminLogin.this, "No Internet Connection", Toast.LENGTH_LONG);
            //toast.show();

            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    /*void loginCheck(){
        //check username and password are correct and then add them to SharedPreferences
        logu.getText().toString();
        logp.getText().toString();
            SharedPreferences.Editor e=sp.edit();
            e.putString();
            e.putString("password",password);
            e.commit();

            startActivity(new Intent(this,Admin.class));
            finish();
        }*/


    private void login() {
        String username = logu.getText().toString().trim();
        String password = logp.getText().toString().trim();


        if (username.equals("")) {
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_LONG).show();
        }
        else if(password.equals("")) {
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_LONG).show();
        }
        else

            userLogin(username, password);
    }


    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {


                super.onPreExecute();
                loading = ProgressDialog.show(AdminLogin.this, "Authenticating...","Please wait...", true, true);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("Success")){
                    Intent intent = new Intent(AdminLogin.this,Admin.class);
                    intent.putExtra(USER_NAME,username);
                    startActivity(intent);

                }else{
                    View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "Oops! Invalid Username or Password", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<String, String>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                Json ruc = new Json();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}