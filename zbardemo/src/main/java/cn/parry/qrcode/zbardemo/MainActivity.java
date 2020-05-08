package cn.parry.qrcode.zbardemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.CrashUtils;

import java.util.List;

import cn.parry.qrcode.core.CodeUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        CodeUtil.setDebug(true);
        initCrash();
    }
    private void initCrash() {
//        if (Constants.isShowHttpLog) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            CrashUtils.init(new CrashUtils.OnCrashListener() {
                @Override
                public void onCrash(String crashInfo, Throwable e) {


                    Log.e("parry","crashInfo" + crashInfo);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_scan_qrcode:
                startActivity(new Intent(this, TestScanActivity.class));
                break;
            case R.id.test_scan_jc:
                startActivity(new Intent(this, TestFastActivity.class));
                break;
            case R.id.test_greate:
                startActivity(new Intent(this, TestGeneratectivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
