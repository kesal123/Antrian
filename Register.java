package com.example.root.antrianbimbinganuajy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.root.antrianbimbinganuajy.Lecturer.DAO.LecturerDataAccess;
import com.example.root.antrianbimbinganuajy.Listener.OnGetDataListenerString;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccess;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccessAcademic;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccessKP;
import com.example.root.antrianbimbinganuajy.Students.DAO.StudentsDataAccessThesis;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private RegisterController controller = new RegisterController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mahasiswa);
        final EditText txtNPM = (EditText)findViewById(R.id.txtNPM);
        final EditText txtName = (EditText)findViewById(R.id.txtName);
        final Spinner spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        final ImageView btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.spinner_gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter1);
        final EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        Button btnStudent = (Button)findViewById(R.id.btnStudent);
        Button btnLecturer = (Button)findViewById(R.id.btnLecturer);

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().equals(""))
                {
                    Toast.makeText(Register.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
                    txtName.findFocus();
                    return;
                }
                if(txtNPM.getText().toString().length()!=9)
                {
                    Toast.makeText(Register.this, "NPM harus 9 digit", Toast.LENGTH_SHORT).show();
                    txtNPM.findFocus();
                    return;
                }
                if(txtPassword.getText().toString().equals(""))
                {
                    Toast.makeText(Register.this, "Password harus diisi", Toast.LENGTH_SHORT).show();
                    txtPassword.findFocus();
                    return;
                }
                if(txtPassword.getText().toString().contains("/") || txtPassword.getText().toString().contains("\\"))
                {
                    Toast.makeText(Register.this, "Tidak boleh backslash", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("(([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z]))");
                Matcher m = p.matcher(txtPassword.getText().toString());
                if(txtPassword.getText().toString().length()<8)
                {
                    Toast.makeText(Register.this, "Password harus lebih dari 7 huruf", Toast.LENGTH_SHORT).show();
                    txtPassword.findFocus();
                    return;
                }
                if(!m.matches())
                {
                    Toast.makeText(Register.this, "Password harus mengandung angka dan huruf", Toast.LENGTH_SHORT).show();
                    txtPassword.findFocus();
                    return;
                }


                controller.cekUsername(txtNPM.getText().toString(), new OnGetDataListenerString() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(String string) {
                        if(string.equals("0"))
                        {
                            Map<String,String> data = new HashMap<>();
                            data.put("NPM",txtNPM.getText().toString());
                            data.put("Name",txtName.getText().toString());
                            data.put("Gender",spinnerGender.getSelectedItem().toString());
                            data.put("Password",txtPassword.getText().toString());
                            data.put("statusDPA","false");
                            data.put("statusDPK","false");
                            data.put("statusDPS","false");
                            controller.registerStudents(data);
                            Toast.makeText(Register.this, "Your account has been created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Register.this, "NPM Sudah ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(String string) {

                    }
                });

            }
        });
        btnLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().equals(""))
                {
                    Toast.makeText(Register.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
                    txtName.findFocus();
                    return;
                }
                if(txtPassword.getText().toString().equals(""))
                {
                    Toast.makeText(Register.this, "Password harus diisi", Toast.LENGTH_SHORT).show();
                    txtPassword.findFocus();
                    return;
                }
                if(txtPassword.getText().toString().contains("/") || txtPassword.getText().toString().contains("\\"))
                {
                    Toast.makeText(Register.this, "Tidak boleh backslash", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("(([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z]))");
                Matcher m = p.matcher(txtPassword.getText().toString());
                if(txtPassword.getText().toString().length()<8)
                {
                    Toast.makeText(Register.this, "Password harus lebih dari 7 huruf", Toast.LENGTH_SHORT).show();
                    txtPassword.findFocus();
                    return;
                }
                if(!m.matches())
                {
                    Toast.makeText(Register.this, "Password harus mengandung angka dan huruf", Toast.LENGTH_SHORT).show();
                    txtPassword.findFocus();
                    return;
                }
                controller.cekUsername(txtNPM.getText().toString(), new OnGetDataListenerString() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(String string) {
                        if(string.equals("0"))
                        {

                            Map<String,String> data = new HashMap<>();
                            data.put("NIK",txtNPM.getText().toString());
                            data.put("Name",txtName.getText().toString());
                            data.put("Password",txtPassword.getText().toString());
                            data.put("Gender",spinnerGender.getSelectedItem().toString());
                            controller.regiterLecturer(data);
                            Toast.makeText(Register.this, "Your account has been created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Register.this, "NIK Sudah ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(String string) {

                    }
                });

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
