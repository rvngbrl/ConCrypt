package com.example.rvn_gbrl.navigationsample;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  CreatingPassword extends AppCompatActivity {
    EditText IdPass, IdCom, etemail,IdSecans;
    Button Confirmbtn;
    String screenshot;
    Spinner spinner;

    String choose[] = {"First name of your first kissed?", "Teacher name who gave you your first failing grade?","What was your mother maiden's name?","What was your first job?","Favourite tv show?"};
    ArrayAdapter <String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);



        SharedPreferences setting = getSharedPreferences("PREFS", 0);

        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }
        setContentView(R.layout.creating_password);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layoutss);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();



        IdPass = (EditText)findViewById(R.id.passid);
        IdCom = (EditText)findViewById(R.id.comid);
        etemail = (EditText)findViewById(R.id.emailid);
        Confirmbtn = (Button)findViewById(R.id.btnconfirm);
        spinner = (Spinner) findViewById(R.id.spinner3);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, choose);


        spinner.setAdapter(adapter);









        Confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String secque1 =   spinner.getSelectedItem().toString();


                final String question;
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinner.setOnItemSelectedListener(this);
                        switch (position) {
                            case 0:
                                spinner.setOnItemSelectedListener(this);
                                String que1 ="First name of your first kissed?";





                                break;
                            case 1:
                                spinner.setOnItemSelectedListener(this);
                                String que2 ="Teacher name who gave you your first failing grade?";

                                break;
                            case 2:

                                spinner.setOnItemSelectedListener(this);
                                String que3 ="What was your mother maiden's name?";

                                break;


                            case 3:
                                spinner.setOnItemSelectedListener(this);
                                String que4 ="What was your first job?";

                                break;

                            case 4:
                                spinner.setOnItemSelectedListener(this);
                                String que5 ="Favourite tv show?";

                                break;

                        }}
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {



                    }
                });






                SharedPreferences settings = getSharedPreferences("PREFS", 0);


                IdSecans =(EditText)findViewById(R.id.secanswers) ;

                String secans1 =   IdSecans.getText().toString().toLowerCase();

                SharedPreferences.Editor editorans = settings.edit();
                editorans.putString("secanswer", secans1);
                editorans.apply();
                SharedPreferences.Editor editorque = settings.edit();
                editorque.putString("secque", secque1);
                editorque.apply();




                if(secans1.equals("")|| secans1.equals(" ")){
                    Toast.makeText(CreatingPassword.this, "Security Answer is Empty", Toast.LENGTH_SHORT).show();
                }else{
                    Button button = (Button)findViewById(R.id.btnconfirm);
                    final Animation myAnim = AnimationUtils.loadAnimation(CreatingPassword.this, R.anim.bounce);

                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);

                    button.startAnimation(myAnim);

                    String pass1 = IdPass.getText().toString();
                    String com1 = IdCom.getText().toString();

                    String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                            "\\@" +

                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                            "(" +

                            "\\." +

                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                            ")+";


                    String emails = etemail.getText().toString();

                    Matcher matcher= Pattern.compile(validemail).matcher(emails);



                    if(pass1.equals("") || com1.equals("") ){

                        Toast.makeText(CreatingPassword.this, "NO Password Entered", Toast.LENGTH_SHORT).show();
                    }else{
                        if(pass1.equals(com1)){
                            if (IdPass.getText().toString().length() > 7) {
                                if (matcher.matches()) {

                                    //para sa mini database ng pass
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("password", pass1);
                                    editor.apply();


                                    //para sa mini database ng email
                                    SharedPreferences uemail = getSharedPreferences("PREFS", 0); //ok lang magkaiba ng name ang sharepref basta wag lang yung PREFS at 0
                                    SharedPreferences.Editor eemail = settings.edit();
                                    eemail.putString("email", emails);
                                    eemail.apply();





                                    Toast.makeText(getApplicationContext(),"Creating Password Succesful",Toast.LENGTH_LONG).show();

                                    String locked = "locked";



                                    String lock1 = locked.toString();
                                    SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                    SharedPreferences.Editor editor1 = settings1.edit();
                                    editor1.putString("lock", lock1);
                                    editor1.apply();


                                    Toast.makeText(CreatingPassword.this,
                                            "LockScreen Enabled",
                                            Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    recreate();



                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(IdPass.getText().toString().length() < 8){ //chcheck kung lessthan 8 yung tinaype ng user
                                Toast.makeText(CreatingPassword.this,
                                        "Short Password",
                                        Toast.LENGTH_SHORT).show();}
                        }else{
                            Toast.makeText(CreatingPassword.this, "Password not Match", Toast.LENGTH_SHORT).show();


                        }



                    }


                }


            }
        });




    }
}
