package cn.parry.qrcode.zbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import cn.parry.qrcode.zxing.QRCodeDecoder;
import cn.parry.qrcode.zxing.QRCodeEncoder;

/**
 * 解析二维码
 */
public class CaptureDecodeAsyncTask extends AsyncTask<Void, Void, String> {

    public Bitmap bitmap;
    private WeakReference<Context> weakReference;

    private CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback;

    public CaptureDecodeAsyncTask(Context context, CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback, Bitmap bitmap) {
        weakReference = new WeakReference<>(context);
        this.asyncTaskDecodeCallback = asyncTaskDecodeCallback;

        this.bitmap = bitmap;

    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = new String();
        try {

            //优先使用zbar识别一次二维码
            String qrContent = UiUtis.getInstance().decodeQRcode(bitmap);
            if (!TextUtils.isEmpty(qrContent)) {
                result = qrContent;
            } else {
                //尝试用zxing再试一次识别反色二维码
                String qrCode = QRCodeDecoder.syncDecodeQRCode(bitmap);
                if (!TextUtils.isEmpty(qrCode)) {
                    result = qrCode;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        bitmap =null;

        return result;
    }

    @Override
    protected void onPostExecute(String content) {
        if (!TextUtils.isEmpty(content)) {
            asyncTaskDecodeCallback.onDecodeSuccess(content);
        } else {
            asyncTaskDecodeCallback.onDecodeFailed();
        }
    }
}
