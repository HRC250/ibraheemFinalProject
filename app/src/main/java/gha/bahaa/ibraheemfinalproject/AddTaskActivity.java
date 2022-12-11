package gha.bahaa.ibraheemfinalproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import gha.bahaa.ibraheemfinalproject.data.Motor;

public class AddTaskActivity extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 100;
    private static final int PERMISSION_CODE = 101;
    private EditText etTitle,etSubject;
    private SeekBar skbrImportant;
    private Button btnSave;

    //upload: 0.1 add firebase storage
    //upload: 0.2 add this per,issions to manifest xml
//          <uses-permission android:name="android.permission.INTERNET" />
//          <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    //upload: 1 add Xml image view or butn and uplaod button
    //upload: 2
    private ImageButton imgBtnl;
    private Button btnUpload;
    private TextView tvImgUrl;
    private Uri filePath;
    private Uri toUploadimageUri;
    private Uri downladuri;
    StorageTask uploadTask;
    private Motor t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motor);

        //upload: 3
        imgBtnl=findViewById(R.id.imgBtn);
        btnUpload=findViewById(R.id.btnUpload);
        //tvImgUrl=findViewById(R.id.tvImgURL);
        SharedPreferences preferences=getSharedPreferences("mypref",MODE_PRIVATE);
        String key = preferences.getString("key", "");
        if(key.length()==0)
        {
            Toast.makeText(this, "No key found", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this, "key:"+key, Toast.LENGTH_SHORT).show();
        }
        //upload: 4
        imgBtnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }

                }
            }
        });
        ////upload: 6
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage();
//            }
//        });

        etTitle=findViewById(R.id.etTitle);
       etSubject=findViewById(R.id.etSubject);
//        etDueDate=findViewById(R.id.etDueDate);
        skbrImportant=findViewById(R.id.skbrImportant);
//        skbrNecessary=findViewById(R.id.skbrNeccesary);
        btnSave=findViewById(R.id.btnSaveTask);
//        btnDatePicker=findViewById(R.id.btnDatePicker);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             dataHandler();
            }
        });
//        btnDatePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    //upload: 5
    private void uploadImage(Uri filePath) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            uploadTask=ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downladuri = task.getResult();
                                    t.setImg(downladuri.toString());
                                    createTask(t);

                                }
                            });

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else
        {
            t.setImg("");
            createTask(t);
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 99 && resultCode == RESULT_OK
//                && data != null && data.getData() != null )
//        {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imgBtnl.setImageBitmap(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

    private void dataHandler() {
        boolean isok=true;
        String title=etTitle.getText().toString();
//        String text=etText.getText().toString();
//        String  date=etDueDate.getText().toString();
        int important=skbrImportant.getProgress();
//        int necessary=skbrNecessary.getProgress();
        if(title.length()==0)
        {
            etTitle.setError("Title can not be empty");
            isok=false;

        }
//        if(text.length()==0)
//        {
//            etText.setError("Text can not be empty");
//            isok=false;
//
//        }
        if(isok)
        {
            t=new Motor();
//            t.setTitle(title);
//            t.setImportant(important);
            //createTask(t);
            if(uploadTask!=null || (uploadTask!=null && uploadTask.isInProgress()))
            {
                Toast.makeText(this, " uploadTask.isInProgress(", Toast.LENGTH_SHORT).show();
            }
            else
             uploadImage(toUploadimageUri);



        }


    }

    private void createTask(Motor t)
    {
        //.1
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //.2
        DatabaseReference reference =
                database.getReference();
        //to get the user uid (or other details like email)
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        t.setOwner(uid);

        String key = reference.child("motors").push().getKey();
        t.setKey(key);
        reference.child("motors").child(uid).child(key).setValue(t).
                addOnCompleteListener(AddTaskActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(AddTaskActivity.this, "add successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(AddTaskActivity.this, "add failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    task.getException().printStackTrace();
                }

            }
        });



    }

    private void pickImageFromGallery(){
        //intent to pick image
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    //permission was denied
                    Toast.makeText(this,"Permission denied...!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //handle result of picked images
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){
            //set image to image view
            toUploadimageUri = data.getData();
            imgBtnl.setImageURI(toUploadimageUri);
        }
    }
}
