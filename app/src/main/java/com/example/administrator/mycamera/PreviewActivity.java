package com.example.administrator.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private int viewW, viewH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new MyCallback());

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takPic();
            }
        });
    }

    private class MyCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.i("brad", "initCamera");
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.i("brad", "release");
            if (camera!=null){
                camera.release();
            }
        }
    }

    private void initCamera()  {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        if (camera != null){
            Camera.Parameters parameters = camera.getParameters();

            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            for (Camera.Size size  : sizes){
               // Log.i("brad", "size:" + size.width + " x " + size.height);
            }

            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.set("jpeg-quality", 85);
            parameters.setPictureSize(1920,1080);

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            }catch (Exception e){
                Log.i("brad", "ERR1:" + e.toString());
            }


        }else{
            Log.i("brad", "camera null");
        }
    }

    private void takPic(){
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b){
                    camera.takePicture(new Camera.ShutterCallback() {
                                           @Override
                                           public void onShutter() {

                                           }
                                       },
                            new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(byte[] bytes, Camera camera) {

                                }
                            },
                            new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(byte[] bytes, Camera camera) {
                                    afterTakPic(bytes);
                                }
                            });
                }
            }
        });
    }

    private void afterTakPic(byte[] data){
        camera.stopPreview();

        Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,false);

        Intent it = new Intent();
        it.putExtra("bmp", bmp);
        setResult(RESULT_OK, it);
        finish();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (surfaceView != null){
            viewW = surfaceView.getWidth(); viewH = surfaceView.getHeight();
            Log.i("brad", viewW + ":" + viewH);
        }

    }
}
