package com.phonemedia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity {

    public static final int RequestPermissionCode = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionCheck();

        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, new HomeFragment(),
                HomeFragment.class.getSimpleName()).commit();
    }

    private void permissionCheck() {
        if(CheckingPermissionIsEnabledOrNot()) {
            Log.d("PERMISSION_!", "All Permissions Granted Successfully");
        } else {
            RequestMultiplePermission();

        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {
        int readExternalPermission = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return readExternalPermission == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                READ_EXTERNAL_STORAGE
        }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean readExternalPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (readExternalPermission) {
                        Log.d("PERMISSION_!", "Permissions Granted");
                    }
                    else {
                        Log.d("PERMISSION_!", "Permissions Denied");
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Permission!");
                        alertDialog.setMessage("This App need your permission to continue");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                permissionCheck();
                            }
                        });
                        alertDialog.show();

                    }
                }
                break;
        }
    }

}
