package com.lulu.weichatshake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lulu.weichatshake.door.DeviceBean;
import com.lulu.weichatshake.door.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by lin on 2016/8/15.
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOGIN_SUCCESS = 0x00;

    private static final int LOGIN_FAILED = 0x01;

    private static final int CLIENT_ID_NULL = 0x02;

    private static final int DATA_NULL = 0x03;

    private static final String ACCOUNT = "1806902230@qq.com";
    private static final String PASSWORD = "He123456987";

    private static final String TAG = "LoginActivity";

    private static String client_id = null;
    private static ArrayList<DeviceBean> devList = new ArrayList<DeviceBean>();

    private EditText mAccountEdit;
    private EditText mPwdEdit;

    private String mUserName;
    private String mPassword;
    private Button mLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        getSp();

        initView();

        initListener();
    }

    private void initListener() {
        mLoginBtn.setOnClickListener(this);
    }

    private void initView() {

        mAccountEdit = (EditText) findViewById(R.id.edit_input_account);
        mPwdEdit = (EditText) findViewById(R.id.edit_input_pwd);
        mLoginBtn = (Button) findViewById(R.id.btn_login);

        if (!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPassword)) {
            mAccountEdit.setText(mUserName);
            mPwdEdit.setText(mPassword);
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * 将用户名和密码记录进sharedpreferences中
     */
    private void initSp() {
        SharedPreferences sp = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.ACCOUNT, mUserName);
        editor.putString(Constants.PASSWORD, mPassword);
        editor.apply();
    }

    private void getSp() {
        SharedPreferences sp = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        mUserName = sp.getString(Constants.ACCOUNT, null);
        mPassword = sp.getString(Constants.PASSWORD, null);
    }


    /**
     * -------------------------------------------------------------------------------------------------------------------------------------------------
     */


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mUserName = mAccountEdit.getText().toString().trim();
                mPassword = mPwdEdit.getText().toString().trim();
                if (TextUtils.equals(mUserName, "") || TextUtils.equals(mPassword, "")) {
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    // 登录
    private void login() {

        try {
            JSONObject login_ret = Request.login(mUserName, mPassword);
            if (login_ret == null || login_ret.isNull("ret")) {
                Log.e("doormaster", "login_ret error");
                return;
            }
            Log.e("doomaster", login_ret.toString());
            int ret = login_ret.getInt("ret");
            Message msg = new Message();
            if (ret == 0) {
                if (login_ret.isNull("data")) {
                    mHandler.sendEmptyMessage(DATA_NULL);
                    return;
                }
                JSONObject data = login_ret.getJSONObject("data");
                if (!data.isNull("client_id")) {
                    client_id = data.getString("client_id");
                    devList = Request.reqDeviceList(client_id);
                    if (devList == null) {
                        Log.e("doormaster", "devList is null");
                        devList = new ArrayList<DeviceBean>();
                    }
                    mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                } else {
                    msg.what = CLIENT_ID_NULL;
                    mHandler.sendMessage(msg);
                }
            } else {
                msg.what = LOGIN_FAILED;
                msg.obj = ret;
                mHandler.sendMessage(msg);
            }
        } catch (JSONException e) {
            Log.e("doormaster ", e.getMessage());
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    for (DeviceBean deviceBean : devList) {
                        Log.d(TAG, deviceBean.toString());
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Log.d(TAG, devList.get(1).toString());
                    intent.putExtra(Constants.DATA, devList.get(1));
                    startActivity(intent);
                    initSp();
                    finish();
//                    refreshScanList();
//
//                    // Get the last device to test
//                    DeviceBean dev = devList.get(devList.size()-1);
//                    String devSn = dev.getDevSn();

                    break;
                case CLIENT_ID_NULL:
                    Toast.makeText(LoginActivity.this, "client_id is null",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "client_id is null");
                    break;
                case LOGIN_FAILED:
                    int ret = (Integer) msg.obj;
                    Toast.makeText(LoginActivity.this, "Login Failure" + ret,
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login Failure");
                    break;
            }
        }

    };
}
