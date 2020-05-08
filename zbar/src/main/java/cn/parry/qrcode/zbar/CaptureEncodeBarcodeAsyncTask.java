package cn.parry.qrcode.zbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import cn.parry.qrcode.zxing.QRCodeEncoder;

/**
 * 生成条形二维码
 */
public class CaptureEncodeBarcodeAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    private final String content;
    private final int width;
    private final int height;
    private final int textSize;
    public Bitmap logoBitmap;

    private CaptureUtils.AsyncTaskCallback asuncTaskCallback;

    public CaptureEncodeBarcodeAsyncTask(CaptureUtils.AsyncTaskCallback asyncTaskCallback,
                                         String content, int width, int height, int textSize) {

        this.asuncTaskCallback = asyncTaskCallback;
        this.content = content;
        this.width = width;
        this.height = height;
        this.textSize = textSize;

    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return QRCodeEncoder.syncEncodeBarcode(content, width, height, textSize);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            asuncTaskCallback.onBitmapCreateSuccess(bitmap);
        } else {
            asuncTaskCallback.onBitmapCreateFailed();
        }
    }
}
