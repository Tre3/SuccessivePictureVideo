package org.android.successivepicturevideo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

public class SuccessivePictureVideo extends Activity 
		implements DialogInterface.OnClickListener{
	
	private static final String[] SETTINGS = {
		"Frame Interval",
		"Frame Number",
		"Frame Rotation",
		"Horizontal and Vertical Number",
		"Trimming"
	};
	
	private static final String[] SETTINGS_FRAME_ROTATION = {
		"0",
		"90",
		"180",
		"270"
	};
	
	private NumberPicker mNumberPickerSixDigits,mNumberPickerFiveDigits,mNumberPickerQuadrupleDigits,mNumberPickerTripleDigits,mNumberPickerDoubleDigits,mNumberPickerSingleDigit;
	private NumberPicker mNumberPicker6,mNumberPicker7,mNumberPicker8,mNumberPicker9,mNumberPicker10;
	private AlertDialog mActionsSettings, mActionsFrameInterval, mActionsFrameNumber, mActionsFrameRotation, mActionsHorizontalAndVerticalNumber, mActionsTrimming;
	
	SharedPreferences preference;
	int frameIntervalSixDigits;
	int frameIntervalFiveDigits;
	int frameIntervalQuadrupleDigits;
	int frameIntervalTripleDigits;
	int frameIntervalDoubleDigits;
	int frameIntervalSingleDigit;
	int frameInterval;
	int frameNumber;
	int frameRotation;
	int frameNumberHorizontal;
	int frameNumberVertical;
	int trimmingSizeHorizontal;
	int trimmingSizeVertical;
	int checkMetadataTask;
	
	private int mCheckedItem = 0;
	private int mCheckedItemTemp = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_successive_picture_video);
		
		//shared preferences
		preference = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
		preference.edit()
		          .putInt("frameIntervalSixDigits", 0)
		          .putInt("frameIntervalFiveDigits", 2)
		          .putInt("frameIntervalQuadrupleDigits", 0)
		          .putInt("frameIntervalTripleDigits", 0)
		          .putInt("frameIntervalDoubleDigits", 0)
		          .putInt("frameIntervalSingleDigit", 0)
		          .putInt("frameInterval", 20000)
		          .putInt("frameNumber", 25)
		          .putInt("frameRotation", 90)
		          .putInt("frameNumberHorizontal", 5)
		          .putInt("frameNumberVertical", 5)
		          .putInt("trimmingSizeHorizontal", 50)
		          .putInt("trimmingSizeVertical", 100)
		          .putInt("checkMetadataTask", 0)
		          .commit();
		frameIntervalSixDigits = preference.getInt("frameIntervalSixDigits", 0);
		frameIntervalFiveDigits = preference.getInt("frameIntervalFiveDigits", 2);
		frameIntervalQuadrupleDigits = preference.getInt("frameIntervalQuadrupleDigits", 0);
		frameIntervalTripleDigits = preference.getInt("frameIntervalTripleDigits", 0);
		frameIntervalDoubleDigits = preference.getInt("frameIntervalDoubleDigits", 0);
		frameIntervalSingleDigit = preference.getInt("frameIntervalSingleDigit", 0);
		frameInterval = preference.getInt("frameInterval", 20000);
		frameNumber = preference.getInt("frameNumber", 25);
		frameRotation = preference.getInt("frameRotation", 90);
		frameNumberHorizontal = preference.getInt("frameNumberHorizontal", 5);
		frameNumberVertical = preference.getInt("frameNumberVertical", 5);
		trimmingSizeHorizontal = preference.getInt("trimmingSizeHorizontal", 50);
		trimmingSizeVertical = preference.getInt("trimmingSizeVertical", 100);
		checkMetadataTask = preference.getInt("checkMetadataTask", 0);
		
		AlertDialog.Builder builderSettings = new AlertDialog.Builder(this);
		builderSettings.setTitle("Select Setting Item");
		builderSettings.setItems(SETTINGS, this);
		builderSettings.setNegativeButton("Close", null);
		mActionsSettings = builderSettings.create();
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.activity_successive_picture_video_numberpicker, null);
		mNumberPickerSixDigits = (NumberPicker) view.findViewById(R.id.numberPickerSixDigits);
        mNumberPickerSixDigits.setMaxValue(9);
        mNumberPickerSixDigits.setValue(frameIntervalSixDigits);
        mNumberPickerFiveDigits = (NumberPicker) view.findViewById(R.id.numberPickerFiveDigits);
        mNumberPickerFiveDigits.setMaxValue(9);
        mNumberPickerFiveDigits.setValue(frameIntervalFiveDigits);
        mNumberPickerQuadrupleDigits = (NumberPicker) view.findViewById(R.id.numberPickerQuadrupleDigits);
        mNumberPickerQuadrupleDigits.setMaxValue(9);
        mNumberPickerQuadrupleDigits.setValue(frameIntervalQuadrupleDigits);
        mNumberPickerTripleDigits = (NumberPicker) view.findViewById(R.id.numberPickerTripleDigits);
        mNumberPickerTripleDigits.setMaxValue(9);
        mNumberPickerTripleDigits.setValue(frameIntervalTripleDigits);
        mNumberPickerDoubleDigits = (NumberPicker) view.findViewById(R.id.numberPickerDoubleDigits);
        mNumberPickerDoubleDigits.setMaxValue(9);
        mNumberPickerDoubleDigits.setValue(frameIntervalDoubleDigits);
        mNumberPickerSingleDigit = (NumberPicker) view.findViewById(R.id.numberPickerSingleDigit);
        mNumberPickerSingleDigit.setMaxValue(9);
        mNumberPickerSingleDigit.setValue(frameIntervalSingleDigit);
        
        frameIntervalSixDigits = mNumberPickerSixDigits.getValue();
    	frameIntervalFiveDigits = mNumberPickerFiveDigits.getValue();
    	frameIntervalQuadrupleDigits = mNumberPickerQuadrupleDigits.getValue();
    	frameIntervalTripleDigits = mNumberPickerTripleDigits.getValue();
    	frameIntervalDoubleDigits = mNumberPickerDoubleDigits.getValue();
    	frameIntervalSingleDigit = mNumberPickerSingleDigit.getValue();
		
		AlertDialog.Builder builderFrameInterval = new AlertDialog.Builder(this);
		builderFrameInterval.setTitle("Set Frame Interval");
		builderFrameInterval.setView(view);
		builderFrameInterval.setPositiveButton(
		          "OK", 
		          new DialogInterface.OnClickListener() {          
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	frameIntervalSixDigits = mNumberPickerSixDigits.getValue();
		            	frameIntervalFiveDigits = mNumberPickerFiveDigits.getValue();
		            	frameIntervalQuadrupleDigits = mNumberPickerQuadrupleDigits.getValue();
		            	frameIntervalTripleDigits = mNumberPickerTripleDigits.getValue();
		            	frameIntervalDoubleDigits = mNumberPickerDoubleDigits.getValue();
		            	frameIntervalSingleDigit = mNumberPickerSingleDigit.getValue();
		            	frameInterval = 0;
		            	frameInterval += frameIntervalSingleDigit;
		            	frameInterval += 10*frameIntervalDoubleDigits;
		            	frameInterval += 100*frameIntervalTripleDigits;
		            	frameInterval += 1000*frameIntervalQuadrupleDigits;
		            	frameInterval += 10000*frameIntervalFiveDigits;
		            	frameInterval += 100000*frameIntervalSixDigits;
		            	preference.edit()
				          .putInt("frameIntervalSixDigits", frameIntervalSixDigits)
				          .putInt("frameIntervalFiveDigits", frameIntervalFiveDigits)
				          .putInt("frameIntervalQuadrupleDigits", frameIntervalQuadrupleDigits)
				          .putInt("frameIntervalTripleDigits", frameIntervalTripleDigits)
				          .putInt("frameIntervalDoubleDigits", frameIntervalDoubleDigits)
				          .putInt("frameIntervalSingleDigit", frameIntervalSingleDigit)
				          .putInt("frameInterval", frameInterval)
				          .commit();
		            }
		        });
		builderFrameInterval.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // キャンセルの処理
            }  
        });
    	
        mActionsFrameInterval = builderFrameInterval.create();
        
        LayoutInflater inflater2 = LayoutInflater.from(this);
		View view2 = inflater2.inflate(R.layout.activity_successive_picture_video_numberpicker1, null);
        mNumberPicker6 = (NumberPicker) view2.findViewById(R.id.numberPicker6);
        mNumberPicker6.setMaxValue(50);
        mNumberPicker6.setValue(frameNumber);
        frameNumber = mNumberPicker6.getValue();
        AlertDialog.Builder builderFrameNumber = new AlertDialog.Builder(this);
        builderFrameNumber.setTitle("Set Frame Number");
        builderFrameNumber.setView(view2);
        builderFrameNumber.setPositiveButton(
		          "OK", 
		          new DialogInterface.OnClickListener() {          
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	frameNumber = mNumberPicker6.getValue();
		            	preference.edit()
				          .putInt("frameNumber", frameNumber)
				          .commit();
		            }
		        });
        builderFrameNumber.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // キャンセルの処理
            }  
        });
        mActionsFrameNumber = builderFrameNumber.create();
        
        AlertDialog.Builder builderFrameRotation = new AlertDialog.Builder(this);
		builderFrameRotation.setTitle("Select Frame Rotation");
		builderFrameRotation.setSingleChoiceItems(SETTINGS_FRAME_ROTATION, mCheckedItem, new DialogInterface.OnClickListener(){  
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCheckedItemTemp = which;
            }});
		builderFrameRotation.setPositiveButton("OK", new  DialogInterface.OnClickListener(){  
            @Override  
            public void onClick(DialogInterface dialog, int which) {
            	if (mCheckedItemTemp == 0)
            		frameRotation = 0;
            	else if (mCheckedItemTemp == 1)
            		frameRotation = 90;
            	else if (mCheckedItemTemp == 2)
            		frameRotation = 180;
            	else if (mCheckedItemTemp == 3)
            		frameRotation = 270;
            	preference.edit()
		          .putInt("frameRotation", frameRotation)
		          .commit();
            	mCheckedItem = mCheckedItemTemp;
            	}
            });
		builderFrameRotation.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // キャンセルの処理
            }  
        });
		mActionsFrameRotation = builderFrameRotation.create();
		
		LayoutInflater inflater3 = LayoutInflater.from(this);
		View view3 = inflater3.inflate(R.layout.activity_successive_picture_video_numberpicker2, null);
        mNumberPicker7 = (NumberPicker) view3.findViewById(R.id.numberPicker7);
        mNumberPicker7.setMaxValue(10);
        mNumberPicker7.setValue(5);
        mNumberPicker8 = (NumberPicker) view3.findViewById(R.id.numberPicker8);
        mNumberPicker8.setMaxValue(50);
        mNumberPicker8.setValue(5);
        frameNumberHorizontal = mNumberPicker7.getValue();
    	frameNumberVertical = mNumberPicker8.getValue();
        AlertDialog.Builder builderHorizontalAndVerticalNumber = new AlertDialog.Builder(this);
        builderHorizontalAndVerticalNumber.setTitle("Set Frame Number(Horizontal, Vertical)");
        builderHorizontalAndVerticalNumber.setView(view3);
        builderHorizontalAndVerticalNumber.setPositiveButton(
		          "OK", 
		          new DialogInterface.OnClickListener() {          
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	frameNumberHorizontal = mNumberPicker7.getValue();
		            	frameNumberVertical = mNumberPicker8.getValue();
		            	preference.edit()
				          .putInt("frameNumberHorizontal", frameNumberHorizontal)
				          .putInt("frameNumberVertical", frameNumberVertical)
				          .commit();
		            }
		        });
        builderHorizontalAndVerticalNumber.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // キャンセルの処理
            }  
        });
        mActionsHorizontalAndVerticalNumber = builderHorizontalAndVerticalNumber.create();
        
        LayoutInflater inflater4 = LayoutInflater.from(this);
		View view4 = inflater4.inflate(R.layout.activity_successive_picture_video_numberpicker3, null);
        mNumberPicker9 = (NumberPicker) view4.findViewById(R.id.numberPicker9);
        mNumberPicker9.setMaxValue(100);
        mNumberPicker9.setValue(50);
        mNumberPicker10 = (NumberPicker) view4.findViewById(R.id.numberPicker10);
        mNumberPicker10.setMaxValue(100);
        mNumberPicker10.setValue(100);
        trimmingSizeHorizontal = mNumberPicker9.getValue();
    	trimmingSizeVertical = mNumberPicker10.getValue();
        AlertDialog.Builder builderTrimming = new AlertDialog.Builder(this);
        builderTrimming.setTitle("Set Trimming Scale(Horizontal, Vertical)");
        builderTrimming.setView(view4);
        builderTrimming.setPositiveButton(
		          "OK", 
		          new DialogInterface.OnClickListener() {          
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	trimmingSizeHorizontal = mNumberPicker9.getValue();
		            	trimmingSizeVertical = mNumberPicker10.getValue();
		            	preference.edit()
				          .putInt("trimmingSizeHorizontal", trimmingSizeHorizontal)
				          .putInt("trimmingSizeVertical", trimmingSizeVertical)
				          .commit();
		            }
		        });
        builderTrimming.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                // キャンセルの処理
            }  
        });
        mActionsTrimming = builderTrimming.create();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.successive_picture_video, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				mActionsSettings.show();
				break;
			default:
				break;
		}
		
		return true;
	}

	//SuccessivePictureVideoPictureViewを起動するメソッド
		public void onClickPictureView(View view) {
			//SuccessivePictureVideoPictureViewを制御するためのインデントを生成
			Intent intent = new Intent(this, SuccessivePictureVideoPictureView.class);
			//SuccessivePictureVideoPitureViewを起動
			startActivity(intent);
		}
		
	//SuccessivePictureVideoThumbnailを起動するメソッド
		public void onClickThumbnail(View view) {
			//SuccessivePictureVideoThumbnailを制御するためのインデントを生成
			Intent intent = new Intent(this, SuccessivePictureVideoThumbnail.class);
			//SuccessivePictureVideoThumbnailを起動
			startActivity(intent);
		}
		
		public void onClickSettings(View view) {
			mActionsSettings.show();
		}
		
		//AlertDialogで選択した文字列(Flame Intervalなど)をswitch文で使用可能な形にする
		public enum Settings {
		    FrameInterval("Frame Interval"), 
		    FrameNumber("Frame Number"), 
		    FrameRotation("Frame Rotation"), 
		    HorizontalandVerticalNumber("Horizontal and Vertical Number"),
		    Trimming("Trimming"),
		    NOVALUE("");
		    private final String item;
		    private Settings(String item) {
		        this.item = item;
		    }
		    @Override
		    public String toString() {
		        return item;
		    }
		    public static Settings toSettings(String item) {
		        Settings result = null;
		        for (Settings settings : values()) {
		            if (settings.toString().equals(item)) {
		                result = settings;
		                break;
		            }
		        }
		        return result != null ? result : NOVALUE;
		    }
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String selected = SETTINGS[which];
			switch (Settings.toSettings(selected)) {
		    case FrameInterval:
		    	mNumberPickerSixDigits.setValue(frameIntervalSixDigits);
            	mNumberPickerFiveDigits.setValue(frameIntervalFiveDigits);
            	mNumberPickerQuadrupleDigits.setValue(frameIntervalQuadrupleDigits);
            	mNumberPickerTripleDigits.setValue(frameIntervalTripleDigits);
            	mNumberPickerDoubleDigits.setValue(frameIntervalDoubleDigits);
            	mNumberPickerSingleDigit.setValue(frameIntervalSingleDigit);
		    	mActionsFrameInterval.show();
		        break;
		    case FrameNumber:
		    	mNumberPicker6.setValue(frameNumber);
		    	mActionsFrameNumber.show();
		        break;
		    case FrameRotation:
		    	mActionsFrameRotation.show();
		        break;
		    case HorizontalandVerticalNumber:
		    	mNumberPicker7.setValue(frameNumberHorizontal);
            	mNumberPicker8.setValue(frameNumberVertical);
		    	mActionsHorizontalAndVerticalNumber.show();
		    	break;
		    case Trimming:
            	mNumberPicker9.setValue(trimmingSizeHorizontal);
            	mNumberPicker10.setValue(trimmingSizeVertical);
		    	mActionsTrimming.show();
		    	break;
		    default:
		        // それ以外の場合の処理
		        break;
			}
		}	
}