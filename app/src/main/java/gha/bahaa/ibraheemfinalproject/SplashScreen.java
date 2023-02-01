package gha.bahaa.ibraheemfinalproject;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //start next activity screen automatically after period of time
        Handler h= new Handler();
        Runnable r=new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this,SignIn.class);
                startActivity(i); //to start the i which is going from Splash activity to signIn
                finish();   // to close the current activity
            }
        };
        h.postDelayed(r,2000);



    }
}