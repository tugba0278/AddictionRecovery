package com.example.addictionrecovery;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SubstanceAddictionMainPage extends AppCompatActivity {
    Toolbar tb;
    NavigationView navigationView;

    FirebaseAuth auth;
    RelativeLayout relativeLayout;

    ImageView navIcon,backIcon;
    BottomNavigationView bottomNavigationView;

    GridView gridView1,gridView2;
    String [] symptoms, testTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substance_addiction_main_page);
        relativeLayout=(RelativeLayout) findViewById(R.id.substance_addiction_main_page_layout);
        auth=FirebaseAuth.getInstance();

        gridView1=(GridView) findViewById(R.id.subs_grid1);
        symptoms=getResources().getStringArray(R.array.subs_diagnose_conditions);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this,R.layout.subs_grid_item,symptoms);
        gridView1.setAdapter(arrayAdapter);

        gridView2=(GridView) findViewById(R.id.subs_grid2);
        testTitles= getResources().getStringArray(R.array.subs_add_tests);
        ArrayAdapter<String>arrayAdapter2= new ArrayAdapter<>(this,R.layout.subs_addiction_test_grid,testTitles);
        gridView2.setAdapter(arrayAdapter2);

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan öğenin sırasını bastır
                switch (position){
                    case 0:
                        Intent intent= new Intent(SubstanceAddictionMainPage.this, SubstanceAddictionTest1Page.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        Intent intent2= new Intent(SubstanceAddictionMainPage.this, SubstanceAddictionTest2Page.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case 2:
                        Intent intent3= new Intent(SubstanceAddictionMainPage.this, SubstanceAddictionTest3Page.class);
                        startActivity(intent3);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });




        navBottomArrangements();

        toolBarArrangement();
        drawerInitialization();
        relativeLayoutClickerEnable();

        goToMainPage();

        backButtonActivity();
        getUserName(userName -> setUserName(userName));
    }

    public void backButtonActivity(){
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Yeni aktiviteye geçmek için Intent oluştur
                Intent intent = new Intent(SubstanceAddictionMainPage.this, HomePage.class);
                // Yeni aktiviteyi başlat
                startActivity(intent);
                // Mevcut aktiviteyi sonlandır
                finish();
            }
        });
    }

    public void toolBarArrangement(){
        tb=(Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("Madde Bağımlılığı");
    }

    public void goToMainPage(){
        backIcon=(ImageView) findViewById(R.id.back_to_mainmenu);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SubstanceAddictionMainPage.this, HomePage.class);
                startActivity(intent);
            }
        });
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
                        Intent intent= new Intent( SubstanceAddictionMainPage.this,user_profile.class);
                        startActivity(intent);
                        break;
                    case R.id.neyi_amacliyoruz_option:
                        Intent intent2= new Intent( SubstanceAddictionMainPage.this,Purpose.class);
                        startActivity(intent2);
                        break;
                    case R.id.geri_bildirim_option:
                        Intent intent3 = new Intent(SubstanceAddictionMainPage.this, Feedback.class);
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
        Intent intent= new Intent(SubstanceAddictionMainPage.this, LoginPage.class);
        startActivity(intent);
        finish();
    }
    public void navBottomArrangements(){

        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_nav_id);
        System.out.println(bottomNavigationView.toString());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId= item.getItemId();
                switch (itemId){
                    case R.id.home_tab:
                        System.out.println("home");
                        Intent intent= new Intent(SubstanceAddictionMainPage.this,HomePage.class);
                        startActivity(intent);
                        break;
                    case R.id.questions_tab:
                        System.out.println("questions");
                        Intent intent2= new Intent(SubstanceAddictionMainPage.this,SubstanceAddictionQuestionsPage.class);
                        startActivity(intent2);
                        break;
                    case R.id.videos_tab:
                        System.out.println("videos");
                        Intent intent3= new Intent(SubstanceAddictionMainPage.this,SubstanceAddictionVideosPage.class);
                        startActivity(intent3);
                        break;
                    case R.id.support_tab:
                        System.out.println("support");
                        Intent intent4= new Intent(SubstanceAddictionMainPage.this,SubstanceAddictionSupportPage.class);
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


}