package com.gif.image.photo.gifmaker.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.adapters.RecyclerListAdapter;
import com.gif.image.photo.gifmaker.dialog.WaitDialog;
import com.gif.image.photo.gifmaker.helpers.OnStartDragListener;
import com.gif.image.photo.gifmaker.helpers.SimpleItemTouchHelperCallback;
import com.gif.image.photo.gifmaker.objects.Image;
import com.gif.image.photo.gifmaker.utils.AnimatedGifEncoder;
import com.gif.image.photo.gifmaker.utils.AnimationTranslate;
import com.gif.image.photo.gifmaker.utils.Constant;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class EditActivity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener, RecyclerListAdapter.OnClickImageEdit, SeekBar.OnSeekBarChangeListener {


    public static int CAMERA_PREVIEW_RESULT = 1;
    private ArrayList<Image> imageList;
    private ArrayList<String> paths;
    private RecyclerView recyclerView;
    private RecyclerListAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private TextView tvtitle;
    private ImageView imageViewTest;
    private SeekBar seekBarSpeed;
    private ImageView btnBack, bntSave;
    private RelativeLayout layoutAds;
    private int time;
    private AsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        bntSave = (ImageView) findViewById(R.id.btn_save);
        imageViewTest = (ImageView) findViewById(R.id.img_test);
        bntSave.setVisibility(View.VISIBLE);
        tvtitle = (TextView) findViewById(R.id.tv_title_app_bar);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.IMAGE);
        imageList = bundle.getParcelableArrayList(Constant.IMAGE);
        recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RecyclerListAdapter(this, this, imageList, this);
        recyclerView.setAdapter(adapter);
        time = 600;
        seekBarSpeed = (SeekBar) findViewById(R.id.seekBar);
        seekBarSpeed.setProgress(50);
        seekBarSpeed.setOnSeekBarChangeListener(this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        btnBack.setOnClickListener(this);
        bntSave.setOnClickListener(this);
        tvtitle.setOnClickListener(this);
        layoutAds = (RelativeLayout) findViewById(R.id.layout_ads);

        setTestView();

    }

    private void setTestView() {
        AnimationDrawable animation = new AnimationDrawable();
        for (int i = 0; i < imageList.size(); i++) {
            Drawable drawable = Drawable.createFromPath(imageList.get(i).getPath());
            animation.addFrame(drawable, time);
        }
        animation.setOneShot(false);
        imageViewTest.setImageDrawable(animation);
        animation.start();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
        imageList = adapter.getmItems();
    }

    public static Bitmap getBitmapFromLocalPath(String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            return BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
                AnimationTranslate.previewAnimation(EditActivity.this);
                break;
            }
            case R.id.btn_save: {
                paths = new ArrayList<String>();
                for (int i = 0; i < imageList.size(); i++) {
                    paths.add(imageList.get(i).getPath());
                }
                if (paths.size() != 0) {
                    final ArrayList<Bitmap> bitmaps = new ArrayList<>();
                    for (int i = 0; i < paths.size(); i++) {
                        bitmaps.add(getBitmapFromLocalPath(paths.get(i)));
                    }
                    final WaitDialog waitDialog = new WaitDialog(this, new WaitDialog.OnButtonClicked() {
                        @Override
                        public void onCancelClicked() {
                            task.cancel(true);
                            finish();
                        }
                    });
                    waitDialog.show();
                    task = new AsyncTask() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Object doInBackground(Object[] params) {
                            writeGif(bitmaps);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            waitDialog.dismiss();
                            Intent data = new Intent(EditActivity.this, MyGifActivity.class);
                            data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            AnimationTranslate.nextAnimation(EditActivity.this);
                            startActivity(data);
                            super.onPostExecute(o);
                        }
                    };
                    task.execute();
                } else {
                    Toast.makeText(this, R.string.did_not_selected_image, Toast.LENGTH_SHORT).show();
                }

                break;
            }

        }

    }

    @Override
    public void onBackPressed() {
        AnimationTranslate.previewAnimation(EditActivity.this);
        super.onBackPressed();
    }

    @Override
    public void onClickImageEdit(int position) {

    }

    private byte[] generateGIF(ArrayList<Bitmap> bitmaps) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        encoder.setQuality(5);
        encoder.setDelay(1000);
        for (Bitmap bitmap : bitmaps) {
            encoder.addFrame(bitmap);
        }
        encoder.finish();
        return bos.toByteArray();
    }

    private void writeGif(ArrayList<Bitmap> bitmaps) {
        FileOutputStream outStream = null;
        String name = System.currentTimeMillis() + ".gif";
        try {
            outStream = new FileOutputStream(Constant.PATH_GIF + name);
            outStream.write(generateGIF(bitmaps));
            outStream.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

/*
    private void writeGif2(ArrayList<Bitmap> bitmaps) {
        FileOutputStream outStream = null;
        String name = Constant.PATH_GIF + System.currentTimeMillis() + ".gif";
        GifEncoder gifEncoder = new GifEncoder();
        try {
            gifEncoder.init(1080, 1920, name, GifEncoder.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY);
            for (Bitmap bitmap : bitmaps) {
                gifEncoder.encodeFrame(bitmap, 1000);
            }

            gifEncoder.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        time = 1100 - 10 * progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTestView();
    }
}
