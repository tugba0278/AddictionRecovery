package com.example.addictionrecovery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateName extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db ;
    EditText updateName;
    Button update_button;
    ImageView back_button;

    public void updateName(String uid) {
        // Yorum ve puanlama verisini oluştur
        Map<String, Object> update_user = new HashMap<>();
        update_user.put("Name", updateName.getText().toString()); // Yorum

        if(!updateName.getText().toString().isEmpty()) {
            // Belgeyi güncelle veya oluştur
            db.collection("users").document(uid)
                    .update(update_user) // Belgeyi güncelle veya oluştur
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("UpdateName", "Name updated successfully.");
                            showSuccessDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("UpdateName", "Failed to update name.", e);
                            showErrorDialog();
                        }
                    });
        }
        else{emptyErrorDialog();}
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_update);

        mAuth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        updateName=findViewById(R.id.updateName);
        update_button=findViewById(R.id.update_button);
        back_button=findViewById(R.id.back_icon);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(mAuth.getUid());
                updateName.setText("");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateName.this);
        builder.setMessage("Ad soyad başarılı olarak güncellendi.");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateName.this);
        builder.setMessage("Ad soyad güncellenirken bir hata oluştu.");

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateName.this);
        builder.setMessage("Lütfen ad soyad giriniz.");

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
