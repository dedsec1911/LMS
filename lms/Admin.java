package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class Admin extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        Intent intent = getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);
        TextView tv;
        tv = (TextView) findViewById(R.id.tv1);
        String uname = username;
        tv.setText("Welcome " + uname);


        View view = findViewById(android.R.id.content);
        Snackbar.make(view,"Logged in as " + uname, Snackbar.LENGTH_LONG).show();


        Button btn = (Button) findViewById(R.id.addrem);
        btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Admin.this, AddRemove.class));

                }
            });


        Button btn1 = (Button) findViewById(R.id.mgmt);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(Admin.this, ManageReport.class));

            }
        });

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(Admin.this)
                .setTitle(" Logout & Exit")
                .setMessage("Are you sure you want to logout and Exit!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.mipmap.warning)
                .show();
    }
}