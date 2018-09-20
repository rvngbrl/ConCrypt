package com.example.rvn_gbrl.navigationsample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

public class SplashActivity extends Activity {
    String password, lock,screenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);


        SharedPreferences settingss = getSharedPreferences("PREFS", 0);

        screenshot = settingss.getString("screenshot", "");

        if (screenshot.equals("")) {
            String unscreen = "unallow";



            final String screen2 = unscreen.toString();
            SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor1 = settings1.edit();
            editor1.putString("screenshot", screen2);
            editor1.apply();


        }






        SharedPreferences setting = getSharedPreferences("PREFS", 0);
        password = setting.getString("password", "");


        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        lock = setting.getString("lock", "");



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(password.equals("")){
                    //no detected password or no setup password
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if(lock.equals("unlocked")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();



                }
                else if(lock.equals("locked")){
                    Intent intent = new Intent(getApplicationContext(), EnteringPassword.class);
                    startActivity(intent);
                    finish();


                }

                else{
                    Intent intent = new Intent(getApplicationContext(), EnteringPassword.class);
                    startActivity(intent);
                    finish();



                }

            }
        }, 2000);




    }
}
