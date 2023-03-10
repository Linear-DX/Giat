package com.example.giat.view.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.giat.R;
import com.example.giat.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import xyz.hasnat.sweettoast.SweetToast;

public class AddTeacherActivity extends AppCompatActivity {
    private Toolbar addTeacherToolbar;
    private EditText teacherNameEt,teacherEmailEt,teacherPhoneEt,teacherAddressEt,teacherIdEt;
    private Spinner teacherDesignationSp;
    private String[] desigList;
    private String selectedDesig;
    private Button addTeacherBtn;
    private String intendedDept,intentedShift;
    private DatabaseReference teacherRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        addTeacherToolbar=findViewById(R.id.addTeacherToolbar);
        setSupportActionBar(addTeacherToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         Intent  intent=getIntent();
         intendedDept=intent.getStringExtra("TDEPT");
         intentedShift=intent.getStringExtra("TSHIFT");

        teacherNameEt=findViewById(R.id.addTeacherName);
        teacherEmailEt=findViewById(R.id.addTeacherEmail);
        teacherAddressEt=findViewById(R.id.addTeacherAddress);
        teacherPhoneEt=findViewById(R.id.addTeacherPhone);
        teacherIdEt=findViewById(R.id.addTeacherId);
        addTeacherBtn=findViewById(R.id.addTbtn);
        teacherDesignationSp=findViewById(R.id.teacherDesignationSp);

        desigList=getResources().getStringArray(R.array.desig);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AddTeacherActivity.this,android.R.layout.simple_list_item_1,desigList);
        teacherDesignationSp.setAdapter(arrayAdapter);
        teacherDesignationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDesig=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeacher();
            }
        });


    }


    private void addTeacher(){
        String name=teacherNameEt.getText().toString();
        String email=teacherEmailEt.getText().toString();
        String address=teacherAddressEt.getText().toString();
        String phone=teacherPhoneEt.getText().toString();
        String ID=teacherIdEt.getText().toString();

        if(name.isEmpty()){
            teacherNameEt.setError("Masukkan nama dosen");
            teacherNameEt.requestFocus();
        }else  if(email.isEmpty()){
            teacherEmailEt.setError("Masukkan email");
            teacherEmailEt.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            teacherEmailEt.setError("Masukkan email yang benar");
            teacherEmailEt.requestFocus();
        }else if(address.isEmpty()){
            teacherAddressEt.setError("Masukkan alamat");
            teacherAddressEt.requestFocus();
        }else if(phone.isEmpty()){
            teacherPhoneEt.setError("Masukkan No. HP");
            teacherPhoneEt.requestFocus();
        }else if(selectedDesig.isEmpty() || selectedDesig.equals("Pilih Pangkat")){
            SweetToast.warning(getApplicationContext(),"Pilih pangkat");
        }else if(ID.isEmpty()){
            teacherIdEt.setError("Masukkan NIP");
            teacherIdEt.requestFocus();
        }else {

            teacherRef= FirebaseDatabase.getInstance().getReference().child("Department").child(intendedDept).child("dosen").child(intentedShift);
            String key=teacherRef.push().getKey();

            Teacher teacher=new Teacher(ID,name,intendedDept,selectedDesig,"",phone,email,"","",address,"","1234",intentedShift,"");
            teacherRef.child(key).setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        SweetToast.success(getApplicationContext(),"Data dosen berhasil ditambahkan");
                        finish();
                    }
                }
            });


        }





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
