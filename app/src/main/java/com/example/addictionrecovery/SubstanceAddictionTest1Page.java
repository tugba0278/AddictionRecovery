package com.example.addictionrecovery;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SubstanceAddictionTest1Page extends AppCompatActivity {
    RelativeLayout relativeLayout;

    FirebaseAuth auth;
    NavigationView navigationView;
    ImageView navIcon;
    BottomNavigationView bottomNavigationView;
    Toolbar tb;

    public List<QuizModel> questionList;
    TextView tvQuestion, tvQuestionNo;
    RadioGroup radioGroup;
    RadioButton rb1, rb2, rb3, rb4;
    Button btnNext;
    int totalQuestions, qCounter = 0, score = 0;
    QuizModel currentQuestion;
    String[] answers,videoTitles, videoIds, videoDescriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substance_addiction_test1_page);
        auth=FirebaseAuth.getInstance();
        relativeLayout=(RelativeLayout) findViewById(R.id.substance_addiction_test1_layout);

        videoTitles=getResources().getStringArray(R.array.substance_addiction_video_titles);
        videoIds=getResources().getStringArray(R.array.substance_addiction_video_ids);
        videoDescriptions=getResources().getStringArray(R.array.substance_addiction_video_descriptions);

        drawerInitialization();
        setToolbarTitle();
        backButtonActivity();
        navBottomArrangements();
        getUserName(userName -> setUserName(userName));
        relativeLayoutClickerEnable();

        questionList = new ArrayList<>();
        tvQuestion = findViewById(R.id.questionTitle);
        tvQuestionNo = findViewById(R.id.questionNum);

        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        btnNext = findViewById(R.id.btnNext);

        addQuestions();
        totalQuestions = questionList.size();
        answers = new String[totalQuestions];
        showNextQuestion();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                // Eğer bir seçenek seçilmediyse uyarı ver
                if (selectedRadioButtonId == -1) {
                    Toast.makeText(SubstanceAddictionTest1Page.this, "Lütfen seçim yapınız", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                // Seçilen seçeneği dize dizisine ekleyin
                if (selectedRadioButton != null) {
                    String selectedAnswer = selectedRadioButton.getText().toString();
                    answers[qCounter - 1] = selectedAnswer; // Soru indeksi 0'dan başladığı için qCounter - 1

                    // Skoru güncelle
                    if (selectedRadioButton == rb1) {
                        score += 0;
                    } else if (selectedRadioButton == rb2) {
                        score += 1;
                    } else if (selectedRadioButton == rb3) {
                        score += 2;
                    } else if (selectedRadioButton == rb4) {
                        score += 3;
                    }
                }
                // Sonraki soruyu göster
                showNextQuestion();
            }
        });


    }


    public void addQuestions(){
        // Soru 1
        String question1 = getResources().getString(R.string.subs_addiction_test1_question1);
        String[] options1 = getResources().getStringArray(R.array.subs_addiction_test1_question1_options);
        questionList.add(new QuizModel(question1, options1[0], options1[1], options1[2], options1[3]));

        // Soru 2
        String question2 = getResources().getString(R.string.subs_addiction_test1_question2);
        String[] options2 = getResources().getStringArray(R.array.subs_addiction_test1_question2_options);
        questionList.add(new QuizModel(question2, options2[0], options2[1], options2[2], options2[3]));

        String question3 = getResources().getString(R.string.subs_addiction_test1_question3);
        String[] options3 = getResources().getStringArray(R.array.subs_addiction_test1_question3_options);
        questionList.add(new QuizModel(question3, options3[0], options3[1], options3[2], options3[3]));

        String question4 = getResources().getString(R.string.subs_addiction_test1_question4);
        String[] options4 = getResources().getStringArray(R.array.subs_addiction_test1_question4_options);
        questionList.add(new QuizModel(question4, options4[0], options4[1], options4[2], options4[3]));

        String question5 = getResources().getString(R.string.subs_addiction_test1_question5);
        String[] options5 = getResources().getStringArray(R.array.subs_addiction_test1_question5_options);
        questionList.add(new QuizModel(question5, options5[0], options5[1], options5[2], options5[3]));

        String question6 = getResources().getString(R.string.subs_addiction_test1_question6);
        String[] options6 = getResources().getStringArray(R.array.subs_addiction_test1_question6_options);
        questionList.add(new QuizModel(question6, options6[0], options6[1], options6[2], options6[3]));

        String question7 = getResources().getString(R.string.subs_addiction_test1_question7);
        String[] options7 = getResources().getStringArray(R.array.subs_addiction_test1_question7_options);
        questionList.add(new QuizModel(question7, options7[0], options7[1], options7[2], options7[3]));
    }
    public void showNextQuestion(){
        radioGroup.clearCheck();

        if(qCounter < totalQuestions){
            currentQuestion = questionList.get(qCounter);
            tvQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            qCounter++;
            tvQuestionNo.setText("Soru: "+qCounter+"/"+totalQuestions);

            if(qCounter==totalQuestions){btnNext.setText("Bitir");}

        } else {
            Log.d("Score", "Total score: " + score);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your Score");
            builder.setMessage("Total score: " + score);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Dialog kapatıldığında yapılacak işlemler
                    Intent intent=new Intent(SubstanceAddictionTest1Page.this, SubstanceAddictionMainPage.class);
                    startActivity(intent);
                    finish(); // Activity'i kapat
                }
            });
            builder.setPositiveButton("Önerilen Videoyu İzle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Dialog kapatıldığında yapılacak işlemler
                    if (score>14 && score <=21){
                        setFinalVideo(videoTitles,videoIds,videoDescriptions,5,SubstanceAddictionTest1Page.this,SubstanceAddictionShowVideos.class);
                    }
                    else if (score>7 && score <=14){
                        setFinalVideo(videoTitles,videoIds,videoDescriptions,3,SubstanceAddictionTest1Page.this,SubstanceAddictionShowVideos.class);
                    }
                    else {
                        setFinalVideo(videoTitles,videoIds,videoDescriptions,12,SubstanceAddictionTest1Page.this,SubstanceAddictionShowVideos.class);
                    }
                     // Activity'i kapat
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            //finish();
        }
    }

    void setFinalVideo(String [] videoTitles, String [] videoIds, String[] descriptionArrays, int skipPoint, Context source, Class destination){
        Intent intent = new Intent( source,destination);
        Bundle extras = new Bundle();

        extras.putInt("VIDEO_POS",skipPoint);
        extras.putStringArray("VIDEO_LIST",videoTitles);
        extras.putStringArray("VIDEO_IDS",videoIds);
        extras.putStringArray("VIDEO_DESCRIPTIONS",descriptionArrays);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }


    public void backButtonActivity(){
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Yeni aktiviteye geçmek için Intent oluştur
                Intent intent = new Intent(SubstanceAddictionTest1Page.this, SubstanceAddictionMainPage.class);
                // Yeni aktiviteyi başlat
                startActivity(intent);
                // Mevcut aktiviteyi sonlandır
                finish();
            }
        });
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
                        Intent intent= new Intent(SubstanceAddictionTest1Page.this,SubstanceAddictionMainPage.class);
                        startActivity(intent);
                        break;
                    case R.id.questions_tab:
                        System.out.println("questions");
                        Intent intent2= new Intent(SubstanceAddictionTest1Page.this,SubstanceAddictionQuestionsPage.class);
                        startActivity(intent2);
                        break;
                    case R.id.videos_tab:
                        System.out.println("videos");
                        Intent intent3= new Intent(SubstanceAddictionTest1Page.this,SubstanceAddictionVideosPage.class);
                        startActivity(intent3);
                        break;
                    case R.id.support_tab:
                        System.out.println("support");
                        Intent intent4= new Intent(SubstanceAddictionTest1Page.this,SubstanceAddictionSupportPage.class);
                        startActivity(intent4);
                        break;
                    default:

                        break;
                }

                return false;
            }
        });
    }
    public void setToolbarTitle(){
        tb=(Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("Bağımlılık");
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
                        Intent intent= new Intent(SubstanceAddictionTest1Page.this,user_profile.class);
                        startActivity(intent);
                        break;

                    case R.id.neyi_amacliyoruz_option:
                        Intent intent1=new Intent (SubstanceAddictionTest1Page.this, Purpose.class);
                        startActivity(intent1);
                        break;
                    case R.id.geri_bildirim_option:
                        Intent intent3 = new Intent(SubstanceAddictionTest1Page.this, Feedback.class);
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
    public void logout(){
        auth.signOut();

        // Giriş sayfasına yönlendir
        Intent intent = new Intent(SubstanceAddictionTest1Page.this, LoginPage.class);
        startActivity(intent);
        finish(); // Bu aktiviteyi kapat
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