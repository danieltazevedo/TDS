package com.example.braguia.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class BotaoSOS implements View.OnClickListener {
    private Context mContext;

    public BotaoSOS(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:112"));
        mContext.startActivity(intent);
    }
}
