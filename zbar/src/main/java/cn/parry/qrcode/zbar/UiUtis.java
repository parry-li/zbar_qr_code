package cn.parry.qrcode.zbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.TypedValue;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class UiUtis {
    private static UiUtis instance;

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }


    public static UiUtis getInstance() {
        if (instance == null) {
            instance = new UiUtis();
        }
        return instance;
    }
    /**
     * 识别本地二维码
     *
     * @param path
     * @return
     */
    public String decodeQRcode(Bitmap qrbmp) throws Exception {
        //对图片进行灰度处理，为了兼容彩色二维码
        qrbmp = toGrayscale(qrbmp);
        if (qrbmp != null) {
            return decodeQRcodeForBitmap(qrbmp);
        } else {
            return "";
        }

    }
    public String decodeQRcodeForBitmap(Bitmap barcodeBmp) throws Exception {
        int width = barcodeBmp.getWidth();
        int height = barcodeBmp.getHeight();
        int[] pixels = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        barcodeBmp.recycle();
        return qrCodeString;
    }
    /**
     * 压缩图片
     * @param path
     * @return
     */
    public Bitmap compressImage(String path){
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
     * 对bitmap进行灰度处理
     * @param bmpOriginal
     * @return
     */
    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
