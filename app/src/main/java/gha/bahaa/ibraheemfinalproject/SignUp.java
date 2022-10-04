package gha.bahaa.ibraheemfinalproject;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {


        TextInputEditText etEmail;
        TextInputEditText etPass;
        TextInputEditText etConfirm;
        Button btnSave;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            etEmail = findViewById(R.id.etEmail);
            etPass = findViewById(R.id.etPass);
            etConfirm = findViewById(R.id.etConfirm);
            btnSave = findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAndSave();
                }


            });

        }


        //تستعمل للدخول والتسجيل و الخروج ,sign in up and out
        private void checkAndSave() {
            String email = etEmail.getText().toString();
            String pass = etPass.getText().toString();
            String passConf = etConfirm.getText().toString();

            boolean isok = true;
            if (email.length() * pass.length() * passConf.length() == 0) {
                etEmail.setError("one of the files is empty");
                isok = false;
            }
            if (pass.equals(passConf) == false) {
                etConfirm.setError("is not equal to password");
                isok = false;
            }
            if (isok) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    /**
                     * معالجه حدث عندما تكون المهمه ومعلومات عن الحدث

                     */
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //فحص اذا بناء حساب ناجح
                        if (task.isSuccessful()) {
                            /* if the email and password are valid then the email and the pass are saved in the firebase and a text wil pop
                            up in the bottom of the screen says "password and emaile are saved"
                             */
                            Toast.makeText(SignUp.this, "creation successfuly" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //اغلاق الشاشه الحاليه
                            finish();
                        } else
                            Toast.makeText(SignUp.this, "creation failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

            }
        }

 }











