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
 * Created by huand on 9/9/2017.
 */

public class WaitDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnButtonClicked onButtonClicked;

    private TextView btnCancel;

    public WaitDialog(@NonNull Context context, OnButtonClicked onButtonClicked) {
        super(context);
        this.context = context;
        this.onButtonClicked = onButtonClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_wait);
        setCancelable(false);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                onButtonClicked.onCancelClicked();
                break;
            default:
                dismiss();
                break;
        }
    }

    public interface OnButtonClicked {
        void onCancelClicked();
    }
}
