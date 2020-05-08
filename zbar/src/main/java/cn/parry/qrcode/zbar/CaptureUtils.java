package cn.parry.qrcode.zbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 *
 */
public class CaptureUtils {
    public static final String LAYOUT_ID = "capture_layout_id";
    private static CaptureEncodeQRCodeAsyncTask captureBitmapAsyncTask;
    private static CaptureEncodeBarcodeAsyncTask captureBarCodeAsyncTask;
    private static CaptureDecodeAsyncTask decodeAsyncTask;

    /**
     * 为CaptureFragment设置layout参数
     *
     * @param captureFragment
     * @param layoutId
     */
    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId) {
        if (captureFragment == null || layoutId == -1) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        captureFragment.setArguments(bundle);
    }

    /**
     * 同步创建黑色前景色、白色背景色的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content 要生成的二维码图片内容
     * @param size    图片宽高，单位为px
     */
    public static void createEncodeQRCode(Context context, AsyncTaskCallback asyncTaskCallback, String content, int size) {
        createEncodeQRCode(context, asyncTaskCallback, content, size, Color.BLACK);
    }

    /**
     * 同步创建指定前景色、白色背景色的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content         要生成的二维码图片内容
     * @param size            图片宽高，单位为px
     * @param foregroundColor 二维码图片的前景色
     */
    public static void createEncodeQRCode(Context context, AsyncTaskCallback asyncTaskCallback, String content, int size, int foregroundColor) {
        createEncodeQRCode(context, asyncTaskCallback, content, size, foregroundColor, null);
    }

    /**
     * 同步创建指定前景色、白色背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content         要生成的二维码图片内容
     * @param size            图片宽高，单位为px
     * @param foregroundColor 二维码图片的前景色
     * @param logo            二维码图片的logo
     */
    public static void createEncodeQRCode(Context context, AsyncTaskCallback asyncTaskCallback, String content, int size, int foregroundColor, Bitmap logo) {

        createEncodeQRCode(context, asyncTaskCallback, content, size, Color.BLACK, Color.WHITE, null);
    }

    /**
     * 同步创建指定前景色、指定背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
     *
     * @param content         要生成的二维码图片内容
     * @param size            图片宽高，单位为px
     * @param foregroundColor 二维码图片的前景色
     * @param backgroundColor 二维码图片的背景色
     * @param logo            二维码图片的logo
     */
    public static void createEncodeQRCode(Context context, AsyncTaskCallback asyncTaskCallback, String content, int size, int foregroundColor, int backgroundColor, Bitmap logo) {
        size=  UiUtis.dp2px(context,size);

        captureBitmapAsyncTask = new CaptureEncodeQRCodeAsyncTask(context, content, asyncTaskCallback, size, foregroundColor, backgroundColor, logo);
        captureBitmapAsyncTask.execute();

    }

    /**
     * 取消二维码创建过程
     */
    public static void cancelCreateEncodeQRCode() {
        if (captureBitmapAsyncTask != null &&
                captureBitmapAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            captureBitmapAsyncTask.cancel(true);
        }
    }
    /**
     * 创建条形码图片  不带文字
     *
     * @param content  要生成条形码包含的内容
     * @param width    条形码的宽度，单位px
     * @param height   条形码的高度，单位px
     * @param textSize 字体大小，单位px，如果等于0则不在底部绘制文字
     * @return 返回生成条形的位图
     */
    public static void createEncodeBarcode(Context context,AsyncTaskCallback asyncTaskCallback, String content, int width, int height) {
        createEncodeBarcode(context,asyncTaskCallback, content, width, height, 0);
    }

    /**
     * 创建条形码图片  带文字
     *
     * @param content  要生成条形码包含的内容
     * @param width    条形码的宽度，单位px
     * @param height   条形码的高度，单位px
     * @param textSize 字体大小，单位px，如果等于0则不在底部绘制文字
     * @return 返回生成条形的位图
     */
    public static void createEncodeBarcode(Context context,AsyncTaskCallback asyncTaskCallback,
                                           String content, int width, int height, int textSize) {
        textSize=  UiUtis.dp2px(context,textSize);
        width=  UiUtis.dp2px(context,width);
        height=  UiUtis.dp2px(context,height);
        captureBarCodeAsyncTask = new CaptureEncodeBarcodeAsyncTask(asyncTaskCallback, content, width, height, textSize);
        captureBarCodeAsyncTask.execute();
    }

    /**
     * 取消形码二维码创建过程
     */
    public static void cancelCreateEncodeBarcode() {
        if (captureBarCodeAsyncTask != null &&
                captureBarCodeAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            captureBarCodeAsyncTask.cancel(true);
        }
    }


    /**
     * 从相册获取Bitmap解析二维码，支持反色二维码
     */
    public static void decode(Context context ,AsyncTaskDecodeCallback asyncTaskDecodeCallback,Bitmap bitmap){
         decodeAsyncTask = new CaptureDecodeAsyncTask(context,asyncTaskDecodeCallback,bitmap);
        decodeAsyncTask.execute();
    }
    /**
     * 从相册获取地址解析二维码，支持反色二维码
     */
    public static void decode(Context context ,AsyncTaskDecodeCallback asyncTaskDecodeCallback,String imagePath){
        Bitmap bitmap =   compressImage(imagePath);
         decodeAsyncTask = new CaptureDecodeAsyncTask(context,asyncTaskDecodeCallback,bitmap);
        decodeAsyncTask.execute();
    }

    /**
     * 取消解析相册获取二维码
     */
    public static void cancelDecodeAsyncTask() {
        if (decodeAsyncTask != null &&
                decodeAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            decodeAsyncTask.cancel(true);
        }
    }

    /**
     * 压缩图片
     * @param path
     * @return
     */
    private static Bitmap compressImage(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        options.inJustDecodeBounds = false;
        int sampleSizeH = (int) (options.outHeight / (float) 800);
        int sampleSizeW = (int) (options.outWidth / (float) 800);
        int sampleSize = Math.max(sampleSizeH,sampleSizeW);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap qrbmp = BitmapFactory.decodeFile(path,options);
        return qrbmp;
    }
    /**
     * 解析二维码结果
     */
    public interface AnalyzeCallback {

        public void onAnalyzeSuccess(String result);

        public void onAnalyzeFailed();
    }

    /**
     * 异步线程解析的结果
     */
    public interface AsyncTaskCallback {

        public void onBitmapCreateSuccess(Bitmap mBitmap);

        public void onBitmapCreateFailed();
    }
    /**
     * 异步线程解析的结果
     */
    public interface AsyncTaskDecodeCallback {

        public void onDecodeSuccess(String content);

        public void onDecodeFailed();
    }

}
