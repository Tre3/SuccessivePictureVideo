package org.android.successivepicturevideo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

@SuppressWarnings("serial")
public class SuccessivePictureVideoSaveBitmap extends IOException {
	private Bitmap saveImage;
	private Context mContext;
	
	@SuppressLint("SimpleDateFormat")
	public SuccessivePictureVideoSaveBitmap(Bitmap saveBitmap, Context context) throws IOException{
		saveImage = saveBitmap;
		mContext = context;

    final String SAVE_DIR = "/SuccessivePicture/";
    File file = new File(Environment.getExternalStorageDirectory().getPath() + SAVE_DIR);
    try{
        if(!file.exists()){
            file.mkdir();
        }
    }catch(SecurityException e){
        e.printStackTrace();
        throw e;
    }

    Date mDate = new Date();
    SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String fileName = fileNameDate.format(mDate) + ".jpg";
    String AttachName = file.getAbsolutePath() + "/" + fileName;

    try {
        FileOutputStream out = new FileOutputStream(AttachName);
        saveImage.compress(CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();
    } catch(IOException e) {
        e.printStackTrace();
        throw e;
    }
    
        // save index
    ContentValues values = new ContentValues();
    ContentResolver contentResolver = mContext.getContentResolver();
    values.put(Images.Media.MIME_TYPE, "image/jpeg");
    values.put(Images.Media.TITLE, fileName); 
    values.put(Images.Media.DISPLAY_NAME, fileName);
    values.put(Images.Media.DATE_TAKEN, mDate.getTime());
    values.put(Images.Media.DATA, AttachName);
    //values.put("_data", AttachName);
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}
}
