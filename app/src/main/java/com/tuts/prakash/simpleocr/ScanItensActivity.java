package com.tuts.prakash.simpleocr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rashmi Kataria
 * 3/13/2020
 */
public class ScanItensActivity extends AppCompatActivity {
    SurfaceView mCameraView;
    TextView mTextView;
    CameraSource mCameraSource;

    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);

        startCameraSource();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ScanItensActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                if (items.size() != 0) {
                                    TextBlock item = items.valueAt(0);
                                    if (item != null && item.getValue() != null) {

                                        String matchedString = item.getValue();
                                        Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());


                                        if (matchedWords.size() == 2) {
                                            moveToDashboard(matchedWords);
                                            return;
                                        }

                                        if (matchedString.toLowerCase().contains("entrega")) {

                                            for (int i=0;i<matchedWords.size();i++){
                                                if (matchedWords.get(i).contains(matchedString)){
                                                    break;
                                                }
                                            }
                                            matchedWords.add(matchedString);
                                        } else if (matchedString.toLowerCase().equals("dni")) {
                                            matchedWords.add(matchedString);
                                        }

                                        stringBuilder.append(item.getValue());
                                        stringBuilder.append("\n");


//                                        if (matchedString.contains("Entrega") ||matchedString.contains("entrega") ||
//                                                matchedString.contains("dni") || matchedString.contains("DNI")) {
//
//                                            stringBuilder.append(item.getValue());
//                                            stringBuilder.append("\n");
//
//
//                                            if (matchedWords.size() == 2)
//                                                moveToDashboard(matchedWords);
//                                            else{
//                                                matchedWords.contains("DNI");
//                                                matchedWords.add(matchedString);}
//                                        }
                                    }

                                }
                                mTextView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }

    List<String> matchedWords = new ArrayList<>();


    private void moveToDashboard(List<String> matchedString) {
        Bundle bundle = new Bundle();
        bundle.putString("Entrega", matchedString.get(0));
        bundle.putString("DNI", matchedString.get(1));
        Intent intent = new Intent(ScanItensActivity.this, NewScreenActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}

