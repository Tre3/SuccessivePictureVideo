package org.android.successivepicturevideo;

import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ImageView;

public class SuccessivePictureVideoThumbnail extends Activity implements SuccessivePictureVideoCallback{
	private static final int REQUEST_GALLERY = 0;
	private ImageView imgView;
	Bitmap mBitmap;
	Bitmap mBitmapResized;
	
	int metaTaskFlag;
	
	int imgViewWidth;  
	int imgViewHeight;
	
	SuccessivePictureVideoMetadataTask task;
	SuccessivePictureVideoSaveBitmap saveBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_successive_picture_video_thumbnail);

		imgView = (ImageView)findViewById(R.id.image_video_to_picture);
	
		// ギャラリー呼び出し
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_GALLERY);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		if (isFinishing()){
			if (task!=null){
				task.cancel(true);
				task = null;
			}
			metaTaskFlag = 0;
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		metaTaskFlag = 0;
	}
	
	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
        //アプリがシステムにキルされたかどうかを記録
        outState.putInt("metaTaskFlag", metaTaskFlag);
    }
	
	@Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) { 
		super.onRestoreInstanceState(savedInstanceState);
        //アプリがシステムにキルされたかどうかを取得
        metaTaskFlag = savedInstanceState.getInt("metaTaskFlag");
        
        if (metaTaskFlag == 1){
			if (task != null){
				task.cancel(true);
	            task = null;
			}
			Toast.makeText(this, "Task was canceled. Select a video again.", Toast.LENGTH_LONG).show();
		}
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && task != null) {
            task.cancel(true);
            task = null;
            metaTaskFlag = 0;
            
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.successive_picture_video_thumbnail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_save_thumbnail:
				try {
					saveBitmap= new SuccessivePictureVideoSaveBitmap(mBitmap, this);
					Toast.makeText(this, "The picture was saved." , Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(this, "Failed to save the picture." , Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;	
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			try {
				Uri video = data.getData();
				task = new SuccessivePictureVideoMetadataTask(this, this, this);
				task.execute(video);
				metaTaskFlag = 1;
				
			} catch (Exception e) {
				}
		}
		if(resultCode != RESULT_OK){
			super.onBackPressed();
		}
	}
	
	public void callback(final Bitmap resultBitmap) {
		if (resultBitmap != null){
			mBitmap = resultBitmap;
			int mBitmapWidth = mBitmap.getWidth();
			int mBitmapHeight = mBitmap.getHeight();
			imgViewWidth = findViewById(R.id.image_video_to_picture).getWidth();  
			imgViewHeight = findViewById(R.id.image_video_to_picture).getHeight();
			if (imgViewHeight < mBitmapHeight){
				mBitmapResized = Bitmap.createScaledBitmap(mBitmap, mBitmapWidth*imgViewHeight/mBitmapHeight, imgViewHeight, false);
				imgView.setImageBitmap(mBitmapResized);
			}
			else
				imgView.setImageBitmap(mBitmap);
			metaTaskFlag = 0;
		}
	} 
	
}
