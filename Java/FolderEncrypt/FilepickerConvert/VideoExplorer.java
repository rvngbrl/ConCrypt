
package com.example.rvn_gbrl.navigationsample.FilepickerConvert;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoAudioConverter.AudioFormat;
import com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoAudioConverter.IConvertCallback;
import com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoAudioConverter.ILoadCallback;
import com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoFile.VideoChoose;
import com.example.rvn_gbrl.navigationsample.FilepickerDecrypt.DecryptExplorer;
import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptChoose;
import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptExplorer;
import com.example.rvn_gbrl.navigationsample.MyBounceInterpolator;
import com.example.rvn_gbrl.navigationsample.R;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
//   import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//   import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
//   import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//request permission ito para magalaw nya yung laman ng phone na lollipop pataas
public class VideoExplorer extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    String screenshot;
    private static final int REQUEST_WRITE_PERMISSION =  786;

    public void OnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode==REQUEST_WRITE_PERMISSION&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.e("TAG", "Permission is Granted");
        }
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }else{
            Log.e("TAG", "Permission is Granted");
        }
    }
    //spinner value
    Spinner spinner;
    EditText display_data;
    String choose[] = {"Choose Conversion", "MP3","MP4","MKV","AVI", "MOV", "M4V", "FLV", "WMV"};
    ArrayAdapter<String> adapter;
    //
    private ProgressDialog progress;
    private static final int REQUEST_PATH = 1;
    String curFileName;
    String curPathName;
    // File currentDir;

    File dir;
    private File destinationFile;
    String alamu;

    EditText edittext, pathname;

    //AndroidVideoConverter dito na yung process para makapag convert
    public static class AndroidAudioConverter {
        private static boolean loaded;
        private Context context;
        private File audioFile;
        private AudioFormat format;
        private IConvertCallback callback;

        private AndroidAudioConverter(Context context) {
            this.context = context;
        }

        public static boolean isLoaded() {
            return loaded;
        }

        //iloload nya yung Binary ng ffmpeg para malaman kung gumagana yung pangconvert
        public static void load(Context context, final ILoadCallback callback) {
            try {
                FFmpeg.getInstance((Context) context).loadBinary(new FFmpegLoadBinaryResponseHandler() {
                    public void onStart() {
                    }

                    public void onSuccess() {
                        AndroidAudioConverter.loaded = true;
                        callback.onSuccess();
                    }

                    public void onFailure() {
                        AndroidAudioConverter.loaded = false;
                        callback.onFailure(new Exception("Failed to loaded FFmpeg lib"));
                    }

                    public void onFinish() {
                    }
                });
            } catch (Exception var3) {
                loaded = false;
                callback.onFailure(var3);
            }

        }

        public static AndroidAudioConverter with(Context context) {
            return new AndroidAudioConverter((Context) context);
        }

        public AndroidAudioConverter setFile(File originalFile) {
            this.audioFile = originalFile;
            return this;
        }

        public AndroidAudioConverter setFormat(AudioFormat format) {
            this.format = format;
            return this;
        }

        public AndroidAudioConverter setCallback(IConvertCallback callback) {
            this.callback = callback;
            return this;
        }
        //itetest na nya yung ffmpeg kung nagloload
        public void convert() {
            if (!isLoaded()) {
                this.callback.onFailure(new Exception("Press the Button Again"));
            } else if (this.audioFile != null && this.audioFile.exists()) {
                if (!this.audioFile.canRead()) {
                    this.callback.onFailure(new IOException("Can\'t read the file. Missing permission?"));
                } else {
                    final File convertedFile = getConvertedFile(this.audioFile, this.format);
                    String[] cmd = new String[]{"-y", "-i", this.audioFile.getPath(), convertedFile.getPath()};

                    try {
                        FFmpeg.getInstance(this.context).execute(cmd, new FFmpegExecuteResponseHandler() {
                            public void onStart() {
                            }

                            public void onProgress(String message) {
                            }

                            public void onSuccess(String message) {
                                AndroidAudioConverter.this.callback.onSuccess(convertedFile);
                            }

                            public void onFailure(String message) {
                                AndroidAudioConverter.this.callback.onFailure(new IOException(message));
                            }

                            public void onFinish() {
                            }
                        });
                    } catch (Exception var4) {
                        this.callback.onFailure(var4);
                    }
                }
            } else {
                this.callback.onFailure(new IOException("File not exists"));
            }
        }
        private static File getConvertedFile(File originalFile, AudioFormat format) {
            String[] f = originalFile.getPath().split("\\.");
            String filePath = originalFile.getPath().replace(f[f.length - 1], format.getFormat());
            return new File(filePath);
        }
    }
    //AndroidVideoConverter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences setting = getSharedPreferences("PREFS", 0);

        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }


        setContentView(R.layout.videoexplorer);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        requestPermission();
        edittext = (EditText) findViewById(R.id.editText);
        pathname = (EditText) findViewById(R.id.pathname);
        edittext.setKeyListener(null);

        // spinner
        spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, choose);
        spinner.setAdapter(adapter);
    }
//

    //Button mshowDialog = (Button) findViewById(R.id.btnVidConDialog);
    // mshowDialog.setOnClickListener(new View.OnClickListener() {
    public void Con(View v) {
        //button animation
        Button button = (Button)findViewById(R.id.btnvideocon);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
        //button animation
        final EditText browser = (EditText) findViewById(R.id.editText);
        if (browser.getText().toString().isEmpty()) { //dito yung dapat may laman yung field ng pagpili ng file

            Toast.makeText(this,
                    "There's no video file to be converted",
                    Toast.LENGTH_SHORT).show();

        }
        else if(pathname.getText().toString().equals("")){
            Toast.makeText(VideoExplorer.this,
                    "Invalid File",
                    Toast.LENGTH_SHORT).show();
            browser.getText().clear();


        }
        else { //dipa tapos to dadagdagan pa na dapat valid yung file extension na ilalagay
            String nakuha = browser.getText().toString(); //icoconvert into string na gagawin nyang pangalan na kailangan ng directory nf dir
            dir = new File(alamu); //alamu meaning non dun niya kukunin yung pinaka directory ng file na pinili ng user
            final File file = new File(dir, nakuha); // keni y mka initialized nung nanu ya directory


            //eto na nabasa na nya yung file
            if  (file.exists()) {
                if (spinner.getSelectedItem().toString().equals("MP3")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(Exception var1) {

                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.MP3)
                            .setCallback(callback)
                            .convert();
                }

                else if (spinner.getSelectedItem().toString().equals("MP4")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    // Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                    //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //builder.setSound(soundUri);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.MP4)
                            .setCallback(callback)
                            .convert();
                }

                else if (spinner.getSelectedItem().toString().equals("MKV")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                            //great

                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    // Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                    //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //builder.setSound(soundUri);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.MKV)
                            .setCallback(callback)
                            .convert();
                }

                else if (spinner.getSelectedItem().toString().equals("AVI")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                            //great
                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }

                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.AVI)
                            .setCallback(callback)
                            .convert();
                }

                else if (spinner.getSelectedItem().toString().equals("MOV")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                            //great
                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.MOV)
                            .setCallback(callback)
                            .convert();
                }

                else if (spinner.getSelectedItem().toString().equals("M4V")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                            //great
                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.M4V)
                            .setCallback(callback)
                            .convert();
                }

                else if (spinner.getSelectedItem().toString().equals("FLV")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                            //great
                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.FLV)
                            .setCallback(callback)
                            .convert();
                }
                else if (spinner.getSelectedItem().toString().equals("WMV")) {
                    destinationFile = new File(file.getAbsolutePath().toString());
                    AndroidAudioConverter.load(this, new ILoadCallback() {
                        @Override
                        public void onSuccess() {
                            //great
                        }
                        @Override
                        public void onFailure(Exception var1) {
                            var1.printStackTrace();
                        }
                    });
                    IConvertCallback callback = new IConvertCallback() {
                        @Override
                        public void onSuccess(File convertedFile) {
                            AlertDialog.Builder showbuilder =new AlertDialog.Builder(VideoExplorer.this);
                            showbuilder.setCancelable(false);

                            showbuilder.setTitle("Report");
                            showbuilder.setMessage("Your File Has been Converted");
                            showbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    browser.getText().clear();

                                }
                            });
                            showbuilder.create().show();
                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("Concrypt")
                                    .setContentText("Done Converting")
                                    .setProgress(0, 0, false)
                                    .setColor(0x0f2b46)
                                    .setLights(0xff493C7C, 1000, 1000);

                            builder.setAutoCancel(true);
                            builder.setLights(Color.BLUE, 500, 500);
                            long[] pattern = {1000,1000,1000,1000,1000,1000,1000,1000,1000};
                            builder.setVibrate(pattern);
                            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);
                            //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(soundUri);
                            builder.setOngoing(false);

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, builder.build());
                        }
                        @Override
                        public void onFailure(Exception error) {
                            NotificationCompat.Builder mBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                                    .setSmallIcon(R.mipmap.ic_converticon)
                                    .setContentTitle("ConCrypt")
                                    .setContentText("Converting...");

                            NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            Toast.makeText(VideoExplorer.this, "Are you sure you want to convert? " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    };
                    Toast.makeText(VideoExplorer.this, "Converting file....", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(VideoExplorer.this)
                            .setSmallIcon(R.mipmap.ic_converticon)
                            .setContentTitle("Concrypt")
                            .setContentText("Converting...")
                            .setProgress(0, 0, true)
                            .setColor(0x0f2b46)
                            .setLights(0xff493C7C, 1000, 1000);

                    builder1.setAutoCancel(true);
                    builder1.setLights(Color.BLUE, 500, 500);
                    builder1.setOngoing(true);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, builder1.build());
                    AndroidAudioConverter.with(this)
                            .setFile(destinationFile)
                            .setFormat(AudioFormat.WMV)
                            .setCallback(callback)
                            .convert();
                }

            }}}



    //button browse para makuha yung file
    public void getfile(View view) {
        Intent browse = new Intent(this, VideoChoose.class);
        startActivityForResult(browse, REQUEST_PATH);

        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                //great
            }
            @Override
            public void onFailure(Exception var1) {
                var1.printStackTrace();
            }
        });
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {

            }
            @Override
            public void onFailure(Exception error) {

            }
        };
        AndroidAudioConverter.with(this)
                .setCallback(callback)
                .convert();
    }


    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // See which child activity is calling us back.
        if (requestCode == REQUEST_PATH) {
            if (resultCode == RESULT_OK) {
                curPathName = data.getStringExtra("GetPath");
                String pathfile = "\"" + curPathName + "\"";
                pathname.setText(pathfile);
                final EditText path = (EditText) findViewById(R.id.pathname);

                alamu = curPathName;
                curFileName = data.getStringExtra("GetFileName");
                edittext.setText(curFileName);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
