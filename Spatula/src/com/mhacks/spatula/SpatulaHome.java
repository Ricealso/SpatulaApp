package com.mhacks.spatula;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SpatulaHome extends Activity {
	

	// Debugging
	static final String TAG = "Spatula";
	

	// Layout Objects
	Button gBtnGetData;
	TextView gTV1,gTV2,gTV3,gTV4;
	
	// Result Data Vars.
		int intUID, intPetID, intHappiness, intExperience, intResult;
		String strPetName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Instantiate Layout Objects
		gBtnGetData = (Button) findViewById(R.id.btn_getData);

		gTV1 = (TextView) findViewById(R.id.tv_d1);
		gTV2 = (TextView) findViewById(R.id.tv_d2);
		gTV3 = (TextView) findViewById(R.id.tv_d3);
		gTV4 = (TextView) findViewById(R.id.tv_d4);
		
	}//end onCreate()

}
