package com.winraguini.momoapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;


public class VideoCaptureActivity extends Activity implements Callback {

    public MediaRecorder mrec = new MediaRecorder();
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private Camera mCamera;

    @Override
    protected void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);

        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mCamera = Camera.open();

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 0, 0, "Start");
        return super.onCreateOptionsMenu(menu);
    }


    public void onStartClicked(View v) {
        try {

            startRecording();

        } catch (Exception e) {

            String message = e.getMessage();
            Log.i(null, "Problem " + message);
            mrec.release();
        }
    }

    public void onStopClicked(View v) {
        mrec.stop();
        mrec.reset();
        mrec.release();
        mrec = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Start")) {
            try {

                startRecording();
                item.setTitle("Stop");

            } catch (Exception e) {

                String message = e.getMessage();
                Log.i(null, "Problem " + message);
                mrec.release();
            }

        } else if (item.getTitle().equals("Stop")) {
            mrec.stop();
            mrec.release();
            mrec = null;
            item.setTitle("Start");
        }

        return super.onOptionsItemSelected(item);
    }

    protected void startRecording() throws IOException {
        if (mCamera == null)
            mCamera = Camera.open();

        String filename;
        String path;

        path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();

        Date date = new Date();
        filename = new SimpleDateFormat("rec-yyyy-MM-dd-ss").format(date) + ".mp4";

        //create empty file it must use
        File file = new File(path, filename);

        mrec = new MediaRecorder();

        mCamera.lock();
        mCamera.unlock();

        // Please maintain sequence of following code. 

        // If you change sequence it will not work.
        mrec.setCamera(mCamera);
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //mrec.setAudioSource(MediaRecorder.AudioSource.MIC);     
        mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mrec.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        //mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setOutputFile(file.getAbsolutePath());
        Log.d("debug", "Writing to: " + file.getAbsolutePath());
        mrec.prepare();
        mrec.start();


    }

    protected void stopRecording() {

        if (mrec != null) {
            mrec.stop();
            mrec.release();
            mCamera.release();
            mCamera.lock();
        }
    }

    private void releaseMediaRecorder() {

        if (mrec != null) {
            mrec.reset(); // clear recorder configuration
            mrec.release(); // release the recorder object
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mCamera != null) {
            Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
            Log.i("Surface", "Created");
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!",
                    Toast.LENGTH_LONG).show();

            finish();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();

    }
}
