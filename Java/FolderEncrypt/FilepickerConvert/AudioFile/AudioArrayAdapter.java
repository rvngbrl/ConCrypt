package com.example.rvn_gbrl.navigationsample.FilepickerConvert.AudioFile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rvn_gbrl.navigationsample.R;

import java.util.List;


public class AudioArrayAdapter extends ArrayAdapter<AudioItem>{

	private Context c;
	private int id;
	private List<AudioItem> decryptItems;

	public AudioArrayAdapter(Context context, int textViewResourceId,
                               List<AudioItem> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		decryptItems = objects;
	}
	public AudioItem getItem(int i)
	{
		return decryptItems.get(i);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(id, null);
		}

 //create a new view of my layout and inflate it in the row

		//convertView = ( RelativeLayout ) inflater.inflate( resource, null );

		final AudioItem o = decryptItems.get(position);
		if (o != null) {
			TextView t1 = (TextView) v.findViewById(R.id.TextView01);
			TextView t2 = (TextView) v.findViewById(R.id.TextView02);
			TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
 //Take the ImageView from layout and set the city's image

			ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);
			String uri = "drawable/" + o.getImage();

			int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
			Drawable images = c.getResources().getDrawable(imageResource);
			imageCity.setImageDrawable(images);

// set the image


			if(t1!=null)
				t1.setText(o.getName());
			if(t2!=null)
				t2.setText(o.getData());
			if(t3!=null)
				t3.setText(o.getDate());

		}
		return v;
	}

}
