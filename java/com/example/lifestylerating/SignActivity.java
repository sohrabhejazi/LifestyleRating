package com.example.lifestylerating;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import static com.example.lifestylerating.R.id.sign_in_button;

public class SignActivity extends AppCompatActivity implements OnConnectionFailedListener {
	private CallbackManager callbackManager;
	Drawable mDrawable;
	public static String userID;
	private Intent pushIntent;
	private GoogleApiClient mGoogleApiClient ;
	private int RC_SIGN_IN = 0;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_sign);
		// Configure sign-in to request the user's ID, email address, and basic
		// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestProfile()

				.build();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
		pushIntent = this.getIntent();
		Bundle intentBundle = pushIntent.getExtras();
		 //imageViiew = (ImageView) this.findViewById(R.id.pic);
		callbackManager = CallbackManager.Factory.create();
		LoginButton Bt1 = (LoginButton) this.findViewById(R.id.B1);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if(actionBar != null)
		{ //actionBar.setTitle(getResources().getString(R.string.app_name));
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);

			//actionBar.setIcon(R.drawable.app_icon);
		}
		//List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");
		//Bt1.setReadPermissions(permissionNeeds);
		Bt1.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
		AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(
					AccessToken oldAccessToken,
					AccessToken currentAccessToken) {

				if (currentAccessToken == null){
					//User logged out
					pushIntent.putExtra("NAME", "LOGOUTSOHRAB");
					SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
					SignActivity.this.finish();

				}
			}
		};
		Bt1.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(final LoginResult loginResult) {

				/*Log.d("msg", "User ID: " + loginResult.getAccessToken().getUserId()
						+ "\n" +
						"Email: "
						+ loginResult.getAccessToken().getPermissions());*/

				userID =  loginResult.getAccessToken().getUserId();

				GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
							@Override
							public void onCompleted(JSONObject object, GraphResponse response) {

								try
								{
									String userName = (String) object.get("name");
									pushIntent.putExtra("NAME", userName);
									SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);

									Log.v("NAME", userName);
								} catch (JSONException e) {
									e.printStackTrace();
									pushIntent.putExtra("NAME", "GANZEMPTYSOHRAB");
									SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
									SignActivity.this.finish();
								}
									//ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.userProfilePicture);
									//profilePicture.setProfileId((String) object.get("user_id"));
									//Picasso.with(MainActivity.this).load("https://graph.facebook.com/" + userID+ "/picture?type=large").into(imageView);
								/*Bitmap bitmap=null; //= (Bitmap) object.get("picture");
								URL imageURL = null;
								try
								{
									imageURL = new URL("https://graph.facebook.com/" + userID + "/picture");
										//InputStream inputStream = (InputStream) imageURL.getContent();
								}
								catch (MalformedURLException e)
								{
									e.printStackTrace();
								}

								try
								{
									bitmap = BitmapFactory.decodeStream(imageURL != null ? imageURL.openConnection().getInputStream() : null);
								} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
								}
								//ProfilePictureView profilePictureView =  new  ProfilePictureView(getApplicationContext());

								//bitmap = getCroppedBitmap(BitmapFactory.decodeStream(inputStream));
								mDrawable = new BitmapDrawable(getResources(), bitmap);
									//getActionBar().setLogo(mDrawable);
									//getActionBar().setTitle(userName);




								imageViiew.setImageDrawable(mDrawable);
								try {
									Bitmap mBitmap = getFacebookProfilePicture(userID);
									imageViiew.setImageBitmap(mBitmap);
								} catch (IOException e) {
									e.printStackTrace();
								}*/

								try {
									JSONObject data = response.getJSONObject();
									if (data.has("picture")) {
										URL profilePicUrl =  new URL("https://graph.facebook.com/" + userID + "/picture?width=200&height=200");//data.getJSONObject("picture").getJSONObject("data").getString("url");
										//getFacebookProfilePicture profilePic = new getFacebookProfilePicture(profilePicUrl);

										//Bitmap imageView=(new getFacebookProfilePicture().execute(profilePicUrl).get());

										pushIntent.putExtra("STRING_I_NEED", profilePicUrl.toString());
										//pushIntent.putExtra("PIC", imageView);
										SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
										SignActivity.this.finish();

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});


				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,email,gender, birthday, picture.type(large)");
				request.setParameters(parameters);
				request.executeAsync();

			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onError(FacebookException e) {

			}
		});

	       /* Bt1.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {

						//FaceBookFunc();

				}
			});*/

		Button Bt2 = (Button) this.findViewById(R.id.sign_in_button);
		Bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (v.getId()) {
					case sign_in_button:
						signIn();
						break;
					// ...
				}

			}
		});

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}


	private void handleSignInResult(GoogleSignInResult result) {
		Log.d("TAG", "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.

			GoogleSignInAccount acct = result.getSignInAccount();
			String userName = acct.getDisplayName();
			String personEmail = acct.getEmail();
			String personId = acct.getId();
			Uri personPhoto = acct.getPhotoUrl();
			//Person currentPerson = Plus.PeopleApi
					//.getCurrentPerson(mGoogleApiClient);
			String personPhotoUrl="KHALI";
			if(personPhoto!=null)
			 personPhotoUrl = personPhoto.toString();
			pushIntent.putExtra("NAME", userName);
			pushIntent.putExtra("STRING_I_NEED", personPhotoUrl);

			SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
			SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
			SignActivity.this.finish();
			/*Bitmap bitmap = null;
			try {
				bitmap=(new getFacebookProfilePicture().execute(new URL(personPhotoUrl)).get());
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				/*bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] bytes = stream.toByteArray();*/
			/*
			try {
				bitmap= new LoadProfileImage().execute(personPhotoUrl).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			//SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
			//Bitmap imageView=(new getFacebookProfilePicture().execute(profilePicUrl).get());
			/*Bitmap bitmap = null;
			try {
				java.net.URI ji = new java.net.URI(personPhoto.toString());
				URL url=ji.toURL();
				bitmap=(new getFacebookProfilePicture().execute(url).get());


			} catch (IOException e) {
				// Log exception
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}*/


		} else {
			// Signed out, show unauthenticated UI.
			//updateUI(false);
			pushIntent.putExtra("NAME", "GANZEMPTYSOHRAB");
			SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
			SignActivity.this.finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	static void signOut() {
		// TODO Auto-generated method stub
		LoginManager.getInstance().logOut();

	}

		/*protected void FaceBookFunc() {
		// TODO Auto-generated method stub

	    }*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle app bar item clicks here. The app bar
		// automatically handles clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
			case android.R.id.home:
				pushIntent.putExtra("NAME", "GANZEMPTYSOHRAB");
				SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
				SignActivity.this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Sign Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.example.lifestylerating/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Sign Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.example.lifestylerating/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}



	/*public static Bitmap getFacebookProfilePicture(URL url) throws IOException {
		//URL aURL = new URL(url);
		//URLConnection conn = aURL.openConnection();
	
				Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

				return bitmap;

	};*/


	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		/*ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}*/

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			//bmImage.setImageBitmap(result);
		}
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_sign,
					container, false);

			return rootView;
		}
	}

	@Override
	public void onBackPressed() {
		pushIntent.putExtra("NAME", "GANZEMPTYSOHRAB");
		SignActivity.this.setResult(Activity.RESULT_OK, pushIntent);
		SignActivity.this.finish();

	}


}
