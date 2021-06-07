package com.example.healthpedometer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class Pedometer extends Activity implements SensorEventListener {
    static final int PERMISSIONS_REQUEST= 0x0000001;
    //현재 걸음 수
    private int Steps = 0;
    //리스너가 등록되고 난 후의 step count
//    private int CounterSteps = 0;

    //센서 연결을 위한 변수
    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    @Override
    public void onSensorChanged(SensorEvent event) {
        final TextView Walknum = (TextView) findViewById(R.id.main_stepcount);
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
//            if (CounterSteps < 1) {
//                // initial value
//                CounterSteps = (int) event.values[0];
//            }
            //리셋 안된 값 + 현재값 - 리셋 안된 값
//            Steps = (int) event.values[0] - CounterSteps;
//            CounterSteps++;
            if(event.values[0] == 1.0f) {
                Steps++;
                Walknum.setText(Steps + "걸음");
                Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + Steps);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        sensorManager.unregisterListener(this);
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_UI);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정되었습니다", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정되었습니다", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometeter);
        OnCheckPermission();
        //센서 연결[걸음수 센서를 이용한 흔듬 감지]
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        final TextView tex = (TextView) findViewById(R.id.main_stepcount);
        ImageButton Start = (ImageButton) findViewById(R.id.main_imgBtn_start);
        ImageButton Stop = (ImageButton) findViewById(R.id.main_imgBtn_stop);
        ImageButton Map = (ImageButton) findViewById(R.id.main_imgBtn_map);
        tex.setText(Steps +"걸음");
        // Required empty public constructor
        if (stepCountSensor == null) {
            Toast.makeText(this.getApplicationContext(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        Start.setOnClickListener(v -> {
            onStart();
        });

        Stop.setOnClickListener(v -> {
            onStop();
        });
        Map.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, MapsActivity.class);
            startActivity(intent1);
        });
    }



    public void OnCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PERMISSION_GRANTED)  {//|| ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACTIVITY_RECOGNITION)) {
                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION,Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSIONS_REQUEST);
            }
        }
    }


    public void onStart() {
        super.onStart();
        //센서의 속도 설정
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_GAME);
        if (stepCountSensor != null) {
            Toast.makeText(this.getApplicationContext(), "connect Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        else if (sensorManager == null){
            Toast.makeText(this.getApplicationContext(), "Step Detect Sensor connection fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
        if (sensorManager != null) {
            Toast.makeText(this.getApplicationContext(), "disconnect Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }else if (sensorManager == null){
            Toast.makeText(this.getApplicationContext(), "Step Detect Sensor disconnect fail", Toast.LENGTH_SHORT).show();
        }
    }
}