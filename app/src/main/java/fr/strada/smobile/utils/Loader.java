package fr.strada.smobile.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.PorterDuff.Mode;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import fr.strada.smobile.R;


public class Loader {
    private static fr.strada.smobile.utils.Loader INSTANCE = null;
    private ProgressBar progressBar;
    /* access modifiers changed from: private */
    public Dialog progressDialog;

    private Loader() {
    }

    public static fr.strada.smobile.utils.Loader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new fr.strada.smobile.utils.Loader();
        }
        return INSTANCE;
    }

    public void show(final Activity activity) {
        this.progressDialog = new Dialog(activity);
        this.progressDialog.requestWindowFeature(1);
        this.progressDialog.setContentView(R.layout.loader);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4 && fr.strada.smobile.utils.Loader.this.progressDialog.isShowing()) {
                    fr.strada.smobile.utils.Loader.this.progressDialog.dismiss();
                }
                activity.onBackPressed();
                return true;
            }
        });
        LayoutParams lp = new LayoutParams();
        lp.copyFrom(this.progressDialog.getWindow().getAttributes());
        lp.width = -1;
        lp.height = -2;
        this.progressDialog.getWindow().setAttributes(lp);
        this.progressBar = (ProgressBar) this.progressDialog.findViewById(R.id.ProgressBar);
        this.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), Mode.MULTIPLY);
        this.progressDialog.show();
    }

    public void dismiss() {
        if (progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }
}
