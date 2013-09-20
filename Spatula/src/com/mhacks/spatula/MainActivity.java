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
	
	//Http Objects
	String gStrResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Instantiate Layout Objects
		gBtnLogin = (Button) findViewById(R.id.btn_login);
		gBtnSignup = (Button) findViewById(R.id.btn_signup);
		gET_UN = (EditText) findViewById(R.id.et_uname);
		gET_PW = (EditText) findViewById(R.id.et_pword);

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
				/*MakeConnect mSignupConnect = new MakeConnect(MainActivity.this,
						"signup", gET_UN.getText().toString(), gET_PW.getText()
								.toString());
				mSignupConnect.execute("");*/
				startActivity(new Intent("SpatulaHome.class"));//use this to bypass server not up
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

		@Override
		protected void onPostExecute(Boolean valid) {
			checkLogin(gStrResult);
			gBtnLogin.setEnabled(true);
			if (exception != null) {
				Toast.makeText(mContext, exception.getMessage(), 500).show();
				Log.i(TAG, "**Error:  " + exception.getMessage());
			}

		}// end onPostExecute()

	}// end class MakeConnect

}// end class MainActivity()
