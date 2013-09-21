package com.mhacks.spatula;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.mhacks.spatula.MainActivity.MakeConnect;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GameWalking extends Activity implements SensorEventListener {

	// Debugging
	static final String TAG = "Spatula";

	// Android Objects
	private SensorManager gSensorManager;
	private Sensor mAccelerometer;

	// Pertinant
	int counter=1 , sessionCount = 0, imageflip = 0;

	// Layout Objects
	TextView gTVsteps, gTVhappiness, gTVtest1;
	ImageView gImgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_gamewalking_layout);
		Init();

		// Instantiate Layout Obj.

		gTVsteps = (TextView) findViewById(R.id.tv_steps);
		gTVtest1 = (TextView) findViewById(R.id.tv_test1);
		gImgView = (ImageView) findViewById(R.id.game_img);

		// Instant. Android Obj.
		gSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = gSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}// end onCreate()

	protected void onResume() {
		super.onResume();
		gSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		gSensorManager.unregisterListener(this);
		sendGameUpdates(2);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	void updateGUI() {
		gTVsteps.setText(" " + sessionCount);
		// attempt to get dog image to "animate"
		/*
		 * imageflip++; if(imageflip==1)
		 * gImgView.setBackgroundResource(R.drawable.dog_icon); if(imageflip==2)
		 * gImgView.setBackgroundResource(R.drawable.dog2_icon);
		 * if(imageflip==3){ imageflip=0;
		 * gImgView.setBackgroundResource(R.drawable.dog3_icon); }
		 * gImgView.refreshDrawableState();
		 */

	}// end updateGUI

	// ##################Code used from Open Source Pedometer APP########
	// @author Levente Bagi
	private float mLimit = 10;
	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

	public void Init() {
		int h = 480; // TODO: remove this constant
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
	}

	public void setSensitivity(float sensitivity) {
		mLimit = sensitivity; // 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75
								// 50.62
	}

	public void addStepListener(StepListener sl) {
		mStepListeners.add(sl);
	}

	// public void onSensorChanged(int sensor, float[] values) {
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
			} else {
				int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
				if (j == 1) {
					float vSum = 0;
					for (int i = 0; i < 3; i++) {
						final float v = mYOffset + event.values[i] * mScale[j];
						vSum += v;
					}
					int k = 0;
					float v = vSum / 3;

					float direction = (v > mLastValues[k] ? 1
							: (v < mLastValues[k] ? -1 : 0));
					if (direction == -mLastDirections[k]) {
						// Direction changed
						int extType = (direction > 0 ? 0 : 1); // minumum or
																// maximum?
						mLastExtremes[extType][k] = mLastValues[k];
						float diff = Math.abs(mLastExtremes[extType][k]
								- mLastExtremes[1 - extType][k]);

						if (diff > mLimit) {

							boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
							boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
							boolean isNotContra = (mLastMatch != 1 - extType);

							if (isAlmostAsLargeAsPrevious
									&& isPreviousLargeEnough && isNotContra) {
								Log.i(TAG, "step");

								updateCounter(0);// I added this!

								for (StepListener stepListener : mStepListeners) {
									stepListener.onStep();
								}
								mLastMatch = extType;
							} else {
								mLastMatch = -1;
							}
						}
						mLastDiff[k] = diff;
					}
					mLastDirections[k] = direction;
					mLastValues[k] = v;
				}
			}
		}
	}// end onSensorChanged()

	public interface StepListener {
		public void onStep();

		public void passValue();
	}

	// #####################END OF OTHERS CODE############################3

	@Override
	protected void onStop() {
		updateCounter(1);//sends the remaining steps <50 on app close
		super.onStop();
	}

	void updateCounter(int mode) {
		Log.i(TAG,"SessionCount: "+sessionCount +", counter: " + counter);
		
		switch (mode) {
		case 0: // from reaching 50 steps
			counter++;// I added this!
			updateGUI();// and this too!
			if ((counter / 50) == 0) {
				sessionCount += counter;
				counter = 0;
				sendGameUpdates(50);
			}
			break;
		case 1: // from app close
			sendGameUpdates(counter);
			break;
		case 2: //from on pause, in case app quits w/o resume
			sendGameUpdates(counter);
			counter=0;
		default:
			break;
		}// end switch

	}

	void sendGameUpdates(int numb) {
		Log.i(TAG,"send update: num: "+ numb);
		MakeConnect_Game mMakeConnect = new MakeConnect_Game(GameWalking.this,
				"gamedata", numb);
		mMakeConnect.execute("");
	}// end sendGameUpdates

	// Class for making connection to http site
	public class MakeConnect_Game extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		int number = 0;
		Exception exception = null;

		// Constructor
		MakeConnect_Game(Context context, String type, int stepp) {
			mContext = context;
			number = stepp;
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("game", "walking"));
				nameValuePairs
						.add(new BasicNameValuePair("steps", "" + number));

				String gStrResult = SimpleConnect(nameValuePairs);// Request
				Log.i(TAG, "**Result: " + gStrResult);

				// Use this result from DB to talk about pets happiness..
				//JSONObject j = new JSONObject(gStrResult);

			} catch (Exception e) {
				Log.e(TAG, "**Error in Background!!", e);
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
					"http://bmyf.clanslots.com/drupal/data.php");
			httppost.setEntity(new UrlEncodedFormEntity(nvp));
			// Response
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			Log.i(TAG, "Response: " + response.toString() + " \n Entity: "
					+ entity.toString());

			String st = EntityUtils.toString(entity);
			Log.i(TAG, "TEST: " + st);
			return st;
		}// end SimpleConnect()
	}// end class MakeConnect_Game

}// end class GameWalking
