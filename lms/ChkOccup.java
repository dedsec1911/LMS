package com.ilb.deds3c.lms;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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

public class ChkOccup extends Activity {

    String REGISTER_URL = "http://dedsec1911.hol.es/occupancy.php";
    String DELETE_URL = "http://dedsec1911.hol.es/remoccu.php";
    String myJSON;
    String uname,LabName;
    String username,user_name;

    TextView lname;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_LNAME = "LabName";
    private static final String TAG_UNAME = "username";
    Handler handler = new Handler();
    JSONArray persons = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;
    TextView tv;

    Bundle bundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view2);
        //doTheAutoRefresh();
        Intent intent = getIntent();
        user_name = intent.getStringExtra(Login.USER_NAME);
        bundle = intent.getExtras();
        tv = (TextView) findViewById(R.id.tv4);
        uname = user_name;
        tv.setText("Welcome " + uname);
        list = (ListView) findViewById(R.id.listView2);
        personList = new ArrayList<HashMap<String, String>>();
        lname = (TextView) findViewById(R.id.tv_labname);
        getData();


    }


    /*private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    //doTheAutoRefresh();
                    startActivity(intent);// Write code for your refresh logic
                    //doTheAutoRefresh();
                }
            }, 15000);
        new CountDownTimer(20000, 100) {
            public void onTick(long milisuntilfinished) {

                //  view = inflater.inflate(R.layout.list_items_chkoccup, parent, false);
                // view.setOnClickListener(null);
// counter++;
            }

            public void onFinish() {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                //doTheAutoRefresh();
                startActivity(intent);
                doTheAutoRefresh();
            }
           // doTheAutoRefresh();
        }.start();*/





    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            persons = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < persons.length(); i++) {
                JSONObject c = persons.getJSONObject(i);
                String LabName = c.getString(TAG_LNAME);
                String username = c.getString(TAG_UNAME);


                HashMap<String, String> lms = new HashMap<String, String>();

                lms.put(TAG_LNAME, LabName);
                lms.put(TAG_UNAME, username);

                personList.add(lms);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ChkOccup.this, personList, R.layout.list_items_chkoccup,
                    new String[]{TAG_LNAME,TAG_UNAME},
                    new int[]{R.id.tv_labname,R.id.unamechk}

            );

            /*ImageView myView = (ImageView) findViewById(R.id.imgview);
            if (username.equals("")) {
                myView.setVisibility(View.VISIBLE);
                //myView.setImageResource(R.drawable.green);
            } else {
                //myView.setImageResource(R.drawable.red);
                myView.setVisibility(View.INVISIBLE);
            }*/

            list.setAdapter(adapter);
            list.setOnItemClickListener(new ChkOccup.Itemlist());

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
                loading = ProgressDialog.show(ChkOccup.this, "Please Wait", "Processing", true, true);
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
    public void getData1() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

           /* @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ChkOccup.this, "Please Wait", "Processing", true, true);
            }*/

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

               // loading.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public class Itemlist implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {

            ViewGroup vg = (ViewGroup) view;

            final TextView ln = (TextView) vg.findViewById(R.id.tv_labname);
            final TextView un = (TextView) vg.findViewById(R.id.unamechk);



            LabName = ln.getText().toString();
            //  id = id1.getText().toString();
            username = un.getText().toString();

            new AlertDialog.Builder(ChkOccup.this)

                    .setTitle(" Confirm Occupancy!")
                    .setMessage("Do you want to occupy " + LabName  +" from the list?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            //private void registerUser()
                            // String name = nm.getText().toString();
                            // String username = unm1.getText().toString();
                            String LabName = ln.getText().toString();
                            String username = un.getText().toString();
                           // if(un != null || !un.getText().equals("") || un.getText().toString().length()>0){
                            if(username.equals("")){
                               // Toast.makeText(getApplicationContext(), "true",
                                       // Toast.LENGTH_LONG).show();

                                addUser(LabName,user_name);
                                adapterView.setEnabled(false);
                                doNotify();
                            }

                            else{
                                Toast.makeText(getApplicationContext(), "Already Occupied by "+ username +"!",
                                        Toast.LENGTH_LONG).show();
                               // getData();
                               // view.setEnabled(false);
                                //finish();
                                //doTheAutoRefresh();
                            }

                            /*ImageView myView = (ImageView) findViewById(R.id.imgview);
                            if (username.equals("")) {
                                myView.setVisibility(View.VISIBLE);
                            } else {
                                //myView.setImageResource(R.drawable.red);
                                myView.setVisibility(View.INVISIBLE);
                            }*/
                           // addUser(LabName,username);
                           // doNotify();
                        }

                        public void doNotify() {
                            new CountDownTimer(10000, 100) {
                                public void onTick(long milisuntilfinished) {

                                    //  view = inflater.inflate(R.layout.list_items_chkoccup, parent, false);
                                    // view.setOnClickListener(null);
// counter++;
                                }

                                public void onFinish() {
                                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Intent resultIntent = new Intent(ChkOccup.this, ChkOccup.class);
                                    resultIntent.setAction(Intent.ACTION_MAIN);
                                    resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                                    // PendingIntent pendingIntent = PendingIntent.getActivity(ChkOccup.this, 0,
                                    //  resultIntent, 0);
                                    PendingIntent intent=PendingIntent.getActivity(ChkOccup.this,0,resultIntent,0);
                                    getAct();

                                    // PendingIntent intent= PendingIntent.getActivity(ChkOccup.this,0,new Intent(ChkOccup.this,ChkOccup.class),0);
                                    //intent.getAct();
                                    //PendingIntent intent = PendingIntent.getAct();
                                    NotificationCompat.Builder nb = new NotificationCompat.Builder(ChkOccup.this);
                                    nb.setSound(sound);
                                    nb.setSmallIcon(R.drawable.ic_air_play);
                                    nb.setContentTitle("Lecture Over");
                                    nb.setContentText("Do you want to continue?");
                                    nb.setContentIntent(intent);
                                    nb.setAutoCancel(true);

                                    NotificationManager nm = (NotificationManager) getSystemService(ChkOccup.NOTIFICATION_SERVICE);
                                    nm.notify(1, nb.build());


                                }
                            }.start();
                        }

                        public void getAct() {
                            new AlertDialog.Builder(ChkOccup.this)
                                    .setTitle(" Lecture Termination!")
                                    .setMessage("Lecture Over! \nDo you want to continue?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            adapterView.setEnabled(false);
                                            doNotify();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            adapterView.setEnabled(true);
                                            deleteUser(LabName); // do nothing
                                            //Toast.makeText(ChkOccup.this,"Lab Vacant", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setIcon(R.mipmap.warning)
                                    .show();
                        }

                        private void deleteUser(final String LabName) {
                            class Delete extends AsyncTask<String, Void, String> {
                                ProgressDialog loading;
                                Json ruc = new Json();

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    loading = ProgressDialog.show(ChkOccup.this, "Updating...", "Please Wait...", false, false);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    loading.dismiss();
                                    Toast.makeText(ChkOccup.this, s, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                protected String doInBackground(String... params) {

                                    HashMap<String, String> data = new HashMap<String, String>();

                                    data.put("LabName", params[0]);
                                    String s = ruc.sendPostRequest(DELETE_URL, data);
                                    return s;
                                }
                            }
                            Delete de = new Delete();
                            de.execute(LabName);

                            // Intent intent = new Intent(RemoveTeacher.this,AddRemove.class);
                            //startActivity(intent);


                        }



                        private void addUser(final String LabName, final String username) {
                            class RegisterUser extends AsyncTask<String, Void, String> {
                                ProgressDialog loading;
                                Json ruc = new Json();


                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    loading = ProgressDialog.show(ChkOccup.this, "Updating...", "Please Wait...", false, false);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    loading.dismiss();
                                    Toast.makeText(ChkOccup.this, s, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                protected String doInBackground(String... params) {

                                    HashMap<String, String> data = new HashMap<String, String>();

                                    data.put("LabName", params[0]);
                                    data.put("user_name",params[1]);


                                    String result = ruc.sendPostRequest(REGISTER_URL, data);


                                    return result;
                                }
                            }


                            RegisterUser ru = new RegisterUser();
                            ru.execute(LabName,user_name);
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



