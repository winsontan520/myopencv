package com.winsontan520.myopencv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Winson Tan on 7/9/17.
 */

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final int DEFAULT_THRESHOLD = 5000;

    private Class[] clazzes;
    private EditText mThresholdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defined Array values to show in ListView
        String[][] values = new String[][]{
                {"FAST", "Feature detection with FAST. Permission to use camera required. Threshold limit 5000"},
                {"FAST with Camera2", "Feature detection with FAST using android camera2. Permissions to use camera and write storage required."},
                {"FAST with custom native lib", "Pass Mat to custom native method to process image with OpenCV FAST detection."}
        };

        clazzes = new Class[]{
                FASTActivity.class,
                FASTwithCamera2Activity.class,
                FASTcustomNativeActivity.class
        };

        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, values) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(getItem(position)[0]);
                text2.setText(getItem(position)[1]);
                text2.setPadding(0, 0, 0, 20);
                text2.setTextColor(Color.GRAY);
                return view;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchActivity(clazzes[position]);
            }
        });


        mThresholdEditText = (EditText) findViewById(R.id.threshold_edittext);

    }

    private void launchActivity(Class clazz) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permissions camera
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            // check threshold enter and pass to next activity
            int threshold = DEFAULT_THRESHOLD;
            try {
                String thresholdString = mThresholdEditText.getText().toString();

                if (thresholdString.length() > 0) {
                    threshold = Integer.valueOf(thresholdString);
                    if (threshold <= 0) {
                        // reset to default value if input invalid
                        threshold = DEFAULT_THRESHOLD;
                        mThresholdEditText.setText(String.valueOf(threshold));
                    }
                } else {
                    // reset to default
                    mThresholdEditText.setText(String.valueOf(threshold));
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception " + e);
                threshold = DEFAULT_THRESHOLD;
            }

            Toast.makeText(this, "Threshold selected = " + threshold, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, clazz);
            intent.putExtra("THRESHOLD", threshold);
            startActivity(intent);
        }
    }
}
