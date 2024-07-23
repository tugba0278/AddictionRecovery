package com.example.addictionrecovery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdatePassword extends AppCompatActivity {
    EditText updatePassword;
    EditText repeatPassword;
    ImageView back_button;
    Button update_button;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db ;

    public void updatePassword() {
        String newPassword = updatePassword.getText().toString();
        String repPassword = repeatPassword.getText().toString();

       if (user != null &&  newPassword.length() >= 6 && repPassword.length() >= 6  && newPassword.equals(repPassword)) { // Minimum şifre uzunluğu 6 karakter olmalıdır
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()  ) {
                    Log.d("UpdatePassword", "Password updated successfully.");
                    showSuccessDialog();
                }
                else {
                    Log.w("UpdatePassword", "Failed to update password.", task.getException());
                    showErrorDialog();
                }
            });
        } else {
            Log.w("UpdatePassword", "Invalid password or user is not signed in.");
            emptyErrorDialog();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_password_update);

        mAuth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        updatePassword=findViewById(R.id.updatePassword);
        repeatPassword=findViewById(R.id.repeatPassword);
        update_button=findViewById(R.id.update_button);
        back_button=findViewById(R.id.back_icon);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
                updatePassword.setText("");
                repeatPassword.setText("");
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }
        });

    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePassword.this);
        builder.setMessage("Şifre başarılı olarak güncellendi.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePassword.this);
        builder.setMessage("Şifre güncellenirken bir hata oluştu.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void emptyErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePassword.this);
        builder.setMessage("Minimum 6 karakterli bir şifre giriniz ve şifrelerin aynı olduğundan emin olunuz.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
