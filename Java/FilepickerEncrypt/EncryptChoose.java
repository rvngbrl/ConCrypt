package com.example.rvn_gbrl.navigationsample.FilepickerEncrypt;


import android.app.ListActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.ListView;

import com.example.rvn_gbrl.navigationsample.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EncryptChoose extends ListActivity {
    String screenshot;

    private File currentDir;
    private EncryptArrayAdapter adapter;
    String filename;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.folder_list_layout); ///eto yung may listview na layout


        //create a new folder when app installed
        File newfile = new File(Environment.getExternalStorageDirectory() + File.separator, "Concrypt");
        newfile.mkdir(); //folder
        File newfileenc = new File(Environment.getExternalStorageDirectory()+"/Concrypt/", ".Encrypted Files");
        newfileenc.mkdir(); //sub folder
        File newfiledec = new File(Environment.getExternalStorageDirectory()+"/Concrypt/", "Decrypted Files");
        newfiledec.mkdir(); //sub folder


        SharedPreferences setting = getSharedPreferences("PREFS", 0);

        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }
        currentDir = new File(Environment.getExternalStorageDirectory()+"/");


        fill(currentDir);
    }


    private void fill(File f) {

        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());


        List<EncryptItem> dir = new ArrayList<EncryptItem>();
        List<EncryptItem> fls = new ArrayList<EncryptItem>();
        try {
            for (File ff : dirs) {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if (ff.isDirectory()) {


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if (fbuf != null) {
                        buf = fbuf.length;
                    } else buf = 0;
                    String num_item = String.valueOf(buf);
                    if (buf == 0) num_item = num_item + " item";
                    else {
                        num_item = num_item + " items";
                    }

                    //String formated = lastModDate.toString();
                    dir.add(new EncryptItem(ff.getName(), num_item, date_modify, ff.getAbsolutePath(), "filefolder"));
                } else if (ff.getName().endsWith("ced")) {
                    ff.isHidden();
                } else if (ff.getName().endsWith("mp3") || ff.getName().endsWith("wav") || ff.getName().endsWith("rec") || ff.getName().endsWith("m4a")|| ff.getName().endsWith("aac")|| ff.getName().endsWith("flac")|| ff.getName().endsWith("ogg")|| ff.getName().endsWith("wma")) {
                    fls.add(new EncryptItem(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "filesound1"));
                } else if  (ff.getName().endsWith("mp4") || ff.getName().endsWith("mkv") || ff.getName().endsWith("avi") || ff.getName().endsWith("flv")|| ff.getName().endsWith("mov") || ff.getName().endsWith("m4v")|| ff.getName().endsWith("wmv")) {
                    fls.add(new EncryptItem(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "filevideo1"));
                } else if (ff.getName().endsWith("jpg") || ff.getName().endsWith("jpeg") || ff.getName().endsWith("gif") || ff.getName().endsWith("png")) {
                    fls.add(new EncryptItem(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "fileimage1"));
                } else if (ff.getName().endsWith("zip") || ff.getName().endsWith("rar") || ff.getName().endsWith("7z")) {
                    fls.add(new EncryptItem(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "filezip"));
                } else if (ff.getName().endsWith("apk")) {
                    fls.add(new EncryptItem(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "fileapk1"));
                } else {

                    fls.add(new EncryptItem(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "filefiles1"));
                }
                filename = ff.getName();

            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0, new EncryptItem("..", "Parent Directory", "", f.getParent(), "filearrow"));
        adapter = new EncryptArrayAdapter(EncryptChoose.this, R.layout.file_view, dir);  ///at eto naman yung adaptena gusto kong ipasok sa layout na may list view

        this.setListAdapter(adapter);


    }


    @Override
    public void onListItemClick(final ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        final EncryptItem o = adapter.getItem(position);


        if (o.getImage().equalsIgnoreCase("filefolder") || o.getImage().equalsIgnoreCase("filearrow")) {
            currentDir = new File(o.getPath());
            fill(currentDir);


        } else {
            onFileClick(o);

        }


/*
        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

String selected = (String)l.getItemAtPosition(position);
                System.out.println(  selected);
                if(!filename.contains(".")){



                    AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(EncryptChoose.this);
                    showbuilder1.setCancelable(false);

                    showbuilder1.setTitle("Encryption");
                    showbuilder1.setMessage("Encrypt This Whole Folder?");
                    showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            onFileClick(o);




                        }
                    });

                    showbuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {




                        }
                    });
                    showbuilder1.create().show();
                }

                else{

                    Toast.makeText(EncryptChoose.this,
                            filename.toString(),
                            Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(EncryptChoose.this);
                    showbuilder1.setCancelable(false);

                    showbuilder1.setTitle("Encryption");
                    showbuilder1.setMessage("Encrypt This File?");
                    showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            onFileClick(o);




                        }
                    });

                    showbuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {




                        }
                    });
                    showbuilder1.create().show();

                }








                return true;
            }
        });*/


    }


    private void onFileClick(EncryptItem o) {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDir.toString());
        intent.putExtra("GetFileName", o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

}


