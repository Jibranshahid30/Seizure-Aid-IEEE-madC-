package com.example.mobeensohail.firstproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Epilepsy extends AppCompatActivity {
    private Button Back;
    private TextView link1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epilepsy);
        Back = (Button) findViewById(R.id.BtnBack1);
        link1 = (TextView) findViewById(R.id.Tvlink1);
        link1.setMovementMethod(LinkMovementMethod.getInstance());//for enter the link


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Epilepsy.this, Option.class));

            }
        });

    }
}
