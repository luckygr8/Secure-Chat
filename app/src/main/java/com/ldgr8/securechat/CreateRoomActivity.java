package com.ldgr8.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateRoomActivity extends AppCompatActivity {

    private TextInputEditText createroom_TIED_roomname, createroom_TIED_roompass;
    private MaterialButton createroom_createroom;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<String> all_activerooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_create_room);

        Init();
    }

    public void Init() {

        //firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("activerooms");

        // view init
        createroom_TIED_roomname = findViewById(R.id.createroom_TIED_roomname);
        createroom_TIED_roompass = findViewById(R.id.createroom_TIED_roompass);
        createroom_createroom = findViewById(R.id.createroom_createroom);

        // var init
        all_activerooms = new LinkedList<>();

        // Listners init
        createroom_createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateroom_createroom();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> mp = (Map<String, Object>) ds.getValue();
                    String roomname = (String) mp.get("roomname");
                    all_activerooms.add(roomname);
                }
            }

        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    });
}

    public void setCreateroom_createroom() {

        String roomname = createroom_TIED_roomname.getText().toString().trim();
        String roompass = createroom_TIED_roompass.getText().toString().trim();

        if (roomname.isEmpty() || roompass.isEmpty())
            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();
        else {

            for(String room : all_activerooms)
            {
                if(roomname.equals(room))
                {
                    Toast.makeText(CreateRoomActivity.this, "Another room with the same name already exists", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            String ID = Util.getRandomID(15);
            databaseReference.child(ID).setValue(new Room(roomname,roompass));

            startChat(ID);
        }


    }

    public void startChat(String roomID)
    {
        Intent intent = new Intent(CreateRoomActivity.this , ChatActivity.class);
        intent.putExtra("roomID",roomID);
        String nickname = getIntent().getStringExtra("username");
        intent.putExtra("nickname",nickname);
        intent.putExtra("roomcreater","true");

        startActivity(intent);
        finish();
    }
}
