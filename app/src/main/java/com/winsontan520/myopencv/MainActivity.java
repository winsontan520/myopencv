package com.winsontan520.myopencv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Winson Tan on 7/9/17.
 */

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Class[] clazzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defined Array values to show in ListView
        String[][] values = new String[][]{
                {"FAST", "Feature detection with FAST. Permission to use camera required. Threshold limit 5000"},
                {"FAST with Camera2", "Feature detection with FAST using android camera2. Permissions to use camera and write storage required."}
        };

        clazzes = new Class[]{
                FASTActivity.class,
                FASTwithCamera2Activity.class
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

    }

    private void launchActivity(Class clazz) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permissions camera
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            startActivity(new Intent(this, clazz));
        }
    }
}
