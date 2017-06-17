package com.ilb.deds3c.lms;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.list;

public class UpdateProfile extends Activity {

    String myJSON;
    //String name,phone,username,email,password;
    String UPDATE_URL = "http://dedsec1911.hol.es/Update.php";

    EditText nam2, phn2, unm2, em2, pw2;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHN = "phone";
    private  static final String TAG_USR = "username";
    private  static final String TAG_EMAIL = "email";
    private  static final String TAG_PWD = "password";

    JSONArray persons = null;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        //list = (ListView) findViewById(R.id.listView2);
        personList = new ArrayList<HashMap<String, String>>();
        nam2 = (EditText) findViewById(R.id.upname);
        phn2 = (EditText) findViewById(R.id.upphn);
        unm2 = (EditText) findViewById(R.id.upuname);
        em2 = (EditText)findViewById(R.id.upemail);
        pw2 = (EditText)findViewById(R.id.uppwd);
        getData();


        Intent intent = getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);
        TextView tv;
        tv = (TextView) findViewById(R.id.uptv);
        String uname = username;
        tv.setText(uname);
    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateProfile.this, "Please Wait", "Processing", true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://www.dedsec1911.hol.es/Update.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
                loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            persons = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < persons.length(); i++) {
                JSONObject c = persons.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String phone = c.getString(TAG_PHN);
                String username =c.getString(TAG_USR);
                String email = c.getString(TAG_EMAIL);
                String password = c.getString(TAG_PWD);


                HashMap<String, String> lms = new HashMap<String, String>();

                lms.put(TAG_NAME, name);
                lms.put(TAG_PHN, phone);
                lms.put(TAG_USR,username);
                lms.put(TAG_EMAIL,email);
                lms.put(TAG_PWD,password);

                personList.add(lms);
          }

            ListAdapter adapter = new SimpleAdapter(
                    UpdateProfile.this, personList, R.layout.update_profile,
                    new String[]{TAG_NAME, TAG_PHN, TAG_USR, TAG_EMAIL, TAG_PWD},
                    new int[]{R.id.upname, R.id.upphn, R.id.upuname, R.id.upemail, R.id.uppwd}
            );

          list.setAdapter(adapter);
         //   list.setOnItemClickListener(new ConfirmReg.Itemlist());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
