package com.example.addictionrecovery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class user_profile extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        ImageView button_name,button_email,button_sifre,back_button;

        button_name =findViewById(R.id.button1);
        button_email =findViewById(R.id.button2);
        button_sifre =findViewById(R.id.button3);
        back_button=findViewById(R.id.back_to_mainpage_icon);

        button_name.setOnClickListener(new SetOnClickListenerUserName());
        button_email.setOnClickListener(new SetOnClickListenerUserMail());
        button_sifre.setOnClickListener(new SetOnClickListenerUserPassword());

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });
    }

    public class SetOnClickListenerUserName implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent= new Intent(getApplicationContext(), UpdateName.class);
            startActivity(intent);
        }
    }

    public class SetOnClickListenerUserMail implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent= new Intent(getApplicationContext(), UpdateMail.class);
            startActivity(intent);
        }
    }

    public class SetOnClickListenerUserPassword implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent= new Intent(getApplicationContext(), UpdatePassword.class);
            startActivity(intent);
        }
    }
}
