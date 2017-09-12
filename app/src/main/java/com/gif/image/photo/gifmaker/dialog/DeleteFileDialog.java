package com.gif.image.photo.gifmaker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.gif.image.photo.gifmaker.R;


/**
 * Created by binhnk on 7/25/2017.
 */

public class DeleteFileDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnButtonClicked onButtonClicked;

    private TextView btnYes, btnNo;

    public DeleteFileDialog(@NonNull Context context, OnButtonClicked onButtonClicked) {
        super(context);
        this.context = context;
        this.onButtonClicked = onButtonClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_delete_file);

        btnYes = (TextView) findViewById(R.id.btn_yes);
        btnNo = (TextView) findViewById(R.id.btn_no);

        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                dismiss();
                onButtonClicked.onYesClicked();
                break;

            case R.id.btn_no:
                dismiss();
                break;

            default:
                dismiss();
                break;
        }
    }

    public interface OnButtonClicked {
        void onYesClicked();
    }
}
