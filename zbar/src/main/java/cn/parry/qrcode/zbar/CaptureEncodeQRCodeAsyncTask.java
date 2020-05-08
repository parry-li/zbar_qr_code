package cn.parry.qrcode.zbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import cn.parry.qrcode.zxing.QRCodeEncoder;

/**
 * 生成二维码
 */
public class CaptureEncodeQRCodeAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    private final Context context;
    private final int size;
    private final int foregroundColor;
    private final int backgroundColor;
    private final String content;
    public   Bitmap logoBitmap ;
    private WeakReference<Context> weakReference;

    private CaptureUtils.AsyncTaskCallback asuncTaskCallback;
    public CaptureEncodeQRCodeAsyncTask(Context context, String content, CaptureUtils.AsyncTaskCallback asyncTaskCallback, int size, int foregroundColor, int backgroundColor, Bitmap logo) {
        weakReference = new WeakReference<>(context);
        this.asuncTaskCallback = asyncTaskCallback;
        this.context =context;
        this.content =content;
        this.logoBitmap =logo;
        this.size =size;
        this.foregroundColor =foregroundColor;
        this.backgroundColor =backgroundColor;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return QRCodeEncoder.syncEncodeQRCode(content, UiUtis.dp2px(context,size), foregroundColor, backgroundColor, logoBitmap);
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
