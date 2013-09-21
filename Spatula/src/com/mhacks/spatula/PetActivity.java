package com.mhacks.spatula;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class PetActivity extends Activity {

	//Android Objects
	
	//Layout Objects
	GridView gGridView;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pet_layout);
	
		//Instatntiate Layout Obj.
		gGridView = (GridView) findViewById(R.id.pet_gridview);
		
		//Sample Pet Data
		String[] names = new String[] { "doggy", "kitty", "bunny" };
		String[] happiness = new String[] {"6", "3", "7"};
		
		
		
	}//end onCreate()
	
}//end class PetActivity
