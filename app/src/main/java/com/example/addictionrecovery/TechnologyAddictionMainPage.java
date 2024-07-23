package com.example.addictionrecovery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TechnologyAddictionMainPage extends AppCompatActivity {
    RelativeLayout relativeLayout;
    FirebaseAuth auth;
    GridView gridView1,gridView2;
    String [] symptoms,testTitles;
    Toolbar tb;
    ImageView backIcon, navIcon;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology_addiction_main_page);

        relativeLayout=findViewById(R.id.technology_addiction_main_page_layout);
        auth=FirebaseAuth.getInstance();

        gridView1=findViewById(R.id.tech_grid1);
        symptoms=getResources().getStringArray(R.array.tech_diagnose_conditions);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this,R.layout.tech_grid_item,symptoms);
        gridView1.setAdapter(arrayAdapter);

        gridView2=(GridView) findViewById(R.id.tech_grid2);
        testTitles= getResources().getStringArray(R.array.tech_add_tests);
        ArrayAdapter<String>arrayAdapter2= new ArrayAdapter<>(this,R.layout.tech_addiction_test_grid,testTitles);
        gridView2.setAdapter(arrayAdapter2);


        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Sadece ilk öğeye tıklandığında işlem yap
                if (position == 0) {
                    Intent intent = new Intent(TechnologyAddictionMainPage.this, TechnologyAddictionTestPage.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(TechnologyAddictionMainPage.this, TechnologyAddictionTest2Page.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(TechnologyAddictionMainPage.this, TechnologyAddictionTest3Page.class);
                    startActivity(intent);
                }
            }
        });

        toolBarArrangement();
        goToMainPage();
        drawerInitialization();
        navBottomArrangements();
        relativeLayoutClickerEnable();
        getUserName(userName -> setUserName(userName));

    }
    public void toolBarArrangement(){
        tb=(Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("Teknoloji Bağımlılığı");
    }

    public void goToMainPage(){
        backIcon=(ImageView) findViewById(R.id.back_to_main_page);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TechnologyAddictionMainPage.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    public void getUserName(final OnUserNameFetchedListener listener) {
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

    public interface OnUserNameFetchedListener {
        void onUserNameFetched(String userName);
    }

    public void setUserName(String userName) {
        if (userName != null) {
            TextView user_name = findViewById(R.id.user_name);
            user_name.setText(userName);
        }
    }

    public void drawerInitialization(){
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setVisibility(View.INVISIBLE);

        navIcon=(ImageView) findViewById(R.id.navigation_icon);
        navIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(navigationView.getVisibility()==View.INVISIBLE){
                    navigationView.setVisibility(View.VISIBLE);

                }
                else{
                    navigationView.setVisibility(View.INVISIBLE);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.kullanici_profili_option:
                        Intent intent= new Intent( TechnologyAddictionMainPage.this,user_profile.class);
                        startActivity(intent);
                        break;
                    case R.id.neyi_amacliyoruz_option:
                        Intent intent2= new Intent( TechnologyAddictionMainPage.this,Purpose.class);
                        startActivity(intent2);
                        break;
                    case R.id.geri_bildirim_option:
                        Intent intent3 = new Intent(TechnologyAddictionMainPage.this, Feedback.class);
                        startActivity(intent3);
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

    public void logout(){
        auth.signOut();
        Intent intent= new Intent(TechnologyAddictionMainPage.this, LoginPage.class);
        startActivity(intent);
        finish();
    }

    public void navBottomArrangements(){

        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_nav_id);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId= item.getItemId();
                switch (itemId){
                    case R.id.home_tab:
                        System.out.println("home");
                        Intent intent= new Intent(TechnologyAddictionMainPage.this,HomePage.class);
                        startActivity(intent);
                        break;
                    case R.id.questions_tab:
                        System.out.println("questions");
                        Intent intent2= new Intent(TechnologyAddictionMainPage.this,TechnologyAddictionQuestionsPage.class);
                        startActivity(intent2);
                        break;
                    case R.id.videos_tab:
                        System.out.println("videos");
                        Intent intent3= new Intent(TechnologyAddictionMainPage.this,TechnologyAddictionVideosPage.class);
                        startActivity(intent3);
                        break;
                    case R.id.support_tab:
                        System.out.println("support");
                        Intent intent4= new Intent(TechnologyAddictionMainPage.this,TechnologyAddictionSupportPage.class);
                        startActivity(intent4);
                        break;
                    default:

                        break;
                }

                return false;
            }
        });
    }

    public void relativeLayoutClickerEnable(){

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigationView.getVisibility()==View.VISIBLE){
                    navigationView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onBackPressed() {
        // Yeni aktiviteye geçiş yapmak için Intent oluştur
        super.onBackPressed();
        Intent intent = new Intent(this, HomePage.class);
        // Yeni aktiviteyi başlat
        startActivity(intent);
        // Mevcut aktiviteyi sonlandır
        finish();
    }
}
