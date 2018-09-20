package com.example.rvn_gbrl.navigationsample.FirstInstall;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rvn_gbrl.navigationsample.R;

/**
 * Created by RVN-GBRL on 4/5/2018.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){

        this.context=context;
    }

    public int[]slide_image={
           R.drawable.firstslide,
            R.drawable.secondslide,
            R.drawable.thirdslide,
            R.drawable.fourthslide


    };
    public String[] slide_headings ={ "Convert","Encrypt","Decrypt","Simulation"};
    public String[] slide_descs = new String[]{"Convert audio, video, and text files for compatibility and instruction.", "Encrypt all types of files stored in your phone, including folders, for privacy and security.", "Decrypt and bring back the damaged file from being encrypted.","Show the Mathematical processes behind the conversion of different types of texts."};
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
@Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slide_layout,container, false);

        ImageView slideImageView=(ImageView)view.findViewById(R.id.sliderimg);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);




        slideImageView.setImageResource(slide_image[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);
        container.addView(view);
        return view;



    }

@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
