package com.gif.image.photo.gifmaker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.objects.Gif;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huand on 9/12/2017.
 */

public class GifDialog extends Dialog {
    private Gif gif;
    private GifImageView gifImageView;
    private ImageView bntBack, btnShare, btnDelete;

    public GifDialog(Context context, Gif gif) {
        super(context);
        this.gif = gif;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_show_gif);

        initView();
    }


    private void initView() {
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        File gifFile = new File(gif.getPath());
        try {
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            //gifImageView.setImageDrawable(gifFromFile);
            gifImageView.setBackground(gifFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bntBack = (ImageView) findViewById(R.id.btn_back);
        btnDelete = (ImageView) findViewById(R.id.btn_delete);
        btnShare = (ImageView) findViewById(R.id.btn_share);
        bntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


}
