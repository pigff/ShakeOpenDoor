package com.lulu.weichatshake;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.lulu.weichatshake.door.DeviceBean;

/**
 * Created by lin on 2017/3/18.
 */

public class Main2Activity extends Activity implements View.OnClickListener, SensorEventListener {

    private static final String TAG = "Main2Activity";

    private DrawerLayout mDrawerLayout;
    private LinearLayout mFirstLayout;
    private LinearLayout mSecondLayout;
    private LinearLayout mThirdLayout;
    private LinearLayout mForthLayout;
    private LinearLayout mFifthLayout;
    private LinearLayout mSixthLayout;
    private DeviceBean mDeviceBean;
    private ImageView mImageView;

    //    private MyHandler mHandler;
    private int mWeiChatAudio;
    private String mDevSn;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    //记录摇动状态
    private boolean isShake = false;

    final LibInterface.ManagerCallback callback = new LibInterface.ManagerCallback() {
        @Override
        public void setResult(final int result, Bundle bundle) {

//                    pressed = false; // 二次点击处理（避免重复点击）
//					mHandler.sendEmptyMessage(OPEN_AGAIN);
            if (result == 0x00) {
//                        Toast.makeText(MainActivity.this, "Success",
//                                Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Success");
            } else {
//                        Toast.makeText(MainActivity.this, "Failure:" + result,
//                                Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failure:");
            }
            isShake = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.fragment_property);

//        Intent intent = getIntent();
//        mDeviceBean = (DeviceBean) intent.getSerializableExtra(Constants.DATA);
        mDeviceBean = DeviceBean.getDefault();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawale_layout);
        mFirstLayout = (LinearLayout)findViewById(R.id.grid_btn_notice);
        mSecondLayout = (LinearLayout) findViewById(R.id.grid_btn_payment);
        mThirdLayout = (LinearLayout) findViewById(R.id.grid_btn_repair);
        mForthLayout = (LinearLayout) findViewById(R.id.grid_btn_complain);
        mFifthLayout = (LinearLayout) findViewById(R.id.group_01);
        mSixthLayout = (LinearLayout) findViewById(R.id.group_02);
        mImageView = (ImageView) findViewById(R.id.test_img);
        mImageView.setOnClickListener(this);

        mFirstLayout.setOnClickListener(this);
        mSecondLayout.setOnClickListener(this);
        mThirdLayout.setOnClickListener(this);
        mForthLayout.setOnClickListener(this);
        mFifthLayout.setOnClickListener(this);
        mSixthLayout.setOnClickListener(this);

        //初始化SoundPool
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);

        //获取Vibrator震动服务
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_01:
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.putExtra(Constants.DATA, mDeviceBean);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.test_img:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                Toast.makeText(this, "功能尚在开发中", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 20 || Math.abs(y) > 20 || Math
                    .abs(z) > 20) && !isShake) {
                if (TextUtils.equals(Utils.getBLEState(), "true")) {
                    isShake = true;
                    mVibrator.vibrate(300);
                    //发出提示音
                    mSoundPool.play(mWeiChatAudio, 1, 1, 0, 0, 1);
                    LibDevModel libDev = getLibDev(mDeviceBean);
                    int ret = LibDevModel.openDoor(Main2Activity.this, libDev, callback); //Open door
                } else {
                    Toast.makeText(this, Utils.getBLEState(), Toast.LENGTH_SHORT).show();
                }

                //整体效果结束, 将震动设置为false

                //开始震动 发出提示音 展示动画效果
//                mHandler.obtainMessage(START_SHAKE).sendToTarget();
//
//                mHandler.obtainMessage(END_SHAKE).sendToTarget();
            }
        }
    }

    public LibDevModel getLibDev(DeviceBean dev) {
        LibDevModel device = new LibDevModel();
        device.devSn = dev.getDevSn();
        device.devMac = dev.getDevMac();
        device.devType = dev.getDevType();
        device.eKey = dev.geteKey();
        device.endDate = dev.getEndDate();
        device.openType = dev.getOpenType();
        device.privilege = dev.getPrivilege();
        device.startDate = dev.getStartDate();
        device.useCount = dev.getUseCount();
        device.verified = dev.getVerified();
        return device;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
