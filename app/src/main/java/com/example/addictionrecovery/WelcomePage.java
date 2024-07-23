package com.example.addictionrecovery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {


    ImageView welcomeImage,forwardArrow;
    TextView welcomeMessage;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        welcomeMessage=(TextView)findViewById(R.id.welcome_text);
        welcomeImage= (ImageView) findViewById(R.id.nature);
        forwardArrow= (ImageView) findViewById(R.id.forward);

        bitmap= BitmapFactory.decodeResource(getResources(),R.raw.bro);
        welcomeImage.setImageBitmap(bitmap);
        welcomeMessage.setText(R.string.welcome_speech2);
        welcomeMessage.setOnTouchListener(new MainActivity.OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                startHomePage();
            }

            @Override
            public  void onSwipeRight(){
                startHomePage();
            }
        });

        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomePage();
            }
        });




    }

    public void startHomePage(){
            Intent intent= new Intent(WelcomePage.this,HomePage.class);
            startActivity(intent);
    }
}