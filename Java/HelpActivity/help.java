package com.example.rvn_gbrl.navigationsample.HelpActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.rvn_gbrl.navigationsample.Converts;
import com.example.rvn_gbrl.navigationsample.Enable;
import com.example.rvn_gbrl.navigationsample.FirstInstall.firstinstall;
import com.example.rvn_gbrl.navigationsample.MyBounceInterpolator;
import com.example.rvn_gbrl.navigationsample.R;


public class help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

    }
    public void converthelpclick(View view){
        Intent convertbtn = new Intent(this, Converting.class);
        startActivity(convertbtn);

    }

    public void encrypthelpclick(View view){
        Intent convertbtn = new Intent(this, Encryptinghelp.class);
        startActivity(convertbtn);

    }

    public void palitawclick(View view){
        Intent convertbtn = new Intent(this, firstinstall.class);
        startActivity(convertbtn);

    }
    public void decryptclick(View view){
        Intent convertbtn = new Intent(this, Decryptinghelp.class);
        startActivity(convertbtn);

    }

    public void helpclick(View view){
        Intent convertbtn = new Intent(this, helpsettings.class);
        startActivity(convertbtn);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
