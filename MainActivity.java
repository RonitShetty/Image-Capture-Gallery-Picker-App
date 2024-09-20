package com.example.experiment6;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private ImageView imageView;
    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        cameraLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                {
                    Intent data = activityResult.getData();
                    Bundle extras = data.getExtras();
                    Bitmap mBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(mBitmap);
                }
            }

        });

        galleryLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {

                Intent data = activityResult.getData();
                Uri chosenImageUri=data.getData();

                Bitmap mBitmap=null;
                try{
                    mBitmap= MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(),chosenImageUri);
                    imageView.setImageBitmap(mBitmap);
                } catch(IOException e){
                    Log.e("TAG","error loading image",e);
                    Toast.makeText(MainActivity.this, "error loading image", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void selectImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT) ;
        photoPickerIntent.setType("image/*");
        //startActivityForResult(Intent.createChooser(photoPickerIntent, "Pick Image"),1);
        galleryLauncher.launch(photoPickerIntent);
    }

    /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(resultCode==RESULT_OK && requestCode==1){
             Uri chosenImageUri=data.getData();

             Bitmap mBitmap=null;
             try{
                 mBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),chosenImageUri);
                 imageView.setImageBitmap(mBitmap);
             } catch(IOException e){
                 Log.e("TAG","error loading image",e);
                 Toast.makeText(this, "error loading image", Toast.LENGTH_SHORT).show();
             }
         }
         else if(resultCode==RESULT_OK && requestCode==2){
             Bundle extras = data.getExtras();
             Bitmap mBitmap = (Bitmap) extras.get("data");
             imageView.setImageBitmap(mBitmap);
         }
     }*/
    public void captureImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
        //startActivityForResult(intent, 2);
    }
}

