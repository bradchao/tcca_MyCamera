package com.example.administrator.mycamera;

import android.graphics.ImageFormat;
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
            }catch (Exception e){
                Log.i("brad", "ERR1:" + e.toString());
            }


        }else{
            Log.i("brad", "camera null");
        }
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
