package com.example.app_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.app_template.object.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mBack;

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mCPassword;

    private Button mRegister;

    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComponents();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void initComponents(){
        //Initialize DB Components
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI Components
        mToolbar = (Toolbar) findViewById(R.id.toolBarSignUp);
        mBack = (ImageView) findViewById(R.id.imgBack);

        mName = (EditText) findViewById(R.id.editTextName);
        mEmail = (EditText) findViewById(R.id.editTextEmail);
        mPassword = (EditText) findViewById(R.id.editTextPassword);
        mCPassword = (EditText) findViewById(R.id.editTextCPassword);

        mRegister = (Button) findViewById(R.id.buttonRegister);

        mProgress = mProgress = new ProgressDialog(this);

    }

    private void registerUser(){
        mProgress.setTitle("Registering a user");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.show();

        String fName = mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String cPassword = mCPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fName)){
            mName.setError("Required field!");
            mProgress.dismiss();
            return;
        }
        if (TextUtils.isEmpty(email)){
            mEmail.setError("Required field!");
            mProgress.dismiss();
            return;
        }
        if (!email.contains("@")){
            mEmail.setError("Invalid email!");
            mProgress.dismiss();
            return;
        }
        if (TextUtils.isEmpty(password)){
            mPassword.setError("Required field!");
            mProgress.dismiss();
            return;
        }
        if (TextUtils.isEmpty(cPassword)){
            mCPassword.setError("Required field!");
            mProgress.dismiss();
            return;
        }
        if (!password.equals(cPassword)){
            mCPassword.setError("Password should be equal to confirm password!");
            mProgress.dismiss();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                }
                else{
                    final FirebaseUser user = mAuth.getCurrentUser();
                    final String uid = mAuth.getCurrentUser().getUid();


                    //instance of an object
                    final UserDetail userDetail = new UserDetail();
                    userDetail.setUid(uid);
                    userDetail.setName(fName);
                    userDetail.setEmail(email.toLowerCase());

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                DatabaseReference userProfile = mDatabase.child("userProfile").child(uid);
                                userProfile.setValue(userDetail);
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Please verify your account in email", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            }
                        }
                    });
                }
            }
        });

    }

}