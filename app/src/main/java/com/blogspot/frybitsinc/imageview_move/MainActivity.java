package com.blogspot.frybitsinc.imageview_move;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate;
    private int minValue =  -820;//right
    private int maxValue = 0;//left
    private int minY = -30;//down
    private int maxY = 0;//up

    private ImageView mDrawable;
    HorizontalScrollView mLayout;
    public static int x;
    public static int y;
    int acc_x = 0;
    //same for every image
    private double MARGIN_RATIO = 0.03;
    // DIFFERENT in every image
    private int IMAGE_WIDTH = 2128;
    private int IMAGE_HEIGHT = 1500;
    private int SCROLL_START = 500;
    // user input (DIFFERENT scroll speed vary in every android device)
    //0.5 , 1.0 , 1.5 , 2.0 , 2.5 (VERY SLOW, SLOW, MODERATE, FAST, VERY FAST)
    private double TIMES_FASTER = 2.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mLayout = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        mLayout.setScrollX(SCROLL_START);
        mDrawable = (ImageView) findViewById(R.id.kkori);

        //get display width, height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int pxWidth  = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;
        Log.d(TAG, "onCreate: displayMetrics.widthPixels  = "+displayMetrics.widthPixels);
        Log.d(TAG, "onCreate: displayMetrics.heightPixels = "+displayMetrics.heightPixels);

        //get image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        ////////////////////   put image name  ////////////////////
        BitmapFactory.decodeResource(getResources(), R.drawable.sunglasses_emoji, options);
        ///////////////////////////////////////////////////////////
        int img_width = options.outWidth;
        int img_height = options.outHeight;
        Log.d(TAG, "onCreate: BitmapDrawable img_height = "+img_height);
        Log.d(TAG, "onCreate: BitmapDrawable img_width  = "+img_width);
        IMAGE_WIDTH = img_width;
        IMAGE_HEIGHT = img_height;
        //resize to fit display
        float resizeW = (pxHeight*IMAGE_WIDTH)/IMAGE_HEIGHT;
        Log.d(TAG, "override: resizeW = "+resizeW);
        int resizeW_int = (int) resizeW;
        Log.d(TAG, "override: resizeW_int = "+resizeW_int);
        Log.d(TAG, "override: pxHeight= "+pxHeight);


        ////////////////////   put image name  ////////////////////
        Glide.with(this).load(R.drawable.sunglasses_emoji).override(resizeW_int, pxHeight).into(mDrawable);
        ///////////////////////////////////////////////////////////
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
//                mLayout.setLayoutParams(
//                    new ViewGroup.LayoutParams(
//                            // or ViewGroup.LayoutParams.WRAP_CONTENT
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT ) );
                Log.d(TAG, "override===============================================");
                Log.d(TAG, "override:getWidth = "+mDrawable.getWidth());
                Log.d(TAG, "override:getHeight = "+mDrawable.getHeight());

            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // X min max
//            Log.d(TAG, "onSensorChanged: x                         = "+x);
//            Log.d(TAG, "onSensorChanged: x - (int) event.values[0] = "+(x - (int) event.values[0]));
//            Log.d(TAG, "onSensorChanged: .............................................................");
//            if(x - (int) event.values[0] < minValue){
//                x = minValue;
//                Log.d(TAG, "onSensorChanged: ....    RIGHT END   ......");
//            }
//            else if(x - (int) event.values[0] > maxValue){
//                x = maxValue;
//                Log.d(TAG, "onSensorChanged: !!!!!!   LEFT END   !!!!!");
//            }
//            else {
//                x -= (int) event.values[0];
//                Log.d(TAG, "onSensorChanged:  --in  between --");
//            }
//            Log.d(TAG, "onSensorChanged: x                         = "+x);
//            Log.d(TAG, "onSensorChanged: x - (int) event.values[0] = "+(x - (int) event.values[0]));
//            mDrawable.setTranslationX((float) (x * (2.5)));
//            Log.d(TAG, "    getX: X = "+mDrawable.getX());
//            Log.d(TAG, "    getWidth    = " + mDrawable.getWidth());
//            Log.d(TAG, "onSensorChanged: --------------------------------------------------");

//            if(x - (int) event.values[0] < 0){
//                x = minValue;
//                Log.d(TAG, "onSensorChanged: ....    RIGHT END   ......");
//            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            int maxscroll = mLayout.getChildAt(0).getMeasuredWidth()-getWindowManager().getDefaultDisplay().getWidth();
            int margin = (int) (maxscroll * MARGIN_RATIO);
            Log.d(TAG, "onSensorChanged: margin = " + margin);
            Log.d(TAG, "onSensorChanged:  mLayout.getScrollX() = "+ mLayout.getScrollX());
            x -= (int) event.values[0];
            acc_x =  (int) (- (x * TIMES_FASTER));
            if(acc_x < margin) {
                x = (int)(- (margin / TIMES_FASTER));
                Log.d(TAG, "onSensorChanged: !!!!!!   LEFT END   !!!!!");
            }
            else if(acc_x > (maxscroll-margin)) {
                x =  (int)(- ( (maxscroll-margin) / TIMES_FASTER));
                Log.d(TAG, "onSensorChanged: ....    RIGHT END   ......");
            }
            else{
                mLayout.smoothScrollTo(acc_x, 0);
                Log.d(TAG, "onSensorChanged:  --in  between --");
            }

//            else{
//
//                //scroll n times faster
//                acc_x = -(x*TIMES_FASTER);
//                mLayout.smoothScrollTo(acc_x, 0);
//                Log.d(TAG, "onSensorChanged:  --in  between --");
//            }
            Log.d(TAG, "onSensorChanged: x      = " + x);
            Log.d(TAG, "onSensorChanged: acc_x  = " + acc_x);
////////////////////////////////////////////////////////////////////////

            // Y min max
//            y += (int) event.values[1];

//
//            if(y + (int) event.values[1] < minY){
//                y = minY;
//                Log.d(TAG, "onSensorChanged: ....    DOWN END   ......");
//            }
//            else if(y + (int) event.values[1] > maxY){
//                y = maxY;
//                Log.d(TAG, "onSensorChanged: !!!!!!   UP END   !!!!!");
//            }
//            else {
//                y += (int) event.values[1];
//                Log.d(TAG, "onSensorChanged:  --in  between --");
//            }
////            Log.d(TAG, "onSensorChanged: Y = "+y);
//            mDrawable.setTranslationY((float) (y * (1.5)));
            Log.d(TAG, "==============================================================");
            Log.d(TAG, "==============================================================");
        }
    }

}
