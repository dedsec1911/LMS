package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TeacherConsole extends Activity {
    public static final String USER_NAME = "USER_NAME";
    String uname;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_console);


        Intent intent = getIntent();
       String username = intent.getStringExtra(Login.USER_NAME);
        bundle = intent.getExtras();
        TextView tv;
        tv = (TextView) findViewById(R.id.tcon);
        uname = username;
        tv.setText("Welcome " + username);

        View view = findViewById(android.R.id.content);
        Snackbar.make(view,"Logged in as " + username, Snackbar.LENGTH_LONG).show();


    Button btn = (Button) findViewById(R.id.chkopc);
    //Button btn1 = (Button) findViewById(R.id.teacher);

            btn.setOnClickListener(new View.OnClickListener()

    {

        @Override
        public void onClick (View v){

       // startActivity(new Intent(TeacherConsole.this, ChkOccup.class));
            Intent nextActi = new Intent(TeacherConsole.this, ChkOccup.class);
            //nextActi.putExtra("mealID", 0);
            nextActi.putExtra("USER_NAME", uname);
            startActivity(nextActi);

        }
    });

        Button btn1 = (Button) findViewById(R.id.report);
        //Button btn1 = (Button) findViewById(R.id.teacher);

        btn1.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick (View v){

                // startActivity(new Intent(TeacherConsole.this, ChkOccup.class));
                Intent nextActi = new Intent(TeacherConsole.this, Report.class);
                //nextActi.putExtra("mealID", 0);
                nextActi.putExtra("USER_NAME", uname);
                startActivity(nextActi);

            }
        });

        Button btn2 = (Button) findViewById(R.id.updpro);

        btn2.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick (View v){

                Intent nextActi = new Intent(TeacherConsole.this, UpdateProfile1.class);
                nextActi.putExtra("USER_NAME", uname);
                startActivity(nextActi);

            }
        });
}


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(TeacherConsole.this)
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