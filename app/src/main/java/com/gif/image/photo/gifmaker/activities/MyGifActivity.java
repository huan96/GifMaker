package com.gif.image.photo.gifmaker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.adapters.GifAdapter;
import com.gif.image.photo.gifmaker.dialog.DeleteFileDialog;
import com.gif.image.photo.gifmaker.dialog.GifDialog;
import com.gif.image.photo.gifmaker.objects.Gif;
import com.gif.image.photo.gifmaker.utils.Constant;

import java.io.File;
import java.util.ArrayList;

public class MyGifActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycleView;
    private GifAdapter adapterGif;
    private ArrayList<Gif> arrGif = new ArrayList<>();
    private TextView titleappbar;
    private ImageView btnBack;
    private RelativeLayout layoutAds;
    private ArrayList<Gif> gifPicked;
    private RelativeLayout layoutSelect, layoutAcction;
    private ImageView btnDelete, btnShare;
    private TextView btnCancel, btnSelectAll, tvSelect;
    private RelativeLayout layoutTitle;
    private boolean isChoise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_gif);
        init();
    }

    public void init() {
        layoutAds = (RelativeLayout) findViewById(R.id.layout_ads);
        recycleView = (RecyclerView) findViewById(R.id.rcv_gif);
        titleappbar = (TextView) findViewById(R.id.tv_title_app_bar);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        arrGif = new ArrayList<>();
        gifPicked = new ArrayList<>();
        layoutSelect = (RelativeLayout) findViewById(R.id.rl_select);
        layoutAcction = (RelativeLayout) findViewById(R.id.rl_action);
        btnDelete = (ImageView) findViewById(R.id.btn_delete);
        btnShare = (ImageView) findViewById(R.id.btn_share);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnSelectAll = (TextView) findViewById(R.id.btn_select_all);
        layoutTitle = (RelativeLayout) findViewById(R.id.rl_title);
        tvSelect = (TextView) findViewById(R.id.tv_select_item);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        titleappbar.setOnClickListener(this);
        isChoise = false;
        setData();

    }

    private void setData() {
        File folder = new File(Constant.PATH_GIF);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Gif gif = new Gif();
                gif.setPath(file.getPath());
                gif.setClicked(false);
                arrGif.add(gif);
            }
        }
        recycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        adapterGif = new GifAdapter(arrGif, getBaseContext(), new GifAdapter.OnClickImage() {
            @Override
            public void onClickImage(int position) {
                if (!isChoise) {
                    GifDialog gifDialog = new GifDialog(MyGifActivity.this, arrGif.get(position));
                    gifDialog.show();
                } else {
                    if (arrGif.get(position).isClicked() == true) {
                        gifPicked.remove(gifPicked.indexOf(arrGif.get(position)));
                        arrGif.get(position).setClicked(false);
                    } else {
                        arrGif.get(position).setClicked(true);
                        gifPicked.add(arrGif.get(position));
                    }
                    adapterGif.notifyDataSetChanged();
                }
            }

            @Override
            public void onLongClickImage(int position) {
                if (!arrGif.get(position).isClicked()) {
                    arrGif.get(position).setClicked(true);
                    gifPicked.add(arrGif.get(position));
                } else {
                    gifPicked.remove(gifPicked.indexOf(arrGif.get(position)));
                    arrGif.get(position).setClicked(false);
                }
                if (gifPicked.size() >= 1) {
                    isChoise = true;
                    layoutTitle.setVisibility(View.GONE);
                    layoutSelect.setVisibility(View.VISIBLE);
                    layoutAcction.setVisibility(View.VISIBLE);
                } else {
                    isChoise = false;
                    layoutTitle.setVisibility(View.VISIBLE);
                    layoutSelect.setVisibility(View.GONE);
                    layoutAcction.setVisibility(View.GONE);
                }
                adapterGif.notifyDataSetChanged();
            }
        });
        recycleView.setAdapter(adapterGif);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_cancel:
                clear();
                break;
            case R.id.btn_select_all:
                selectAll();
                break;
            case R.id.btn_share:
                share();
                break;
            case R.id.btn_delete:
                delete();
                break;
            default:
                break;
        }
    }

    private void delete() {
        DeleteFileDialog deleteFileDialog = new DeleteFileDialog(this, new DeleteFileDialog.OnButtonClicked() {
            @Override
            public void onYesClicked() {
                for (Gif gif : gifPicked) {
                    arrGif.remove(arrGif.indexOf(gif));
                    File file = new File(gif.getPath());
                    file.delete();
                }
                adapterGif.notifyDataSetChanged();
            }
        });
        deleteFileDialog.show();

    }

    private void share() {
        String s = new String();

    }

    private void selectAll() {
        for (Gif gif : arrGif) {
            gif.setClicked(true);
        }
        gifPicked.clear();
        gifPicked.addAll(arrGif);
        adapterGif.notifyDataSetChanged();
        layoutTitle.setVisibility(View.GONE);
        layoutSelect.setVisibility(View.VISIBLE);
        layoutAcction.setVisibility(View.VISIBLE);
    }

    private void clear() {
        for (Gif gif : arrGif) {
            gif.setClicked(false);
        }
        gifPicked.clear();
        adapterGif.notifyDataSetChanged();
        layoutTitle.setVisibility(View.VISIBLE);
        layoutSelect.setVisibility(View.GONE);
        layoutAcction.setVisibility(View.GONE);
    }
}
