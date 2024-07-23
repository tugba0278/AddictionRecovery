package com.example.addictionrecovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button loginButton, registerButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginPage.this, WelcomePage.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        mAuth = FirebaseAuth.getInstance();
        initialize();

    }

    public void initialize() {

        progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.eposta_edit);
        userPassword = findViewById(R.id.sifre_edit);
        loginButton = findViewById(R.id.giris_yap);
        registerButton = findViewById(R.id.kayit_ol);
        loginButton.setOnClickListener(new SetOnClickListenerLogin());
        registerButton.setOnClickListener(new SetOnClickListenerRegister());
    }

    public class SetOnClickListenerLogin implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            String email = String.valueOf(userEmail.getText());
            String password = String.valueOf(userPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginPage.this, "Login Successful.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginPage.this, WelcomePage.class);
                                startActivity(intent);
                                //finish();
                            } else {
                                Toast.makeText(LoginPage.this, "Authentication failed: incorrect email or password " ,
                                        Toast.LENGTH_SHORT).show();
                                Log.e("LoginPage", "Kullanıcı girişi başarısız oldu: " );
                                userEmail.setText("");
                                userPassword.setText("");
                            }
                        }
                    });
        }
    }

    public class SetOnClickListenerRegister implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent= new Intent(getApplicationContext(),RegisterPage.class);
            startActivity(intent);
        }
    }
 }
