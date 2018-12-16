package com.example.mobeensohail.firstproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class Dairy extends AppCompatActivity {

    private EditText mmedication,mexp,mdoctor,mfreq;
    private Button Btnupdate,btnNext;
    private Firebase mRootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);

        mRootRef= new Firebase("https://mobeenproject.firebaseio.com/Epilepsy");
        mmedication = (EditText)findViewById(R.id.etop);
        mexp= (EditText)findViewById(R.id.etdays);
        mdoctor = (EditText)findViewById(R.id.etdoctor);
        mfreq=(EditText)findViewById(R.id.etfrequ);
        btnNext=(Button)findViewById(R.id.BTNnext);

        Btnupdate=(Button)findViewById(R.id.btnupd);

        Btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mmedication.getText().toString();
                String value1=mexp.getText().toString();
                String value2=mdoctor.getText().toString();
                String value3=mfreq.getText().toString();
                // Firebase childRef = mRootRef.child("Medication").child("Expereance").child("Doctor").child("Frequency");
                //childRef.setValue(value);
                //childRef.setValue(value1);
                //childRef.setValue(value2);
                //childRef.setValue(value3);
                mRootRef.child("Medication").push().setValue(value);
                mRootRef.child("Experiance").push().setValue(value1);
                mRootRef.child("Doctor").push().setValue(value2);
                mRootRef.child("Frequency").push().setValue(value3);


            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dairy.this,Option.class));
            }
        });

    }
}
