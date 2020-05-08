package cn.parry.qrcode.zbardemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.parry.qrcode.zbar.CaptureUtils;


public class TestGeneratectivity extends AppCompatActivity {
    private ImageView mChineseIv;
    private ImageView mEnglishIv;
    private ImageView mChineseLogoIv;
    private ImageView mEnglishLogoIv;
    private ImageView mBarcodeWithContentIv;
    private ImageView mBarcodeWithoutContentIv;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_generate);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initView();
        createQRCode();
    }

    private void initView() {
        mChineseIv = findViewById(R.id.iv_chinese);
        mChineseLogoIv = findViewById(R.id.iv_chinese_logo);
        mEnglishIv = findViewById(R.id.iv_english);
        mEnglishLogoIv = findViewById(R.id.iv_english_logo);
        mBarcodeWithContentIv = findViewById(R.id.iv_barcode_with_content);
        mBarcodeWithoutContentIv = findViewById(R.id.iv_barcode_without_content);
    }

    private void createQRCode() {
        createChineseQRCode();
        createEnglishQRCode();
        createChineseQRCodeWithLogo();
        createEnglishQRCodeWithLogo();

        createBarcodeWidthContent();
        createBarcodeWithoutContent();
    }

    private void createChineseQRCode() {
        CaptureUtils.AsyncTaskCallback asyncTaskCallback  = new CaptureUtils.AsyncTaskCallback() {
            @Override
            public void onBitmapCreateSuccess(Bitmap mBitmap) {
                    mChineseIv.setImageBitmap(mBitmap);
            }

            @Override
            public void onBitmapCreateFailed() {
            Toast.makeText(TestGeneratectivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };
        CaptureUtils.createEncodeQRCode(this, asyncTaskCallback,"利",150);
    }

    private void createEnglishQRCode() {
        CaptureUtils.AsyncTaskCallback asyncTaskCallback  = new CaptureUtils.AsyncTaskCallback() {
            @Override
            public void onBitmapCreateSuccess(Bitmap mBitmap) {
                mEnglishIv.setImageBitmap(mBitmap);
            }

            @Override
            public void onBitmapCreateFailed() {
                Toast.makeText(TestGeneratectivity.this, "生成英文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };
        CaptureUtils.createEncodeQRCode(this, asyncTaskCallback,"parry",150,Color.parseColor("#ff0000"));

    }

    private void createChineseQRCodeWithLogo() {
        CaptureUtils.AsyncTaskCallback asyncTaskCallback  = new CaptureUtils.AsyncTaskCallback() {
            @Override
            public void onBitmapCreateSuccess(Bitmap mBitmap) {
                mChineseLogoIv.setImageBitmap(mBitmap);
            }

            @Override
            public void onBitmapCreateFailed() {
                Toast.makeText(TestGeneratectivity.this, "生成带logo的中文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };
        Bitmap logoBitmap = BitmapFactory.decodeResource(TestGeneratectivity.this.getResources(), R.mipmap.ic_launcher);
        CaptureUtils.createEncodeQRCode(this, asyncTaskCallback,"利",150,Color.parseColor("#ff0000"),logoBitmap);


    }

    private void createEnglishQRCodeWithLogo() {
        CaptureUtils.AsyncTaskCallback asyncTaskCallback  = new CaptureUtils.AsyncTaskCallback() {
            @Override
            public void onBitmapCreateSuccess(Bitmap mBitmap) {
                mEnglishLogoIv.setImageBitmap(mBitmap);
            }

            @Override
            public void onBitmapCreateFailed() {
                Toast.makeText(TestGeneratectivity.this, "生成带logo的英文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };
        Bitmap logoBitmap = BitmapFactory.decodeResource(TestGeneratectivity.this.getResources(), R.mipmap.ic_launcher);
        CaptureUtils.createEncodeQRCode(this, asyncTaskCallback,"parry",150,Color.parseColor("#ff0000"),logoBitmap);



    }


    private void createBarcodeWidthContent() {
        CaptureUtils.AsyncTaskCallback asyncTaskCallback  = new CaptureUtils.AsyncTaskCallback() {
            @Override
            public void onBitmapCreateSuccess(Bitmap mBitmap) {
                mBarcodeWithContentIv.setImageBitmap(mBitmap);
            }

            @Override
            public void onBitmapCreateFailed() {
                Toast.makeText(TestGeneratectivity.this, "生成条底部带文字形码失败", Toast.LENGTH_SHORT).show();
            }
        };
        CaptureUtils.createEncodeBarcode(this,asyncTaskCallback,"parry520", 150, 70, 14);
    }

    private void createBarcodeWithoutContent() {
        CaptureUtils.AsyncTaskCallback asyncTaskCallback  = new CaptureUtils.AsyncTaskCallback() {
            @Override
            public void onBitmapCreateSuccess(Bitmap mBitmap) {
                mBarcodeWithoutContentIv.setImageBitmap(mBitmap);
            }

            @Override
            public void onBitmapCreateFailed() {
                Toast.makeText(TestGeneratectivity.this, "生成条底部不带文字形码失败", Toast.LENGTH_SHORT).show();
            }
        };
        CaptureUtils.createEncodeBarcode(this,asyncTaskCallback,"parry520", 150, 70, 0);


    }

    public void decodeChinese(View v) {
        CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
            @Override
            public void onDecodeSuccess(String content) {
                Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecodeFailed() {
                Toast.makeText(TestGeneratectivity.this, "解析中文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };


        mChineseIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mChineseIv.getDrawingCache();
        CaptureUtils. decode( this,asyncTaskDecodeCallback,bitmap);
    }

    public void decodeEnglish(View v) {

        CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
            @Override
            public void onDecodeSuccess(String content) {
                Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecodeFailed() {
                Toast.makeText(TestGeneratectivity.this, "解析英文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };


        mEnglishIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mEnglishIv.getDrawingCache();
        CaptureUtils. decode( this,asyncTaskDecodeCallback,bitmap);
    }

    public void decodeChineseWithLogo(View v) {

        CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
            @Override
            public void onDecodeSuccess(String content) {
                Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecodeFailed() {
                Toast.makeText(TestGeneratectivity.this, "解析带logo的中文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };


        mChineseLogoIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mChineseLogoIv.getDrawingCache();
        CaptureUtils. decode( this,asyncTaskDecodeCallback,bitmap);
    }

    public void decodeEnglishWithLogo(View v) {


        CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
            @Override
            public void onDecodeSuccess(String content) {
                Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecodeFailed() {
                Toast.makeText(TestGeneratectivity.this, "解析带logo的英文二维码失败", Toast.LENGTH_SHORT).show();
            }
        };


        mEnglishLogoIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mEnglishLogoIv.getDrawingCache();
        CaptureUtils. decode( this,asyncTaskDecodeCallback,bitmap);

    }

    public void decodeBarcodeWithContent(View v) {
        mBarcodeWithContentIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mBarcodeWithContentIv.getDrawingCache();
        CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
            @Override
            public void onDecodeSuccess(String content) {
                Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecodeFailed() {
                Toast.makeText(TestGeneratectivity.this, "识别底部带文字的条形码失败", Toast.LENGTH_SHORT).show();
            }
        };



        CaptureUtils. decode( this,asyncTaskDecodeCallback,bitmap);

    }

    public void decodeBarcodeWithoutContent(View v) {
        mBarcodeWithoutContentIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mBarcodeWithoutContentIv.getDrawingCache();
        CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
            @Override
            public void onDecodeSuccess(String content) {
                Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDecodeFailed() {
                Toast.makeText(TestGeneratectivity.this, "识别底部没带文字的条形码失败", Toast.LENGTH_SHORT).show();
            }
        };
        CaptureUtils. decode( this,asyncTaskDecodeCallback,bitmap);
    }
    public void decodeAlbum(View v) {
  /*
                从相册选取二维码图片，这里为了方便演示，使用的是
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 */
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(null)
                .maxChooseCount(1)
                .selectedPhotos(null)
                .pauseOnScroll(false)
                .build();
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
                @Override
                public void onDecodeSuccess(String content) {
                    Toast.makeText(TestGeneratectivity.this, content, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDecodeFailed() {
                    Toast.makeText(TestGeneratectivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                }
            };
           CaptureUtils.decode(this,asyncTaskDecodeCallback,picturePath);
        }
    }



}