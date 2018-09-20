package com.example.rvn_gbrl.navigationsample.FilepickerEncrypt;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rvn_gbrl.navigationsample.R;


public class EncryptArrayAdapter extends ArrayAdapter<EncryptItem>{

	private Context c;
	private int id;
	private List<EncryptItem> encryptItems;

	public EncryptArrayAdapter(Context context, int textViewResourceId,
                               List<EncryptItem> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		encryptItems = objects;
	}
	public EncryptItem getItem(int i)
	{
		return encryptItems.get(i);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(id, null);
		}

               /* create a new view of my layout and inflate it in the row */
		//convertView = ( RelativeLayout ) inflater.inflate( resource, null );

		final EncryptItem o = encryptItems.get(position);
		if (o != null) {
			TextView t1 = (TextView) v.findViewById(R.id.TextView01);
			TextView t2 = (TextView) v.findViewById(R.id.TextView02);
			TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
                       /* Take the ImageView from layout and set the city's image */
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
