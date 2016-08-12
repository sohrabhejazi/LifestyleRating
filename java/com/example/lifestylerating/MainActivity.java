package com.example.lifestylerating;

//import com.example.lifestylerating.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.GridView;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

//import com.example.lifestylerating.MyScrollView;

@SuppressLint({"ClickableViewAccessibility", "NewApi"})
public class MainActivity extends Activity {
	static RelativeLayout layout;
	private int widthView = ViewGroup.LayoutParams.WRAP_CONTENT;
	private int leftBorder = 0;
	private int topBorder = 0;
	private ObservableScrollView scrollView1 = null;
	static int count;
	static File app_Dir;
	private Display display;
	private int displayWidth;
	private int displayHeight;
	private GridView gridview;
	public boolean touched;
	private int _xDelta;
	private int _yDelta;
	public int buttonSize;
	private static File order_File;
	private File pic_File;
	private File name_File;
	private int Max = 11;
	static String name;
	private Menu menu;
	//public ScrollView SV;
	static int[] actual = {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1};
	static String[] labels = {
			"Smoking",
			"Alcoholic Drinks",
			"Reading",
			"Smartphone Using",
			"Water Drinking",
			"Time with family",
			"Cycling",
			"Running",
			"Exercise",
			"Waching Tv",
			"Follow some topics"
	};

	public Integer[] images = {
			R.drawable.smoking2, R.drawable.alcohol9,

			R.drawable.reading3, R.drawable.using3,
			R.drawable.water2,
			R.drawable.family3, R.drawable.cycling2,
			R.drawable.running, R.drawable.exercise2,
			R.drawable.waching, R.drawable.follow
	};
	static int[] orders;
	private ViewGroup root;
	private RelativeLayout.LayoutParams layoutParams;
	static Context mContext;
	String msg;
	public String orderText = null;
	private int REQUEST_CODE = 0;
	private int R_CODE = 10;
	static Button BtDel;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestonInterceWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);


		app_Dir = this.getDir("Properties", MODE_PRIVATE);
		if (name_File == null) {

			name_File = new File(app_Dir, "nameFile");
			pic_File = new File(app_Dir, "picFile.png");

		}
		if (name_File.exists()) {
			//Do somehting
			try {
				name = this.read(name_File);
				getActionBar().setTitle(name);
			} catch (Exception e) {
				Log.e("TAG", e.toString());
			}
			readBitmapFile(pic_File);
		} else {
			// Do something else.
			getActionBar().setTitle("SIGN IN");
			getActionBar().setLogo(R.drawable.unknown);
			//pic_File = new File(app_Dir, "picFile");
			//name_File = new File(app_Dir, "nameFile");
			saveInFile(name_File, "SIGN IN");

		}
		final int abTitleId = getResources().getIdentifier("action_bar_title", "id", "android");
		findViewById(abTitleId).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			/*
			AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
			alertDialog.setMessage("SIGN IN WITH FACEBOOK");
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				            dialog.dismiss();
				        }
				    });
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            dialog.dismiss();
			        }
			    });
			alertDialog.show();*/
				if(getActionBar().getTitle().equals("SIGN IN")) {
					Intent intent = new Intent(MainActivity.this, SignActivity.class);
					MainActivity.this.startActivityForResult(intent, R_CODE);
				}
			}
		});
		root = (ViewGroup) findViewById(android.R.id.content);
		mContext = root.getContext();
		if (order_File == null) {

			order_File = new File(app_Dir, "orderFile");
		}
		try {
			orderText = this.read(order_File);
			Log.i(" order read of File", (String) orderText);
		} catch (Exception e) {
			Log.e("TAG", e.toString());
		}
		setOrders();
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout );
		display = this.getWindowManager().getDefaultDisplay();
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		buttonSize = displayWidth / 2 - 1;
		//CreatGridView();
		layout = (RelativeLayout) findViewById(R.id.RltLayout);
		scrollView1 = (ObservableScrollView) findViewById(R.id.scrollV);
		//scrollView1.setScrollViewListener((ScrollViewListener) this);
		CreateRelativeLayout();
		MakeClickAndDrag();


		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	//////////////////////////////////////////////////////////////////////////////
	private void saveBitmapInFile(File file, Bitmap bitmapImage) {
		ContextWrapper cw = new ContextWrapper(getApplicationContext());
		// path to /data/data/yourapp/app_data/imageDir
		//File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		// Create imageDir
		//File mypath=new File(directory,"profile.jpg");

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			// Use the compress method on the BitMap object to write image to the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	////////////////////////////////////////////////////////////////////////////////

	private void readBitmapFile(File file) {

		try {
			//File f=new File(path, "profile.jpg");
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
			//ImageView img=(ImageView)findViewById(R.id.imgPicker);
			//img.setImageBitmap(b);
			Drawable mDrawable = new BitmapDrawable(getResources(), b);
			getActionBar().setLogo(mDrawable);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	////////////////////////////////////////////////////////////////////////////////

	private void setOrders() {
		// TODO Auto-generated method stub

		count = 0;
		if (orderText == null || orderText == "") {

			orders = new int[Max];
			for (int i = 0; i < labels.length; i++) {
				if (actual[i] == 1) {
					orders[count] = i;
					count++;
				}
			}
			return;
		}
		List<String> elephantList = Arrays.asList(orderText.split(","));
		int countList = elephantList.size();
		//getMaxCount();
		orders = new int[Max];
		for (int i = 0; i < Max; i++) {
			actual[i] = 0;
		}


		for (int i = 0; i < countList; i++) {
			int index = IndexOfLabel(elephantList.get(i));
			orders[i] = index;
			actual[index] = 1;
			count++;

		}


	}
////////////////////////////////////////////////////////////////////////////////////////

	static int IndexOfLabel(String label) {
		int index = -1;
		for (int i = 0; i < labels.length; i++) {
			if (labels[i].equals(label)) {
				index = i;
				return index;
			}
		}
		return index;
	}

	static int IndexOfOrder(int ind) {
		int index = -1;
		for (int i = 0; i < orders.length; i++) {
			if (orders[i]==(ind)) {
				index = i;
				return index;
			}
		}
		return index;
	}
////////////////////////////////////////////////////////////////////////////////////////

	private String read(File fileName) throws Exception {
		String retString = "";
		BufferedReader reader = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(in));
			String zeile;
			while ((zeile = reader.readLine()) != null)
				retString += zeile;
		} finally {
			if (reader != null)
				reader.close();
		}
		return retString;
	}

////////////////////////////////////////////////////////////////////////////////////////

	private static void saveInFile(File file, String text) {
		try {
			FileOutputStream out = new FileOutputStream(
					file);
			OutputStreamWriter writer =
					new OutputStreamWriter(out);
			writer.write(text);
			writer.close();
		} catch (FileNotFoundException fnfe) {
			Log.e("TAG", fnfe.toString());
		} catch (IOException ioe) {
			Log.e("TAG", ioe.toString());
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////

	// Callback-Methode Follow
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
		super.onActivityResult(requestCode, resultCode, returnIntent);
		Log.d("TAG", "requestCode=" + REQUEST_CODE + ", resultCode=" + resultCode);
		if (requestCode == 0) {
			RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.RltLayout);
			Bundle extrasBundle = returnIntent.getExtras();
			if (extrasBundle != null) {
				String temp = extrasBundle.getString("FollowList");
				final int restSize;
				if (temp != null) {
					List<String> restList = Arrays.asList(temp.split(","));
					if (restList.get(0).equals("empty"))
						return;
					/*for (int i = 0; i < Max; i++)
						orders[i] = 0;
					for (int i = 0; i < count; i++) {
						Button Bt = (Button) layout2.getChildAt(i);
						CharSequence St = Bt.getText();
						orders[i] = IndexOfLabel((String) St);
					}*/


					restSize = restList.size();
					if (restSize == 0)
						return;
					int base = count;
					//int indexFollow=orders[base-1];
					for (int i = 0; i < restSize; i++) {
						int index = IndexOfLabel(restList.get(i));
						orders[base + i - 1] = index;
						actual[index] = 1;
						count++;
					}
					orders[base + restSize - 1] = Max - 1;
					if (layout2.getChildCount() > 0)
						layout2.removeAllViews();
					CreateRelativeLayout();
					MakeClickAndDrag();
					saveViews();
					//REQUEST_CODE++;
				}
			} else
				Log.d("TAG", "Keine Extras im return-Intent");
		} else if (requestCode == 10) {
			RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.RltLayout);
			Bundle extrasBundle = returnIntent.getExtras();
			if (extrasBundle != null) {
				String temp = extrasBundle.getString("NAME");
				final int restSize;
				if (temp != null) {

					if (temp.equals("GANZEMPTYSOHRAB"))
						return;
					else if (temp.equals("LOGOUTSOHRAB")) {
						getActionBar().setTitle("SIGN IN");
						getActionBar().setLogo(R.drawable.unknown);
						saveInFile(name_File, "SIGN IN");
						Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);

						saveBitmapInFile(pic_File, bitmapIcon);
						return;
					}
					getActionBar().setTitle(temp);
					Bitmap bitmap = null;
					//Bitmap bitmap = (Bitmap) returnIntent.getParcelableExtra("PIC");
					String turl = extrasBundle.getString("STRING_I_NEED");

					URL url = null;
					try {
						url = new URL(turl);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					if (url == null) {
						bitmap = drawableToBitmap(getResources().getDrawable(R.drawable.unknown));
					} else
						try {
							bitmap = (new getFacebookProfilePicture().execute(url).get());
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
					getActionBar().setLogo(mDrawable);
					//pic_File = new File(app_Dir, "picFile");
					//name_File = new File(app_Dir, "nameFile");
					if (name_File.exists()) {
						saveInFile(name_File, temp);
						saveBitmapInFile(pic_File, bitmap);
					}
					//menu.findItem(R.id.sign_out).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_edit));
				} else
					Log.d("TAG", "Keine Extras im return-Intent");
			}

		}
	}

	////////////////////////////////////////////////////////////////////////////////////////
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
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
				"Main Page", // TODO: Define a title for the content shown.
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

	/////////////////////////////////////////////////////////////////////////////////////
	public class getFacebookProfilePicture extends AsyncTask<URL, Void, Bitmap> {


		@Override
		protected Bitmap doInBackground(URL... params) {
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPreExecute() {

		}

	}

///////////////////////////////////////////////////////////////////////////////////

	static void saveViews() {

		//RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.RltLayout);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			//Button Bt = (Button) layout.getChildAt(i);
			//CharSequence St = Bt.getText();
			CharSequence St = labels[orders[i]];
			sb.append(St);
			if (i < count - 1)
				sb.append(",");
		}
		saveInFile(order_File, (String) sb.toString());
	}

///////////////////////////////////////////////////////////////////////////////////
@Override
public void onBackPressed() {
	//moveTaskToBack(true);
	//fileCount.saveInFile(Integer.toString(counter));
	//fileMax.saveInFile(Integer.toString(Max));
	saveViews();
	this.finish();

}
	////////////////////////////////////////////////////////////////////////////////////


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub


		saveViews();
		super.onDestroy();

	}

	private void MakeClickAndDrag() {
		// TODO Auto-generated method stub
		for (int i = 0; i < count; i++) {
			Button Bt = (Button) this.findViewById(i + 1);
			ClickAndDrag BtD = new ClickAndDrag(mContext, layout, Bt, buttonSize, displayHeight, scrollView1);
			BtD.startClickListener(BtD);

			Bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button Bt = (Button) v;
					if (v.getId() == count)
						FollowFunc();
					else if (((Button) v).getText() == "Smoking") {
						Intent intent = new Intent(MainActivity.this, smoking.class);
						intent.putExtra("title",((Button) v).getText() );
						MainActivity.this.startActivity(intent);
					}

				}
			});
			//BtD.startDrog();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	protected void FollowFunc() {
		Intent intent = new Intent(
				MainActivity.this, FollowActivity.class);
		StringBuilder sb = new StringBuilder();
		if (count == Max) {
			AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
			//alertDialog.setTitle("Alert");
			alertDialog.setMessage("There is no more topic to follow");
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.show();
			return;
		}
		for (int i = 0; i < Max; i++) {
			if (actual[i] == 0) {
				sb.append(labels[i]);
				if (i < Max - 1)
					sb.append(",");
			}

		}
		intent.putExtra("RestToFollow", sb.toString());
		MainActivity.this.startActivityForResult(intent, REQUEST_CODE);
	}

	@SuppressLint("NewApi")
	protected void CreateRelativeLayout() {
		int x = 0;
		int y = 100;

		RelativeLayout.LayoutParams params;

		//private Drawable mTh = getResources().getDrawable(R.drawable.smoking2);
		// references to our images

		//layout = (AbsoluteLayout) View.inflate(this, R.layout.activity_main, null);


		//int M=6;
		for (int i = 0; i < count; i++) {
			params = new RelativeLayout.LayoutParams(buttonSize,
					buttonSize);

			if (i == 1) {
				params.addRule(RelativeLayout.RIGHT_OF, i);
				params.leftMargin = 2;
			} else if (i % 2 == 0 && i != 0) {
				params.addRule(RelativeLayout.BELOW, i - 1);
				params.topMargin = 2;

			} else if (i % 2 == 1) {
				params.addRule(RelativeLayout.BELOW, i - 1);
				params.addRule(RelativeLayout.RIGHT_OF, i);
				params.leftMargin = 2;
				params.topMargin = 2;
			}


			Button Bt = new Button(this);
			Bt.setId(i + 1);

			//Bt.setTag(i+1);
			if (i != (count - 1)) {
				Bt.setGravity(Gravity.LEFT);
				Bt.setTextColor(Color.parseColor("red"));
			} else
				Bt.setTextColor(Color.parseColor("white"));
			Bt.setText(labels[orders[i]].toString());
			Bt.setLayoutParams(params);
			Bt.setBackgroundDrawable(getResources().getDrawable(images[orders[i]]));
			layout.addView(Bt);

		}
		//ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
		//drawable.getPaint().setColor(Color.GRAY);
		//drawable.getPaint().setAntiAlias(true);

		AbsoluteLayout.LayoutParams paramsDel = new AbsoluteLayout.LayoutParams(40, 40, x, y);
		BtDel = new Button(this);
		paramsDel.width = 150;//displayWidth/2;
		paramsDel.height = 150;//displayWidth/2;
		BtDel.setId(100);
		BtDel.setLayoutParams(paramsDel);
		//BtDel.setBackgroundResource(R.id.circle); //(android.R.drawable.ic_menu_delete);
		BtDel.setBackgroundResource(R.drawable.roundedbutton);
		layout.addView(BtDel);
		BtDel.setVisibility(View.INVISIBLE);
		ClickAndDrag Btt = new ClickAndDrag(mContext, layout, BtDel, buttonSize, displayHeight, scrollView1);
		Btt.startDel(Btt);
		//layoutDel.addView(BtDel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		//menu.findItem(R.id.sign_out).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_edit));
		//menu.getItem(1).setIcon(R.drawable.ic_launcher);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == R.id.sign_out) {
			SignActivity.signOut();
			getActionBar().setTitle("SIGN IN");
			getActionBar().setLogo(R.drawable.unknown);
			saveInFile(name_File, "SIGN IN");
			Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);

			saveBitmapInFile(pic_File, bitmapIcon);
			return true;
		}
		return super.onOptionsItemSelected(item);
		/*int itemId = item.getItemId();
		DialogFragment dialog;
		android.app.FragmentManager fragmentManager;
		switch(itemId)
		{

		case R.id.m_webserver:
			if(projectNotes == null) {
				tvOutput.setText("Bitte vor Kontakt mit dem Webserver zunächst ein Projekt auswählen oder eine Notiz erstellen und speichern.");
				return false;
				} else {
				dialog = new WebserverDialogFragment();
				fragmentManager = getFragmentManager();
				dialog.show(fragmentManager, webserverTag);
				return true;
				}

		case R.id.del:

		    	Note note = projectNotes.get(notesIndex);
		    	int size = projectNotes.size();
		    	if(size==0)
		    		return false;
		        dbManager.writeInDb(note.getdeleteString());
		        projectNotes.remove(note);
		        if(size==1)
		        {
		        	etSubject.setText("");
					etNote.setText("");
					tvNoteNumberText = 0 + "/"+ projectNotes.size();
				    tvNoteNumber.setText(tvNoteNumberText);
				    dbManager.writeInDb(project.getdeleteString());
		        	return true;
		        }
		        else if(size==notesIndex+1)
		        notesIndex--;
		        showNote(notesIndex);
		        return true;
		   case R.id.m_edit_project:
				dialog = new EditProjectDialogFragment();
				fragmentManager = getFragmentManager();
				dialog.show(fragmentManager, editProjectTag);
				return true;
			case R.id.m_select_project:
				dialog = new SelectProjectDialogFragment();
				fragmentManager = getFragmentManager();
				dialog.show(fragmentManager, selectProjectTag);
				return true;
			case R.id.action_settings:
				Log.d(TAG, "Eventhandling zu \"action-settings\" noch nicht programmiert");
				return false;
			case R.id.gps_1:
				return setGpsInterval(0, item);
			case R.id.gps_2:
				return setGpsInterval(1, item);
			case R.id.gps_3:
				return setGpsInterval(2, item);
			case R.id.gps_4:
				return setGpsInterval(3, item);
			case R.id.m_backup_project:
				MailHelper.sendProject(project, projectNotes, this);
				return true;
			case R.id.m_show_osm:
				if(htmlGenerator == null)
					htmlGenerator = new HtmlGenerator(this);
				if(projectNotes != null)
				{
					//htmlGenerator.getOsmHtmlPath(projectNotes,
					//notesIndex, R.drawable.pin);
					String filePath = htmlGenerator
							.getOsmHtmlPath(projectNotes, notesIndex,
							R.drawable.pin);
							if(filePath != null)
							this.displayOnOSM(filePath);
							else
							tvOutput.setText("Fehler beim Zugriff auf \"osm.html\"");
				} else
					tvOutput.setText("Bitte zunächst ein Projekt erstellen oder auswählen");
				return true;
			default:
				return super.onOptionsItemSelected(item);
	    }
	}
	// Hilfsmethode zu den GPS-Vorgaben:
	private boolean setGpsInterval(int modus, MenuItem item)
	{
		binder.setGpsInterval(time[modus], distance[modus]);
		item.setChecked(true);
		return true;*/
	}


}

        /*SV.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                Log.v("TAG","PARENT TOUCH");
                
                //childToGetTheEvent.onTouchEvent(layout);
                return false;
            }
        }); 
        
        Button Bt = (Button) layout.findViewById(3);
        Bt.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
    		@Override
             public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   
                }
                else
                {
                   return false;
                }
				return false;
             }
          });
	   
	
		/* 
		SV.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                Log.v("TAG","PARENT TOUCH");
                
                childToGetTheEvent.onTouchEvent(layout);
                return false;
            }
        }); 
		//layout.requestDisallowInterceptTouchEvent(false);
		//findViewById(R.id.AbsLayout).getParent().requestDisallowInterceptTouchEvent(false);
		//layout.dispatchTouchEvent(MotionEvent.ACTION_DOWN);
		
		//MotionEvent ev = ACTION_DOWN;
		
		layout.getParent().requestDisallowInterceptTouchEvent(false);          
		layout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                Log.v("TAG","CHILD TOUCH");
                 //  Disallow the touch request for parent on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
	}
		
		/*
		GestureDetector.OnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
		    @Override
		    public void onLongPress(MotionEvent e)
		    {
		        Toast.makeText(MainActivity.this, "LongClick", Toast.LENGTH_SHORT).show();
		    }
		};

		final GestureDetector gestureDetector = new GestureDetector(this, listener);

		layout.setLongClickable(true);
		layout.setClickable(true);
		layout.setOnTouchListener(new View.OnTouchListener() {  
		    @Override
		    public boolean onTouch(View arg0, MotionEvent arg1) {
		        //gesture detector to detect swipe.
		        gestureDetector.onTouchEvent(arg1);
		        return true;//always return true to consume event
		    }
		});
	}
		
		
		/*setOnLongClickListener( new View.OnLongClickListener() {
			
			
	        @Override
	        public boolean onLongClick(View v) {
	        Button Bt= (Button) v;
	        ClickAndDrag BtD = new ClickAndDrag(Bt,displayWidth,orders);
	        BtD.startClickListener();
	        BtD.startDrog();
	        return true;
	        } 
	        }); 
		
}
		
	
		
/*
 GestureDetector.OnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
		    @Override
		    public void onLongPress(MotionEvent e)
		    {
		        Toast.makeText(MainActivity.this, "LongClick", Toast.LENGTH_SHORT).show();
		    }
		};

		final GestureDetector gestureDetector = new GestureDetector(this, listener);

		layout.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event)
		    {
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		final Button Bt = (Button) layout.findViewById(3);
		Bt.setOnDragListener(new View.OnDragListener() {
	         @Override
	         public boolean onDrag(View v, DragEvent event) {
	            switch(event.getAction())
	            {
	               case DragEvent.ACTION_DRAG_STARTED:
	               layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
	               Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
	               
	               // Do nothing
	               break;
	               
	               case DragEvent.ACTION_DRAG_ENTERED:
	               Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
	               int x_cord = (int) event.getX();
	               int y_cord = (int) event.getY();
	               break;
	               
	               case DragEvent.ACTION_DRAG_EXITED :
	               Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
	               x_cord = (int) event.getX();
	               y_cord = (int) event.getY();
	               layoutParams.leftMargin = x_cord;
	               layoutParams.topMargin = y_cord;
	               v.setLayoutParams(layoutParams);
	               break;
	               
	               case DragEvent.ACTION_DRAG_LOCATION  :
	               Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
	               x_cord = (int) event.getX();
	               y_cord = (int) event.getY();
	               break;
	               
	               case DragEvent.ACTION_DRAG_ENDED   :
	               Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
	               
	               
	               break;
	               
	               case DragEvent.ACTION_DROP:
	               Log.d(msg, "ACTION_DROP event");
	               
	               x_cord = (int) event.getX();
	               y_cord = (int) event.getY();
	               int row,column;
	               row= y_cord%(displayWidth/2);
	               column= x_cord%(displayWidth/2);
	               
	               break;
	               default: break;
	            }
	            return true;
	         }
	      });
	      
	      Bt.setOnTouchListener(new View.OnTouchListener() {
	        @SuppressLint("NewApi")
			@Override
	         public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction() == MotionEvent.ACTION_DOWN) {
	               ClipData data = ClipData.newPlainText("", "");
	               View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(Bt);
	               
	               Bt.startDrag(data, shadowBuilder, Bt, 0);
	               Bt.setVisibility(View.INVISIBLE);
	               return true;
	            }
	            else
	            {
	               return false;
	            }
	         }
	      });
		
		}
		
		
		*/
		
		
		
		//Bt.setOnTouchListener(new OnTouchListener() 
		/*Bt.setOnLongClickListener(new View.OnLongClickListener()
		{
	        @Override
	        //public boolean onTouch(View v, MotionEvent event) 
	        public boolean onLongClick(View v) 

	        {
	            touched = true;
	            MarginLayoutParams marginParams = new MarginLayoutParams(Bt.getLayoutParams());              
	            //int left = (int) event.getRawX() - (v.getWidth() / 2);
	            //int top = (int) event.getRawY() - (v.getHeight());
	            //marginParams.setMargins(left, top, 0, 0);
	            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
	            //Bt.setLayoutParams(layoutParams); 
	            return false;
	        }
	    });*/
		
		/*
		Bt.setOnTouchListener(new OnTouchListener()
		  {
		    int prevX,prevY,prevX2,prevY2;

		    @Override
		    public boolean onTouch(final View v,final MotionEvent event)
		      {
		      final RelativeLayout.LayoutParams par=(RelativeLayout.LayoutParams)v.getLayoutParams();
		      final int X = (int) event.getRawX();
		      final int Y = (int) event.getRawY();
		      final int actionIndex = event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		      final int location[] = { 0, 0 };
		      v.getLocationOnScreen(location);
		      //rawX = (int) event.getX(actionIndex) + location[0];
		      //rawY = (int) event.getY(actionIndex) + location[1];
		      
		      switch(event.getAction())
		        {
		        case MotionEvent.ACTION_MOVE:
		          {
		        	  RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
		              layoutParams.leftMargin = X - _xDelta;
		              layoutParams.topMargin = Y - _yDelta;
		              //v.startDrag(data, shadowBuilder, myLocalState, flags)
		              layoutParams.rightMargin = -2;
		              layoutParams.bottomMargin = -2;
		              v.setLayoutParams(layoutParams);
		              prevX2=(int)event.getRawX();
			          if(prevX2-prevX >60)
			          {
			        	  prevY2=(int)event.getRawY();
			        	  orders[2]=3;
			        	  orders[3]=2;
			        	  _root.invalidate();
			        	  CreateRelativeLayout();
			          }
		              
		              return true;
		          //par.topMargin+=(int)event.getRawY()-prevY;
		          
		          //par.leftMargin+=(int)event.getRawX()-prevX;
		          
		        	  //par.addRule(RelativeLayout.BELOW, 1 );
					  //par.leftMargin = 2; 
		        	  //final RelativeLayout.LayoutParams par2=(RelativeLayout.LayoutParams) findViewById(4).getLayoutParams();
		        	  /*RelativeLayout.LayoutParams par2=(RelativeLayout.LayoutParams)v.getLayoutParams();
		        	  par.addRule(RelativeLayout.BELOW, 1 );
		        	  par.addRule(RelativeLayout.RIGHT_OF, 4 );
					  par.leftMargin = 2; 
					  par2.addRule(RelativeLayout.BELOW, 1 );
		        	 // par2.addRule(RelativeLayout.RIGHT_OF, 4 );
					  par2.rightMargin = 2; 
					  findViewById(4).setLayoutParams(par);
		        	  v.setLayoutParams(par2);*/
		        	  
		        	  /* Main
		        	  prevX2=(int)event.getRawX();
			          if(prevX2-prevX >10){
		        	  prevY2=(int)event.getRawY();
		        	  orders[2]=3;
		        	  orders[3]=2;
		        	  CreateRelativeLayout();
		        	  return true;
		               }*/
		         // v.setLayoutParams(par2);
/*		          
		             
		          }
		        case MotionEvent.ACTION_UP:
		          {
		          //par.topMargin+=(int)event.getRawY()-prevY;
		          //par.leftMargin+=(int)event.getRawX()-prevX;
		          //v.setLayoutParams(par);
		        	  CreateRelativeLayout();
		          return true;
		          }
		        case MotionEvent.ACTION_DOWN:
		          {
		          prevX=(int)event.getRawX();
		          prevY=(int)event.getRawY();
		          //par.bottomMargin=-2*v.getHeight();
		          //par.rightMargin=-2*v.getWidth();
		          //v.setLayoutParams(par);
		          RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
		          _xDelta = X - lParams.leftMargin;
		          _yDelta = Y - lParams.topMargin;
		          return true;
		          }
		        }
		   
		      return false;
		      }
		  });

	    layout.setOnTouchListener(new OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	        Button Bt= (Button) this.findViewById(i+1);
		  ClickAndDrag BtD = new ClickAndDrag(Bt,displayWidth,orders);
		  BtD.startClickListener();
		  BtD.startDrog();
	            if(touched == true) // any event from down and move
	            {
	            	//RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)event.getX()-button_Countries.getWidth()/2,(int)event.getY()-myButton.getHeight()/2);
	                //Bt.setLayoutParams(lp);
	            }
	            if(event.getAction()==MotionEvent.ACTION_UP){
	                touched = false;
	            }
	            return true;
	        }
	    });
		*/
	    
	

	
			/*params = new AbsoluteLayout.LayoutParams(widthView, widthView, x, y);
			Button Bt = new Button(this);
			params.width=displayWidth/2;
			params.height=displayWidth/2;
			
			Bt.setId(i);
			Bt.setGravity(Gravity.LEFT);
			Bt.setText(labels[i].toString());
			Bt.setLayoutParams(params);
			Bt.setTextColor(Color.parseColor("green"));
			Bt.setBackgroundDrawable(getResources().getDrawable(images[i]));
			//Bt1.setBackground();
			//int M = layout.getChildCount();
			layout.addView(Bt);
			//Bt1.setBackground(smoking2);
			//int N = layout.getChildCount();
			if(i%2==0)
				y=displayWidth/2+2;
			else 
			{
				y=0;
				x=x+displayWidth/2+2;
			}
			
		
		
		Button Bt = new Button(this);
		Bt=(Button) layout.findViewById(1);
		Bt.setOnLongClickListener(new View.OnLongClickListener() {
	         @Override
	         public boolean onLongClick(View v) {
	             
	            	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(buttonSize,
	     					buttonSize);
	                 //set the margins. Not sure why but multiplying the height by 1.5 seems to keep my finger centered on the button while it's moving
	                 //params.setMargins((int)me.getRawX() - v.getWidth()/2, (int)(me.getRawY() - v.getHeight()*1.5), (int)me.getRawX() - v.getWidth()/2, (int)(me.getRawY() - v.getHeight()*1.5));
	                 //v.setLayoutParams(params);
	           
	             return true;
	        }
	    });*/
		//if(N > M) Toast.makeText(MainActivity.this, "View Successfully Added!", 30).show();
		/*
		for(int j=0;j<4;j++)
		{
			int blast=0;
			switch (j)
			{
				case 0: blast=50; break;
				case 1: blast=100; break;
				case 2: blast=150; break;
				case 3: blast=displayWidth/2; break;
			}
				
			for(int i=0;i<M;i++)
				{
					
					Button Bt = new Button(this);
					Bt=(Button) layout.findViewById(i+1);
					params = new RelativeLayout.LayoutParams(blast,
							blast);
					if(i==1) 
					{
						params.addRule(RelativeLayout.RIGHT_OF, i );
						params.leftMargin = 2; 
					}
					else if( i%2==0 && i!=0)
					{
						params.addRule(RelativeLayout.BELOW, i-1 );
						params.topMargin = 2;
						 
					}
					else if( i%2==1)
					{
						params.addRule(RelativeLayout.BELOW, i-1 );
						params.addRule(RelativeLayout.RIGHT_OF, i );
						params.leftMargin = 2; 
						params.topMargin = 2;
					}
					Bt.setLayoutParams(params);
					
				}
			
		}*/
		
	
	/*
	private void CreatGridView() {
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setNumColumns(2);
		gridview.setVerticalSpacing(2);
		gridview.setHorizontalSpacing(2);
		//gridview.setColumnWidth(50);
		
		
		gridview.setAdapter(new ImageAdapter(this));
		//gridview.getChildAt(1);
		
		//Bt0.setBackgroundDrawable(getResources().getDrawable(R.drawable.smoking2));
		//int M = gridview.getChildCount();
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
	        	//gridview.getViewTreeObserver();
	    		//int M = gridview.getChildCount();
	            Toast.makeText(MainActivity.this, "" + position,
	                    Toast.LENGTH_SHORT).show();
	        }
	    });
		
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        //return mThumbIds.length;
	    	return filesnames.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    	
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	     
		public View getView(int position, View convertView, ViewGroup parent) {
	    	Button button;
	    	//ImageView imageView;
	        if (convertView == null) {
	            // if it's not recycled, initialize some attributes
	        	button = new Button(mContext);
	        	button.setLayoutParams(new GridView.LayoutParams(displayWidth/2,displayWidth/2));
	        	//imageView = new ImageView(mContext);
	        	//imageView.setLayoutParams(new GridView.LayoutParams(displayWidth/2,displayWidth/2));//(displayWidth/2 ,displayHeight/4));
	            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            //imageView.setPadding(2, 2, 2, 2);
	        } else {
	        	button = (Button) convertView;
	        }
	        
	       
	        
	        //button.setBottom(1);
	        button.setBackgroundDrawable(mTh);
	        button.setText(filesnames[position]);
	        button.setTextColor(Color.parseColor("white"));
	        button.setGravity(Gravity.LEFT);
	       
	        //gridview.getChildAt(1).setBackgroundColor(Color.RED);
	        return button;
	        
	        //imageView.setImageResource(mThumbIds[position]);
	        //return imageView;
	        
	    }
	    public String[] filesnames = {   
	            "File 1",   
	            "File 2",  
	            "Roflcopters"  
	            };  

	    private Drawable mTh = getResources().getDrawable(R.drawable.smoking2);
	    // references to our images
	    private Integer[] mThumbIds = {
	    		R.drawable.smoking2,  R.drawable.smoking2,
	    		 R.drawable.smoking2
	    };
	}   */

	
	
	