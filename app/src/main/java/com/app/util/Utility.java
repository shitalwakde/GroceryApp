package com.app.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.app.R;

public class Utility {

    public Utility() {
    }

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dialog_custom, null);
        dialog.setCancelable(false);
        // TextView txtMessage = (TextView)
        // view.findViewById(R.id.pdc_tv_message);
        // txtMessage.setText(message);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        dialog.dismiss();

        return dialog;
    }

    public static boolean isValidEmail(CharSequence str){
        if (str==null){
            return false;
        }else {
            return Patterns.EMAIL_ADDRESS.matcher(str).matches();
        }
    }

}
