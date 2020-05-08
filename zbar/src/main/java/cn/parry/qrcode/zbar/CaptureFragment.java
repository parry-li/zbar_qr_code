package cn.parry.qrcode.zbar;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.parry.qrcode.core.BarcodeType;
import cn.parry.qrcode.core.QRCodeView;

import static android.content.Context.VIBRATOR_SERVICE;


public class CaptureFragment extends Fragment implements QRCodeView.Delegate {

    private ZBarView mZBarView;
    private CaptureUtils.AnalyzeCallback analyzeCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = null;
        if (bundle != null) {
            int layoutId = bundle.getInt(CaptureUtils.LAYOUT_ID);
            if (layoutId != -1) {
                view = inflater.inflate(layoutId, null);
            }
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_capture, null);
        }

        mZBarView = view.findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }


    @Override
    public void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getActivity().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /*成功扫描结果*/
    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.d("parry", "result:" + result);
        analyzeCallback.onAnalyzeSuccess(result);
        vibrate();
        mZBarView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZBarView.getScanBoxView().getTipText();

        String  ambientBrightnessTip = "\n环境过暗，请打开闪光灯\n";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {

                mZBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZBarView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    public void setAnalyzeCallback(CaptureUtils.AnalyzeCallback analyzeCallback) {
        this.analyzeCallback = analyzeCallback;
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        analyzeCallback.onAnalyzeFailed();
        Log.e("parry", "打开相机出错");
    }

    /*解析二维码，不支持反色二维码*/
    public void decodeFromAlbum(String imagePath) {
        mZBarView. decodeQRCode(imagePath);
    }


    public ZBarView getZbarView(){
      return mZBarView;
    }

    // 打开后置摄像头开始预览
    public void startCamera(){
        mZBarView.startCamera();
    }
    // 关闭摄像头预览，并且隐藏扫描框
    public void stopCamera(){
        mZBarView.stopCamera();
    }
    // 开始识别
    public void startSpot(){
        mZBarView.startSpot();
    }
    // 停止识别
    public void stopSpot(){
        mZBarView.stopSpot();
    }
    // 显示扫描框
    public void startSpotAndShowRect(){
        mZBarView.startSpotAndShowRect();
    }
    // 隐藏扫描框
    public void hiddenScanRect(){
        mZBarView.hiddenScanRect();
    }
    // 仅识别扫描框中的码
    public void setOnlyDecodeScanBoxArea(boolean b){
        mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(b);
    }
    // true 仅识别扫描框中的码 false 识别整个屏幕中的码
    public void setOnlyDecodeScanBoxArea(){
        mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(true);
    }

    // 打开闪光灯
    public void openFlashlight(){
        mZBarView.openFlashlight();
    }
    // 关闭闪光灯
    public void closeFlashlight(){
        mZBarView.closeFlashlight();
    }



}
