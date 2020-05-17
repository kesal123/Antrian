package com.example.root.antrianbimbinganuajy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.example.root.antrianbimbinganuajy.Admin.DA.AdminDataAccessDosen;
import com.example.root.antrianbimbinganuajy.Admin.UI.DefaultAdmin;
import com.example.root.antrianbimbinganuajy.Lecturer.DAO.LecturerDataAccess;
import com.example.root.antrianbimbinganuajy.Lecturer.UI.LecturerDefaultLayout;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccess;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccessKP;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccessThesis;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccessAcademic;
import com.example.root.antrianbimbinganuajy.Students.UI.DefaultStudents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class Login extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference();
        TextView txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        Button btn = (Button)findViewById(R.id.btnLogin);
        try
        {

            SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
            String username = sharedPreferences.getString("username","null");
            String role = sharedPreferences.getString("role","null");
            if(!username.equals("null") && !role.equals("null"))
            {
                if(role.equals("student"))
                {
                    goToMahasiswa(username);
                    finish();
                    startActivity(new Intent(Login.this,DefaultStudents.class));
                }
                else if(role.equals("lecturer"))
                {
                    goToDosen(username);
                    finish();
                    startActivity(new Intent(Login.this,LecturerDefaultLayout.class));
                }
                else if(role.equals("admin"))
                {
                    new AdminDataAccessDosen().setUsername(username);
                    finish();
                    startActivity(new Intent(Login.this,DefaultAdmin.class));
                }
            }
        }
        catch (Exception e)
        {}
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    final EditText username = (EditText)findViewById(R.id.txtUsername);
                    final EditText password = (EditText)findViewById(R.id.txtPassword);

                    Query query = myRef.child("Users");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            /*
                            goToMahasiswa("170709205");
                            goToDosen("123456");
                            finish();
                            startActivity(new Intent(Login.this,DefaultAdmin.class));*/
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                Users u = ds.getValue(Users.class);

                                
                                if(ds.getKey().equals(username.getText().toString()) && u.getPassword().equals(password.getText().toString()))
                                {

                                    if(ds.child("Status").getValue().toString().equals("Verifikasi"))
                                    {
                                        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username",username.getText().toString());
                                        if(u.getRole().equals("Mahasiswa"))
                                        {
                                            editor.putString("role","student");
                                            goToMahasiswa(username.getText().toString());
                                            finish();
                                            startActivity(new Intent(Login.this,DefaultStudents.class));
                                        }
                                        else if(u.getRole().equals("Dosen"))
                                        {
                                            editor.putString("role","lecturer");
                                            goToDosen(username.getText().toString());
                                            finish();
                                            startActivity(new Intent(Login.this,LecturerDefaultLayout.class));
                                        }
                                        else
                                        {
                                            editor.putString("role","admin");
                                            new AdminDataAccessDosen().setUsername(username.getText().toString());
                                            finish();
                                            startActivity(new Intent(Login.this,DefaultAdmin.class));
                                        }
                                        editor.commit();
                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this, "Your account is not activated", Toast.LENGTH_SHORT).show();
                                    }
                                    return;

                                    
                                }
                                else
                                {

                                }
                            }
                            Toast.makeText(Login.this, "Gagal", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                catch(Exception e)
                {
                    Toast.makeText(Login.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void goToMahasiswa(String npm)
    {
        new StudentsDataAccess().setNpm(npm);
        new StudentsDataAccessAcademic().setNpm(npm);
        new StudentsDataAccessKP().setNpm(npm);
        new StudentsDataAccessThesis().setNpm(npm);
    }
    public void goToDosen(String nik)
    {
        new LecturerDataAccess().setNik(nik);
    }
}
