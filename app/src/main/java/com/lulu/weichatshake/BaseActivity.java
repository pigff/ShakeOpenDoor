package com.lulu.weichatshake;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lin on 2017/2/24.
 */

public abstract class BaseActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
