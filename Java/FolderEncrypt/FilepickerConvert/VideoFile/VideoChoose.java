package com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoFile;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.rvn_gbrl.navigationsample.FilepickerDecrypt.DecryptArrayAdapter;
import com.example.rvn_gbrl.navigationsample.FilepickerDecrypt.DecryptItem;
import com.example.rvn_gbrl.navigationsample.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoChoose extends ListActivity {

String screenshot,algorithm;
    private File currentDir;
    private DecryptArrayAdapter adapter;
    @Override


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
        List<DecryptItem>dir = new ArrayList<DecryptItem>();
        List<DecryptItem>fls = new ArrayList<DecryptItem>();
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
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new DecryptItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"filefolder"));
                }






                    if  (ff.getName().endsWith("mp4") || ff.getName().endsWith("mkv") || ff.getName().endsWith("avi") || ff.getName().endsWith("flv")|| ff.getName().endsWith("mov") || ff.getName().endsWith("m4v")|| ff.getName().endsWith("wmv"))
                    {

                        fls.add(new DecryptItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"filevideo1"));
                    }





            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new DecryptItem("..","Parent Directory","",f.getParent(),"filearrow"));
        adapter = new DecryptArrayAdapter(VideoChoose.this, R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        DecryptItem o = adapter.getItem(position);

        if(o.getImage().equalsIgnoreCase("filefolder")||o.getImage().equalsIgnoreCase("filearrow")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(o);
        }
    }
    private void onFileClick(DecryptItem o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
