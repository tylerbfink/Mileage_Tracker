package com.example.mileagetracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    // variables for camera permissions
    private static final String[] PERMISSION_CAMERA = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_CODE = 20;

    ProcessCameraProvider cameraProvider;

    ImageProxy thisProxy;
    InputImage imageProcess;
    Button take_photo_button;
    TextView photo_text;

    private PreviewView previewView;

    private long mLastAnalysisResultTime;
    Executor executor;

    // for text recognition
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    Task<Text> result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);

        photo_text = (TextView) findViewById(R.id.photo_text);

        // sets online to get text from camera
        take_photo_button = (Button) findViewById(R.id.take_photo_button);
        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //starts async task to convert image to text
                    //CameraActivity.Process_image process_image = new CameraActivity.Process_image();
                    //process_image.execute();
                recognizeText(imageProcess);

            }
        });

        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // starts camera if permission or otherwise asks
        if (hasPermission()) {
            startCamera();
        }
        else {
            ActivityCompat.requestPermissions(this, PERMISSION_CAMERA, CAMERA_CODE);
        }
    }

    // returns true if camera permissions already granted
    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // requests camera permissions if not already granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_CODE) {
            if (hasPermission()) {
                startCamera();
            }
            else {
                Toast.makeText(this, "Camera permission required for app.", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    // starts camera
    private void startCamera () {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindImageAnalysis(cameraProvider);
                }
                catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    //binds camera to analyzer
    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        executor = Executors.newSingleThreadExecutor();

        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                thisProxy = image;

                @SuppressLint("UnsafeOptInUsageError") Image mediaImage = image.getImage();
                if (mediaImage != null) {
                    imageProcess = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                }

                //slows images going for analysis
                if (SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 100) {
                    image.close();
                    return;
                }

                mLastAnalysisResultTime = SystemClock.elapsedRealtime();
            }
        });

        //allows cropping of camera image
        ViewPort viewPort = ((PreviewView)findViewById(R.id.previewView)).getViewPort();

        UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageAnalysis)
                .setViewPort(viewPort)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, useCaseGroup);

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    // text processing
    private void recognizeText(InputImage image) {

        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() { //successful text recognition
                    @Override
                    public void onSuccess(Text visionText) {
                        photo_text.setText(visionText.getText());
                        thisProxy.close();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {  //failure text recognition
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "No text recognized!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cameraProvider.unbindAll();

        Intent intent = new Intent(CameraActivity.this, AddStop.class);
        startActivity(intent);
        finish();
    }
}


