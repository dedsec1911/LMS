package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;


public class Login extends Activity implements View.OnClickListener {

    EditText logu, logp;
    Button btn;

    public static final String USER_NAME = "USER_NAME";
    private static final String LOGIN_URL = "http://dedsec1911.hol.es/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);


        btn = (Button) findViewById(R.id.login1);
        logu = (EditText) findViewById(R.id.login_uname);
        logp = (EditText) findViewById(R.id.login_pass);
        btn.setOnClickListener(this);
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
            login();

        } else {
            //no connection
           // Toast toast = Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG);
           // toast.show();

            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }


    private void login() {
        String username = logu.getText().toString().trim();
        String password = logp.getText().toString().trim();

        if (username.equals("")) {
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_LONG).show();
        }
        else if(password.equals("")){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Please fill all fields", Snackbar.LENGTH_LONG).show();
        }
        else
            userLogin(username, password);
    }


    private void userLogin(final String username, final String password) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Authenticating...", "Please wait...", true, true);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();


                if (s.equalsIgnoreCase("Success")) {
                    Intent intent = new Intent(Login.this, TeacherConsole.class);
                    intent.putExtra(USER_NAME, username);
                    startActivity(intent);
                    //Toast.makeText(Login.this, "Welcome back!", Toast.LENGTH_LONG).show();

                } else {
                    View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "Oops! Invalid Username or Password", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", params[0]);
                data.put("password", params[1]);

                Json ruc = new Json();

                String result = ruc.sendPostRequest(LOGIN_URL, data);

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


/*public class Login extends Activity {

    private EditText logu;
    private EditText logp;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        logu = (EditText) findViewById(R.id.login_uname);
        logp = (EditText) findViewById(R.id.login_pass);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login1_Click(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "Please wait...", "Processing...", true);

        (firebaseAuth.signInWithEmailAndPassword(logu.getText().toString(), logp.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Login.this, Admin.class);
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}             */