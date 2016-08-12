package com.example.lifestylerating;

//import com.example.lifestylerating.MyScrollView.OnScrollViewListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
//import android.widget.ScrollView;
import android.content.ClipDescription;

import static com.example.lifestylerating.MainActivity.count;
import static com.example.lifestylerating.MainActivity.orders;
import static com.example.lifestylerating.MainActivity.saveViews;
import static com.example.lifestylerating.MainActivity.IndexOfLabel;
import static com.example.lifestylerating.MainActivity.IndexOfOrder;

//import com.example.lifestylerating.MyScrollView;

@SuppressLint({ "NewApi", "ShowToast" })
public class ClickAndDrag extends Activity   implements ScrollViewListener{
	private RelativeLayout.LayoutParams layoutParams;
	String msg;
	public int displayHeight;
	public Button Bt;
	static int idB;
	static Button BtT;
	//public int[] orders;
	static RelativeLayout.LayoutParams ParamB;
    //ScrollView myScrollView;
	static int mScrollDistance=0;
	private ObservableScrollView scroll;
	private myDragEventListener mDragListen;
	public Runnable down_runnable;
	public Runnable up_runnable;

	public boolean isScrollRunning;
	public int buttonSize;
	static int scrollY;
	public RelativeLayout layout;
	public Context mContext;



	public ClickAndDrag(Context mContext, RelativeLayout Layout,Button Bt1,int BtSize,int disHeight,ObservableScrollView SV) {
		// TODO Auto-generated constructor stub
		this.layout=Layout;
		Bt=Bt1;
		this.displayHeight=disHeight;
		this.mContext=mContext;
        //myScrollView = SV;
		//this.orders=orders;
		buttonSize=BtSize;
		scroll = SV;
		scrollY=0;
		//context=con;
		/*down_runnable = new Runnable() {


			@Override
	        public void run() {
	            isScrollRunning = true;
	            scroll.smoothScrollBy(0, 2);
	            View down_scroll = null;
				down_scroll.postDelayed(this,50);
	        }
	    };

	    up_runnable = new Runnable() {
	        @Override
	        public void run() {
	            isScrollRunning = true;
	            scroll.smoothScrollBy(0, -2);
	            View up_scroll = null;
				up_scroll.postDelayed(this, 50);
	        }
	    };*/

        //scroll.setScrollViewListener(this);
	}

	public void UpdateActualOrderList(String ST)
	{
		for (int i=0;;i++){
			if(ST==MainActivity.labels[i]){
				MainActivity.actual[i]=0;
				return;
			}
		}
	}

	public void startDel(final ClickAndDrag BtD)
	{

		mDragListen = new myDragEventListener();
		Bt.setOnDragListener(mDragListen);


	}

	public void startClickListener(final ClickAndDrag BtD)
	{
		scroll.setScrollViewListener((ScrollViewListener) ClickAndDrag.this);
		mDragListen = new myDragEventListener();
		Bt.setOnDragListener(mDragListen);



		scroll.setOnDragListener(new OnDragListener() {
	        @Override
	        public boolean onDrag(View v, DragEvent event) {
	        	//Log.i("scroll drag Y",Float.toString( event.getY()));
	        	//scrollY=(int) event.getY();
	        	if ((event.getY() - (buttonSize/2)) <= 1) {
	                //isScrollRunning = true;
	                //scroll.postDelayed(down_runnable, 50);
	        		scroll.scrollBy(0, -buttonSize);
	        		MainActivity.BtDel.setY(displayHeight/2+scrollY-100);
	            }
	        	if ((event.getY() + (buttonSize)) >= displayHeight) {
	                //isScrollRunning = true;
	                //scroll.postDelayed(down_runnable, 50);
	        		scroll.scrollBy(0, buttonSize);
	        		MainActivity.BtDel.setY(displayHeight/2+scrollY-100);
	            }

	        	//Log.i("scroll drag heightPixels",Float.toString(v.get ));
		        //Log.i("scroll drag Y",Float.toString( event.getY()));
/*
 
				if (event.getY() > 0.8 * dm.heightPixels && !isScrollRunning) {
	                //isScrollRunning = true;
	                down_scroll.postDelayed(down_runnable, 50);
	            }
	            if (event.getY() < 0.8 * dm.heightPixels) {
	                isScrollRunning = false;
	                down_scroll.removeCallbacksAndMessages(null);
	            }

	            if (event.getY() < 0.1 * dm.heightPixels && !isScrollRunning) {
	                isScrollRunning = true;
	                up_scroll.postDelayed(up_runnable, 50);
	            }
	            if (event.getY() > 0.1 * dm.heightPixels) {
	                isScrollRunning = false;
	                up_scroll.removeCallbacksAndMessages(null);
	            }*/

	            return false;
	        }
	    });

	  Bt.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
        	 //AbsoluteLayout.LayoutParams paramsDel = new AbsoluteLayout.LayoutParams(40, 40, 100, 100);
        	 //MainActivity.BtDel.setLayoutParams(paramsDel);
        	 MainActivity.BtDel.setY(displayHeight/2+scrollY-100);
        	 //MainActivity.BtDel.setX(-40);
        	 if(Bt.getId()==MainActivity.count || Bt.getId()==100)
        		 return true;
        	 else MainActivity.BtDel.setVisibility(View.VISIBLE);

        	 ClipData data = ClipData.newPlainText("", "");
             View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(Bt);
             idB = (Integer) Bt.getId();
             ParamB=(LayoutParams) Bt.getLayoutParams();
             BtT=Bt;
             //buttonSize=v.getWidth();
             Bt.startDrag(data, shadowBuilder, Bt, 0);
             //BtD.dragStarer();
             Bt.setVisibility(View.INVISIBLE);

             return true;
         }
      });
      
	 /*Bt.setOnTouchListener(
 	        new View.OnTouchListener() {
 	            public boolean onTouch(View myView, MotionEvent event) {
 	                int action = event.getAction();
 	                if (action==MotionEvent.ACTION_MOVE)
 	                {
 	                    //myView.setBackgroundColor(Color.BLUE);
 	                	return true;
 	                }
 	               //MotionEvent.BUTTON_BACK
 	                if (action==MotionEvent.ACTION_UP )
 	                {
 	                    //myView.setBackgroundColor(Color.RED);
 	                	myView.setVisibility(View.VISIBLE);
 	                	return true;
 	                }
 	                if (action==MotionEvent.ACTION_DOWN)
 	                {
 	                    //myView.setBackgroundColor(Color.YELLOW);
 	                   ClipData data = ClipData.newPlainText("", "");
 	                   View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(Bt);
 	                   idB = (Integer) Bt.getId();
 	                   ParamB=(LayoutParams) Bt.getLayoutParams();
 	                   BtT=Bt;
 	                   //
 	                   Bt.startDrag(data, shadowBuilder, Bt, 0);
 	                   //BtD.dragStarer();
 	                   Bt.setVisibility(View.INVISIBLE); 
 	                  //ClickAndDrag.BtT.setVisibility(View.VISIBLE);
 	                  
 	                   return true;
 	                }
 	                // TODO Auto-generated method stub
 	                return true;
 	            }
 	        }); */

	}




	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		//if(scrollView == scroll) 
		//Log.i("scroll Y",Float.toString( y));
		scrollY =y;

	}
  protected class myDragEventListener implements View.OnDragListener {

	  // This is the method that the system calls when it dispatches a drag event to the
	  // listener.


	  public boolean onDrag(View v, DragEvent event) {

		  // Defines a variable to store the action type for the incoming event
		  int IdB = (Integer) v.getId();
		  //Log.i("Button id",Integer.toString(IdB));
		  final int action = event.getAction();
		  //Log.i("dragX",Float.toString(event.getX() ));
		  //Log.i("dragY",Float.toString( event.getY()));
		  // Handles each of the expected events
		  switch (action) {

			  case DragEvent.ACTION_DRAG_STARTED:

				  // Determines if this View can accept the dragged data
				  if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {


					  v.invalidate();

					  // returns true to indicate that the View can accept the dragged data.
					  return true;

				  }

				  // Returns false. During the current drag and drop operation, this View will
				  // not receive events again until ACTION_DRAG_ENDED is sent.
				  return false;

			  case DragEvent.ACTION_DRAG_ENTERED:

				  // Applies a green tint to the View. Return true; the return value is ignored.

				  //v.setColorFilter(Color.GREEN);

				  // Invalidate the view to force a redraw in the new tint

				  v.invalidate();

				  return true;

			  case DragEvent.ACTION_DRAG_LOCATION:

				  RelativeLayout.LayoutParams ParamV = (LayoutParams) v.getLayoutParams();
				  int idV = (Integer) v.getId();
				  if (idV == MainActivity.count)
					  return true;
				  if (idV == 100) {
					  MainActivity.BtDel.setBackgroundResource(R.drawable.rounded);
					  return true;
				  }
				  BtT.setLayoutParams(ParamV);
				  BtT.setId(idV);
				  String t1 = (String) BtT.getText();
				  int index1 = IndexOfOrder(IndexOfLabel(t1));
				  String t2=(String) Bt.getText();
				  int index2 = IndexOfOrder(IndexOfLabel(t2));
				  v.setLayoutParams(ParamB);
				  v.setId(idB);
				  ParamB = ParamV;
				  idB = idV;
				  int temp=orders[index1];
				  orders[index1]=orders[index2];
				  orders[index2]=temp;
				  saveViews();
				  return true;
		  //break;

		  case DragEvent.ACTION_DRAG_EXITED:

		  // Re-sets the color tint to blue. Returns true; the return value is ignored.
		  //v.setColorFilter(Color.BLUE);

		  // Invalidate the view to force a redraw in the new tint
		  v.invalidate();

		  if (v.getId() == 100) {
			  MainActivity.BtDel.setBackgroundResource(R.drawable.roundedbutton);

			  return true;
		  }
		  return true;

		  case DragEvent.ACTION_DROP:


		  v.invalidate();
		  // Log.i("Button drop",Integer.toString(Bt.getId()));

		  // Returns true. DragEvent.getResult() will return true.
		  if (v.getId() == 100) {
			  MainActivity.BtDel.setBackgroundResource(R.drawable.roundedbutton);
			  Vibrator Vib = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
			  Vib.vibrate(500);
			  //int BtTid=BtT.getId();
			  //int BtTid=idB;

			  RelativeLayout.LayoutParams params2;
			  int id2;
			  int id1 = idB;
			  RelativeLayout.LayoutParams params1 = ParamB;
			  Button B = (Button) layout.findViewById(idB);
			  String ST = (String)B.getText();
			  UpdateActualOrderList((String) ST);
			  int ind  = IndexOfOrder(IndexOfLabel(ST));
			  for(int i=ind;i<count-1;i++)
			  orders[i]=orders[i+1];
			  orders[count-1]=0;
			  layout.removeView(layout.findViewById(idB));
			  for (int i = idB; i < MainActivity.count; i++) {

				  if (i < MainActivity.count) {
					  id2 = i + 1;
					  params2 = (RelativeLayout.LayoutParams) layout.findViewById(id2).getLayoutParams();

					  layout.findViewById(id2).setLayoutParams(params1);
					  layout.findViewById(id2).setId(id2 - 1);
					  params1 = params2;
					  id1 = id2;
				  }


			  }//end for


			  MainActivity.count = MainActivity.count - 1;
			  saveViews();

			  return true;
		  }
		  return true;

		  case DragEvent.ACTION_DRAG_ENDED:

		  // Turns off any color tinting
		  //v.clearColorFilter();

		  // Invalidates the view to force a redraw
		  //Log.i("Button drag end",Integer.toString(Bt.getId()));
		  if (v.getId() == 100) {
			  Bt.setVisibility(View.INVISIBLE);
			  return true;

		  }
		  int iidd = v.getId();
		  v.invalidate();
		  if (v.getId() == MainActivity.count)
			  v.post(new Runnable() {
				  public void run() {
					  BtT.setVisibility(View.VISIBLE);
				  }
			  });
		  // BtT.setVisibility(View.VISIBLE);
		  // Does a getResult(), and displays what happened.
		  if (event.getResult()) {
			  //Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG);

		  } else {
			  //Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG);

		  }

		  // returns true; the value is ignored.

		  return true;

		  // An unknown action type was received.
		  default:
		  Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
		  break;
	  }

	        return false;
	    }


	};

 } // end of class
 

/*
public void dragStarer()
{
	
	
	 Bt.setOnDragListener(new View.OnDragListener() {

		
		public boolean onDrag(View v, DragEvent event) {
			// TODO Auto-generated method stub
			scroll.scrollTo(0, 700);
			return false;
		}
	 });
} // end of dragStarter */

			 /*
	         @Override
	         public boolean onDrag(final View v, DragEvent event) {
	            //Button dropZoneView = (Button) v;
	            //OnScrollChangeListener l;
				//v.setOnScrollChangeListener(l);
	        
	            switch(event.getAction())
	            {
	               case DragEvent.ACTION_DRAG_STARTED:
		               layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
		               Log.d("msg", "Action is DragEvent.ACTION_DRAG_STARTED");
		               
		               
		               // Do nothing
	               break;
	               
	               case DragEvent.ACTION_DRAG_ENTERED:
		               Log.d("msg", "Action is DragEvent.ACTION_DRAG_ENTERED");
		               int x_cord = (int) event.getX();
		               int y_cord = (int) event.getY();
	               break;
	               
	               case DragEvent.ACTION_DRAG_EXITED :
		               Log.d("msg", "Action is DragEvent.ACTION_DRAG_EXITED");
		               x_cord = (int) event.getX();
		               y_cord = (int) event.getY();
		               layoutParams.leftMargin = x_cord;
		               layoutParams.topMargin = y_cord;
		               
		               //v.setLayoutParams(layoutParams);
		               break;
		               
		           case DragEvent.ACTION_DRAG_LOCATION  :
		               Log.d("msg", "Action is DragEvent.ACTION_DRAG_LOCATION");
		               //myScrollView.scrollTo(1200, 100);
		               x_cord = (int) event.getX();
		               y_cord = (int) event.getY();
		               int y = Math.round(event.getY());
		               int translatedY = y_cord - mScrollDistance;
		               int threshold = 50;
		               scroll.scrollTo(0, 700);
		               scroll.scrollTo(0,800);
		               // make a scrolling up due the y has passed the threshold
		              
		              
	               break;
	               
	               case DragEvent.ACTION_DROP:
		               Log.d("msg", "ACTION_DROP event");
		               
		               //myScrollView.scrollTo(200, 100);
	               break;
	               default: break;
	            }
	            return true;
	         }

		*/
	
