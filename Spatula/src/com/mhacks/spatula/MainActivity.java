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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Debugging
	static final String TAG = "Spatula";
	static final boolean D = true; // Debugging Toasts

	// Android Objects

	// Layout Objects
	Button gBtnLogin, gBtnSignup;
	EditText gET_UN, gET_PW;

	// Http Objects
	String gStrResult;
	JSONObject gJsonObject;
	int gUID;
	String gType,gSuccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Instantiate Layout Objects
		gBtnLogin = (Button) findViewById(R.id.btn_login);
		gBtnSignup = (Button) findViewById(R.id.btn_signup);
		gET_UN = (EditText) findViewById(R.id.et_uname);
		gET_PW = (EditText) findViewById(R.id.et_pword);

		
		if(D){//to prevent typing credentials all the time
			gET_UN.setText("abc@def.ghi");
			gET_PW.setText("jklmnop");	
		}
		
		// Button Listeners
		gBtnLogin.setOnClickListener(listener);
		gBtnSignup.setOnClickListener(listener);
	}// end onCreate()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_settings:

			Intent i = new Intent(getApplicationContext(),
					SpatulaHome.class);// use this to bypass server not up
			i.putExtra("uid",4);
			startActivity(i);
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}



	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (D)
				Toast.makeText(getApplicationContext(),
						"Clicked: " + v.toString(), Toast.LENGTH_SHORT).show();
			switch (v.getId()) {
			case R.id.btn_login:
				// Getting Data
				MakeConnect mMakeConnect = new MakeConnect(MainActivity.this,
						"login", gET_UN.getText().toString(), gET_PW.getText()
								.toString());
				mMakeConnect.execute("");
				gBtnLogin.setEnabled(false); // So you click a million requets
				break;
			case R.id.btn_signup:
				
				  MakeConnect mSignupConnect = new
				  MakeConnect(MainActivity.this, "signup",
				  gET_UN.getText().toString(), gET_PW.getText() .toString());
				  mSignupConnect.execute("");
				 gBtnSignup.setEnabled(false);
				break;
			default:
				break;
			}// end switch
				// TODO Auto-generated method stub

		}// end onClick
	};// end onClickListener

	// Class for making connection to http site
	public class MakeConnect extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String usernameStr = "", passwordStr = "", strType = "";
		Exception exception = null;

		// Constructor
		MakeConnect(Context context, String type, String uStr, String pStr) {
			mContext = context;
			strType = type;
			usernameStr = uStr;
			passwordStr = pStr;
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				// #Comment Out Line below to get to old state
				nameValuePairs.add(new BasicNameValuePair("type", strType));
				nameValuePairs.add(new BasicNameValuePair("username",
						usernameStr));
				nameValuePairs.add(new BasicNameValuePair("password",
						passwordStr));

				gStrResult = SimpleConnect(nameValuePairs);// Request
				if (D)
					Log.i(TAG, "**Result: " + gStrResult);
				
				/*//TEST
				JSONObject gJO = new JSONObject(gStrResult);
				gType = gJO.getString("new");
				Log.i(TAG,"test json inside thread: " + gType);
				//END TEST
*/				
				
				gJsonObject = new JSONObject(gStrResult);
				
				Log.i(TAG,"Past json made.");
				// Verify credentials

				// if (!checkLogin(result)) {
				// } else {
				/*
				 * JSONObject jsonObject = new JSONObject(result); // Getting
				 * data from this JSON intUID = jsonObject.getInt("uid");
				 * intPetID = jsonObject.getInt("petid"); intExperience =
				 * jsonObject.getInt("experience"); intHappiness =
				 * jsonObject.getInt("happiness"); strPetName =
				 * jsonObject.getString("petname");
				 */
				// }

			} catch (Exception e) {
				Log.e(TAG, "**Error in Background!!", e);
				exception = e;
			}// end catch

			return true;
		}// end do in background

		String SimpleConnect(ArrayList<NameValuePair> nvp)
				throws ParseException, IOException {
			
			Log.i(TAG,"NVP: " + nvp.get(1).toString() + ", NVP2: " + nvp.get(2).toString());
			
			// Request
			HttpParams httpParameters = new BasicHttpParams();
			Log.i(TAG,"Point 1.");
			// Timeouts
			HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
			HttpConnectionParams.setSoTimeout(httpParameters, 15000);
			Log.i(TAG,"Point 2.");
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(
					"http://bmyf.clanslots.com/drupal/login.php");
			Log.i(TAG,"Point 3.");
			httppost.setEntity(new UrlEncodedFormEntity(nvp));
			Log.i(TAG,"Point 4.");
			HttpResponse response = httpclient.execute(httppost);
			Log.i(TAG,"Point 5.");
			HttpEntity entity = response.getEntity();

			Log.i(TAG,"Response: " + response.toString() + " \n Entity: " + entity.toString());
			
			String st = EntityUtils.toString(entity);
			Log.i(TAG,"TEST: "+st);
			return st;
		}// end SimpleConnect()

		void checkLogin(String s) {

			if (s.equals("0")) {
				Log.i(TAG, "Failed Login.");
				Toast.makeText(mContext,
						"Login failed. Please try again or sign up.",
						Toast.LENGTH_LONG).show();
				gBtnLogin.setEnabled(true); // allow them to re-attempt login
			} else if (s.equals("1")) {
				Log.i(TAG, "Login Success!");
				Intent i = new Intent("com.mhacks.spatula.SpatulaHome");
				startActivity(i);
			} else {
				Toast.makeText(mContext, "Server invalid return.",
						Toast.LENGTH_SHORT).show();
			}
		}// end checkLogin()

		void checkLogin() {
			String type;
			int verified;
			try {
				type = gJsonObject.getString("type");
				verified = gJsonObject.getInt("success");

				Log.i(TAG,"Type: " + type + ", Success: " + verified);
				if (type.equals("login")) {
					if (verified == 0)
						loginFailed();
					if (verified == 1)
						loginSuccess(gJsonObject.getString("uid"));
				if (type.equals("signup")){
					if (verified == 0)
						signupFailed();
					if (verified ==1)
						signupSuccess();
				}
				}// end if login
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// end checkLogin()

		void loginFailed() {
			Log.i(TAG, "Failed Login.");
			Toast.makeText(mContext,
					"Login failed. Please try again or sign up.",
					Toast.LENGTH_LONG).show();
			gBtnLogin.setEnabled(true); // allow them to re-attempt login
		}// end loginFailed()

		void loginSuccess(String uid) {
			Log.i(TAG, "Login Success!");
			Intent i = new Intent(getApplicationContext(),SpatulaHome.class);
			i.putExtra("uid", uid);
			startActivity(i);
			gBtnLogin.setEnabled(true); // allow them to re-attempt login
		}// end loginSuccess()
		
		void signupFailed(){
			Log.i(TAG, "Signup Failed");
			Toast.makeText(mContext,
					"Server failure, account not created.",
					Toast.LENGTH_LONG).show();
			gBtnSignup.setEnabled(true);
		}//end signupFailed()
		
		void signupSuccess(){
			Log.i(TAG,"Sign up Success!");
			Toast.makeText(mContext,
					"Account Created!",
					Toast.LENGTH_LONG).show();
		}//end signupSuccess()

		@Override
		protected void onPostExecute(Boolean valid) {
			//checkLogin(gStrResult);
			Log.i(TAG,"onPostExecute");
			checkLogin();
			gBtnLogin.setEnabled(true);
			if (exception != null) {
				Toast.makeText(mContext, exception.getMessage(), 500).show();
				Log.i(TAG, "**Error:  " + exception.getMessage());
			}

		}// end onPostExecute()

	}// end class MakeConnect

}// end class MainActivity()
