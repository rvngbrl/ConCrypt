package com.example.rvn_gbrl.navigationsample.SendingDelete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rvn_gbrl.navigationsample.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SendingChoose extends AppCompatActivity {

    String screenshot,algorithm;
    private File currentDir;
    ListView listView;
    String filename;
    private SendingArrayAdapter adapter;
    @Override


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.encryptlistview);
        SharedPreferences setting = getSharedPreferences("PREFS", 0);

        screenshot = setting.getString("screenshot", "");

        if (screenshot.equals("unallow")) {


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

        }





        currentDir = new File(Environment.getExternalStorageDirectory()+"/Concrypt/.Encrypted Files");


        fill(currentDir);
    }
    private void fill(File f)
    {


        SharedPreferences settings = getSharedPreferences("PREFS", 0);


        // TextView lck =(TextView)findViewById(R.id.lckpass);
        algorithm = settings.getString("algorithm", "");


        if (algorithm.equals("AES")) {

            this.setTitle("Encrypted files algorithm: AES");

        } else if(algorithm.equals("Blowfish")){


            this.setTitle("Encrypted files algorithm: Blowfish");
        }



        File[]dirs = f.listFiles();



        List<SendingItem>dir = new ArrayList<SendingItem>();
        List<SendingItem>fls = new ArrayList<SendingItem>();
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
                    dir.add(new SendingItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"filefolder"));
                }



                SharedPreferences setting = getSharedPreferences("PREFS", 0);
                algorithm = setting.getString("algorithm", "");
                String blow = "Blowfish";

                if (algorithm.equals(blow)){

                    if (ff.getName().endsWith("cbd"))
                    {

                        fls.add(new SendingItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"filecbd1"));
                    }


                }
                else{

                    if (ff.getName().endsWith("ced"))
                    {

                        fls.add(new SendingItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"fileced1"));
                    }

                }


            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);

        if(!f.getName().equalsIgnoreCase("sdcard"))

            adapter = new SendingArrayAdapter(SendingChoose.this, R.layout.file_view,dir);

        listView =(ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        if(adapter.isEmpty()){
            TextView textView= (TextView) findViewById(R.id.textviewalgo);
            textView.setText("No Encrypted Files");

        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SendingItem o = adapter.getItem(position);
                filename=o.getName();

                if(o.getName().isEmpty()){


                }

                else{

                    AlertDialog.Builder mEBuilder = new AlertDialog.Builder(SendingChoose.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialogsend, null);
                    mEBuilder.setView(mView);
                    final AlertDialog dialog = mEBuilder.create();

                    Button mshare = (Button) mView.findViewById(R.id.file);
                    Button mdelete = (Button) mView.findViewById(R.id.folder);
                    mshare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //AES

                            onFileClick(o);



                            System.out.println(currentDir.toString()+"/"+o.getName());

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                            //  intent.putExtra(Intent.EXTRA_STREAM, file);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files");
                            intent.setType("*/*");

                            ArrayList<Uri> files = new ArrayList<>();
                            File path = new File(currentDir.toString()+"/"+o.getName() );
                            Uri uri = Uri.fromFile(path);
                            files.add(uri);
                            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                            startActivity(intent);





                        }
                    });
                    mdelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //AES


                            AlertDialog.Builder showbuilder1 = new AlertDialog.Builder(SendingChoose.this);
                            showbuilder1.setCancelable(false);


                            showbuilder1.setTitle("Report");
                            showbuilder1.setMessage("Delete this File?");
                            showbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onFileClick(o);
                                    File path = new File(currentDir.toString()+"/"+o.getName() );
                                    path.delete();


                                    Toast.makeText(SendingChoose.this,
                                            "File Delete",
                                            Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();

                                }
                            });

                            showbuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    dialog.dismiss();

                                }
                            });
                            showbuilder1.create().show();




                        }
                    });

                    dialog.show();
                }




            }
        });


    }

    private void onFileClick(SendingItem o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString());
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
