package org.android.successivepicturevideo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class SuccessivePictureVideoMetadataTask extends AsyncTask<Uri, Integer, Bundle>{
	private Context mContext;
	private Activity mActivity;
	private ProgressDialog mProgress;
	
	Bitmap canvasBmp;
	
	SharedPreferences preference;
	
	int frameInterval;
	int frameNumber;
	int frameRotation;
	int frameNumberHorizontal;
	int frameNumberVertical;
	
	int canvasWidth;
	int canvasHeight;
	
	int mCheckFinishedFrame;
	
	float scale;
	
	int trimmingSizeHorizontal;
	int trimmingSizeVertical;
	
    private SuccessivePictureVideoCallback callback; 
	
	public SuccessivePictureVideoMetadataTask(Context context, Activity activity ,SuccessivePictureVideoCallback callback) {
        // 呼び出し元のコンテキストを変数へセット
        this.mContext = context;
        this.mActivity = activity;
        this.callback = callback;
        preference = mContext.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        frameInterval = preference.getInt("frameInterval", 20000);
		frameNumber = preference.getInt("frameNumber", 25);
		frameRotation = preference.getInt("frameRotation", 90);
		frameNumberHorizontal = preference.getInt("frameNumberHorizontal", 5);
		frameNumberVertical = preference.getInt("frameNumberVertical", 5);
		trimmingSizeHorizontal = preference.getInt("trimmingSizeHorizontal", 50);
		trimmingSizeVertical = preference.getInt("trimmingSizeVertical", 100);
    }

	@Override
    protected void onPreExecute() {
    	mProgress = new ProgressDialog(mContext);
    	mProgress.setMessage("Image generating...");
    	mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	mProgress.setCanceledOnTouchOutside(false);
    	mProgress.setIndeterminate(false);
    	mProgress.setMax(frameNumber);
        mProgress.setProgress(0);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            	SuccessivePictureVideoMetadataTask.this.cancel(true);
            	mActivity.onBackPressed();
            }
        });
        mProgress.show();
    }
	
	@Override
    protected Bundle doInBackground(Uri... params) {
        Uri video = params[0];
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext, video);
        
        Bitmap frame = retriever.getFrameAtTime(0);
        
        int width = frame.getWidth();
        int height = frame.getHeight();
        
		if ((width >= height) && ((frameRotation == 0) || (frameRotation == 180))){
			canvasWidth = width;
			canvasHeight = height*trimmingSizeVertical*canvasWidth*frameNumberVertical/width/trimmingSizeHorizontal/frameNumberHorizontal;
			scale = (float)canvasWidth*100/frameNumberHorizontal/width/trimmingSizeHorizontal;
		}
		else if ((width >= height) && ((frameRotation == 90) || (frameRotation == 270))){
			canvasWidth = width;
			canvasHeight = width*trimmingSizeHorizontal*canvasWidth*frameNumberVertical/height/trimmingSizeVertical/frameNumberHorizontal;
			scale = (float)canvasWidth*100/frameNumberHorizontal/height/trimmingSizeVertical;
		}
		else if ((width < height) && ((frameRotation == 0) || (frameRotation == 180))){
			canvasWidth = height;
			canvasHeight = height*trimmingSizeVertical*canvasWidth*frameNumberVertical/width/trimmingSizeHorizontal/frameNumberHorizontal;
			scale = (float)canvasWidth*100/frameNumberHorizontal/width/trimmingSizeHorizontal;
		}
		else if ((width < height) && ((frameRotation == 90) || (frameRotation == 270))){
			canvasWidth = height;
			canvasHeight = width*trimmingSizeHorizontal*canvasWidth*frameNumberVertical/height/trimmingSizeVertical/frameNumberHorizontal;
			scale = (float)canvasWidth*100/frameNumberHorizontal/height/trimmingSizeVertical;
		}
        
		canvasBmp = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
		
		Canvas offScreen = new Canvas(canvasBmp);
		
		//キャンバスの大きさ確認用
		//現在は確認用に赤い色にしているが、透明に設定する予定
        offScreen.drawColor(Color.RED);
        
        Bundle finishedImage = new Bundle();
        finishedImage.putParcelable("finishedImage", canvasBmp);
		
		for (int i = 0; i < frameNumber; i++) {
        	if (isCancelled() != true){
				Bitmap bmp1 = retriever.getFrameAtTime(frameInterval*i, MediaMetadataRetriever.OPTION_CLOSEST);
				if (bmp1==null){
					mCheckFinishedFrame = i+1;
					return finishedImage;
				}
			  	int sizeWidth = width*trimmingSizeHorizontal/100;
				int sizeHeight = height*trimmingSizeVertical/100;
			  	Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
			  	matrix.postRotate(frameRotation, scale/2, scale/2);
			  	Bitmap bmp2 = Bitmap.createBitmap(bmp1, (width-sizeWidth)/2, (height-sizeHeight)/2, sizeWidth, sizeHeight, matrix, true);
			  	
			  	int rowImages = i/frameNumberHorizontal;
			  	int lineImages = i%frameNumberHorizontal;
			  	
			  	offScreen.drawBitmap(bmp2, (float)lineImages*(canvasWidth/frameNumberHorizontal), (float)rowImages*(canvasHeight/frameNumberVertical), null);
			  	mCheckFinishedFrame = i+1;
			  	publishProgress(Integer.valueOf(i+1));
        	}
        }
		
        finishedImage.putParcelable("finishedImage", canvasBmp);
        
        return finishedImage;
    }
	
	@Override
    protected void onProgressUpdate(Integer... progress){
    	mProgress.setProgress(progress[0]);
    }
    
    @Override
    protected void onCancelled() {
    	if (mProgress !=  null && mProgress.isShowing() ) {
            mProgress.dismiss();
        }
        super.onCancelled();
    }
    
    @Override
    protected void onPostExecute(Bundle finishedImage) {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
        if (mCheckFinishedFrame < frameNumber)
        	Toast.makeText(mContext, "The video is too short.", Toast.LENGTH_LONG).show();
        Bitmap pastedImage = finishedImage.getParcelable("finishedImage");
        callback.callback(pastedImage);
    }
}