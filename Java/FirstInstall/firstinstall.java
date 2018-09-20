package com.example.rvn_gbrl.navigationsample.FirstInstall;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rvn_gbrl.navigationsample.R;


public class firstinstall extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentPage;
    CheckBox checkBoxs;
String showme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_firstinstall);









        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layoutss);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        mSlideViewPager=(ViewPager)findViewById(R.id.slideViewPager);
mDotLayout=(LinearLayout)findViewById(R.id.dotsLayout);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        mNextBtn=(Button)findViewById(R.id.nextBtn);
        mBackBtn=(Button)findViewById(R.id.prevBtn);
mSlideViewPager.addOnPageChangeListener(viewListener);



        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNextBtn.getText().equals("Finish")) {




                    String show1 = "show";
                    String unshow1 = "unshow";



                    final String show2 = show1.toString();
                    final String unshow2 = unshow1.toString();



                            if(checkBoxs.isChecked()){

                                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor1 = settings1.edit();
                                editor1.putString("showme", unshow2);
                                editor1.apply();




                            }
                            else {

                                SharedPreferences settings1 = getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor1 = settings1.edit();
                                editor1.putString("showme",show2);
                                editor1.apply();




                            }



                    finish();





                }
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });


    }

ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int i) {
        addDotsIndicator(i);
        mCurrentPage = i;
     checkBoxs=(CheckBox) findViewById(R.id.checkBoxes);
        if(i==0){

            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(false);
            mBackBtn.setVisibility(View.INVISIBLE);
            checkBoxs.setVisibility(View.INVISIBLE);

            mNextBtn.setText("Next");
            mBackBtn.setText("");
        }else if(i==mDots.length -1 ){
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(true);
            mBackBtn.setVisibility(View.VISIBLE);
            checkBoxs.setVisibility(View.VISIBLE);

            mNextBtn.setText("Finish");
            mBackBtn.setText("Back");

            SharedPreferences setting = getSharedPreferences("PREFS", 0);
            showme = setting.getString("showme", "");

            if (showme.equals("show")) {
                checkBoxs.setChecked(false);


            }else if (showme.equals("unshow")){
                checkBoxs.setChecked(true);
            }




        }else{

            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(true);
            mBackBtn.setVisibility(View.VISIBLE);
            checkBoxs.setVisibility(View.INVISIBLE);
            mNextBtn.setText("Next");
            mBackBtn.setText("Back");
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
};
    public void addDotsIndicator(int position) {
        this.mDots = new TextView[4];
        this.mDotLayout.removeAllViews();
        for (int i = 0; i < this.mDots.length; i++) {
            this.mDots[i] = new TextView(this);
            this.mDots[i].setText(Html.fromHtml("&#8226;"));
            this.mDots[i].setTextSize(35.0f);
            this.mDots[i].setTextColor(getResources().getColor(R.color.transparentwhite));
           mDotLayout.addView(this.mDots[i]);
        }
        if (this.mDots.length > 0) {
            this.mDots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }

}
