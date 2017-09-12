package com.gif.image.photo.gifmaker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.dialog.RateAppDialog;
import com.gif.image.photo.gifmaker.utils.AnimationTranslate;
import com.gif.image.photo.gifmaker.utils.Constant;
import com.gif.image.photo.gifmaker.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout btnNewGif, btnMyGif;
    private RelativeLayout layoutAds;
    private RateAppDialog rateAppDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnNewGif = (LinearLayout) findViewById(R.id.btn_new_gif);
        btnMyGif = (LinearLayout) findViewById(R.id.btn_my_gif);
        turnPermiss();
    }


    public void init() {
        layoutAds = (RelativeLayout) findViewById(R.id.layout_ads);
        btnNewGif.setOnClickListener(this);
        btnMyGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyGifActivity.class));
                AnimationTranslate.nextAnimation(MainActivity.this);
            }
        });
        rateAppDialog = new RateAppDialog(this, new RateAppDialog.OnButtonClicked() {
            @Override
            public void onRateClicked() {
                Utils.rateApp(MainActivity.this);
            }

            @Override
            public void onCancelClicked() {
                finish();
            }
        });
    }

    public void turnPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, MainActivity.this) == PackageManager.PERMISSION_GRANTED) {
                creatFolder();
                init();
            } else {
                MainActivity.this.requestPermissions(PERMISSION, 1);
            }
        } else {
            creatFolder();
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                creatFolder();
                init();
            }
        }

    }

    public void creatFolder() {
        File file = new File(Constant.PATH);
        if (!file.exists()) {
            file.mkdirs();
            File filetemp = new File(Constant.PATH_TEMP);
            if (!filetemp.exists()) {
                filetemp.mkdirs();
                Log.d("DEBUG", "filetemp not creat");
            }
            File filevideo = new File(Constant.PATH_GIF);
            if (!filevideo.exists()) {
                filevideo.mkdirs();
                Log.d("DEBUG", "filevideo notcreate");
            }
        }
        createFile();

    }

    public void createFile() {
        File fileGif = new File(Constant.PATH_TEMP + "test.gif");
        if (fileGif.exists()) {
            Log.d("DEBUG", "file created");
        } else {
            try {
                FileOutputStream out = new FileOutputStream(fileGif);
                Log.d("DEBUG", "file created success");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_gif:
            case R.id.iv_create: {
                startActivity(new Intent(MainActivity.this, ImagePickerActivity.class));
                AnimationTranslate.nextAnimation(MainActivity.this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        rateAppDialog.show();
    }
}
