package com.example.addictionrecovery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class drawer_menu extends AppCompatActivity {
    TextView user_name;
    FirebaseFirestore db;

    DocumentReference docRef  ;
    FirebaseAuth auth;
    FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.drawer_header);

        user_name=findViewById(R.id.user_name);
        db = FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        docRef = db.collection("users").document(user.getUid());


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("Name");
                        if (name != null) {
                            // TextView'e atama işlemi
                            Log.d("drawer_menu", "Name alanı not null.");
                            user_name.setText(name);
                        } else {
                            Log.d("drawer_menu", "Name alanı null.");
                        }

                    } else {
                        Log.d("drawer_menu", "Doküman bulunamadı");
                    }
                } else {
                    Log.d("drawer_menu", "Belge alınamadı: ", task.getException());
                }
            }
        });
    }
}
