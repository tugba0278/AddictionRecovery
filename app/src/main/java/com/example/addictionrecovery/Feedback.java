package com.example.addictionrecovery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    ImageView star1, star2, star3, star4, star5,back_button;
    Button feedback_send_button;
    EditText feedback_text;
    int rating = 0; // Tıklanan yıldız sayısını tutmak için değişken
    FirebaseAuth mAuth;
    FirebaseFirestore db ;

    public void getToDocument(String uid){

        db.collection("users").document(uid).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("Name"); // Firestore'dan alınan kullanıcı adı
                        String email=documentSnapshot.getString("Email");
                        // addToDatabase metodunu çağır
                        addToDatabase(username,email, uid);
                        feedback_text.setText("");

                    } else {
                        Log.d("getUserInfo", "No such document");
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("getUserInfo", "Error getting document: ", e);
                }
            });}

    public void addToDatabase(String username,String email,String uid) {
        // Create a new user with a first and last name
        Map<String, Object> feedback = new HashMap<String, Object>();

        feedback.put("Name", username); // Kullanıcı adı
        feedback.put("Email", email); // Kullanıcı e-posta adresi
        feedback.put("Comment", feedback_text.getText().toString());
        feedback.put("Score", rating);
        feedback.put("OwnerId", uid);
            if(!feedback_text.getText().toString().isEmpty() && rating!=0) {
                // Add a new document with a generated ID
                db.collection("users").document(uid)
                        .set(feedback)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("addToDatabase", "DocumentSnapshot added");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("addToDatabase", "Error adding document", e);
                            }
                        });
                showSuccessDialog();
            }
            else if(feedback_text.getText().toString().isEmpty() && rating==0){emptyErrorDialog();}
            else if(rating==0){emptyRatingDialog();}
            else if(feedback_text.getText().toString().isEmpty() ){emptyCommentDialog();}
            rating=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        mAuth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        feedback_send_button=findViewById(R.id.feedback_send);
        feedback_text=findViewById(R.id.feedback);
        back_button=findViewById(R.id.back_to_mainpage_icon);

        // Yıldızlara tıklanınca sayısını güncelleme
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 1) {
                    rating = 0; // Yıldızın tıklanması geri alındı
                } else {
                    rating = 1; // Tıklanan yıldız sayısını güncelle
                }
                updateStars();
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 2) {
                    rating = 0; // Yıldızın tıklanması geri alındı
                } else {
                    rating = 2; // Tıklanan yıldız sayısını güncelle
                }
                updateStars();
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 3) {
                    rating = 0; // Yıldızın tıklanması geri alındı
                } else {
                    rating = 3; // Tıklanan yıldız sayısını güncelle
                }
                updateStars();
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 4) {
                    rating = 0; // Yıldızın tıklanması geri alındı
                } else {
                    rating = 4; // Tıklanan yıldız sayısını güncelle
                }
                updateStars();
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 5) {
                    rating = 0; // Yıldızın tıklanması geri alındı
                } else {
                    rating = 5; // Tıklanan yıldız sayısını güncelle
                }
                updateStars();
            }
        });
        feedback_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToDocument(mAuth.getUid());

                star1.setImageResource(R.mipmap.emptystar);
                star2.setImageResource(R.mipmap.emptystar);
                star3.setImageResource(R.mipmap.emptystar);
                star4.setImageResource(R.mipmap.emptystar);
                star5.setImageResource(R.mipmap.emptystar);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });
    }

    // Yıldızları güncelleyen metot
    private void updateStars() {
        // Tüm yıldızları varsayılan (boş) durumda ayarlama
        star1.setImageResource(R.mipmap.emptystar);
        star2.setImageResource(R.mipmap.emptystar);
        star3.setImageResource(R.mipmap.emptystar);
        star4.setImageResource(R.mipmap.emptystar);
        star5.setImageResource(R.mipmap.emptystar);

        // Tıklanan yıldıza göre dolu yıldızları ayarlama
        switch (rating) {
            case 1:
                star1.setImageResource(R.mipmap.fillstar);
                break;
            case 2:
                star1.setImageResource(R.mipmap.fillstar);
                star2.setImageResource(R.mipmap.fillstar);
                break;
            case 3:
                star1.setImageResource(R.mipmap.fillstar);
                star2.setImageResource(R.mipmap.fillstar);
                star3.setImageResource(R.mipmap.fillstar);
                break;
            case 4:
                star1.setImageResource(R.mipmap.fillstar);
                star2.setImageResource(R.mipmap.fillstar);
                star3.setImageResource(R.mipmap.fillstar);
                star4.setImageResource(R.mipmap.fillstar);
                break;
            case 5:
                star1.setImageResource(R.mipmap.fillstar);
                star2.setImageResource(R.mipmap.fillstar);
                star3.setImageResource(R.mipmap.fillstar);
                star4.setImageResource(R.mipmap.fillstar);
                star5.setImageResource(R.mipmap.fillstar);
                break;
            default:
                break;
        }
    }

    private void emptyRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);
        builder.setMessage("Lütfen puanlama yapınız.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void emptyCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);
        builder.setMessage("Lütfen yorumu boş bırakmayınız.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);
        builder.setMessage("Yorum ve puanlamanız gönderildi.");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);
        builder.setMessage("Lütfen puanlamayı ve yorumu boş bırakmayınız.");

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
