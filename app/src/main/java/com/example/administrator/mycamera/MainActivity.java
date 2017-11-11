package com.example.administrator.mycamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView img, img2;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                    },
                    0);
        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    private void init(){
        img = (ImageView)findViewById(R.id.img);
        img2 = (ImageView)findViewById(R.id.img2);
    }

    public void test1(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    public void test2(View view) {
        File root = Environment.getExternalStorageDirectory();
        File save = new File(root, "brad2.jpg");
        outputFileUri = Uri.fromFile(save);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 2);
    }
    public void test3(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                Log.i("brad", "OK");
                takePic1(data);
            }else if(resultCode == RESULT_CANCELED){
                Log.i("brad", "XX");
            }
        }else if (requestCode == 2){
            if (resultCode == RESULT_OK){
                takePic2();
            }
        }
    }

    private void takePic2() {


    }

    private void takePic1(Intent data){
        Bitmap bmp  = (Bitmap)data.getExtras().get("data");
        img.setImageBitmap(bmp);

        try {
            File root = Environment.getExternalStorageDirectory();
            File save = new File(root, "brad.jpg");
            FileOutputStream fout = new FileOutputStream(save);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fout);

            FileInputStream fin = new FileInputStream(save);
            Bitmap bmp2 = BitmapFactory.decodeStream(fin);
            img2.setImageBitmap(bmp2);

        }catch (Exception ee){

        }

    }

}
