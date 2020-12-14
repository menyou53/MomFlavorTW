package com.example.momflavortw.ui.notifications.message;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.momflavortw.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MessageimgFragment extends Fragment {
    private String imageUrl;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_message_img, container, false);
        final ImageView imageView= root.findViewById(R.id.imageViewMessageImg);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if(getArguments()!=null) {
            imageUrl = getArguments().getString("imageUrl");
            Picasso.get()
                    .load(imageUrl)
                    .resize(width, height)
                    .centerInside()
                    .into(imageView);
        }
        final ImageButton imageButton = root.findViewById(R.id.imageButtonMessageBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applypermission();
                try {
                    downloadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




        return root;
    }

    private void downloadFile() throws IOException {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        final String rt = sdf.format(calendar.getTime());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(imageUrl);

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Download");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        final File localFile = new File(rootPath,rt+".jpg");
        httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(),"已儲存",Toast.LENGTH_SHORT).show();

                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(),"Download failed",Toast.LENGTH_SHORT).show();

                // Handle any errors
            }
        });


    }
    public void applypermission(){
        if(Build.VERSION.SDK_INT>=23){
            //檢查是否已經給了許可權
            int checkpermission= ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if(checkpermission!= PackageManager.PERMISSION_GRANTED){//沒有給許可權
                Log.e("permission","動態申請");
                //引數分別是當前活動，許可權字串陣列，requestcode，調取的CAMERA許可權
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults陣列與許可權字串陣列對應，裡面存放許可權申請結果
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(),"STORAGE已授權",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(),"拒絕授權STORAGE",Toast.LENGTH_SHORT).show();
        }
    }
}
