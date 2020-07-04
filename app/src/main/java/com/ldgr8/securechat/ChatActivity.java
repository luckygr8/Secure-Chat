package com.ldgr8.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String isroomcreator;

    private String nickname;
    private String RoomID;
    private String PersonID;

    private Runnable onstop;
    private Handler handler;
    private int TIME_OUT = 20000; // 20 seconds

    private EditText ET;
    private RecyclerView message_area;
    //private ImageView more_options;
    private ChatRecylerAdapter recycler;
    FloatingActionButton but;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Init();

        joinRoom();

        setBut();

        setChatref();
    }

    public void setChatref() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, Object> mp = (Map<String, Object>) dataSnapshot.getValue();
                recycler.AddnewUser(mp.get("nickname").toString());
                Toast.makeText(ChatActivity.this, mp.get("nickname").toString() + " has joined the chat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, Object> mp = (Map<String, Object>) dataSnapshot.getValue();
                String sendername = mp.get("nickname").toString();
                String message = mp.get("message").toString();
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("hh.mm");
                String time = sdf.format(date);
                if (sendername.equals(nickname)) {
                    recycler.AddIntoMessageList(new You(message, time));
                } else {
                    recycler.AddIntoMessageList(new Sender(sendername, message, time));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> mp = (Map<String, Object>) dataSnapshot.getValue();
                Toast.makeText(ChatActivity.this, mp.get("nickname").toString() + " left the chat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Init() {
        //firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();

        // var init
        nickname = getIntent().getStringExtra("nickname");
        RoomID = getIntent().getStringExtra("roomID");
        isroomcreator = getIntent().getStringExtra("roomcreater");
        PersonID = Util.getRandomID(20);
        databaseReference = firebaseDatabase.getReference("chat").child(RoomID);

        handler = new Handler();

        onstop = new Runnable() {
            @Override
            public void run() {
                logout();
            }
        };

        //view init
        ET = findViewById(R.id.message);
        message_area = findViewById(R.id.message_area);
        recycler = new ChatRecylerAdapter(this, message_area);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        message_area.setLayoutManager(lmg);
        message_area.setAdapter(recycler);
        but = findViewById(R.id.but);

    }

    public void joinRoom() {
        //databaseReference.child(RoomID).child(PersonID).setValue(new Person(nickname, ""));
        databaseReference.child(PersonID).setValue(new Person(nickname, ""));
    }

    @Override
    protected void onStop() {
        super.onStop();
        super.onStop();
        if (doubleBackToExitPressedOnce) {
            logout();
        } else {
            Toast.makeText(this, "you will be removed from the room in " + TIME_OUT / 1000 + " seconds", Toast.LENGTH_LONG).show();
            handler.postDelayed(onstop, TIME_OUT);
        }
    }

    public void logout() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("chat").child(RoomID);
        myRef.child(PersonID).removeValue();

        /*if (isroomcreator.equals("true")) {
            myRef = database.getReference().child("activerooms");
            myRef.child(RoomID).removeValue();
        }*/
        finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "press back again to leave the room", Toast.LENGTH_SHORT).show();

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
        handler.removeCallbacks(onstop);

    }

    public void setBut() {
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messgage = ET.getText().toString().trim();
                if (messgage.isEmpty())
                    return;
                databaseReference.child(PersonID).setValue(new Message(nickname, messgage));
                ET.setText("");
            }
        });
    }
}
