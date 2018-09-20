package com.example.rvn_gbrl.navigationsample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.rvn_gbrl.navigationsample.FilepickerConvert.AudioExplorer;
import com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoExplorer;
import com.example.rvn_gbrl.navigationsample.HelpActivity.help;
import com.example.rvn_gbrl.navigationsample.SendingDelete.SendingChoose;

public class Converts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_PATH = 1;
    String password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_converts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //load email for checking
        SharedPreferences uemail = getSharedPreferences("PEEFS", 0);
        email = uemail.getString("email", "");
        //load password for checking
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences setting = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");
        email = settings.getString("email", "");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView formail = (TextView) header.findViewById(R.id.foremail);
        formail.setText(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.converts, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lock) {

            Intent intent = new Intent(getApplicationContext(), Enable.class);
            startActivity(intent);


        } else if (id == R.id.nav_encrypt) {
            Intent browse = new Intent(this, SendingChoose.class);
            startActivityForResult(browse, REQUEST_PATH);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), About.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_AES) {
            Intent intent = new Intent(getApplicationContext(), Algorithm.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_help) {
            Intent intent = new Intent(getApplicationContext(), help.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void textcon(View view) {

        Button button = (Button)findViewById(R.id.btntcon);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);

        Intent textconIntent = new Intent(this, TextConverter.class);
        startActivity(textconIntent);

    }

    public void vidcon(View view) {

        Button button = (Button)findViewById(R.id.btnvcon);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);

      Intent vidconIntent = new Intent(this, VideoExplorer.class);
        startActivity(vidconIntent);

    }

    public void audcon(View view) {

        Button button = (Button)findViewById(R.id.btnaudcon);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);

        Intent audconIntent = new Intent(this, AudioExplorer.class);
        startActivity(audconIntent);

    }
}