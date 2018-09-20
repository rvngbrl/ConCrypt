package com.example.rvn_gbrl.navigationsample.FilepickerEncrypt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

;


import com.example.rvn_gbrl.navigationsample.FolderEncrypt.FolderChoose;
import com.example.rvn_gbrl.navigationsample.MyBounceInterpolator;
import com.example.rvn_gbrl.navigationsample.R;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptExplorer extends AppCompatActivity {
    private static final int REQUEST_PATH = 1;
    String alamu;
    String algorithm;
    String choose;

    String curFileName;
    String curPathName;
    private File destinationFile;
    File dir;
    File zipdir;

    String keypass;
    CheckBox chkParents;
    int file_size, file_sizezip;
    EditText edittext;
    EditText pathname;
    private ProgressDialog progress;
    private static final String[] optionItems = new String[]{"One Click Auto Detect", "Manual Select"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encryptexplorer);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        requestPermission();

        this.edittext = (EditText) findViewById(R.id.editText);
        this.pathname = (EditText) findViewById(R.id.pathname);
        edittext.setKeyListener(null);


        ((Button) findViewById(R.id.btnEncDialog)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //animation button
                Button button = (Button)findViewById(R.id.btnEncDialog);
                final Animation myAnim = AnimationUtils.loadAnimation(EncryptExplorer.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                button.startAnimation(myAnim);
                //animation button





                final EditText browser = (EditText) EncryptExplorer.this.findViewById(R.id.editText);
                String nakuha = browser.getText().toString();
//
//                dir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
//                File file = new File(dir, nakuha); // keni y mka initialized nung nanu ya directory
//
//                zipdir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
//                File filezip = new File(zipdir.getAbsolutePath()); // keni y mka initialized nung nanu ya directory
//
//                System.out.println("dir ="+dir);
//                System.out.println("zipdir ="+zipdir);
//                System.out.println("file ="+file);
//                System.out.println("filezip ="+filezip);




                if (nakuha.toString().isEmpty()) {
                    Toast.makeText(EncryptExplorer.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    return;
                }  else if(pathname.getText().toString().equals("")){
                    Toast.makeText(EncryptExplorer.this,
                            "Invalid File",
                            Toast.LENGTH_SHORT).show();
                    browser.getText().clear();

                }else{

                    Builder mEBuilder = new Builder(EncryptExplorer.this);
                    View mView = EncryptExplorer.this.getLayoutInflater().inflate(R.layout.dialog_encrypt, null);
                    final EditText mPass = (EditText) mView.findViewById(R.id.etpass);
                    final EditText mconPass = (EditText) mView.findViewById(R.id.etconpass);
                    Button mProceed = (Button) mView.findViewById(R.id.btnproceed);
                    mEBuilder.setView(mView);
                    final AlertDialog dialog = mEBuilder.create();
                    mProceed.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (mPass.getText().toString().isEmpty() || mconPass.getText().toString().isEmpty()) {
                                Toast.makeText(EncryptExplorer.this, R.string.error_password_msg,Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String nakuha = browser.getText().toString();
                            EncryptExplorer.this.dir = new File(EncryptExplorer.this.alamu);
                            File file = new File(EncryptExplorer.this.dir, nakuha);

                            if (mPass.getText().toString().length() > 7) {
                                keypass=mPass.getText().toString();
                                String passm = mPass.getText().toString();
                                 String passmcon = mconPass.getText().toString();

                                if (passm.equals(passmcon)) {
                                    //asyntask
                                    progress= new ProgressDialog(EncryptExplorer.this);

                                    progress.setMessage("Validating File");
                                    progress.setMax(100);
                                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progress.setCancelable(false);
                                    progress.setIcon(R.mipmap.filec);
                                    Drawable progressDrawable = getResources().getDrawable(R.drawable.cpdialog);
                                    progress.setProgressDrawable(progressDrawable);
                                    progress.setProgress(0);
                                    progress.show();
                                    ProcessData p = new ProcessData();
                                    p.execute(60);
                                    file_size =Integer.parseInt(String.valueOf(file.length()/1024));
                                    System.out.println( "file_size "+  file_size);



                                }  else if (passm.equals("")&& passmcon.equals("")) {
                                Toast.makeText(EncryptExplorer.this, "Password Empty", Toast.LENGTH_SHORT).show();
                            } else if (passm != passmcon) {
                                Toast.makeText(EncryptExplorer.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                            }






                            } else if (mPass.getText().toString().length() < 8) {
                                Toast.makeText(EncryptExplorer.this, "Minimum of 8 characters required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();



                }
            }
        });
    }
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




    public class ProcessData extends AsyncTask<Integer, String, String> {

        String password = keypass;



        final EditText browser = (EditText) findViewById(R.id.editText);
        String nakuha = browser.getText().toString();

        @Override
        protected String doInBackground(Integer... integers) {
            dir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
            File file = new File(dir, nakuha); // keni y mka initialized nung nanu ya directory

            zipdir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
            File filezip = new File(zipdir.getAbsolutePath()); // keni y mka initialized nung nanu ya directory
            System.out.println( dir.toString());
            System.out.println( file.toString());

            //dito kukuhanin yung file na cocompress
        String dataPath = filezip.getAbsolutePath();

            //dito mapupunta yung na compress



          String zipPath=Environment.getExternalStorageDirectory()+"/Concrypt/";




          //  String zipPath="/storage/sdcard0/Concrypt/";
            //checkbox para pati parent folder ma compress

            SharedPreferences setting = getSharedPreferences("PREFS", 0);
            choose = setting.getString("choose", "");
            String sngl ="SingleFile";


            if (choose.equals(sngl)){


                if (file.exists()) {

                    file_size =Integer.parseInt(String.valueOf(file.length()/1024));
                    System.out.println( "file_size "+  file_size);
                    System.out.println("exist");
                    System.out.println(file);
                    try {

                        algorithm = setting.getString("algorithm", "");
                        String blow = "Blowfish";

                        if (algorithm.equals(blow)){
                            EncryptExplorer.this.destinationFile = new File(file.getAbsolutePath().concat(".cbd"));



                        }
                        else{

                            EncryptExplorer.this.destinationFile = new File(file.getAbsolutePath().concat(".ced"));

                        }








                        if (EncryptExplorer.this.destinationFile.exists()) {
                            AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(EncryptExplorer.this);
                            showbuilder1.setCancelable(false);

                            showbuilder1.setTitle("Report");
                            showbuilder1.setMessage("Encrypt Error:\nReason: Theres a Encrypted file that has the same name, Delete it First. ");
                            showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();


                                }
                            });
                            showbuilder1.create().show();}
                        else{


                            BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));

                            FileOutputStream fileWriter=new FileOutputStream (destinationFile, true);

                            MessageDigest digest = MessageDigest.getInstance("SHA-256");
                            byte[] bytess =digest.digest(password.getBytes());





                            StringBuilder sb = new StringBuilder();
                            for(int i=0; i< bytess.length ;i++)
                            {
                                sb.append(Integer.toString((bytess[i] & 0xff) + 0x100, 16).substring(1));
                            }
                            String hashOfPassword = sb.toString();
                            System.out.println("hashOfPassword length= "+hashOfPassword.length());
                            System.out.println("hashOfPassword encrypt= " +hashOfPassword);
                            System.out.println("hashOfPassword get bytes encrypt= " +hashOfPassword.getBytes());
                            System.out.println("bytes encrypt= " +bytess);
                            System.out.println("passm= " +keypass);
                            byte[] keyHash;

                            keyHash=hashOfPassword.getBytes();


                            fileWriter.write(keyHash);



                            String aes ="AES";
                            final String aess = aes.toString();


                            algorithm = setting.getString("algorithm", "");

                            if (algorithm==""){
                                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor1 = settings1.edit();
                                editor1.putString("algorithm", aess);
                                editor1.apply();
                            }
                            else{

                                SecretKeySpec secretKeySpec = new SecretKeySpec(digest.digest(), algorithm);
                                Cipher enc = Cipher.getInstance(algorithm);
                                enc.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                                CipherOutputStream cos = new CipherOutputStream(fileWriter, enc);
                                byte[] buf = new byte[262144];
                                while (true) {
                                    int read = fileReader.read(buf);
                                    if (read != -1) {
                                        cos.write(buf, 0, read);

                                    } else {




                                        fileReader.close();


                                        fileWriter.flush();
                                        cos.close();
                                        fileWriter.close();


                                        file.delete();
                                        File oldFile = new File(destinationFile.getParent()+"/"+destinationFile.getName());

                                        File newFile = new File(Environment.getExternalStorageDirectory()+"/Concrypt/.Encrypted Files/"+destinationFile.getName());

                                        oldFile.renameTo(newFile);








                                        if(file_size<=1000){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(10); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }



                                        }else if((file_size>=1001)&&(file_size<=10000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(100); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }

                                        }
                                        else if((file_size>=10001)&&(file_size<=20000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(200); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }
                                        }
                                        else if((file_size>=20001)&&(file_size<=40000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(450); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }

                                        }  else if((file_size>=40001)&&(file_size<=80000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(500); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }
                                        }
                                        else if((file_size>=80001)&&(file_size<=100000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(750); //1.30 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }
                                        }
                                        else if((file_size>=100001)&&(file_size<=200000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(1000); //2 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }

                                        }
                                        else if((file_size>=200001)&&(file_size<=400000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(1500); //4 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }
                                        }
                                        else if((file_size>=400001)&&(file_size<=800000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(2500); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }
                                        }
                                        else if((file_size>=800001)&&(file_size<=900000)){
                                            //by 1
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(3000); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }
                                        }
                                        else{
                                            int progress1 = 0;
                                            int total = integers[0];
                                            while(progress1 <= total) {
                                                try {


                                                    Thread.sleep(5000); //1 second


                                                } catch (InterruptedException e) {

                                                }

                                                String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                progress1++;
                                            }


                                        }




                                    }
                                }
                            }







                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (NoSuchAlgorithmException e2) {
                        e2.printStackTrace();

                    } catch (NoSuchPaddingException e3) {
                        e3.printStackTrace();

                    } catch (InvalidKeyException e4) {
                        e4.printStackTrace();

                    } catch (IOException e5) {
                        e5.printStackTrace();

                    }


                }

            }
            else{

                if (filezip.isDirectory()){ //ing gawan mu compressed me

                    if (FileHelper.zip(dataPath,zipPath,filezip.getName().concat(".zip"),true)) {
                        File filefolder = new File(Environment.getExternalStorageDirectory()+"/Concrypt/"+filezip.getName().concat(".zip"));

filezip.delete();
                        file_sizezip =Integer.parseInt(String.valueOf(filefolder.length()/1024));
                        System.out.println( "file_sizefolder "+  file_sizezip);

                        if (filefolder.exists()) {
                            System.out.println("exist");
                            System.out.println(filefolder);
                            try {

                                algorithm = setting.getString("algorithm", "");
                                String blow = "Blowfish";

                                if (algorithm.equals(blow)){
                                    EncryptExplorer.this.destinationFile = new File(filefolder.getAbsolutePath().concat(".cbd"));



                                }
                                else{

                                    EncryptExplorer.this.destinationFile = new File(filefolder.getAbsolutePath().concat(".ced"));

                                }








                                if (EncryptExplorer.this.destinationFile.exists()) {


                                    destinationFile.delete();
                                 }
                                else{


                                    BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(filefolder.getAbsolutePath()));

                                    FileOutputStream fileWriter=new FileOutputStream (destinationFile, true);

                                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                    byte[] bytess =digest.digest(password.getBytes());





                                    StringBuilder sb = new StringBuilder();
                                    for(int i=0; i< bytess.length ;i++)
                                    {
                                        sb.append(Integer.toString((bytess[i] & 0xff) + 0x100, 16).substring(1));
                                    }
                                    String hashOfPassword = sb.toString();
                                    System.out.println("hashOfPassword length= "+hashOfPassword.length());
                                    System.out.println("hashOfPassword encrypt= " +hashOfPassword);
                                    System.out.println("hashOfPassword get bytes encrypt= " +hashOfPassword.getBytes());
                                    System.out.println("bytes encrypt= " +bytess);
                                    System.out.println("passm= " +keypass);
                                    byte[] keyHash;

                                    keyHash=hashOfPassword.getBytes();


                                    fileWriter.write(keyHash);



                                    String aes ="AES";
                                    final String aess = aes.toString();


                                    algorithm = setting.getString("algorithm", "");

                                    if (algorithm==""){
                                        SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                        SharedPreferences.Editor editor1 = settings1.edit();
                                        editor1.putString("algorithm", aess);
                                        editor1.apply();
                                    }
                                    else{

                                        SecretKeySpec secretKeySpec = new SecretKeySpec(digest.digest(), algorithm);
                                        Cipher enc = Cipher.getInstance(algorithm);
                                        enc.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                                        CipherOutputStream cos = new CipherOutputStream(fileWriter, enc);
                                        byte[] buf = new byte[262144];
                                        while (true) {
                                            int read = fileReader.read(buf);
                                            if (read != -1) {
                                                cos.write(buf, 0, read);

                                            } else {




                                                fileReader.close();


                                                fileWriter.flush();
                                                cos.close();
                                                fileWriter.close();


                                                filefolder.delete();
                                                File oldFile = new File(destinationFile.getParent()+"/"+destinationFile.getName());

                                                File newFile = new File(Environment.getExternalStorageDirectory()+"/Concrypt/.Encrypted Files/"+destinationFile.getName());

                                                oldFile.renameTo(newFile);








                                                if(file_sizezip<=1000){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(10); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }



                                                }else if(( file_sizezip>=1001)&&( file_sizezip<=10000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(100); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }

                                                }
                                                else if(( file_sizezip>=10001)&&( file_sizezip<=20000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(200); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ?"Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }
                                                }
                                                else if(( file_sizezip>=20001)&&( file_sizezip<=40000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(450); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }

                                                }  else if(( file_sizezip>=40001)&&( file_sizezip<=80000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(500); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }
                                                }
                                                else if(( file_sizezip>=80001)&&( file_sizezip<=100000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(750); //1.30 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }
                                                }
                                                else if(( file_sizezip>=100001)&&( file_sizezip<=200000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(1000); //2 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }

                                                }
                                                else if(( file_sizezip>=200001)&&( file_sizezip<=400000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(1500); //4 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }
                                                }
                                                else if(( file_sizezip>=400001)&&( file_sizezip<=800000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(2500); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }
                                                }
                                                else if(( file_sizezip>=800001)&&( file_sizezip<=900000)){
                                                    //by 1
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(3000); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }
                                                }
                                                else{
                                                    int progress1 = 0;
                                                    int total = integers[0];
                                                    while(progress1 <= total) {
                                                        try {


                                                            Thread.sleep(5000); //1 second


                                                        } catch (InterruptedException e) {

                                                        }

                                                        String z = progress1 % 2 == 0 ? "Encrypting." : "Encrypting" ;

                                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                                        progress1++;
                                                    }


                                                }






                                            }
                                        }
                                    }







                                }


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                            } catch (NoSuchAlgorithmException e2) {
                                e2.printStackTrace();

                            } catch (NoSuchPaddingException e3) {
                                e3.printStackTrace();

                            } catch (InvalidKeyException e4) {
                                e4.printStackTrace();

                            } catch (IOException e5) {
                                e5.printStackTrace();

                            }


                        }

                    }

                    System.out.println("directory ");
                    System.out.println(filezip.getAbsolutePath().toString());



                }else{
                    System.out.println("not a directory ");
                    System.out.println(filezip.getAbsolutePath().toString());







                }

            }

            return "DONE";
        }
        @Override
        protected void onProgressUpdate(String... values){

            super.onProgressUpdate(values);

            Float progress1 = Float.valueOf(values[0]);
            Float total = Float.valueOf(values[1]);

            String message = values[2];

            progress.setProgress((int) ((progress1/total)*100));
            progress.setMessage(message);



            if(values[0].equals(values[1])){
                //dito natapos



                progress.cancel();
                AlertDialog.Builder showbuilder =new AlertDialog.Builder(EncryptExplorer.this);
                showbuilder.setCancelable(false);

                showbuilder.setTitle("Report");
                showbuilder.setMessage("Your file has been encrypted");
                showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        browser.getText().clear();

                    }
                });
                showbuilder.create().show();









                NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(EncryptExplorer.this)
                        .setSmallIcon(R.mipmap.ic_encrypticon)
                        .setContentTitle("Concrypt")
                        .setContentText("Done Ecrypting");

                builder1.setLights(Color.BLUE, 500, 500);
                long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                builder1.setVibrate(pattern);
                Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder1.setSound(soundUri);

                NotificationManager notificationManager1 =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager1.notify(1, builder1.build());

            }
        }

    }





    public void getfile(View view) {



        AlertDialog.Builder mEBuilder = new AlertDialog.Builder(EncryptExplorer.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_selected, null);
        mEBuilder.setView(mView);
        final AlertDialog dialog = mEBuilder.create();


        Button maes = (Button) mView.findViewById(R.id.file);
        Button mblowfish = (Button) mView.findViewById(R.id.folder);


        String sngl ="SingleFile";
        String fldr ="Folder";

        final String sfile = sngl.toString();
        final String folder = fldr.toString();

        maes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //file
                dialog.dismiss();

                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor1 = settings1.edit();
                editor1.putString("choose", sfile);
                editor1.apply();
                startActivityForResult(new Intent(EncryptExplorer.this, EncryptChoose.class), REQUEST_PATH);



            }
        });

        mblowfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //folder
                dialog.dismiss();
                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor1 = settings1.edit();
                editor1.putString("choose", folder);
                editor1.apply();
                startActivityForResult(new Intent(EncryptExplorer.this, FolderChoose.class), REQUEST_PATH);


            }
        });

        dialog.show();
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PATH && resultCode == -1) {
            this.curPathName = data.getStringExtra("GetPath");
            this.pathname.setText("\"" + this.curPathName + "\"");
            EditText path = (EditText) findViewById(R.id.pathname);
            this.alamu = this.curPathName;
            this.curFileName = data.getStringExtra("GetFileName");
            this.edittext.setText(this.curFileName);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
