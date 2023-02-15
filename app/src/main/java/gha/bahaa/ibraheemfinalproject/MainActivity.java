package gha.bahaa.ibraheemfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gha.bahaa.ibraheemfinalproject.data.Motor;
import gha.bahaa.ibraheemfinalproject.data.MotorAdapter;

public class MainActivity extends AppCompatActivity {
    private ImageButton imgbtn;
    private GridView dyn;
    private SearchView searchView;
    //تعريف الوسيط
    MotorAdapter motorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgbtn = findViewById(R.id.imgbtn);
        //بناء الوسيط3.2
        motorAdapter = new MotorAdapter(getApplicationContext());
        //تجهيز مؤشر للقائمه لعرض
        dyn = findViewById(R.id.gvlist);
        //ربط قائمه العرض للوسيط 3.3
        dyn.setAdapter(motorAdapter);
        //تشغيل مراقب(ليسينير) لاي نغيير على قاعدة البيانات
//ويقوم بتنظيف المعطيات الموجه(حذفها)وتنزيل المعلومات الجديدة
        readMotorFromFireBase();



        //الانتقال من main activity الى addTask عند الضغط على زر الزائد
         imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddMotorActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * لبناء واضافه قائمه
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itmSettings) {
            Intent i = new Intent(MainActivity.this, Settings.class);
            startActivity(i);
        }
        if (item.getItemId() == R.id.itmSignOut) {
            //1تجهيز البناء للديالوج
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Signing out");
            builder.setMessage("are you sure");

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //اخفاء الديالوج
                    dialogInterface.dismiss();
                    //تسجيل الخروج من الحساب
                    FirebaseAuth.getInstance().signOut();
                    // الخروج من الشاشه
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //
                    dialogInterface.cancel();
                }
            });
            //بناء الديالوج
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;

    }

    private void readMotorFromFireBase() {
        //مؤشر لجذر قاعدة البيانات التابعة للمشروع

        //استخراج الرقم المميز للمهمة
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("motor");//listener لمراقبة اي تغيير يحدث تحت الجذر المحدد
// اي تغيير بقيمة صفة او حذف او اضافة كائن يتم اعلام ال listener
        // عندما يتم تنزيل كل المعطيات الموجودة تحت الجذر
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //remove all tasks from adapter
                motorAdapter.clear();
                for (DataSnapshot d : snapshot.getChildren())//يمر على جميع قيم مبنى المعطيات  d
                {

                    Motor m = d.getValue(Motor.class);//استخراج الكائن المحفوظ
                    // if(m.getSymptoms().contains(toSearch))
                    motorAdapter.add(m);//اضافة الكائن للوسيط
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}