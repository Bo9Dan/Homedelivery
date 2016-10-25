package myahkota.homedelivery.com.fragments;

import android.app.Dialog;
import android.content.Context;

public class LoadDataError extends Dialog {


    public LoadDataError(Context context) {
        super(context);
    }

    public LoadDataError(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadDataError(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
