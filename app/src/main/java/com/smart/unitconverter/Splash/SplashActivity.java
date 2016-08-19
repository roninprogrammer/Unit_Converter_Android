package com.smart.unitconverter.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smart.unitconverter.Home.UnitAreaConverterActivity;
import com.smart.unitconverter.R;


/**
 * Created by VICKNESH on 8/17/2016.
 */
public class SplashActivity extends Activity {

    Handler timeHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, UnitAreaConverterActivity.class);
                startActivity(intent);
            }
        }, 1000);



    }
}