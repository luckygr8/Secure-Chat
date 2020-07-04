package com.ldgr8.securechat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity /* CREATE OR JOIN ROOM ACTIVITY */ {

    private MaterialButton createroom,joinroom;
    private Runnable onstop;
    private Handler handler;
    private int TIME_OUT = 10000;
    private String signedinrandom;
    private boolean USERSIGNIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

    }

    public void Init()
    {
        FirebaseApp.initializeApp(this);

        createroom = findViewById(R.id.createroom);
        joinroom = findViewById(R.id.joinroom);

        signedinrandom = getIntent().getStringExtra("signedinrandom");

        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateroom();
            }
        });

        joinroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJoinroom();
            }
        });

        handler = new Handler();
        USERSIGNIN=false;

        onstop = new Runnable() {
            @Override
            public void run() {
                Logout();
            }
        };
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "press back again to log out and exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        USERSIGNIN=false;
        handler.removeCallbacks(onstop);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!USERSIGNIN)
        {
            if (doubleBackToExitPressedOnce) {

                Logout();
            } else {
                Toast.makeText(this, "you will be signed out automatically in " + TIME_OUT / 1000 + " seconds", Toast.LENGTH_LONG).show();
                handler.postDelayed(onstop, TIME_OUT);
            }
        }
    }

    public void Logout() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef ;
        myRef = database.getReference().child("signedinusers");
        myRef.child(signedinrandom).removeValue();
        finish();
    }

    public void setCreateroom() {
        USERSIGNIN=true;
        Intent intent = new Intent(MainActivity.this,CreateRoomActivity.class);
        String username = getIntent().getStringExtra("username");
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void setJoinroom() {
        USERSIGNIN=true;
        Intent intent = new Intent(MainActivity.this,JoinroomActivity.class);
        String username = getIntent().getStringExtra("username");
        intent.putExtra("username",username);
        startActivity(intent);
    }

}
