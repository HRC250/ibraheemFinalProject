package gha.bahaa.ibraheemfinalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    TextInputEditText EtEmail;
    TextInputEditText Etpass;
    Button btnup;
    Button ButtonSignIn;
    private View btnin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EtEmail = findViewById(R.id.Etemail);
        Etpass = findViewById((R.id.EtPass));
        btnup = findViewById(R.id.btnup);
        btnin = findViewById(R.id.btnin);

        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);

            }
        });

        btnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void checkAndSave() {
        String email = EtEmail.getText().toString();
        String pass = Etpass.getText().toString();
        boolean isOk = true;
        if (email.length() == 0) {
            EtEmail.setError("enter your email");
            isOk = false;
        }
        if (pass.length() == 0) {
            Etpass.setError("enter your password");
            isOk = false;
        }
        if (email.indexOf('@') <= 0) {
            EtEmail.setError("wrong email");
            isOk = false;
        }

        {
            Etpass.setError("password at least 7 character");
            isOk = false;
        }
        //******
        if (isOk) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignIn.this, "successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignIn.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignIn.this, "not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}



