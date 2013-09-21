package com.mhacks.spatula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PetAdapter extends BaseAdapter {

	private Context context;
	private final String[] nameValues, happinessValues;

	PetAdapter(Context ctx, String[] nm, String[] hpnss) {
		context = ctx;
		nameValues = nm;
		happinessValues = hpnss;

	}

	@Override
	public int getCount() {
		return nameValues.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.list_gridview_pet_layout, null);

			TextView tvPetName = (TextView)gridView.findViewById(R.id.list_pet_name);
			tvPetName.setText(nameValues[position]);

			TextView tvHappiness = (TextView)gridView.findViewById(R.id.list_pet_detail);
			tvHappiness.setText("Happiness: " + happinessValues[position]);

		}// end if
		else {
			gridView = (View) convertView;
		}

		return gridView;
	}

}
