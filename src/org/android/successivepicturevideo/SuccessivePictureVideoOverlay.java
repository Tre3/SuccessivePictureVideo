package org.android.successivepicturevideo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class SuccessivePictureVideoOverlay extends Service {
	View view;
	ImageView imgViewOverlay;
    WindowManager wm;
    
    int imgViewWidth;
    int imgViewHeight;
    
    int trimmingSizeHorizontal;
	int trimmingSizeVertical;
    
    SharedPreferences preference;
	
	@Override
    public int onStartCommand(Intent intent, int flag, int startId) {
		super.onStartCommand(intent, flag, startId);
		
		if (intent == null){
			return START_STICKY;
		}
		
        makeOverlayScreen();
        
		return START_STICKY;   
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		
		wm.removeView(view);
		
		makeOverlayScreen();
	}
 
    @Override
    public void onCreate() {
        super.onCreate();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
         
        //wm.removeView(imgViewOverlay);
        wm.removeView(view);
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    void makeOverlayScreen(){
    	LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
		
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        
        view = layoutInflater.inflate(R.layout.successive_picture_video_overlay, null);
        
        imgViewOverlay = (ImageView) view.findViewById(R.id.imageViewOverlay);
		
		Display disp = wm.getDefaultDisplay();
	        
        Point point = new Point();  
        // Display情報からサイズを取得する。  
        disp .getSize(point);
        
        imgViewWidth = point.x;
        imgViewHeight = point.y;
        
        preference = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        
        trimmingSizeHorizontal = preference.getInt("trimmingSizeHorizontal", 50);
		trimmingSizeVertical = preference.getInt("trimmingSizeVertical", 100);
		
		Bitmap bitmap = Bitmap.createBitmap(imgViewWidth*trimmingSizeHorizontal/100, imgViewHeight*trimmingSizeVertical/100, Bitmap.Config.ARGB_8888);
		
		Paint paint = new Paint();
		paint.setColor(Color.argb(127, 255, 255, 0));
		Canvas canvas;
		canvas = new Canvas(bitmap);
        
		for (int i=0; i<10; i++){
	        canvas.drawLine(0, i, imgViewWidth*trimmingSizeHorizontal/100-1, i, paint);
	        canvas.drawLine(i, 0, i, imgViewHeight*trimmingSizeVertical/100-1, paint);
	        canvas.drawLine(0, imgViewHeight*trimmingSizeVertical/100-1-i, imgViewWidth*trimmingSizeHorizontal/100-1, imgViewHeight*trimmingSizeVertical/100-1-i, paint);
	        canvas.drawLine(imgViewWidth*trimmingSizeHorizontal/100-1-i, 0, imgViewWidth*trimmingSizeHorizontal/100-1-i, imgViewHeight*trimmingSizeVertical/100-1, paint);
		}
		
		imgViewOverlay.setImageBitmap(bitmap);
		
        wm.addView(view, params);
    }
}
