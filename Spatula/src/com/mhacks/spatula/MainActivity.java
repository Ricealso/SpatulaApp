package com.mhacks.spatula;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Debugging
	static final String TAG = "Spatula";

	// Android Objects

	// Layout Objects
	Button gBtnLogin, gBtnSignup;
	EditText gET_UN, gET_PW;
	TextView gTVResult1, gTVResult2, gTVResult3, gTVResult4;

	// Result Data Vars.
	int intUID, intPetID, intHappiness, intExperience, intResult;
	String strPetName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Instantiate Layout Objects
		gBtnLogin = (Button) findViewById(R.id.btn_login);
		gBtnSignup = (Button) findViewById(R.id.btn_signup);
		gET_UN = (EditText) findViewById(R.id.et_uname);
		gET_PW = (EditText) findViewById(R.id.et_pword);

		gTVResult1 = (TextView) findViewById(R.id.result1);
		gTVResult2 = (TextView) findViewById(R.id.result2);
		gTVResult3 = (TextView) findViewById(R.id.result3);
		gTVResult4 = (TextView) findViewById(R.id.result4);

		// Button Listeners
		gBtnLogin.setOnClickListener(new ViewStub.OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				Toast.makeText(getApplicationContext(), "Clicked", 500).show();

				// Getting Data
				MakeConnect mMakeConnect = new MakeConnect(MainActivity.this,
						gET_UN.getText().toString(), gET_PW.getText()
								.toString());
				mMakeConnect.execute("");
				gBtnLogin.setEnabled(false);

			}// end onClick()
		});// end onclicklistener

	}// end onCreate()

	// Class for making connection to http site
	public class MakeConnect extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String usernameStr = "", passwordStr = "";

		Exception exception = null;

		// Constructor
		MakeConnect(Context context, String uStr, String pStr) {
			mContext = context;
			usernameStr = uStr;
			passwordStr = pStr;
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				// nameValuePairs.add(new BasicNameValuePair("type", "login"));
				nameValuePairs.add(new BasicNameValuePair("username",
						usernameStr));
				nameValuePairs.add(new BasicNameValuePair("password",
						passwordStr));

				String result = SimpleConnect(nameValuePairs);// Request
				Log.i(TAG, "**Result: " + result);
				//if (!checkLogin(result)) {
				//} else {
					/*JSONObject jsonObject = new JSONObject(result);
					// Getting data from this JSON
					intUID = jsonObject.getInt("uid");
					intPetID = jsonObject.getInt("petid");
					intExperience = jsonObject.getInt("experience");
					intHappiness = jsonObject.getInt("happiness");
					strPetName = jsonObject.getString("petname");*/
				//}

			} catch (Exception e) {
				Log.e(TAG, "**Error!!", e);
				exception = e;
			}// end catch

			return true;
		}// end do in background

		String SimpleConnect(ArrayList<NameValuePair> nvp)
				throws ParseException, IOException {
			// Request
			HttpParams httpParameters = new BasicHttpParams();

			// Timeouts
			HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
			HttpConnectionParams.setSoTimeout(httpParameters, 15000);

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(
					"http://bmyf.clanslots.com/drupal/login.php");
			httppost.setEntity(new UrlEncodedFormEntity(nvp));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity);
		}// end SimpleConnect()

		boolean checkLogin(String s) {
			if (s.equals("0")) {
				Log.i(TAG, "Failed Login.");
				return false;
			} else if (s.equals("1")) {
				Log.i(TAG, "Login Success!");
				Intent i = new Intent();
				startActivity(i);
				return true;
			} else
				return true;
		}// end checkLogin()

		@Override
		protected void onPostExecute(Boolean valid) {
			// Refresh UI
			refreshUI();
			

			// Results from Site
			if (intResult == 1)
				Toast.makeText(mContext, "Failed Login", Toast.LENGTH_LONG)
						.show();

			gBtnLogin.setEnabled(true);
			if (exception != null) {
				Toast.makeText(mContext, exception.getMessage(), 500).show();
				Log.i(TAG, "Error:  " + exception.getMessage());
			}

		}// end onPostExecute()

	}// end class MakeConnect

	void refreshUI(){
		Log.i(TAG,"Refresh UI.");
		gTVResult1.setText("UID, petID: " + intUID + " , " + intPetID);
		gTVResult2.setText("Happiness: " + intHappiness);
		gTVResult3.setText("Experience: " + intExperience);
		gTVResult4.setText("PetName: " + strPetName);	
	}//end refreshUI
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

/*
 * //Class for making connection to http site public class MakeConnect extends
 * AsyncTask<String, Void, Boolean> {
 * 
 * Context mContext = null; String usernameStr = "", passwordStr = "";
 * 
 * 
 * 
 * Exception exception = null;
 * 
 * // Constructor MakeConnect(Context context, String uStr, String pStr) {
 * mContext = context; usernameStr = uStr; passwordStr = pStr; }
 * 
 * @Override protected Boolean doInBackground(String... params) {
 * 
 * try { ArrayList<NameValuePair> nameValuePairs = new
 * ArrayList<NameValuePair>(); // nameValuePairs.add(new
 * BasicNameValuePair("type", "login")); nameValuePairs.add(new
 * BasicNameValuePair("username", usernameStr)); nameValuePairs.add(new
 * BasicNameValuePair("password", passwordStr));
 * 
 * // Request HttpParams httpParameters = new BasicHttpParams();
 * 
 * // Timeouts HttpConnectionParams .setConnectionTimeout(httpParameters,
 * 15000); HttpConnectionParams.setSoTimeout(httpParameters, 15000);
 * 
 * HttpClient httpclient = new DefaultHttpClient(httpParameters); HttpPost
 * httppost = new HttpPost( "http://bmyf.clanslots.com/drupal/login.php");
 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); HttpResponse
 * response = httpclient.execute(httppost); HttpEntity entity =
 * response.getEntity();
 * 
 * String result = EntityUtils.toString(entity);
 * 
 * Log.i(TAG, "Result: " + result);
 * 
 * if (result.equals("0")) {// check if login fails intResult = 1; Log.i(TAG,
 * "Failed Login."); } else {// from the response I get something called a JSON
 * JSONObject jsonObject = new JSONObject(result); // Getting data from this
 * JSON intUID = jsonObject.getInt("uid"); intPetID =
 * jsonObject.getInt("petid"); intExperience = jsonObject.getInt("experience");
 * intHappiness = jsonObject.getInt("happiness"); strPetName =
 * jsonObject.getString("petname"); }// end else } catch (Exception e) {
 * Log.e(TAG, "Error!!", e); exception = e; }// end catch
 * 
 * return true; }// end do in background
 * 
 * 
 * String SimpleConnect(ArrayList<NameValuePair> nvp) throws ParseException,
 * IOException{ // Request HttpParams httpParameters = new BasicHttpParams();
 * 
 * // Timeouts HttpConnectionParams .setConnectionTimeout(httpParameters,
 * 15000); HttpConnectionParams.setSoTimeout(httpParameters, 15000);
 * 
 * HttpClient httpclient = new DefaultHttpClient(httpParameters); HttpPost
 * httppost = new HttpPost( "http://bmyf.clanslots.com/drupal/login.php");
 * httppost.setEntity(new UrlEncodedFormEntity(nvp)); HttpResponse response =
 * httpclient.execute(httppost); HttpEntity entity = response.getEntity();
 * 
 * return EntityUtils.toString(entity); }//end SimpleConnect()
 * 
 * boolean checkLogin(String s){ if(s.equals("0")){ Log.i(TAG,"Failed Login.");
 * return false; } else if(s.equals("1")){ Log.i(TAG,"Login Success!"); Intent i
 * = new Intent(); startActivity(i); return true; } else return false; }//end
 * checkLogin()
 * 
 * 
 * @Override protected void onPostExecute(Boolean valid) { // Refresh UI
 * gTVResult1.setText("UID, petID: " + intUID + " , " + intPetID);
 * gTVResult2.setText("Happiness: " + intHappiness);
 * gTVResult3.setText("Experience: " + intExperience);
 * gTVResult4.setText("PetName: " + strPetName);
 * 
 * // Results from Site if (intResult == 1) Toast.makeText(mContext,
 * "Failed Login", Toast.LENGTH_LONG) .show();
 * 
 * gBtnLogin.setEnabled(true); if (exception != null) { Toast.makeText(mContext,
 * exception.getMessage(), 500).show(); Log.i(TAG, "Error:  " +
 * exception.getMessage()); }
 * 
 * }// end onPostExecute()
 * 
 * }// end class MakeConnect
 */