package com.app.features.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.app.R;
import com.app.features.otp.OtpFragment;
import com.app.features.signup.SignupFragment;

public class LoginActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentManager = getSupportFragmentManager();
        if(getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("login")) {
                fragmentManager.beginTransaction().replace(R.id.login_container, new LoginFragment()).commit();
            } else if (getIntent().getStringExtra("type").equals("singup")) {
                fragmentManager.beginTransaction().replace(R.id.login_container, new SignupFragment()).commit();
            }
        }
    }
}
