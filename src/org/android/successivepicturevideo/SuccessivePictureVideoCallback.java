package org.android.successivepicturevideo;

import android.graphics.Bitmap;

public interface SuccessivePictureVideoCallback {
	//画像を受け渡しするためだけのコールバックメソッド 
    public void callback(final Bitmap resultBitmap);
}
