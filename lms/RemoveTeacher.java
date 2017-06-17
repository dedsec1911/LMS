package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import dmax.dialog.SpotsDialog;

public class RemoveTeacher extends Activity{

    //Code here for deleting a user from the Database.
    String DELETE_URL = "http://dedsec1911.hol.es/delete_user.php";
    String myJSON;
    String name,username,id;

    TextView naam1,unm1,id1;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_USR = "username";


    JSONArray persons = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        naam1 = (TextView) findViewById(R.id.tv_name);
        unm1 = (TextView) findViewById(R.id.tv_uname);
        id1 = (TextView) findViewById(R.id.tv_id);
        getData();

    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            persons = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < persons.length(); i++) {
                JSONObject c = persons.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String username = c.getString(TAG_USR);
                String id = c.getString(TAG_ID);


                HashMap<String, String> lms = new HashMap<String, String>();

                lms.put(TAG_NAME, name);
                lms.put(TAG_USR, username);
                lms.put(TAG_ID, id);

                personList.add(lms);
            }

            ListAdapter adapter = new SimpleAdapter(
                    RemoveTeacher.this, personList, R.layout.list_items_remt,
                    new String[]{TAG_NAME,TAG_USR,TAG_ID},
                    new int[]{R.id.tv_name,R.id.tv_uname,R.id.tv_id}
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
            AlertDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(RemoveTeacher.this, "Please Wait", "Processing", true, true);
                AlertDialog dialog = new SpotsDialog(context);
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://www.dedsec1911.hol.es/remove_teacher_list.php");

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
                //loading.dismiss();
                dialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public class Itemlist implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            ViewGroup vg = (ViewGroup) view;

            final TextView nm = (TextView) vg.findViewById(R.id.tv_name);
            final TextView id1= (TextView) vg.findViewById(R.id.tv_id);


            name = nm.getText().toString();
            id = id1.getText().toString();
            //username = unm1.getText().toString();

            new AlertDialog.Builder(RemoveTeacher.this)

                    .setTitle(" Confirm Removal!")
                    .setMessage("Do you want to remove " + name +" from the list?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            //private void registerUser()

                            // String name = nm.getText().toString();
                            // String username = unm1.getText().toString();
                            String id = id1.getText().toString();

                            deleteUser(id);
                        }

                        private void deleteUser(final String id) {
                            class Delete extends AsyncTask<String, Void, String> {
                                ProgressDialog loading;
                                Json ruc = new Json();

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    loading = ProgressDialog.show(RemoveTeacher.this, "Updating...", "Please Wait...", false, false);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    loading.dismiss();
                                    Toast.makeText(RemoveTeacher.this, s, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                protected String doInBackground(String... params) {

                                    HashMap<String, String> data = new HashMap<String, String>();

                                    data.put("id", params[0]);
                                    String s = ruc.sendPostRequest(DELETE_URL, data);
                                    return s;
                                }
                            }

                            Delete de = new Delete();
                            de.execute(id);

                           // Intent intent = new Intent(RemoveTeacher.this,AddRemove.class);
                            //startActivity(intent);


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

