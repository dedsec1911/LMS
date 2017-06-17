package com.ilb.deds3c.lms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AddRemove extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_remove);



        Button btn = (Button) findViewById(R.id.confirmreg);
        Button btn1 = (Button) findViewById(R.id.remt);
        Button btn2 = (Button) findViewById(R.id.addl);
        Button btn3 = (Button) findViewById(R.id.reml);

    btn.setOnClickListener(new View.OnClickListener()
        {

        @Override
        public void onClick (View v){

        startActivity(new Intent(AddRemove.this, ConfirmReg.class));

            }
        });

        btn1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                startActivity(new Intent(AddRemove.this, RemoveTeacher.class));

            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                startActivity(new Intent(AddRemove.this, AddLab.class));

            }
        });

        btn3.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                startActivity(new Intent(AddRemove.this, RemoveLab.class));

            }
        });

    }
}