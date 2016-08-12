package com.example.lifestylerating;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class smoking extends Activity implements EditProjectDialogFragment.EditDialogListener {
    private Intent pushIntent;
    String title;
    private Display display;
    private int displayWidth;
    private int displayHeight;
    private Button BtPlus;
    private ImageButton BtEdit;
    private RelativeLayout layout;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    static int Max;
    private RelativeLayout layoutImage;
    static int counter;
    private TextView recordTextView;
    public int buttonSizeWidth;
    public int buttonSizeHeight;
    public FileSaveRead fileMax;
    public FileSaveRead fileCount;
    private ScrollView scroll;
    private RegisterHelper helper;
    private SQLiteDatabase registerDB;
    TextView progressText;
    int newCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoking);
        fileCount = new FileSaveRead("fileCounter");
        fileMax= new FileSaveRead("fileMaximum");
        display = this.getWindowManager().getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
        buttonSizeWidth=(displayWidth-80)/4;
        buttonSizeHeight=(displayWidth-80)/4;
        scroll = (ScrollView) findViewById(R.id.scrollImg);
        layout = (RelativeLayout) findViewById(R.id.SmokeLayout);
        layoutImage = (RelativeLayout) findViewById(R.id.ImgLayout);
        recordTextView= (TextView) findViewById(R.id.record);
        pushIntent = this.getIntent();
        Bundle intentBundle = pushIntent.getExtras();
        title=intentBundle.getString("title");
        if(helper == null)
            helper = new RegisterHelper(this, title);
        openDB("onCreate");
        getActionBar().setTitle(title);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#64D668")));
        //getActionBar().setLogo(android.R.color.transparent);
        if(title.equals("Smoking"))
        getActionBar().setLogo(R.drawable.smoking2);
        initMaxCount(savedInstanceState);

        ActivePlusSign();

        BtEdit = (ImageButton) findViewById(R.id.editBt);
        BtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditClick();
            }
        });

        Button BtDone = (Button) findViewById(R.id.btDone);
        BtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInDB();
                smoking.this.finish();
            }
        });

        Button BtDiagram = (Button) findViewById(R.id.btStatistic);
        BtDiagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawDiagram();

            }
        });


    }

    private void openDB(String callingMethod) {
        if(helper == null)
            helper = new RegisterHelper(this, title);
        if(registerDB == null || !registerDB.isOpen()) {
            registerDB = helper.getWritableDatabase();
            Log.d("TAG", "Datenbank in \"" + callingMethod
                    + "\" geoeffnet.");
        }
    }

    private void drawDiagram() {



    }

    private void saveInDB() {
        SQLiteDatabase registerDB;
        //if(registerDB == null || !registerDB.isOpen())
        registerDB = helper.getWritableDatabase();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        int JD = Time.getJulianDay((new Date()).getTime(), 0);
        if(helper.maxDate(registerDB)<JD)
            helper.insert(registerDB,JD,counter);
        else if(helper.maxDate(registerDB)==JD)
            helper.upDate(registerDB, JD, counter);
        registerDB.close();

    }

    private void ProgressBar(int count)
    {
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setProgress((int) (count*100)/Max);
        // Start lengthy operation in a background thread
        /*new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus = doWork();

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();*/
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    void initMaxCount(Bundle savedInstanceState){
        if (fileMax.file.exists()) {
            //Do somehting
            try {
                Max = Integer.parseInt(fileMax.read());
                if(Max<=0)
                    Max=4;

            } catch (Exception e) {
                Log.e("TAG", e.toString());
            }

        } else {
            // Do something else.
            Max=4;
            initProgressBar();
        }
        if (fileCount.file.exists()) {
            //Do somehting
            try {
                newCounter = Integer.parseInt(fileCount.read());
                if(newCounter< 0)
                    newCounter=0;
                resetLayout();

            } catch (Exception e) {
                Log.e("TAG", e.toString());
            }

        } else {
            // Do something else.
            counter=0;
        }
        /*if(savedInstanceState != null) {
            Max = savedInstanceState.getInt("max");
            newCounter= savedInstanceState.getInt("count");
            resetLayout();

        }else{
            Max = 4;
            counter=0;
        }*/

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void initProgressBar(){
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setProgress(0);
        progressText= (TextView) findViewById(R.id.T2);
        //StringBuilder sb = new StringBuilder();
        String txt= "0/"+ Max;
        progressText.setText(txt);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void ActivePlusSign()
    {
        AbsoluteLayout.LayoutParams paramsPlus = new AbsoluteLayout.LayoutParams(40, 40, 0, 100);

        BtPlus = new Button(this);
        paramsPlus.width = displayWidth/10;
        paramsPlus.height = displayWidth/10;
        BtPlus.setId(100);
        BtPlus.setLayoutParams(paramsPlus);
        //BtDel.setBackgroundResource(R.id.circle); //(android.R.drawable.ic_menu_delete);
        BtPlus.setBackgroundResource(R.drawable.plus);
        layout.addView(BtPlus);
        BtPlus.setY(displayHeight - (displayHeight / 3));
        BtPlus.setX(displayWidth / 2 - (displayWidth / 10));
        BtPlus.setVisibility(View.VISIBLE);
        BtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusClick();
            }
        });

    }
    void EditClick(){
        /*AlertDialog alertDialog = new AlertDialog.Builder(smoking.this).create();
        //alertDialog.setTitle("Alert");
        alertDialog.setMessage("There is no more topic to follow");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();*/
        DialogFragment  dialog;
        android.app.FragmentManager fragmentManager;
        dialog =  EditProjectDialogFragment.newInstance("Today Smoked:", "Maximum Allowed:", counter, Max);
        fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "editProjectTag");

    }

    void PlusClick(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(buttonSizeWidth,
                buttonSizeHeight);
        Button Bt = new Button(this);
        Bt.setId(counter + 1);
        if(counter==0)
        {
            recordTextView.setVisibility(View.GONE);

        }
        else if (counter == 1 || counter == 2 || counter == 3) {
            params.addRule(RelativeLayout.RIGHT_OF, counter);

            params.leftMargin = 2;
        } else if (counter % 4 == 0 && counter != 0) {
            params.addRule(RelativeLayout.BELOW, counter - 3);


        } else if (counter % 4 !=0) {
            params.addRule(RelativeLayout.BELOW, counter - 3);
            params.addRule(RelativeLayout.RIGHT_OF, counter);
            params.leftMargin = 2;

        }
        params.topMargin = 10;
        //params.addRule(RelativeLayout.ABOVE , 100);
        params.addRule(RelativeLayout.ABOVE, 100);

        Bt.setLayoutParams(params);
        //Bt.setBackgroundColor(R.color.gray);
        Bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.cigarette));
        layoutImage.addView(Bt);
        counter++;
        ProgressBar(counter);
        String txt= counter + "/"+ Max;
        progressText.setText(txt);
        if(counter>Max)
            progressText.setTextColor(Color.parseColor("red"));
        else progressText.setTextColor(Color.parseColor("#64D668"));
        if(counter % 4 == 1 && counter != 5 && counter != 1 )
        scroll.post(new Runnable() {
            public void run() {
                    scroll.scrollBy(0, buttonSizeHeight+10);
            }
        });
        fileCount.saveInFile(Integer.toString(counter));
        fileMax.saveInFile(Integer.toString(Max));
    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // TODO Auto-generated method stub
        String output = "";
        if (dialog instanceof EditProjectDialogFragment)
        {
            fileCount.saveInFile(Integer.toString(counter));
            fileMax.saveInFile(Integer.toString(Max));
            EditProjectDialogFragment fragment =(EditProjectDialogFragment) dialog;
            int oldMax=Max;
            Max=Integer.parseInt(fragment.MaxToAdd);
            newCounter=Integer.parseInt(fragment.countDescription);
            resetLayout();

        }


        // TODO else-Fall: SelectProjectDialogFragment
        dialog.dismiss();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // TODO Auto-generated method stub


    }
    void resetLayout(){
        counter=0;
        layoutImage.removeAllViews();
        initProgressBar();
        for(int i=0;i<newCounter;i++)
        PlusClick();

    }

    @Override
    protected void onSaveInstanceState(Bundle frozenState) {
        frozenState.putInt("max", Max);
        frozenState.putInt("count", counter);
        // etc. until you have everything important stored in the bundle
    }
    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        //fileCount.saveInFile(Integer.toString(counter));
        //fileMax.saveInFile(Integer.toString(Max));
        saveInDB();
        smoking.this.finish();

    }
}


