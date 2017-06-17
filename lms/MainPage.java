package com.ilb.deds3c.lms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        Button btn = (Button) findViewById(R.id.admin);
        Button btn1 = (Button) findViewById(R.id.teacher);
       // Button btn2 = (Button) findViewById(R.id.action_about);

        btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPage.this, AdminLogin.class));



            }

        });

        btn1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPage.this, LoginSup.class));

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                showAboutUs();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showAboutUs() {
        AboutUs.show(getSupportFragmentManager());
    }

}
