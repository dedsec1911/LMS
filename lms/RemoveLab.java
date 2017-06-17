package com.ilb.deds3c.lms;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class RemoveLab extends Activity {

    String DELLAB_URL = "http://dedsec1911.hol.es/delete_lab.php";
    String myJSON;
    String LabName;

    TextView lname;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_LNAME = "LabName";


    JSONArray persons = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        lname = (TextView) findViewById(R.id.tv_labname);
        getData();

    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            persons = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < persons.length(); i++) {
                JSONObject c = persons.getJSONObject(i);
                String LabName = c.getString(TAG_LNAME);


                HashMap<String, String> lms = new HashMap<String, String>();

                lms.put(TAG_LNAME, LabName);

                personList.add(lms);
            }

            ListAdapter adapter = new SimpleAdapter(
                    RemoveLab.this, personList, R.layout.list_items_labs,
                    new String[]{TAG_LNAME},
                    new int[]{R.id.tv_labname}
            );

            list.setAdapter(adapter);
            list.setOnItemClickListener(new RemoveLab.Itemlist());

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
                loading = ProgressDialog.show(RemoveLab.this, "Please Wait", "Processing", true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://www.dedsec1911.hol.es/labs_list.php");

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

            final TextView lnm = (TextView) vg.findViewById(R.id.tv_labname);


            LabName = lnm.getText().toString();


            new AlertDialog.Builder(RemoveLab.this)

                    .setTitle(" Confirm Removal!")
                    .setMessage("Do you want to remove " + LabName +" from the list?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            //private void registerUser()

                            // String name = nm.getText().toString();
                            // String username = unm1.getText().toString();
                            String LabName = lnm.getText().toString();

                            deleteUser(LabName);
                        }

                        private void deleteUser(final String LabName) {
                            class Delete extends AsyncTask<String, Void, String> {
                                ProgressDialog loading;
                                Json ruc = new Json();

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    loading = ProgressDialog.show(RemoveLab.this, "Updating...", "Please Wait...", false, false);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    loading.dismiss();
                                    Toast.makeText(RemoveLab.this, s, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                protected String doInBackground(String... params) {

                                    HashMap<String, String> data = new HashMap<String, String>();

                                    data.put("LabName", params[0]);
                                    String s = ruc.sendPostRequest(DELLAB_URL, data);
                                    return s;
                                }
                            }

                            Delete de = new Delete();
                            de.execute(LabName);

                           // Intent intent = new Intent(RemoveLab.this,AddRemove.class);
                           // startActivity(intent);

                        }
                    })


                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.mipmap.warning)
                    .show();
        }
    }

}
