package com.mhacks.spatula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {
	private Context context;
	private final String[] homeValues;
 
	public HomeAdapter(Context context, String[] homeValues) {
		this.context = context;
		this.homeValues = homeValues;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View gridView;
 
		if (convertView == null) {
 
			gridView = new View(context);
 
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.list_gridview_layout, null);
 
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.list_text);
			textView.setText(homeValues[position]);
 
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.list_image);
 
			String home = homeValues[position];
 
			if (home.equals("Pets")) {
				imageView.setImageResource(R.drawable.dog_icon);
			} else if (home.equals("Store")) {
				imageView.setImageResource(R.drawable.store_icon);
			} else if (home.equals("Goals")) {
				imageView.setImageResource(R.drawable.goal_icon);
			} else if (home.equals("Exercise")) {
				imageView.setImageResource(R.drawable.exercise_icon);
			} else if (home.equals("Games")) {
				imageView.setImageResource(R.drawable.game_icon);
			} else {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
 
		} else {
			gridView = (View) convertView;
		}
 
		return gridView;
	}

	@Override
	public int getCount() {

		return homeValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
}//end class Home Adapter