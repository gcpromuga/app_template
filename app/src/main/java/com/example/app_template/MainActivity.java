package com.example.app_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FrameLayout frameLayout;

    private FirebaseAuth mAuth;

    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        if (!isAuthenticated()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

//        mLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            }
//        });

    }

    private void initComponents(){
        mLogout = (Button) findViewById(R.id.buttonLogout);

        bottomNav = findViewById(R.id.main_bottom_navigation);
        frameLayout = findViewById(R.id.main_framelayout);

        mAuth = FirebaseAuth.getInstance();

    }

    private boolean isAuthenticated() {
        FirebaseUser user = mAuth.getCurrentUser();
//        return true;
        if (user != null) {
            if (user.isEmailVerified()) {
                // do nothing
                return true;
            }
            else{
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Please verify your account!", Toast.LENGTH_LONG).show();
                return false;
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
        else {
            //startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return false;
        }

    }

}