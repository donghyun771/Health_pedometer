package com.example.healthpedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PedometerFragment pedometer = new PedometerFragment();
    }

    public class PedometerFragment extends Fragment implements SensorEventListener {
//        ImageButton Start;
        ImageButton Reset;
        TextView walknum;

        //현재 걸음 수
        private int Steps = 0;
        //리스너가 등록되고 난 후의 step count
        private int CounterSteps = 0;


        //센서 연결을 위한 변수
        private SensorManager sensorManager;
        //private Sensor accelerormeterSensor;
        private Sensor stepCountSensor;


        private View view;


        public PedometerFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_main, container, false);

//            Start = (ImageButton) Start.findViewById(R.id.main_imgBtn_start);
            Reset = view.findViewById(R.id.main_imgBtn_reset);
            walknum = view.findViewById(R.id.main_stepcount);

            //센서 연결[걸음수 센서를 이용한 흔듬 감지]
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            //accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            if (stepCountSensor == null) {
                // Toast.makeText(this,"No Step Detect Sensor",Toast.LENGTH_SHORT).show();
            }


//            Start.setOnClickListener(v -> {
//
//            });


            //초기화 버튼 : 다시 숫자를 0으로 만들어준다.
            Reset.setOnClickListener(v -> {
                Steps = 0;
                CounterSteps = 0;
                walknum.setText(Steps + "걸음");

            });


            return view;
        }

        public void onStart() {
            super.onStart();
            if (stepCountSensor != null) {
                //센서의 속도 설정
                sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }

        public void onStop() {
            super.onStop();
            if (sensorManager != null) {
                sensorManager.unregisterListener(this);
            }
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

                //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
                if (CounterSteps < 1) {
                    // initial value
                    CounterSteps = (int) event.values[0];
                }
                //리셋 안된 값 + 현재값 - 리셋 안된 값
                Steps = (int) event.values[0] - CounterSteps;
                walknum.setText(Steps + "걸음");
                Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + Steps);
            }

        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}