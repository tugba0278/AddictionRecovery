package com.example.addictionrecovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    RelativeLayout homePageLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navIcon;
    TextView generalAddiction, substanceAddiction, techAddiction,alcoholAddiction;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Firebase Authentication örneği alma
        auth = FirebaseAuth.getInstance();

        // Ana layout ve sayfa butonları tanımlamaları
        homePageLayout = findViewById(R.id.homepage_layout);
        generalAddiction = findViewById(R.id.general_addiction_btn);
        substanceAddiction = findViewById(R.id.drug_addiction_btn);
        techAddiction = findViewById(R.id.tech_addiction_btn);
        alcoholAddiction = findViewById(R.id.alcohol_addiction_btn);

        drawerActions(); // Drawer işlemleri
        homePageClickerEnable(); // Ana sayfa tıklama işlemleri

        // Sayfa butonlarına tıklama işlemleri atanıyor
        generalAddiction.setOnClickListener(new PageButtonsListener());
        substanceAddiction.setOnClickListener(new PageButtonsListener());
        techAddiction.setOnClickListener(new PageButtonsListener());
        alcoholAddiction.setOnClickListener(new PageButtonsListener());
        getUserName(userName -> setUserName(userName));
    }

    // Çıkış işlemini gerçekleştiren metot
    private void logout() {
        // Firebase'den çıkış yap
        auth.signOut();

        // Giriş sayfasına yönlendir
        Intent intent = new Intent(HomePage.this, LoginPage.class);
        startActivity(intent);
        finish(); // Bu aktiviteyi kapat
    }
    private void showLogoutConfirmationDialog() {
        // AlertDialog oluştur
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Çıkış yapmak istediğinize emin misiniz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // İptal durumu, bir şey yapmaya gerek yok
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void getUserName(final TechnologyAddictionMainPage.OnUserNameFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String name = document.getString("Name");
                    if (name != null) {
                        Log.d("drawer_menu", "Name alanı not null.");
                        // İsim başarıyla alındı, dinleyiciye iletilir.
                        listener.onUserNameFetched(name);
                    } else {
                        Log.d("drawer_menu", "Name alanı null.");
                        listener.onUserNameFetched(null);
                    }
                } else {
                    Log.d("drawer_menu", "Doküman bulunamadı");
                    listener.onUserNameFetched(null);
                }
            } else {
                Log.d("drawer_menu", "Belge alınamadı: ", task.getException());
                listener.onUserNameFetched(null);
            }
        });
    }

    public void setUserName(String userName) {
        if (userName != null) {
            TextView user_name = findViewById(R.id.user_name);
            user_name.setText(userName);
        }
    }

    // Drawer işlemleri
    public void drawerActions() {
        // Drawer menu ile alakalı atamalar
        navigationView = findViewById(R.id.nav_view);
        navigationView.setVisibility(View.INVISIBLE);
        drawerLayout = findViewById(R.id.my_drawer_layout);

        navIcon = findViewById(R.id.navigation_icon);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationView.getVisibility() == View.INVISIBLE) {
                    navigationView.setVisibility(View.VISIBLE);
                } else {
                    navigationView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Çıkış yap butonuna tıklama işlemi atanıyor
        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.cikis_yap_option) { // Çıkış yap öğesi seçildiyse
                    // Çıkış işlemini gerçekleştir
                    logout();
                    return true;
                }
                return false;
            }
        });*/

        //Switch case daha kolay olur diye switch case e çevirdim
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.kullanici_profili_option:
                        Intent i = new Intent(HomePage.this, user_profile.class);
                        startActivity(i);
                        break;
                    case R.id.neyi_amacliyoruz_option:
                        Intent j = new Intent(HomePage.this, Purpose.class);
                        startActivity(j);
                        break;

                    case R.id.geri_bildirim_option:
                        Intent intent = new Intent(HomePage.this, Feedback.class);
                        startActivity(intent);
                        break;
                    case R.id.cikis_yap_option:
                        showLogoutConfirmationDialog();
                        break;
                    default:
                        return true;
                }
                return  false;
            }
        });
    }

    // Ana sayfa tıklama işlemleri
    public void homePageClickerEnable() {
        homePageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationView.getVisibility() == View.VISIBLE) {
                    navigationView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // Sayfa butonları tıklama işlemleri
    public class PageButtonsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.general_addiction_btn:
                    Intent intent = new Intent(HomePage.this, GeneralAddictionMainPage.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.drug_addiction_btn:
                    //pass
                    Intent intent2=new Intent(HomePage.this, SubstanceAddictionMainPage.class);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.tech_addiction_btn:
                    Intent intent3=new Intent(HomePage.this, TechnologyAddictionMainPage.class);
                    startActivity(intent3);
                    finish();
                    break;
                case R.id.alcohol_addiction_btn:
                    Intent intent4=new Intent(HomePage.this, AlcoholAddictionMainPage.class);
                    startActivity(intent4);
                    finish();
                    break;
                default:
                    System.out.println("Nothing Clicked");
                    break;
            }
        }
    }
}

