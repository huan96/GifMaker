package com.gif.image.photo.gifmaker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gif.image.photo.gifmaker.R;
import com.gif.image.photo.gifmaker.objects.Gif;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huand on 9/8/2017.
 */

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.ItemImage> {
    private ArrayList<Gif> imageList;
    private Context mContext;
    private OnClickImage onClickImage;

    public GifAdapter(ArrayList<Gif> imageList, Context mContext, OnClickImage onClickImage) {
        this.imageList = imageList;
        this.mContext = mContext;
        this.onClickImage = onClickImage;
    }

    @Override
    public ItemImage onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_gif_card, parent, false);
        return new ItemImage(itemView);
    }

    @Override
    public void onBindViewHolder(ItemImage holder, final int position) {
        File gifFile = new File(imageList.get(position).getPath());
        try {
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            gifFromFile.stop();
            holder.gifImageView.setImageDrawable(gifFromFile);
            //holder.gifImageView.setBackground(gifFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage.onClickImage(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickImage.onLongClickImage(position);
                return true;
            }
        });
        holder.isChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage.onClickImage(position);
            }
        });
        if (imageList.get(position).isClicked()) {
            holder.isChecked.setVisibility(View.VISIBLE);
        } else {
            holder.isChecked.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public interface OnClickImage {
        void onClickImage(int position);

        void onLongClickImage(int position);
    }

    public class ItemImage extends RecyclerView.ViewHolder {
        GifImageView gifImageView;
        ImageView isChecked;

        public ItemImage(View itemView) {
            super(itemView);
            gifImageView = (GifImageView) itemView.findViewById(R.id.gifview);
            isChecked = (ImageView) itemView.findViewById(R.id.iv_checked);
        }
    }

    public void updateList(List<Gif> items) {
        Log.e("updateList", "sssss");
        if (items != null && items.size() > 0) {
            imageList.clear();
            imageList.addAll(items);
            notifyDataSetChanged();
        }
    }
}
