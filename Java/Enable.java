package com.example.rvn_gbrl.navigationsample;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Enable extends AppCompatActivity {
    Switch lockswitch;
    EditText mPass, mconPass, moldPass, mEmail;
    String password, email, lock,screenshot,secanswer,secque,algorithm;
    ConnectionDetector cd;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences setting = getSharedPreferences("PREFS", 0);


        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }




        setContentView(R.layout.activity_settings);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final Button mshowDialogalgo = (Button) findViewById(R.id.algo);
        Button mshowDialog = (Button) findViewById(R.id.btnshowpass);
        Button mshoweDialog = (Button) findViewById(R.id.btnshowemail);
        Button mshowsDialog = (Button) findViewById(R.id.forgot);



        lockswitch = (Switch) findViewById(R.id.switch1);
        final CheckedTextView ctv =(CheckedTextView)findViewById(R.id.checkedTextView1);






        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        // TextView lck =(TextView)findViewById(R.id.lckpass);
        algorithm = setting.getString("algorithm", "");
        Button mshowss = (Button) findViewById(R.id.algo);


        if (algorithm.equals("AES")) {

            mshowss.setText("Algorithm\nCurrent: AES (256 bit)");

        } else if(algorithm.equals("Blowfish")){

            mshowss.setText("Algorithm\nCurrent: Blowfish");
        }




        if(password.equals("")){
            lockswitch.setChecked(false);
            mshowDialog.setEnabled(false);
            mshoweDialog.setEnabled(false);
            mshowsDialog.setEnabled(false);


        }
        else{
            lockswitch.setText("LockScreen Password Enabled\n(Click to disable)");
            mshowDialog.setEnabled(true);
            mshoweDialog.setEnabled(true);
            mshowsDialog.setEnabled(true);

        }

        screenshot = setting.getString("screenshot", "");



        // SharedPreferences settings1 = getSharedPreferences("PREFS", 0); //eto yung shared tatawagin lang
        lock = setting.getString("lock", ""); //eto yung lock kanina na may laman na tinawag ko si lock para kunin yung nasa loob
        // nya yung ganto "" para mabasa kung nao yung nasaloob


        if (lock.equals("unlocked")) {
            lockswitch.setChecked(false);

        } else if (lock.equals("locked")) {
            lockswitch.setChecked(true);


        } else {
            lockswitch.setChecked(false);


        }

        if (screenshot.equals("allow")) {
            ctv.setChecked(true);

        } else if (screenshot.equals("unallow")) {
            ctv.setChecked(false);


        }


        //choosing algorithm

        //choosing algorithm



//screenshots
        String screen = "allow";
        String unscreen = "unallow";



        final String screen1 = screen.toString();
        final String screen2 = unscreen.toString();



        ctv.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                ctv.toggle();
                if(ctv.isChecked()){

                    SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor1 = settings1.edit();
                    editor1.putString("screenshot", screen1);
                    editor1.apply();



                }
                else {

                    SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor1 = settings1.edit();
                    editor1.putString("screenshot", screen2);
                    editor1.apply();




                }
            }
        } );



        //eto po


        //last na
        //screenshots


        //choosing algo
        mshowDialogalgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mEBuilder = new AlertDialog.Builder(Enable.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_algo, null);
                mEBuilder.setView(mView);
                final AlertDialog dialog = mEBuilder.create();


                String blow ="Blowfish";
                String aes ="AES";

                final String blowfish = blow.toString();
                final String aess = aes.toString();



                RadioGroup radioGroup=(RadioGroup) mView.findViewById(R.id.radio);



                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                algorithm = settings1.getString("algorithm", "");



                if (algorithm.equals("AES")) {
                    radioGroup.check(R.id.aes);


                } else if(algorithm.equals("Blowfish")){

                    radioGroup.check(R.id.blowfish);
                }



                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int i) {
                        AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(Enable.this);
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

                                        mshowDialogalgo.setText("Algorithm\nCurrent: AES (256 bit)");
                                        algorithm = settings1.getString("algorithm", "");
                                        Toast.makeText(Enable.this,
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

                                        mshowDialogalgo.setText("Algorithm\nCurrent: Blowfish");

                                        algorithm = settings1.getString("algorithm", "");
                                        Toast.makeText(Enable.this,
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
                        }


                    }


                });


                dialog.show();







            }

        });





//changing password
        mshowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mEBuilder = new AlertDialog.Builder(Enable.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_changepass, null);
                moldPass = (EditText) mView.findViewById(R.id.etoldpass);
                mPass = (EditText) mView.findViewById(R.id.etpass);
                mconPass = (EditText) mView.findViewById(R.id.etconpass);
                Button mProceed = (Button) mView.findViewById(R.id.btnproceed);


                mProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldpass1 = moldPass.getText().toString();
                        String pass1 = mPass.getText().toString();
                        String com1 = mconPass.getText().toString();
                        SharedPreferences setting = getSharedPreferences("PREFS", 0);
                        password = setting.getString("password", "");


                        if (oldpass1.equals(password)) {


                            if (pass1.equals("") || com1.equals("")) {

                                Toast.makeText(Enable.this, "No Password Entered", Toast.LENGTH_SHORT).show();
                            } else {
                                if (pass1.equals(com1)) {


                                    if (pass1.equals(oldpass1)) {
                                        Toast.makeText(Enable.this,
                                                "Old Password and New password is Same",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else  if (mPass.getText().toString().length() > 7) {


                                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("password", pass1);
                                        editor.apply();
                                        Toast.makeText(Enable.this,
                                                "Password Change",
                                                Toast.LENGTH_SHORT).show();
                                        finish();


                                    }
                                    else if (mPass.getText().toString().length() < 8) { //chcheck kung lessthan 8 yung tinaype ng user
                                        Toast.makeText(Enable.this,
                                                "Short Password",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (pass1 != com1) {
                                    Toast.makeText(Enable.this, "Password not Match", Toast.LENGTH_SHORT).show();


                                }
                            }


                        } else {

                            Toast.makeText(Enable.this, "Please input your Old Password", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                mEBuilder.setView(mView);
                AlertDialog dialog = mEBuilder.create();
                dialog.show();


                //eto last
            }
        });
        //last bracket for changing password


//changing email
        mshoweDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mEeBuilder = new AlertDialog.Builder(Enable.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_changeemail, null);
                mEmail = (EditText) mView.findViewById(R.id.etemail);
                mPass = (EditText) mView.findViewById(R.id.etcpass);
                Button mProceed = (Button) mView.findViewById(R.id.btnproceed);
                mProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = mEmail.getText().toString();
                        String pass1 = mPass.getText().toString();

                        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + //eto yung mag ba-validate kung tama ba ang email na nilagy ng user

                                "\\@" +

                                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                                "(" +

                                "\\." +

                                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                                ")+";


                        String emails = mEmail.getText().toString();

                        Matcher matcher = Pattern.compile(validemail).matcher(emails);


                        if (matcher.matches()) {

                            SharedPreferences uemail = getSharedPreferences("PREFS", 0);
                            SharedPreferences.Editor eemail = uemail.edit();
                            eemail.putString("email", emails);
                            eemail.apply();
                            SharedPreferences setting = getSharedPreferences("PREFS", 0);
                            password = setting.getString("password", "");





                            if (mEmail.equals(email)) {
                                Toast.makeText(Enable.this,
                                        "Old Email and New Email is Same",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else if (pass1.equals(password)) {

                                Toast.makeText(Enable.this, "Your New Email is: " + email, Toast.LENGTH_SHORT).show();
                                finish();

                                Toast.makeText(Enable.this, "Please Restart the Application to validate your email", Toast.LENGTH_SHORT).show();


                            }

                            else {

                                Toast.makeText(Enable.this, "Please input your  Password Correctly", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_LONG).show();
                        }


                    }
                });
                mEeBuilder.setView(mView);
                AlertDialog dialog = mEeBuilder.create();
                dialog.show();


            }
        });
        //last bracket ng changing email




    }







    //enabled dsiabled lock
    public void onSwitchClick(View v) {


        String locked = "locked";
        String unlocked = "unlocked";


        String lock1 = locked.toString();
        String lock2 = unlocked.toString();






        if (lockswitch.isChecked()) {
            SharedPreferences settings = getSharedPreferences("PREFS", 0);
            password = settings.getString("password", "");

            if(password.equals("")){
                //no detected password or no setup password
                Intent intent = new Intent(getApplicationContext(), CreatingPassword.class);
                startActivity(intent);

            }else{

                //enabled lockscreen

                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor1 = settings1.edit();
                editor1.putString("lock", lock1);
                editor1.apply();


                Toast.makeText(Enable.this,
                        "LockScreen Enabled",
                        Toast.LENGTH_SHORT).show();
                lockswitch.setText("LockScreen Password Enabled\n(Click to disable)");
            }


        } else {





            //disabled lockscreen
            if(password.equals("")){
                //no detected password or no setup password
                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor1 = settings1.edit();
                editor1.putString("lock", lock2);
                editor1.apply();

            }
            else {

                AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(Enable.this);
                showbuilder1.setCancelable(false);

                showbuilder1.setTitle("Report");
                showbuilder1.setMessage("Disable Lockscreen Password");
                showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String unlocked = "unlocked";
                        String lock2 = unlocked.toString();


                        SharedPreferences settings1 = getSharedPreferences("PREFS", 0); //tatawagin si shared pref
                        SharedPreferences.Editor editor1 = settings1.edit(); //eto yung mag eedit
                        editor1.putString("lock", lock2); //dito muna malalafay yung gustomo ipasok
                        // si lock yun yung pinaka variable, sya yung parang tiga hawak sa pinasok mo na variable kaya kahit saan activity kung tatawagin
                        //mo yung pinasok mo na lock2 kailangan mo din tawagin si lock para makuha nya yung database na nasa loob nya
                        //tapos si lock2 naman kahit ano lang yun na gusto mo ipasok sa shared pref

                        editor1.apply();//eto naman yung ipapasok na yung lock 2 sa shared pref
//

                        Toast.makeText(Enable.this,
                                "LockScreen Disabled",
                                Toast.LENGTH_SHORT).show();

                        lockswitch.setText("LockScreen Password Disabled\n(Click to enable)");
                    }
                });

                showbuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String locked = "locked";
                        String lock1 = locked.toString();




                        SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor1 = settings1.edit();
                        editor1.putString("lock", lock1);
                        editor1.apply();
                        lockswitch.setChecked(true);




                    }
                });
                showbuilder1.create().show();



            }

        }
    }
    //enabled dsiabled lock



    public void sendingemail(View v) {

        cd = new ConnectionDetector(this);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");
        email = settings.getString("email", "");


        String useremail = email;
        String subject = "ConCrypt Forgot Passsword";
        String message = "Hello User! Your LockScreen Password is: " + password + "\n\n\n\t We are sending this message because security is very crucial nowadays and we want to keep your application secure.\n\n\t For More inquiries you can message us in concryption@gmail.com \n\n\t Thank you!";
        if (cd.isConnected()) {
            //Creating SendMail object
            SendMail sm = new SendMail(this, useremail, subject, message);

            //Executing sendmail to send email
            sm.execute();


        } else {
            AlertDialog.Builder mEBuilder = new AlertDialog.Builder(Enable.this);
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
                        AlertDialog.Builder showbuilder =new AlertDialog.Builder(Enable.this);
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
                        Toast.makeText(Enable.this, "Wrong Password", Toast.LENGTH_SHORT).show();



                    }



                }

            });
            mEBuilder.setView(mView);
            dialog.show();

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }



}
