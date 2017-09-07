package com.winsontan520.myopencv;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Winson Tan on 7/9/17.
 */

public class FASTActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "FASTActivity";
    private static final int MIN_THRESHOLD = 5000;
    private JavaCameraView mJavaCameraView;
    private TextView mThresholdText;

    private BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // TODO: own cpp
                    //System.loadLibrary("native-lib");
                    mJavaCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_fast);

        mJavaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        mJavaCameraView.setVisibility(SurfaceView.VISIBLE);
        mJavaCameraView.setCvCameraViewListener(this);

        mThresholdText = (TextView) findViewById(R.id.threshold_text);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV loaded");
            mBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d(TAG, "OpenCV not found");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mBaseLoaderCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(FASTActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    public void disableCamera() {
        if (mJavaCameraView != null)
            mJavaCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        MatOfKeyPoint points = new MatOfKeyPoint();
        try {
            Mat mat = inputFrame.rgba();
            FeatureDetector fast = FeatureDetector.create(FeatureDetector.FAST);
            fast.detect(mat, points);

            List<KeyPoint> listOfKeypoints = points.toList();
            int size = listOfKeypoints.size();
            updateThresholdText(size);

            // show visual cue if min threshold reach
            if(size >= MIN_THRESHOLD){
                Scalar green = new Scalar(0, 255, 0);
                Mat mRgba = mat.clone();
                Imgproc.cvtColor(mat, mRgba, Imgproc.COLOR_RGBA2RGB, 4);
                Features2d.drawKeypoints(mRgba, points, mRgba, green, 1);
                mat.release();
                return mRgba;
            }else{
               return mat;
            }
        } catch (Exception e) {
            Log.e(TAG, "onCameraFrame Error : " + e);
            return inputFrame.rgba();
        }
    }

    private void updateThresholdText(final int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mThresholdText.setText(String.valueOf(size));
            }
        });
    }

//    public native void salt(long matAddrGray, int nbrElem);
}
