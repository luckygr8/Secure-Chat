package com.ldgr8.securechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final TextInputEditText nickname, email, pass, confpass;
        nickname = findViewById(R.id.nickname_signup);
        email = findViewById(R.id.email_signup);
        pass = findViewById(R.id.password_signup);
        confpass = findViewById(R.id.confirmpass_signup);

        final MaterialButton saveandcontinue = findViewById(R.id.saveandcontinue);
        saveandcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String strname = nickname.getText().toString().trim();
                final String stremail = email.getText().toString().trim();
                String strpass = pass.getText().toString().trim();
                String strconfpass = confpass.getText().toString().trim();

                if (strname.isEmpty() || stremail.isEmpty() || strpass.isEmpty() || strconfpass.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!strpass.equals(strconfpass)) {
                    Toast.makeText(SignupActivity.this, "both password should match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(stremail, strpass)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(SignupActivity.this, "use your new sign in details to log in", Toast.LENGTH_LONG).show();
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = firebaseDatabase.getReference().child("users").child(getRandomStringID(15));
                                    myRef.setValue(new User(strname,stremail));

                                    /*boolean result = new LocalDB(Signupactivity.this).AddNickname(stremail,strname);
                                    if(result)
                                        System.out.println("nickname added");
                                    else
                                        System.out.println("failed");*/
                                    finish();

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
                                                message = "some unknown error has occurred";
                                        }
                                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
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
}
