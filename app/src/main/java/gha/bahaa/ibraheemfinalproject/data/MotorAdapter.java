package gha.bahaa.ibraheemfinalproject.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import gha.bahaa.ibraheemfinalproject.R;

public class MotorAdapter extends ArrayAdapter<Motor>
{


    public MotorAdapter(@NonNull Context context) {
        super(context, R.layout.motor_item);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //building item view
        View vitem= LayoutInflater.from(getContext()).inflate(R.layout.motor_item,parent,false);
        TextView tvTitle=vitem.findViewById(R.id.itmTvTitle);
        TextView tvSubject=vitem.findViewById(R.id.itmTvSubject);
        RatingBar rbPrio =vitem.findViewById(R.id.itmRatingPrio);
        CheckBox cbIsCompleted=vitem.findViewById(R.id.itmChbxIsCompleted);
        ImageView ivInfo =vitem.findViewById(R.id.itmImgInfo);
        ImageView imageView =vitem.findViewById(R.id.imageView);

        //getting data source
        final Motor myTask = getItem(position);
        // downloadImageUsingPicasso(myTask.getImage(),imageView);
        //downloadImageToMemory(myTask.getImage(),imageView);
        downloadImageToLocalFile(myTask.getImg(),imageView);   //connect item view to data source
        tvTitle.setText(myTask.getType());
        tvSubject.setText(myTask.getYear()+"");



        return vitem;
    }

//    private void downloadImageUsingPicasso(String imageUrL, ImageView toView)
//    {
//        Picasso.with(getContext())
//                .load(imageUrL)
//                .centerCrop()
//                .error(R.drawable.common_full_open_on_phone)
//                .resize(90,90)
//                .into(toView);
//    }

    private void downloadImageToLocalFile(String fileURL, final ImageView toView) {
        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileURL);
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");


            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Toast.makeText(getContext(), "downloaded Image To Local File", Toast.LENGTH_SHORT).show();
                    toView.setImageURI(Uri.fromFile(localFile));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(getContext(), "onFailure downloaded Image To Local File "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void downloadImageToMemory(String fileURL, final ImageView toView)
    {
        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileURL);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                toView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 90, 90, false));
                Toast.makeText(getContext(), "downloaded Image To Memory", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getContext(), "onFailure downloaded Image To Local File "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
        });

    }


    private void deleteFile(String fileURL) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileURL);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(getContext(), "file deleted", Toast.LENGTH_SHORT).show();
                Log.e("firebasestorage", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Toast.makeText(getContext(), "onFailure: did not delete file "+exception.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("firebasestorage", "onFailure: did not delete file"+exception.getMessage());
                exception.printStackTrace();
            }
        });
    }
}