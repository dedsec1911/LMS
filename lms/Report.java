package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Report extends Activity implements View.OnClickListener {

    String REPORT_URL = "http://dedsec1911.hol.es/report.php";

    Button submit;
    EditText rep;
    //EditText unme;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_cont);

        submit = (Button) findViewById(R.id.sentrep);
        rep = (EditText) findViewById(R.id.repcontent);
        tv = (TextView) findViewById(R.id.tvrep);
        submit.setOnClickListener(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);
        TextView tv;
        tv = (TextView) findViewById(R.id.tvrep);
        String uname = username;
        tv.setText(uname);
    }


    @Override
    public void onClick(View v) {

        reportSend();
    }

    private void reportSend() {

        String username = tv.getText().toString();
        String reportcont = rep.getText().toString();

        if(reportcont.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please fill the field!",Toast.LENGTH_LONG).show();
        }
        else

            reporter(username,reportcont);
    }

    private void reporter(final String username, final String reportcont) {
        class RegisterUser extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        Json ruc = new Json();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Report.this, "Please Wait", "Processing", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                //if(s.equalsIgnoreCase("success")){

                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                /*else{
                    Intent intent = new Intent(Report.this,Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }*/
                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //  startActivity(new Intent(fourth.this, fifth.class));
            }


            @Override
            protected String doInBackground (String...params){

                HashMap<String, String> data = new HashMap<String,String>();


                data.put("username", params[0]);
                data.put("reportcont", params[1]);

                String result = ruc.sendPostRequest(REPORT_URL, data);


                return result;
            }
        }


        RegisterUser ru = new RegisterUser();
        ru.execute(username,reportcont);
    }
}