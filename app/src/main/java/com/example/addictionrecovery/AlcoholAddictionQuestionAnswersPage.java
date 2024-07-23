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

public class AlcoholAddictionQuestionAnswersPage extends AppCompatActivity {

    Toolbar tb;
    NavigationView navigationView;

    ImageView navIcon,backArrow;
    TextView questionTitle,questionAnswer;
    RelativeLayout relativeLayout;

    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_addiction_question_answers_page);
        auth=FirebaseAuth.getInstance();
        relativeLayout=(RelativeLayout) findViewById(R.id.alcohol_addiction_question_answers_relative_layout);

        backButtonActivity();
        toolBarArrangement();
        drawerInitialization();
        relativeLayoutClickerEnable();
        backButtonActivity();

        setContents();
        goBack();
        getUserName(userName -> setUserName(userName));
    }
    public void setContents(){
        Bundle extras= getIntent().getExtras();
        questionTitle=(TextView)findViewById(R.id.question_title);
        questionAnswer=(TextView)findViewById(R.id.answer_view);
        questionTitle.setText(extras.getString("QUESTION"));
        questionAnswer.setText(extras.getString("ANSWER"));

    }
    public void goBack(){
        backArrow=(ImageView) findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AlcoholAddictionQuestionAnswersPage.this,AlcoholAddictionQuestionsPage.class);
                startActivity(intent);
            }
        });
    }

    public void backButtonActivity(){
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Yeni aktiviteye geçmek için Intent oluştur
                Intent intent = new Intent(AlcoholAddictionQuestionAnswersPage.this, AlcoholAddictionQuestionsPage.class);
                // Yeni aktiviteyi başlat
                startActivity(intent);
                // Mevcut aktiviteyi sonlandır
                finish();
            }
        });
    }

    public void toolBarArrangement(){
        tb=(Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("Alkol Bağımlılığı");
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
                        Intent intent= new Intent(AlcoholAddictionQuestionAnswersPage.this,user_profile.class);
                        startActivity(intent);
                        break;
                    case R.id.neyi_amacliyoruz_option:
                        Intent intent2= new Intent(AlcoholAddictionQuestionAnswersPage.this,Purpose.class);
                        startActivity(intent2);
                        break;
                    case R.id.geri_bildirim_option:
                        Intent intent3 = new Intent(AlcoholAddictionQuestionAnswersPage.this, Feedback.class);
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
        Intent intent= new Intent(AlcoholAddictionQuestionAnswersPage.this, LoginPage.class);
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
                        Intent intent= new Intent(AlcoholAddictionQuestionAnswersPage.this,AlcoholAddictionMainPage.class);
                        startActivity(intent);
                        break;
                    case R.id.questions_tab:
                        System.out.println("questions");
                        Intent intent2= new Intent(AlcoholAddictionQuestionAnswersPage.this,AlcoholAddictionQuestionsPage.class);
                        startActivity(intent2);
                        break;
                    case R.id.videos_tab:
                        System.out.println("videos");
                        Intent intent3= new Intent(AlcoholAddictionQuestionAnswersPage.this,AlcoholAddictionVideosPage.class);
                        startActivity(intent3);
                        break;
                    case R.id.support_tab:
                        System.out.println("support");
                        Intent intent4= new Intent(AlcoholAddictionQuestionAnswersPage.this,AlcoholAddictionSupportPage.class);
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