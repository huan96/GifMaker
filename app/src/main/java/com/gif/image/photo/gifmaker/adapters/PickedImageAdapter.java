package com.gif.image.photo.gifmaker.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.objects.Image;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Peih Gnaoh on 8/21/2017.
 */

public class PickedImageAdapter extends RecyclerView.Adapter<PickedImageAdapter.ItemPickedImage> {
    private ArrayList<Image> imageList;
    private Context mContext;
    private File image;
    private OnClickCancel onClickCancel;

    public PickedImageAdapter(ArrayList<Image> imageList, Context mContext, OnClickCancel onClickCancel) {
        this.imageList = imageList;
        this.mContext = mContext;
        this.onClickCancel = onClickCancel;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    @Override
    public ItemPickedImage onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_picked_image_card, parent, false);
        return new ItemPickedImage(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemPickedImage holder, final int position) {
        image = new File(imageList.get(position).getPath());
        Glide.with(mContext).load(image).into(holder.thumbnail);
        holder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancel.onClickCancel(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public interface OnClickCancel {
        void onClickCancel(int position);
    }

    public class ItemPickedImage extends RecyclerView.ViewHolder {
        ImageView thumbnail, ivCancel;

        public ItemPickedImage(View itemView) {
            super(itemView);
            ivCancel = (ImageView) itemView.findViewById(R.id.iv_cancel);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }


}
