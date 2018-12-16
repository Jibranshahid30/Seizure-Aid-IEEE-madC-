package com.example.mobeensohail.firstproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Option extends AppCompatActivity {

    Button diary,epilepsy,contect,fft;
    private FirebaseAuth firebaseAuth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        diary=(Button)findViewById(R.id.BTN1);
        epilepsy=(Button)findViewById(R.id.BTN2);
        contect=(Button)findViewById(R.id.BTN3);
        fft=(Button)findViewById(R.id.button4);
        firebaseAuth1= FirebaseAuth.getInstance();

        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Option.this,Dairy.class));
            }
        });

        epilepsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Option.this,Epilepsy.class));
            }
        });

        contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Option.this,Contect.class));
            }
        });


        fft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Option.this, FftActivity.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    private void Logout() {
        firebaseAuth1.signOut();
        finish();
        startActivity(new Intent(Option.this, MainActivity.class));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.logoutMenu: {
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

