package com.example.rvn_gbrl.navigationsample.FilepickerDecrypt;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class DecryptExplorer extends AppCompatActivity {
    private static final int REQUEST_PATH = 1;
    String alamu;
    String algorithm;
    String curFileName;
    String curPathName;
    String keypass;
    int file_size;
    private File destinationFile;
    File dir;
    private ProgressDialog progress;
    EditText edittext,pathname;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decryptexplorer);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        this.pathname = (EditText) findViewById(R.id.pathname);
        this.edittext = (EditText) findViewById(R.id.editText);
        edittext.setKeyListener(null);
        ((Button) findViewById(R.id.btnDecDialog)).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //button animation
                Button button = (Button)findViewById(R.id.btnDecDialog);
                final Animation myAnim = AnimationUtils.loadAnimation(DecryptExplorer.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                button.startAnimation(myAnim);
                // button animation
                final EditText browser = (EditText) DecryptExplorer.this.findViewById(R.id.editText);
                if (browser.getText().toString().isEmpty()) {
                    Toast.makeText(DecryptExplorer.this, R.string.error_msg1, Toast.LENGTH_SHORT).show();
                    return;
                }else if(pathname.getText().toString().equals("")){
                    Toast.makeText(DecryptExplorer.this,
                            "Invalid File",
                            Toast.LENGTH_SHORT).show();
                    browser.getText().clear();

                }else{


                    Builder mDBuilder = new Builder(DecryptExplorer.this);
                    View mView = DecryptExplorer.this.getLayoutInflater().inflate(R.layout.dialog_decrypt, null);
                    final EditText mPassd = (EditText) mView.findViewById(R.id.etpassd);
                   // final EditText mconPassd = (EditText) mView.findViewById(R.id.etconpassd);
                    Button mdecProceed = (Button) mView.findViewById(R.id.btndecproceed);
                    mDBuilder.setView(mView);
                    final AlertDialog dialog = mDBuilder.create();
                    mdecProceed.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (mPassd.getText().toString().isEmpty() ) {
                                Toast.makeText(DecryptExplorer.this, R.string.error_password_msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String nakuha = browser.getText().toString();
                            DecryptExplorer.this.dir = new File(DecryptExplorer.this.alamu);
                            File file = new File(DecryptExplorer.this.dir, nakuha);

                            if (mPassd.getText().toString().length() > 7) {

                                    keypass=mPassd.getText().toString();



                                    if (file.exists()) {
                                        try {
                                            destinationFile = new File(file.getAbsolutePath().toString().substring(0, file.getAbsolutePath().toString().length() - 4));


                                            BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
                                            final FileOutputStream fileWriter = new FileOutputStream(DecryptExplorer.this.destinationFile);



                                            MessageDigest digest = MessageDigest.getInstance("SHA-256");


                                            byte[] bytes =digest.digest(keypass.getBytes());


                                            StringBuilder sb = new StringBuilder();
                                            for(int i=0; i< bytes.length ;i++)
                                            {
                                                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                                            }
                                            String hashOfPassword = sb.toString();
                                            System.out.println("hashOfPassword length= "+hashOfPassword.length());
                                            System.out.println("hashOfPassword decrypt= " +hashOfPassword);
                                            System.out.println("hashOfPassword get bytes decrypt= " +hashOfPassword.getBytes());
                                            System.out.println("bytes decrypt= " +bytes);
                                            byte[] keyHash;

                                            keyHash=hashOfPassword.getBytes();



                                            //reading key hash from file
                                            StringBuffer keyHashFromFile=new StringBuffer(64);
                                            for(int i=0; i<64; i++)
                                            {
                                                keyHashFromFile.append((char)fileReader.read());
                                            }

                                            //verifying both hashes
                                            System.out.println("keyHashFromFile.to string()= "+keyHashFromFile);
                                            System.out.println("keyHash= "+keyHash);
                                            if(keyHashFromFile.toString().equals(hashOfPassword))

                                            {
                                                //asyntask
                                                progress= new ProgressDialog(DecryptExplorer.this);
                                                progress.setMax(100);
                                                progress.setMessage("Validating File");
                                                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                progress.setCancelable(false);
                                                progress.setIcon(R.mipmap.filec);
                                                Drawable progressDrawable = getResources().getDrawable(R.drawable.cpdialog);
                                                progress.setProgressDrawable(progressDrawable);
                                                progress.setProgress(0);
                                                progress.show();
                                                ProcessData p = new ProcessData();
                                                p.execute(60);

                                                System.out.println( "usable space "+  destinationFile.getUsableSpace());
                                                file_size =Integer.parseInt(String.valueOf(file.length()/1024));
                                                System.out.println( "file_size "+  file_size);









                                            }else{

                                                AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(DecryptExplorer.this);
                                                showbuilder1.setCancelable(false);

                                                showbuilder1.setTitle("Report");
                                                showbuilder1.setMessage("Incorrect Password\n Password verified using SHA-256(64-bit)hash \n Without the actual password you can't decrypt the file ");



                                                showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        browser.getText().clear();
                                                        destinationFile.delete();


                                                    }
                                                });
                                                showbuilder1.create().show();




                                            }






                                        } catch (FileNotFoundException e) {


                                            e.printStackTrace();

                                        } catch (NoSuchAlgorithmException e2) {
                                            Toast.makeText(DecryptExplorer.this, "Wrong Password1", Toast.LENGTH_SHORT).show();
                                            e2.printStackTrace();

                                        }

                                         catch (IOException e5) {
                                            e5.printStackTrace();

                                        }
                                    }





                            } else if (mPassd.getText().toString().length() < 8) {
                                Toast.makeText(DecryptExplorer.this, "Minimum of 8 characters required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }


    public class ProcessData extends AsyncTask<Integer, String,String> {


        String password = keypass;



        final EditText browser = (EditText) findViewById(R.id.editText);
        String nakuha = browser.getText().toString();
        @Override
        protected String doInBackground(Integer... integers) {
            dir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
            File file = new File(dir, nakuha); // keni y mka initialized nung nanu ya directory




            if (file.exists()) {
                try {
                    destinationFile = new File(file.getAbsolutePath().toString().substring(0, file.getAbsolutePath().toString().length() - 4));


                    BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
                    final FileOutputStream fileWriter = new FileOutputStream(DecryptExplorer.this.destinationFile);



                    MessageDigest digest = MessageDigest.getInstance("SHA-256");


                    byte[] bytes =digest.digest(password.getBytes());


                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i< bytes.length ;i++)
                    {
                        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    String hashOfPassword = sb.toString();
                    System.out.println("hashOfPassword length= "+hashOfPassword.length());
                    System.out.println("hashOfPassword decrypt= " +hashOfPassword);
                    System.out.println("hashOfPassword get bytes decrypt= " +hashOfPassword.getBytes());
                    System.out.println("bytes decrypt= " +bytes);
                    byte[] keyHash;

                    keyHash=hashOfPassword.getBytes();



                    //reading key hash from file
                    StringBuffer keyHashFromFile=new StringBuffer(64);
                    for(int i=0; i<64; i++)
                    {
                        keyHashFromFile.append((char)fileReader.read());
                    }

                    //verifying both hashes
                    System.out.println("keyHashFromFile.to string()= "+keyHashFromFile);
                    System.out.println("keyHash= "+keyHash);
                    if(keyHashFromFile.toString().equals(hashOfPassword))

                    {



                        System.out.println("i read");


                        SharedPreferences setting = getSharedPreferences("PREFS", 0);
                        algorithm = setting.getString("algorithm", "");



                        SecretKeySpec secretKeySpec = new SecretKeySpec(digest.digest(), algorithm);
                        Cipher enc = Cipher.getInstance(algorithm);
                        enc.init(Cipher.DECRYPT_MODE, secretKeySpec);
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




                                file.delete();


                                File oldFile = new File(destinationFile.getParent()+"/"+destinationFile.getName());

                                File newFile = new File(Environment.getExternalStorageDirectory()+"/Concrypt/Decrypted Files/"+destinationFile.getName());


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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

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

                                        String z = progress1 % 2 == 0 ? "Decrypting." : "Decrypting" ;

                                        this.publishProgress(String.valueOf(progress1), String.valueOf(total), z);
                                        progress1++;
                                    }


                                }










                            }







                        }


                    }

                } catch (FileNotFoundException e) {


                    e.printStackTrace();

                } catch (NoSuchAlgorithmException e2) {
                    Toast.makeText(DecryptExplorer.this, "Wrong Password1", Toast.LENGTH_SHORT).show();
                    e2.printStackTrace();

                } catch (NoSuchPaddingException e3) {
                    Toast.makeText(DecryptExplorer.this, "Wrong Password2", Toast.LENGTH_SHORT).show();
                    e3.printStackTrace();

                } catch (InvalidKeyException e4) {


                    e4.printStackTrace();

                } catch (IOException e5) {
                    e5.printStackTrace();

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
                AlertDialog.Builder showbuilder =new AlertDialog.Builder(DecryptExplorer.this);
                showbuilder.setCancelable(false);

                showbuilder.setTitle("Report");
                showbuilder.setMessage("Your file has been decrypted");
                showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        browser.getText().clear();


                    }
                });
                showbuilder.create().show();








                NotificationCompat.Builder builder2 = (NotificationCompat.Builder) new NotificationCompat.Builder(DecryptExplorer.this)
                        .setSmallIcon(R.mipmap.ic_decrypticon)
                        .setContentTitle("Concrypt")
                        .setContentText("Done Decrypting");

                builder2.setLights(Color.BLUE, 500, 500);
                long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                builder2.setVibrate(pattern);
                Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder2.setSound(soundUri);

                NotificationManager notificationManager2 =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager2.notify(2, builder2.build());
            }
        }

    }

    public void getfile(View view) {
        startActivityForResult(new Intent(this, DecryptChoose.class), REQUEST_PATH);
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
