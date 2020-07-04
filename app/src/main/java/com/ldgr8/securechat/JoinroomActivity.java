package com.ldgr8.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JoinroomActivity extends AppCompatActivity {

    private EditText joinroom_roomname, joinroom_roompass;
    private Button joinroom_joinroom;

    private List<Room> all_activerooms;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinroom);

        Init();
    }

    public void Init() {

        //firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("activerooms");

        // view init
        joinroom_roomname = findViewById(R.id.joinroom_roomname);
        joinroom_roompass = findViewById(R.id.joinroom_roompass);
        joinroom_joinroom = findViewById(R.id.joinroom_joinroom);

        joinroom_joinroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJoinroom_joinroom();
            }
        });

        //vars init
        all_activerooms = new LinkedList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> mp = (Map<String, Object>) ds.getValue();
                    String roomname = (String) mp.get("roomname");
                    String roompass = (String) mp.get("roompass");
                    String roomID = ds.getKey();
                    Room room = new Room(roomname,roompass);
                    room.setRoomid(roomID);
                    all_activerooms.add(room);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setJoinroom_joinroom() {
        String room = joinroom_roomname.getText().toString().trim();
        String pass = joinroom_roompass.getText().toString().trim();
        boolean found=false;

        if(room.isEmpty() || pass.isEmpty())
            Toast.makeText(this, "please fill out all fields", Toast.LENGTH_SHORT).show();
            else{
                for(Room r : all_activerooms)
                {
                    if(room.equals(r.getRoomname()))
                    {
                        found=true;
                        if(pass.equals(r.getRoompass()))
                        {
                            startChat(r.getRoomid());
                        }
                        else
                        {
                            Toast.makeText(this, "wrong password entered", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if(!found)
            Toast.makeText(this, "this room doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    public void startChat(String roomID)
    {
        Intent intent = new Intent(JoinroomActivity.this,ChatActivity.class);
        intent.putExtra("roomID",roomID);
        String nickname = getIntent().getStringExtra("username");
        intent.putExtra("nickname",nickname);
        intent.putExtra("roomcreater","false");
        startActivity(intent);
        finish();
    }
}
