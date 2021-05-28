package com.example.healthpedometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Pedometer extends Activity implements SensorEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometeter);
        Pedometer();
    }


    //현재 걸음 수
    private int Steps = 0;
    //리스너가 등록되고 난 후의 step count
    private int CounterSteps = 0;


    //센서 연결을 위한 변수
    private SensorManager sensorManager;
    //private Sensor accelerormeterSensor;
    private Sensor stepCountSensor;



    public void Pedometer() {
        ImageButton Start = (ImageButton) findViewById(R.id.main_imgBtn_start);
        ImageButton Stop = (ImageButton) findViewById(R.id.main_imgBtn_stop);

        // Required empty public constructor
        if (stepCountSensor == null) {
            Toast.makeText(this.getApplicationContext(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        Start.setOnClickListener(v -> {
            //센서 연결[걸음수 센서를 이용한 흔듬 감지]
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            //accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            onStart();
        });

        Stop.setOnClickListener(v -> {
            onStop();
        });
    }



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.activity_pedometeter, container, false);

//        Start = (ImageButton) Start.findViewById(R.id.main_imgBtn_start);
//        Stop = view.findViewById(R.id.main_imgBtn_stop);
//        Walknum = view.findViewById(R.id.main_stepcount);


//        if (stepCountSensor == null) {
//            Toast.makeText(this.getApplicationContext(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
////                 Toast.makeText(this,"No Step Detect Sensor", Toast.LENGTH_SHORT).show();
//        }


//        Start.setOnClickListener(v -> {
//
//            onStart();
//        });




//        Stop.setOnClickListener(v -> {
//            onStop();
//        });


//        return view;
//    }

    public void onStart() {
        super.onStart();
        if (stepCountSensor != null) {
            //센서의 속도 설정
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_GAME);
            Toast.makeText(this.getApplicationContext(), "connect Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            Toast.makeText(this.getApplicationContext(), "disconnect Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView Walknum = (TextView) findViewById(R.id.main_stepcount);
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
            if (CounterSteps < 1) {
                // initial value
                CounterSteps = (int) event.values[0];
            }
            //리셋 안된 값 + 현재값 - 리셋 안된 값
            Steps = (int) event.values[0] - CounterSteps;
            Walknum.setText(Steps + "걸음");
            Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + Steps);
        }
//        if (CounterSteps < 1) {
//                // initial value
//                CounterSteps = (int) event.values[0];
//            }
//        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
//            Steps++;
//            walknum.setText(Steps + "걸음");
//            Log.i("log: ", "New step detected by STEP_DETECTOR sensor. Total step count: " + Steps);
//        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}