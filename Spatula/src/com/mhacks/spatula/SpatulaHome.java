package com.mhacks.spatula;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SpatulaHome extends Activity {
	

	// Debugging
	static final String TAG = "Spatula";
	

	// Layout Objects
	GridView gGridView;
	TextView gTV1;
	
	// Result Data Vars.
		int intUID, intPetID, intHappiness, intExperience, intResult;
		String strPetName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_spatulahome_layout);
		
		//Instantiate Layout Objects

		gGridView = (GridView) findViewById(R.id.gridview);
		
		gTV1 = (TextView) findViewById(R.id.tv_d1);
		
		gTV1.setText("WELCOME UID: "+getIntent().getExtras().getString("uid"));
		
		
		
		//Grid View Setting
		String[] strA = new String[] { "Pets","Store","Goals","Exercise","Games" };
		
		ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strA);
		
		gGridView.setAdapter(new HomeAdapter(this, strA));
		
		gGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				Log.i(TAG,"Point1");
			   Toast.makeText(getApplicationContext(),
				((TextView) ((LinearLayout) v).getChildAt(1)).getText(), Toast.LENGTH_SHORT).show();
			   Log.i(TAG,"Point2");
			if(((TextView) ((LinearLayout) v).getChildAt(1)).getText().equals("Exercise")){
				Intent i = new Intent(getBaseContext(),GameWalking.class);
				startActivity(i);
			}
			if((((TextView) ((LinearLayout) v).getChildAt(1)).getText().equals("Store"))){
				Intent j = new Intent(getBaseContext(),StoreActivity.class);
				startActivity(j);
			}
			if((((TextView) ((LinearLayout) v).getChildAt(1)).getText().equals("Pets"))){
				Intent j = new Intent(getBaseContext(),PetActivity.class);
				startActivity(j);
			}
				Log.i(TAG,"Point3");
			}//end click

		});//end listener
		
		
	}//end onCreate()

}
