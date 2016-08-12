package com.example.lifestylerating;




import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FollowActivity extends Activity {
	private Intent pushIntent;
	private Display display;
	private int displayWidth;
	private int displayHeight;
	StringBuilder sb = new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follow);
		display = this.getWindowManager().getDefaultDisplay();
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		FollowActivity.this.callback();
		
	}

	@SuppressLint("NewApi")
	private void callback() {
		String intentParam = null;
		pushIntent = this.getIntent();
		Bundle intentBundle = pushIntent.getExtras();
		if (intentBundle == null)
		Log.d("TAG", "keine Extras im Push-Intent");
		else {
		intentParam = intentBundle.getString("RestToFollow");
		if(intentParam == null){
			Log.d("TAG", "No key RestToFollow");
			return;
		}
		else
			Log.d("TAG", " RestToFollow" + intentParam );
		}
		List<String> restList = Arrays.asList(intentParam.split(","));
		final int restSize=  restList.size(); 
		if (restSize==0)
				return;
		final RelativeLayout layout = (RelativeLayout)findViewById(R.id.FollowLayout);
		
		LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = (int) Math.round(displayHeight/40);
		TextView T1 = new TextView(getBaseContext());
		T1.setText("Hello. What interests you?");
		T1.setId(111);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		T1.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
		T1.setTextColor(Color.parseColor("black"));
		T1.setLayoutParams(params);
		layout.addView(T1);
		
		LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		TextView T2 = new TextView(getBaseContext());
		T2.setText("Pick some topics to follow.");
		T2.setId(112);
		params2.addRule(RelativeLayout.BELOW, 111 );
		params2.topMargin = (int) Math.round(displayHeight/80);
		T2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		T2.setTextColor(Color.parseColor("gray"));
		T2.setLayoutParams(params2);
		layout.addView(T2);
		
		 for(int i=0;i<restSize;i++)
		{
			LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,(int) Math.round(displayHeight/15));
			if(i==0)
			{
				params3.addRule(RelativeLayout.BELOW,112 );
				params3.topMargin = (int) Math.round(displayHeight/50);
			}
			else{
				
				//params3.addRule(RelativeLayout.BELOW, i );
				//params3.topMargin = 20;
				if(i==1) 
				{
					params3.addRule(RelativeLayout.RIGHT_OF, i );
					//params3.leftMargin = 10; 
					params3.addRule(RelativeLayout.BELOW,112 );
					params3.topMargin = (int) Math.round(displayHeight/50);
				}
				else if( i%2==0 && i!=0)
				{
					params3.addRule(RelativeLayout.BELOW, i-1 );
					params3.topMargin = (int) Math.round(displayHeight/50);
					 
				}
				else if( i%2==1)
				{
					params3.addRule(RelativeLayout.BELOW, i-1 );
					params3.addRule(RelativeLayout.RIGHT_OF, i );
					//params3.leftMargin = 10; 
					params3.topMargin = (int) Math.round(displayHeight/50);
				}
			}
			
			params3.leftMargin =(int) Math.round(displayHeight/30); 
			final Button Bt = new Button(this);
			Bt.setId(i+1);
			Bt.setBackgroundColor(Color.WHITE) ;
			Bt.setText(restList.get(i));
			Bt.setTag("1");
			Bt.setLayoutParams(params3);
			Bt.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
			Bt.setTextColor(Color.parseColor("gray"));
			Bt.setVerticalScrollBarEnabled(true);
			ShapeDrawable shapedrawable = new ShapeDrawable();
            shapedrawable.setShape(new RectShape());
            shapedrawable.getPaint().setColor(Color.parseColor("gray"));
            shapedrawable.getPaint().setStrokeWidth(10f);
            shapedrawable.getPaint().setStyle(Style.STROKE);     
            Bt.setBackground(shapedrawable);
			Bt.setOnTouchListener(new View.OnTouchListener() {          
			    @Override
			    public boolean onTouch(View view, MotionEvent event) {
			        switch (event.getAction())
			        {
			            case MotionEvent.ACTION_DOWN:  
			            	if(Bt.getTag()=="1")
			            	{
			                ((Button)view).setBackgroundColor(Color.RED);
			                Bt.setTextColor(Color.parseColor("white"));
			                Bt.setTag("2");
			            	}
			            	else
			            	{
			            		((Button)view).setBackgroundColor(Color.WHITE);
				                Bt.setTextColor(Color.parseColor("gray"));
				                ShapeDrawable shapedrawable = new ShapeDrawable();
				                shapedrawable.setShape(new RectShape());
				                shapedrawable.getPaint().setColor(Color.parseColor("gray"));
				                shapedrawable.getPaint().setStrokeWidth(10f);
				                shapedrawable.getPaint().setStyle(Style.STROKE);     
				                Bt.setBackground(shapedrawable);
				                Bt.setTag("1");
			            	}
			                break;
			            //case MotionEvent.ACTION_UP:
			                //((Button)view).setBackgroundColor(Color.TRANSPARENT);
			        }
			        return false;
			    }

				
			});
			layout.addView(Bt);
			
		    }
			
			LayoutParams params4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			//params.topMargin = 100;
			TextView T4 = new TextView(getBaseContext());
			T4.setText("Follow selected topics");
			T4.setId(114);
			params4.topMargin = (int) Math.round(displayHeight/40);
			params4.bottomMargin = (int) Math.round(displayHeight/40);
			params4.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			T4.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			T4.setTextColor(Color.parseColor("black"));
			T4.setLayoutParams(params4);
			layout.addView(T4);
			
			
			RelativeLayout.LayoutParams params5=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 5);
			params5.addRule(RelativeLayout.ABOVE,114 );
			View v = new View(this);
			v.setLayoutParams(params5);
			v.setBackgroundColor(Color.parseColor("#B3B3B3"));
            layout.addView(v);
		
            LayoutParams params6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,(int) Math.round(displayHeight/15));
			//params6.addRule(RelativeLayout.LEFT,114 );
			//params6.topMargin = 70;
            params6.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            Button Bt = new Button(this);
			Bt.setBackgroundColor(Color.BLUE) ;
			Bt.setText("Done");
			Bt.setLayoutParams(params6);
			Bt.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			Bt.setTextColor(Color.parseColor("white"));	
			layout.addView(Bt);
			Bt.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					int sw=0;
					for(int i=0;i<restSize;i++)
					{
						Button Bt=(Button) layout.getChildAt(i+2) ;
						CharSequence St = null ;
						if(Bt.getTag()=="2")
						{
							sw=1;
							St= Bt.getText();
							sb.append(St);
							if(i<restSize-1)
							sb.append(",");
						}
						
					}
					if(sw==0)
					sb.append("empty");
					pushIntent.putExtra("FollowList",sb.toString());
					
					FollowActivity.this.setResult(Activity.RESULT_OK, pushIntent);
					FollowActivity.this.finish();
					
				}
			});
		
		
		}
	    
	@Override
    public void onBackPressed() {
		sb.append("empty");
		pushIntent.putExtra("FollowList",sb.toString());
		
		FollowActivity.this.setResult(Activity.RESULT_OK, pushIntent);
		FollowActivity.this.finish();
             
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
		return super.onOptionsItemSelected(item);
	}
}
