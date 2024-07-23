package com.example.addictionrecovery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class UpdateMail extends AppCompatActivity {

    TextView title;
    EditText updateMail;
    String newEmail;
    ImageView back_button;
    Button update_button;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db ;

    public void updateEmail(String newEmail) {
        if(!newEmail.isEmpty()){
            if (user != null) {
                user.verifyBeforeUpdateEmail(newEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //sendVerificationEmail(newEmail);
                                Log.d("UpdateEmail", "Email updated successfully.");
                                showSuccessDialog();
                            } else {
                                Log.w("UpdateEmail", "Failed to update email.", task.getException());
                                showErrorDialog();
                            }
                        });
            } else {
                Log.w("UpdateEmail", "User is null.");
            }
        }
        else {emptyErrorDialog();}
    }
    public void addToDatabaseNewEmail(String uid) {
        Map<String, Object> update_user = new HashMap<>();
        update_user.put("Email", updateMail.getText().toString());

            if(user.isEmailVerified() && !updateMail.getText().toString().isEmpty()){
                db.collection("users").document(uid)
                        .update(update_user) // Belgeyi güncelle veya oluştur
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("addToDatabase", "Email updated successfully.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("addToDatabase", "Failed to update email.", e);
                            }
                        });

            }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_update);

        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        title=findViewById(R.id.title);
        updateMail=findViewById(R.id.updateName);
        update_button=findViewById(R.id.update_button);
        back_button=findViewById(R.id.back_icon);

        updateMail.setHint("Email giriniz..");
        updateMail.setHintTextColor(Color.parseColor("#BBAB8C"));
        updateMail.setTextColor(Color.parseColor("#9B4444"));
        title.setText("Email Güncelleme");

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEmail = updateMail.getText().toString();
                updateEmail(newEmail);
                if(mAuth.getCurrentUser().isEmailVerified()){addToDatabaseNewEmail(mAuth.getCurrentUser().getUid());}
                updateMail.setText("");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMail.this);
        builder.setMessage("Kimlik doğrulaması gönderildi. Mail adresinizi kontrol ediniz ve tekrar giriş yapınız.");

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMail.this);
        builder.setMessage("Email güncellenirken bir hata oluştu. Tekrar giriş yapın.");

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMail.this);
        builder.setMessage("Lütfen email adresiniz giriniz.");

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
