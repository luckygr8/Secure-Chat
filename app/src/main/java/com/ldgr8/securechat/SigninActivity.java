package com.ldgr8.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mauthStateListener;

    private Iterable<DataSnapshot> datasnap;
    private List<User> allusers = new LinkedList<>();
    private List<String> signedusers = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        MaterialButton join = findViewById(R.id.join);
        final TextInputEditText email = findViewById(R.id.email);
        final TextInputEditText password = findViewById(R.id.password);
        final TextView signup = findViewById(R.id.signup);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userinfo = database.getReference().child("users");
        DatabaseReference signedinusers = database.getReference().child("signedinusers");

        userinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datasnap = dataSnapshot.getChildren();

                for(DataSnapshot ds : datasnap)
                {
                    Map<String,Object> mp = (Map<String, Object>) ds.getValue();
                    String nickname = (String) mp.get("nickname");
                    String email = (String) mp.get("email");
                    allusers.add(new User(nickname,email));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        signedinusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Map<String,Object> mp = (Map<String, Object>) ds.getValue();
                    String email = (String) mp.get("email");
                    signedusers.add(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String useremail = email.getText().toString().trim();
                String userpass = password.getText().toString().trim();

                if (useremail.isEmpty() || userpass.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "please fill out all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(String signuser : signedusers)
                {
                    if(signuser.equals(useremail))
                    {
                        Toast.makeText(SigninActivity.this, "this user is already signed in", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                mAuth.signInWithEmailAndPassword(useremail, userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            //String nickname = new LocalDB(SignInactivity.this).getnickname(user.getEmail());
                            String nickname="";
                            for(User u : allusers)
                            {
                                if(u.getEmail().equals(user.getEmail()))
                                {
                                    nickname=u.getNickname();
                                    break;
                                }
                            }
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            String random = getRandomStringID(15);
                            DatabaseReference myRef = firebaseDatabase.getReference().child("signedinusers").child(random);
                            myRef.setValue(new Email(user.getEmail()));
                            openMain(nickname , random);
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthException) {
                                String code = ((FirebaseAuthException) e).getErrorCode();
                                String message = "";
                                switch (code) {
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        message = "email already exists";
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        message = "email and password dont match";
                                        break;
                                    case "ERROR_WEAK_PASSWORD":
                                        message = "password seems too weak , try again";
                                        break;
                                    case "ERROR_INVALID_EMAIL":
                                        message="email is badly formatted";
                                        break;
                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        message = "account exists with different credentials";
                                        break;
                                    case "ERROR_USER_DISABLED":
                                        message="The user account has been disabled by an administrator";
                                        break;
                                    default:
                                        message = "Authentication failed";
                                }
                                Toast.makeText(SigninActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }

    public String getRandomStringID(int n) {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public void openMain(String username , String random) {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("signedinrandom",random);
        startActivity(intent);
        finish();
    }
}
