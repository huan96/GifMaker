package com.gif.image.photo.gifmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.adapters.AlbumAdapter;
import com.gif.image.photo.gifmaker.adapters.ImageAdapter;
import com.gif.image.photo.gifmaker.adapters.PickedImageAdapter;
import com.gif.image.photo.gifmaker.fragments.AlbumFragment;
import com.gif.image.photo.gifmaker.fragments.ImageFragment;
import com.gif.image.photo.gifmaker.objects.Image;
import com.gif.image.photo.gifmaker.utils.AnimationTranslate;
import com.gif.image.photo.gifmaker.utils.Constant;
import com.gif.image.photo.gifmaker.utils.ManagerGalary;

import java.util.ArrayList;

public class ImagePickerActivity extends AppCompatActivity implements AlbumAdapter.OnClickAlbum, View.OnClickListener, ImageAdapter.OnClickImage, PickedImageAdapter.OnClickCancel {
    private ArrayList<Image> arrImagesAlbum;
    private ArrayList<String> imagePaths;
    private ManagerGalary managerGalary;
    private AlbumFragment albumFragment;
    private ImageFragment imageFragment;
    private ImageView btnBack, btnSave;
    private ArrayList<Image> imagesAlBum;
    private ArrayList<Image> imagesPicked;
    private RecyclerView rvPickedImage;
    private PickedImageAdapter pickedImageAdapter;
    private TextView tvSelected, tvTitle;
    private ImageView btClear;
    private LinearLayout layoutSelectImage;
    private RelativeLayout layoutAds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_picker);
        initUI();
    }

    private void initUI() {
        tvSelected = (TextView) findViewById(R.id.tv_count_selected_img);
        tvTitle = (TextView) findViewById(R.id.tv_title_app_bar);
        tvTitle.setText(getString(R.string.pick_image));
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnSave = (ImageView) findViewById(R.id.btn_save);
        btnSave.setVisibility(View.VISIBLE);
        layoutAds = (RelativeLayout) findViewById(R.id.layout_ads);
        layoutSelectImage = (LinearLayout) findViewById(R.id.ll_select_image);
        layoutSelectImage.setVisibility(View.GONE);
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btClear = (ImageView) findViewById(R.id.btn_clear);
        btClear.setOnClickListener(this);
        rvPickedImage = (RecyclerView) findViewById(R.id.rv_picked_image);
        rvPickedImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesPicked = new ArrayList<>();
        pickedImageAdapter = new PickedImageAdapter(imagesPicked, this, this);
        pickedImageAdapter.setHasStableIds(true);
        rvPickedImage.setAdapter(pickedImageAdapter);

        managerGalary = new ManagerGalary(this);
        arrImagesAlbum = managerGalary.getArrImage();
        imagePaths = managerGalary.getImagePaths();

        albumFragment = new AlbumFragment();
        albumFragment.setOnClickAlbum(this);
        albumFragment.setImagesAlbum(arrImagesAlbum);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, albumFragment);
        t.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                try {
                    if (imageFragment.isVisible() && imageFragment != null) {
                        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                        t.replace(R.id.frame_image_picker, albumFragment);
                        t.commit();
                    } else {
                        AnimationTranslate.previewAnimation(ImagePickerActivity.this);
                        finish();

                    }
                } catch (Exception e) {
                    finish();
                    AnimationTranslate.previewAnimation(ImagePickerActivity.this);
                    e.printStackTrace();
                }
                break;
            case R.id.btn_save:
                Intent intent = new Intent(this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.IMAGE, imagesPicked);
                intent.putExtra(Constant.IMAGE, bundle);
                startActivity(intent);
                AnimationTranslate.nextAnimation(ImagePickerActivity.this);
                break;
            case R.id.btn_clear:
                layoutSelectImage.setVisibility(View.GONE);
                for (int i = 0; i < imagesPicked.size(); i++) {
                    arrImagesAlbum.get(arrImagesAlbum.indexOf(imagesPicked.get(i))).setClicked(false);
                }
                imageFragment.notifiData(imagesAlBum);
                imagesPicked.clear();
                pickedImageAdapter.notifyDataSetChanged();
                tvSelected.setText(getString(R.string.selected_0_images));
                break;
            default:
                break;
        }
    }

    // Bắt sự kiện ấn ảnh trong album
    @Override
    public void onClickImage(int position) {
        layoutSelectImage.setVisibility(View.VISIBLE);
        imagesPicked.add(imagesAlBum.get(position));
        imagesAlBum.get(position).setClicked(true);
        imageFragment.notifiData(imagesAlBum);
        pickedImageAdapter.notifyDataSetChanged();
        tvSelected.setText(getString(R.string.selected) + " " + imagesPicked.size() + " " + getString(R.string.image));
        rvPickedImage.smoothScrollToPosition(imagesPicked.size() - 1);

    }

    // Bắt sự kiện ấn album
    @Override
    public void onClickAlbum(int position) {
        imagesAlBum = albumFragment.getArrAlbum().get(position).getArrImage();
        imageFragment = new ImageFragment();
        imageFragment.setImagesAlbum(imagesAlBum);
        imageFragment.setOnClickImage(this);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, imageFragment);
        t.commit();
    }

    // Bắt sự kiện ấn hủy
    @Override
    public void onClickCancel(int position) {
        arrImagesAlbum.get(arrImagesAlbum.indexOf(imagesPicked.get(position))).setClicked(false);
        imageFragment.notifiData(imagesAlBum);
        imagesPicked.remove(imagesPicked.get(position));
        pickedImageAdapter.notifyDataSetChanged();
        tvSelected.setText(getString(R.string.selected) + " " + imagesPicked.size() + " " + getString(R.string.image));
        if (imagesPicked.size() < 1) {
            tvSelected.setText(getString(R.string.pick_image));
            layoutSelectImage.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        try {
            if (imageFragment.isVisible() && imageFragment != null) {
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.frame_image_picker, albumFragment);
                t.commit();
            } else {
                finish();
                AnimationTranslate.previewAnimation(ImagePickerActivity.this);
            }
        } catch (NullPointerException e) {
            finish();
            AnimationTranslate.previewAnimation(ImagePickerActivity.this);
        }

    }
}
