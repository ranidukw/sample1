package com.example.dinetime.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dinetime.R;
import com.example.dinetime.Splash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
public class Login extends AppCompatActivity {

    TextView register;
    EditText email,password;
    AppCompatButton btn_login;
    RadioButton admin,user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SetupUI();
        ButtonClicks();
        Login();

    }


    private void SetupUI(){

        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        user = findViewById(R.id.radio1);
        admin = findViewById(R.id.radio2);
        mAuth = FirebaseAuth.getInstance();
    }

    private void ButtonClicks(){

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Login(){

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user.isChecked()){
                    Log.e("UserTypeCheck","User is checked");
                    UserLogin();
                }
                else if(admin.isChecked()){
                    Log.e("UserTypeCheck","Admin is checked");
                    AdminLogin();
                }
                else {
                    Toast.makeText(Login.this, "Please Check User Type", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void AdminLogin(){

        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        DatabaseReference redRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        if(Email.isEmpty() || Password.isEmpty()){
            Toast.makeText(Login.this, "Please Fill the all fields", Toast.LENGTH_SHORT).show();
        }
        else {

            redRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChildren()){
                        String em = snapshot.child("Email").getValue().toString();
                        String ps = snapshot.child("Password").getValue().toString();

                        if (em.contains(Email) && (ps.contains(Password))) {
                            startActivity(new Intent(getApplicationContext(), AdminHome.class));
                            finish();
                            Toast.makeText(Login.this, "Logged to the Admin Account", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login.this, "Login Failed | Please Check Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    private void UserLogin(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if(Email.isEmpty() || Password.isEmpty()){
            Toast.makeText(Login.this, "Please Fill the all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Activity_Bottom_navigation.class));
                        mAuth.getUid();
                        Log.e("CheckUID",""+mAuth.getUid());
                        Log.e("CheckName",""+ Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
                        Log.e("CheckPhone",""+ Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber());
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

