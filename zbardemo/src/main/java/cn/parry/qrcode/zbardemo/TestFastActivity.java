package cn.parry.qrcode.zbardemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.parry.qrcode.zbar.CaptureFragment;
import cn.parry.qrcode.zbar.CaptureUtils;

public class TestFastActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 12345 ;
    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_zbar);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CaptureUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

    }


    /**
     * 二维码解析回调函数
     */
    CaptureUtils.AnalyzeCallback analyzeCallback = new CaptureUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess( String result) {

            Toast.makeText(TestFastActivity.this,result,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAnalyzeFailed() {
            Toast.makeText(TestFastActivity.this,"解析失败",Toast.LENGTH_SHORT).show();
        }
    };

    public void decodeAlbum(View v) {
                /*从相册选取二维码图片，这里为了方便演示，使用的是
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
            /*不支持反色二维码*/
            captureFragment.decodeFromAlbum(picturePath);

//           //以下支持反色二维码
//            CaptureUtils.AsyncTaskDecodeCallback asyncTaskDecodeCallback = new CaptureUtils.AsyncTaskDecodeCallback() {
//                @Override
//                public void onDecodeSuccess(String content) {
//                    Toast.makeText(TestFastActivity.this, content, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onDecodeFailed() {
//                    Toast.makeText(TestFastActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
//                }
//            };
//            CaptureUtils.decode(this,asyncTaskDecodeCallback,picturePath);
        }
    }



}
