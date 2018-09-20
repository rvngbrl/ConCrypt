package com.example.rvn_gbrl.navigationsample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rvn_gbrl.navigationsample.FilepickerDecrypt.DecryptExplorer;
import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptExplorer;
import com.example.rvn_gbrl.navigationsample.FirstInstall.firstinstall;
import com.example.rvn_gbrl.navigationsample.HelpActivity.help;
import com.example.rvn_gbrl.navigationsample.SendingDelete.SendingChoose;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_PATH = 1;
    String password,email,screenshot,algorithm,showme;
    private static final int REQUEST_WRITE_PERMISSIONS =  786;

    public void OnRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

       /* if(requestCode==REQUEST_READ_PERMISSION&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            System.out.print("Permission is granted");
            //Log.e("TAG", "Permission is Granted");
        }*/
        if (requestCode == REQUEST_WRITE_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "Permission is Granted");
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSIONS);
        } else {
            Log.e("TAG", "Permission is Granted Gustin");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();

        SharedPreferences setting= getSharedPreferences("PREFS", 0);




        showme = setting.getString("showme", "");

        if (showme.equals("unshow")) {

            Intent intent = new Intent(getApplicationContext(), firstinstall.class);
            startActivity(intent);


        }

        algorithm = setting.getString("algorithm", "");

        if(algorithm.equals("")){


            Intent intent = new Intent(getApplicationContext(), firstinstall.class);
            startActivity(intent);






            AlertDialog.Builder mEBuilder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.activity_algo, null);
            mEBuilder.setView(mView);
            final AlertDialog dialog = mEBuilder.create();


            String blow ="Blowfish";
            String aes ="AES";

            final String blowfish = blow.toString();
            final String aess = aes.toString();



            RadioGroup radioGroup=(RadioGroup) mView.findViewById(R.id.radio);



            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int i) {
                    AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(MainActivity.this);
                    switch (i){

                        case R.id.aes:
                            dialog.dismiss();



                            showbuilder1.setCancelable(false);

                            showbuilder1.setTitle("Report");
                            showbuilder1.setMessage("You Choosed AES (256 bit) Algorithm");
                            showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                    SharedPreferences.Editor editor1 = settings1.edit();
                                    editor1.putString("algorithm", aess);
                                    editor1.apply();


                                    algorithm = settings1.getString("algorithm", "");
                                    Toast.makeText(MainActivity.this,
                                            algorithm,
                                            Toast.LENGTH_SHORT).show();





                                }
                            });

                            showbuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            showbuilder1.create().show();
                            break;


                        case R.id.blowfish:

                            dialog.dismiss();

                            //AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(Enable.this);
                            showbuilder1.setCancelable(false);

                            showbuilder1.setTitle("Report");
                            showbuilder1.setMessage("You Choosed Blowfish Algorithm");
                            showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                    SharedPreferences.Editor editor1 = settings1.edit();
                                    editor1.putString("algorithm", blowfish);
                                    editor1.apply();



                                    algorithm = settings1.getString("algorithm", "");
                                    Toast.makeText(MainActivity.this,algorithm,Toast.LENGTH_SHORT).show();


                                }
                            });

                            showbuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {





                                }
                            });
                            showbuilder1.create().show();
                            break;
                    }


                }


            });


            dialog.show();




        }



        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }


        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layouts);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//load email for checking
        SharedPreferences uemail = getSharedPreferences("PEEFS", 0);
        email = uemail.getString("email", "");
        //load password for checking
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");







        //String emailmu =email;
      //formail.append(emailmu);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        password = settings.getString("password", "");
        email = settings.getString("email", "");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView formail =(TextView)header.findViewById(R.id.foremail);
        formail.setText(email);
    }

    boolean doubleBacktoExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if (doubleBacktoExitPressedOnce){

            super.onBackPressed();
        }
                        else{
                            Toast.makeText(this,
                                    "Press BACK again to exit",
                                    Toast.LENGTH_SHORT).show();
                            doubleBacktoExitPressedOnce = true;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doubleBacktoExitPressedOnce =false;

                                }
                            },2000); //500 = half second 2000= 2 seconds

                        }


    }


    /*boolean doubleBacktoExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBacktoExitPressedOnce){

            super.onBackPressed();
        }
    else{
        Toast.makeText(this,
                "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();
            doubleBacktoExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBacktoExitPressedOnce =false;

            }
        },2000); //500 = half second 2000= 2 seconds

    }}*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      if (id == R.id.nav_lock) {



              Intent intent = new Intent(getApplicationContext(), Enable.class);
              startActivity(intent);


         /* if(password.equals("")){
              //no detected password or no setup password
              Intent intent = new Intent(getApplicationContext(), CreatingPassword.class);
              startActivity(intent);

          }else{

              Intent intent = new Intent(getApplicationContext(), Enable.class);
              startActivity(intent);



          }
*/


        } else if (id == R.id.nav_encrypt) {
          Intent browse = new Intent(this, SendingChoose.class);
          startActivityForResult(browse, REQUEST_PATH);
        } else if (id == R.id.nav_about) {
          Intent intent = new Intent(getApplicationContext(), About.class);
          startActivity(intent);
        }

      else if (id == R.id.nav_AES) {
          Intent intent = new Intent(getApplicationContext(), Algorithm.class);
          startActivity(intent);
      }


      else if (id == R.id.nav_help) {
          Intent intent = new Intent(getApplicationContext(), help.class);
          startActivity(intent);
      }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void convertclick(View view){
        Button button = (Button)findViewById(R.id.btnconvert);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);


        Intent convertbtn = new Intent(this, Converts.class);
        startActivity(convertbtn);


    }

    public void encryptclick(View view){
        Button button = (Button)findViewById(R.id.btnencrypt);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);

        Intent encryptbtn = new Intent(this, EncryptExplorer.class);
        startActivity(encryptbtn);

    }
    public void decryptclick(View view){
        Button button = (Button)findViewById(R.id.btndecrypt);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);


        Intent decryptbtn = new Intent(this, DecryptExplorer.class);
        startActivity(decryptbtn);


    }



    ///ala



    /*boolean doubleBacktoExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBacktoExitPressedOnce){

            super.onBackPressed();
        }
    else{
        Toast.makeText(this,
                "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();
            doubleBacktoExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBacktoExitPressedOnce =false;

            }
        },2000); //500 = half second 2000= 2 seconds

    }}*/



}
