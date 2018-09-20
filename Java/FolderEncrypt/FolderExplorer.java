package com.example.rvn_gbrl.navigationsample.FolderEncrypt;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptChoose;
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

;
/**
 * Created by RVN-GBRL on 3/10/2018.
 */

public class FolderExplorer extends AppCompatActivity {

    private static final int REQUEST_PATH = 1;
    String alamu;
    String algorithm;

    String curFileName;
    String curPathName;
    int file_size;
    private File destinationFile;
    File dir;
    String keypass;
    EditText edittext;
    EditText pathname;
    private ProgressDialog progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encryptexplorer);


        this.edittext = (EditText) findViewById(R.id.editText);
        this.pathname = (EditText) findViewById(R.id.pathname);


        ((Button) findViewById(R.id.btnEncDialog)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //animation button
                Button button = (Button)findViewById(R.id.btnEncDialog);
                final Animation myAnim = AnimationUtils.loadAnimation(FolderExplorer.this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                button.startAnimation(myAnim);
                //animation button

                final EditText browser = (EditText) FolderExplorer.this.findViewById(R.id.editText);
                if (browser.getText().toString().isEmpty()) {
                    Toast.makeText(FolderExplorer.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    return;
                }  else if(pathname.getText().toString().equals("")){
                    Toast.makeText(FolderExplorer.this,
                            "Invalid File",
                            Toast.LENGTH_SHORT).show();
                    browser.getText().clear();

                }else{

                    Builder mEBuilder = new Builder(FolderExplorer.this);
                    View mView = FolderExplorer.this.getLayoutInflater().inflate(R.layout.dialog_encrypt, null);
                    final EditText mPass = (EditText) mView.findViewById(R.id.etpass);
                    final EditText mconPass = (EditText) mView.findViewById(R.id.etconpass);
                    Button mProceed = (Button) mView.findViewById(R.id.btnproceed);
                    mEBuilder.setView(mView);
                    final AlertDialog dialog = mEBuilder.create();
                    mProceed.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (mPass.getText().toString().isEmpty() || mconPass.getText().toString().isEmpty()) {
                                Toast.makeText(FolderExplorer.this, R.string.error_password_msg,Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String nakuha = browser.getText().toString();
                            FolderExplorer.this.dir = new File(FolderExplorer.this.alamu);
                            File file = new File(FolderExplorer.this.dir, nakuha);

                            if (mPass.getText().toString().length() > 7) {
                                keypass=mPass.getText().toString();
                                String passm = mPass.getText().toString();
                                String passmcon = mconPass.getText().toString();

                                if (passm.equals(passmcon)) {
                                    //asyntask
                                    progress= new ProgressDialog(FolderExplorer.this);
                                    progress.setMax(100);
                                    progress.setMessage("Validating File");
                                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progress.setCancelable(false);
                                    progress.setIcon(R.mipmap.filec);
                                    Drawable progressDrawable = getResources().getDrawable(R.drawable.cpdialog);
                                    progress.setProgressDrawable(progressDrawable);
                                    progress.setProgress(0);
                                    progress.show();
                                    FolderExplorer.ProcessData p = new FolderExplorer.ProcessData();
                                    p.execute(60);




                                }  else if (passm.equals("")&& passmcon.equals("")) {
                                    Toast.makeText(FolderExplorer.this, "Password Empty", Toast.LENGTH_SHORT).show();
                                } else if (passm != passmcon) {
                                    Toast.makeText(FolderExplorer.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                                }






                            } else if (mPass.getText().toString().length() < 8) {
                                Toast.makeText(FolderExplorer.this, "8 minimum characters required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();



                }
            }
        });
    }





    public class ProcessData extends AsyncTask<Integer, String, String> {

        String password = keypass;



        final EditText browser = (EditText) findViewById(R.id.editText);
        String nakuha = browser.getText().toString();
        @Override
        protected String doInBackground(Integer... integers) {
            dir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
            File file = new File(dir, nakuha); // keni y mka initialized nung nanu ya directory




            if (file.isDirectory()){
                System.out.println("directory ");
                System.out.println("yakuni: "+file.getAbsolutePath().toString());
            }else{
                System.out.println("not a directory ");

            }





            if (file.exists()) {
                try {
                    SharedPreferences setting = getSharedPreferences("PREFS", 0);
                    algorithm = setting.getString("algorithm", "");
                    String blow = "Blowfish";

                    if (algorithm.equals(blow)){
                        FolderExplorer.this.destinationFile = new File(file.getAbsolutePath().concat(".cbd"));



                    }
                    else{

                        FolderExplorer.this.destinationFile = new File(file.getAbsolutePath().concat(".ced"));

                    }


                   file_size =Integer.parseInt(String.valueOf(destinationFile.length()/1024));
                    System.out.println( "file_size "+  file_size);





                    if (FolderExplorer.this.destinationFile.exists()) {
                        AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(FolderExplorer.this);
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

                                    File newFile = new File("/storage/sdcard0/Concrypt/.Encrypted Files/"+destinationFile.getName());

                                    oldFile.renameTo(newFile);





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
                AlertDialog.Builder showbuilder =new AlertDialog.Builder(FolderExplorer.this);
                showbuilder.setCancelable(false);

                showbuilder.setTitle("Report");
                showbuilder.setMessage("Your File Has been Encrypted");
                showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        browser.getText().clear();

                    }
                });
                showbuilder.create().show();









                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(FolderExplorer.this)
                        .setSmallIcon(R.mipmap.ic_encrypticon)
                        .setContentTitle("ConCrypt")
                        .setContentText("Done Ecrypting");

                NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, mBuilder.build());
            }
        }

    }





    public void getfile(View view) {



        AlertDialog.Builder mEBuilder = new AlertDialog.Builder(FolderExplorer.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_selected, null);
        mEBuilder.setView(mView);
        final AlertDialog dialog = mEBuilder.create();


        Button maes = (Button) mView.findViewById(R.id.file);
        Button mblowfish = (Button) mView.findViewById(R.id.folder);

        maes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //file
                dialog.dismiss();
                startActivityForResult(new Intent(FolderExplorer.this, EncryptChoose.class), REQUEST_PATH);



            }
        });

        mblowfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //folder
                dialog.dismiss();

                startActivityForResult(new Intent(FolderExplorer.this, EncryptChoose.class), REQUEST_PATH);


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
}
