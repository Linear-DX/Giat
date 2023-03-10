package com.example.giat.view.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.giat.R;
import com.example.giat.model.Admin;
import com.example.giat.storage.SaveUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import xyz.hasnat.sweettoast.SweetToast;

public class AdminLoginActivity extends AppCompatActivity {
    private LinearLayout goSignUp;
    private EditText adminSignINEmailET,adminSingInPassET;
    private Button adminLogInBtn;
    private FirebaseAuth auth;
    private SaveUser saveUser=new SaveUser();
    private String femail,fpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_login);
        adminSignINEmailET=findViewById(R.id.adminSignInEmailET);
        adminSingInPassET=findViewById(R.id.adminSignInPassET);
        adminLogInBtn=findViewById(R.id.adminLoginBtn);

        auth=FirebaseAuth.getInstance();


        adminLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        final String nip=adminSignINEmailET.getText().toString().trim();
        final String password=adminSingInPassET.getText().toString().trim();

        if(nip.isEmpty()){
            adminSignINEmailET.setError("Masukkan NIP");
            adminSignINEmailET.requestFocus();
        }else if(password.isEmpty()){
            adminSingInPassET.setError("Masukkan Password");
            adminSingInPassET.requestFocus();
        }else {

            DatabaseReference adminLogRef=FirebaseDatabase.getInstance().getReference().child("admin");
            adminLogRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        boolean isLogin = false;
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            Admin admin=dataSnapshot1.getValue(Admin.class);
                            if(admin.getNip().equals(nip) && admin.getPassword().equals(password)){
                                isLogin = true;
                                break;
                            }
                        }
                        if(isLogin){
                            SweetToast.success(getApplicationContext(),"Berhasil Login");
                            startActivity(new Intent(AdminLoginActivity.this,AdminActivity.class));
                            saveUser.admin_saveData(getApplicationContext(),true);
                            finish();
                        }else{
                            SweetToast.error(getApplicationContext(),"NIP atau Password Salah");
                        }
                    }
                    else{
                        SweetToast.error(getApplicationContext(),"Data Admin Tidak Ditemukan");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
           /* auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminLoginActivity.this,AdminActivity.class));
                                saveUser.admin_saveData(getApplicationContext(),true);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });*/
        }

    }
}
