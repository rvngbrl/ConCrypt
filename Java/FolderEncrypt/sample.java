package com.example.rvn_gbrl.navigationsample.FolderEncrypt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.rvn_gbrl.navigationsample.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RVN-GBRL on 3/10/2018.
 */

public class sample extends AppCompatActivity {

  ListView litview;
   Adapter adapter;
    ArrayList arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       setContentView(R.layout.folder_list_layout); ///eto yung may listview na layout
        litview=(ListView)findViewById(R.id.listview);



    }


}
