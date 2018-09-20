package com.example.rvn_gbrl.navigationsample;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EnteringPassword extends AppCompatActivity {
    EditText EnterPass;
    Button Enterbtn;
    String password, email,screenshot,secanswer,secque;
    ConnectionDetector cd;
   Integer tries= 3;
    Integer tryenter= 3;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences setting = getSharedPreferences("PREFS", 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);


        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }

        screenshot = setting.getString("screenshot", "");

        setContentView(R.layout.entering_password);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        EnterPass = (EditText)findViewById(R.id.Identer);
        Enterbtn = (Button) findViewById(R.id.btnenter);
        final Button forgot=(Button)findViewById(R.id.forgotpass);
        EnterPass.setLongClickable(false);
        forgot.setVisibility(View.INVISIBLE);



        Enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        password = settings.getString("password", "");


                        String pass1 = EnterPass.getText().toString();
                        if(pass1.equals(password)){


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }


                        else
                        {


                            EnterPass.startAnimation(shakeError());
                            Vibrator vibrate =(Vibrator) EnteringPassword.this.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrate.vibrate(200);
                            EnterPass.getText().clear();
                            forgot.setVisibility(View.VISIBLE);
                            TextView time=(TextView) findViewById(R.id.tv10);
                            if (!pass1.equals(password)){

                                tries--;
                                if(tries.equals(0)){
                                    time.setText("Try Again in ");
                                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(EnteringPassword.this)
                                            .setSmallIcon(R.mipmap.ic_encrypticon)
                                            .setContentTitle("Intruder Alert")
                                            .setContentText("Concrypt");

                                    builder.setLights(Color.BLUE, 500, 500);
                                    long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                                    builder.setVibrate(pattern);
                                    Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert);
                                    //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    builder.setSound(soundUri);

                                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                                    notificationManager.notify(0, builder.build());

                                    EnterPass.setEnabled(false);
                                    Enterbtn.setEnabled(false);
                                    start();
                                }
                            }


                        }





            }
        });


    }

    private void start(){

        final TextView time=(TextView) findViewById(R.id.tv10);
        time.setText("Try Again in: 30 seconds");
        timer = new CountDownTimer(30*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("Try Again in: "+millisUntilFinished/1000+" seconds");

            }

            @Override
            public void onFinish() {
             time.setText("Please Input Your Password");
                EnterPass.setEnabled(true);
                Enterbtn.setEnabled(true);
                tries=3;
            }
        };
        timer.start();

    }
    public void sendingemail(View v){
        cd = new ConnectionDetector(this);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");
        email = settings.getString("email", "");









        String useremail = email;
        String subject = "ConCrypt Forgot Passsword";
        String message =  "Hello User! Your LockScreen Password is: "+password +"\n\n\n\t We are sending this message because security is very crucial nowadys and we want to keep your application secure.\n\n\t For More inquiries you can message us in concryption@gmail.com \n\n\t Thank you!";

        if(cd.isConnected()){
            //Creating SendMail object
            SendMail sm = new SendMail(this, useremail, subject, message);

            //Executing sendmail to send email
            sm.execute();


        }
        else{
            AlertDialog.Builder mEBuilder = new AlertDialog.Builder(EnteringPassword.this);
            View mView = getLayoutInflater().inflate(R.layout.dialogsecque, null);
            mEBuilder.setView(mView);
            final AlertDialog dialog = mEBuilder.create();


            secque = settings.getString("secque", "");
            secanswer=settings.getString("secanswer", "");
            TextView textss =(TextView)mView.findViewById(R.id.sectext);
            textss.setText(secque);





            Button proceed = (Button) mView.findViewById(R.id.proceedans);
           final EditText answersec = (EditText) mView.findViewById(R.id.etsec);

            proceed.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    //validate the security answer
                    String answerss = answersec.getText().toString().toLowerCase();
                    if(answerss.equals(secanswer)){
                        AlertDialog.Builder showbuilder =new AlertDialog.Builder(EnteringPassword.this);
                        showbuilder.setCancelable(false);

                        showbuilder.setTitle("Report");
                        showbuilder.setMessage("Your Password is: "+password);
                        showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                        showbuilder.create().show();


                    }else{
                        Toast.makeText(EnteringPassword.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        answersec.getText().clear();
tryenter--;
                        if (tryenter.equals(0)){

finishAndRemoveTask();

                        }



                    }



                }
                   ;
            });
            mEBuilder.setView(mView);
            dialog.show();


        }


    }
    public TranslateAnimation shakeError(){
        TranslateAnimation shake =new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));

        return  shake;
    }
}
