package com.ilb.deds3c.lms;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AddLab extends Activity implements View.OnClickListener {

    String ADDLAB_URL = "http://dedsec1911.hol.es/add_lab.php";

    Button adl;
    EditText labed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lab);
        adl = (Button) findViewById(R.id.addlbtn);
        labed = (EditText) findViewById(R.id.labedit);
        adl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        addLab();

        //startActivity(new Intent(fourth.this,fifth.class));
    }


    private void addLab() {
        //String name = editTextName.getText().toString().trim().toLowerCase();
        String LabName = labed.getText().toString().trim().toLowerCase();

        if(LabName.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please fill all fields",Toast.LENGTH_LONG).show();
        }
        else

            add(LabName);
    }

    private void add(final String LabName) {
        class addLab extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            Json ruc = new Json();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                String LabName = labed.getText().toString().trim().toLowerCase();

                if(LabName.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please fill all values",Toast.LENGTH_LONG).show();
                }

                loading = ProgressDialog.show(AddLab.this, "Please Wait", "Processing", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("Success")){
                    //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                }else{
                    Intent intent = new Intent(AddLab.this,AddRemove.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //  startActivity(new Intent(fourth.this, fifth.class));
            }


            @Override
            protected String doInBackground (String...params){

                HashMap<String, String> data = new HashMap<String, String>();

                data.put("LabName",params[0]);

                String result = ruc.sendPostRequest(ADDLAB_URL, data);


                return result;
            }
        }


        addLab ru = new addLab();
        ru.execute(LabName);
    }

}
