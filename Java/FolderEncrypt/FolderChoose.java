package com.example.rvn_gbrl.navigationsample.FolderEncrypt;

/**
 * Created by RVN-GBRL on 3/10/2018.
 */

import android.app.Activity;
import android.app.ListActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rvn_gbrl.navigationsample.Enable;
import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptArrayAdapter;
import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptChoose;
import com.example.rvn_gbrl.navigationsample.FilepickerEncrypt.EncryptItem;
import com.example.rvn_gbrl.navigationsample.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class FolderChoose extends Activity {



    String screenshot;

    private File currentDir;
    private EncryptArrayAdapter adapter;
    String filename;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    setContentView(R.layout.folder_list_layout); ///eto yung may listview na layout





        //create a new folder when app installed
        File newfile = new File(Environment.getExternalStorageDirectory() + File.separator, "Concrypt");
        newfile.mkdir(); //folder
        File newfileenc = new File(Environment.getExternalStorageDirectory()+"/Concrypt/", ".Encrypted Files");
        newfileenc.mkdir(); //sub folder
        File newfiledec = new File(Environment.getExternalStorageDirectory()+"/Concrypt/", "Decrypted Files");
        newfiledec.mkdir(); //sub foldersub folder


        SharedPreferences setting = getSharedPreferences("PREFS", 0);

        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }
        currentDir = new File(Environment.getExternalStorageDirectory()+"/");



        fill(currentDir);
    }




    private void fill(File f)
    {

        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());


        List<EncryptItem>dir = new ArrayList();
        List<EncryptItem>fls = new ArrayList();
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else {
                        num_item = num_item + " items";
                    }

                    //String formated = lastModDate.toString();
                    dir.add(new EncryptItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"filefolder"));
                }




            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard")) dir.add(0,new EncryptItem("..","Parent Directory","",f.getParent(),"filearrow"));
        adapter = new EncryptArrayAdapter(FolderChoose.this, R.layout.filelist,dir);  ///at eto naman yung adaptena gusto kong ipasok sa layout na may list view

        //setListAdapter(this.adapter);
        listView =(ListView) findViewById(R.id.listview);
      listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final EncryptItem o = adapter.getItem(position);
                filename=o.getName();

                if(o.getImage().equalsIgnoreCase("filefolder")||o.getImage().equalsIgnoreCase("filearrow")){
                    currentDir = new File(o.getPath());
                    fill(currentDir);


                }




            }
        });

        Button call =(Button) findViewById(R.id.button1);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent();
                    intent.putExtra("GetPath",currentDir.toString());
                    intent.putExtra("GetFileName",filename.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                    }

                catch(Exception ex){

                    Toast.makeText(FolderChoose.this,
                            "No Permission to Select This",
                            Toast.LENGTH_SHORT).show();
                }






            }



        });


    }






    private void onFileClick(EncryptItem o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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






}
