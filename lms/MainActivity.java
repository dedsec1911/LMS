package com.ilb.deds3c.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {

    private Handler mHandler;
    Runnable myTask = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(MainActivity.this, MainPage.class));
            MainActivity.this.finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        //finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler.postDelayed(myTask, 2500);
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(myTask);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}