package com.example.assignment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


public class Utility {

    public static void showSignUpErrMsg(String msg, Context ctx) {

        if (TextUtils.isEmpty(msg.trim())) {
            msg = ctx.getString(R.string.msg_server_not_responding);
        } else if (msg.equals("1")) {
            msg = ctx.getString(R.string.msg_warn_internet);
        }

        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }




}
