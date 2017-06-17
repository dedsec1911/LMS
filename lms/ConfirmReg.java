package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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


public class ConfirmReg extends Activity {

    String myJSON;
    String name,phone,username,email,password;
    String REGISTER_URL = "http://dedsec1911.hol.es/register1.php";

    TextView naam1, phn1, unm1, em1, pw1;

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
        setContentView(R.layout.list_view);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        naam1 = (TextView) findViewById(R.id.tvname);
        phn1 = (TextView) findViewById(R.id.tvphone);
        unm1 = (TextView) findViewById(R.id.tvuname);
        em1 = (TextView )findViewById(R.id.tvename);
        pw1= (TextView)findViewById(R.id.tvpname);
        getData();

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
                    ConfirmReg.this, personList, R.layout.list_items,
                    new String[]{TAG_NAME, TAG_PHN, TAG_USR, TAG_EMAIL, TAG_PWD},
                    new int[]{R.id.tvname, R.id.tvphone, R.id.tvuname, R.id.tvename, R.id.tvpname}
            );

            list.setAdapter(adapter);
            list.setOnItemClickListener(new Itemlist());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ConfirmReg.this, "Please Wait", "Processing", true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://www.dedsec1911.hol.es/teachers_list.php");

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


    public class Itemlist implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            ViewGroup vg = (ViewGroup) view;

            final TextView nm = (TextView) vg.findViewById(R.id.tvname);
            final TextView ph1 = (TextView) vg.findViewById(R.id.tvphone);
            final TextView unm1= (TextView) vg.findViewById(R.id.tvuname);
            final TextView em1= (TextView) vg.findViewById(R.id.tvename);
            final TextView pw1= (TextView) vg.findViewById(R.id.tvpname);


            name = nm.getText().toString();
            phone = ph1.getText().toString();
            username = unm1.getText().toString();
            email = em1.getText().toString();
            password = pw1.getText().toString();

            new AlertDialog.Builder(ConfirmReg.this)

                    .setTitle(" Confirm Registration")
                    .setMessage("Do you want to allow "+ name + " to register?")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            //private void registerUser() {

                            String name = nm.getText().toString();
                            String phone = ph1.getText().toString();
                            String username = unm1.getText().toString();
                            String email = em1.getText().toString();
                            String password = pw1.getText().toString();


                            register(name, phone,username,email,password);

                        }

                        private void register(final String name, final String phone, final String username, final String email, final String password) {
                            class RegisterUser extends AsyncTask<String, Void, String> {
                                ProgressDialog loading;
                                Json ruc = new Json();


                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    loading = ProgressDialog.show(ConfirmReg.this, "Updating...", "Please Wait...", false, false);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    loading.dismiss();
                                    Toast.makeText(ConfirmReg.this, s, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                protected String doInBackground(String... params) {

                                    HashMap<String, String> data = new HashMap<String, String>();

                                    data.put("name", params[0]);
                                    data.put("phone", params[1]);
                                    data.put("username",params[2]);
                                    data.put("email", params[3]);
                                    data.put("password", params[4]);

                                    String result = ruc.sendPostRequest(REGISTER_URL, data);


                                    return result;
                                }
                            }


                            RegisterUser ru = new RegisterUser();
                            ru.execute(name,phone,username,email,password);
                        }

                    })
                    .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.mipmap.warning)
                    .show();
        }
    }
}