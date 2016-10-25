package myahkota.homedelivery.com.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import myahkota.homedelivery.com.R;

public class LoadingDialog extends DialogFragment {
    private boolean shown = false;

    @Override
    public void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        return _inflater.inflate(R.layout.dialog_loading, _container, true);
    }

    @Override
    public void show(FragmentManager _manager, String _tag) {
        if (shown) return;

        super.show(_manager, _tag);
        shown = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle _savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.transparent)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface _dialog) {
        shown = false;
        super.onDismiss(_dialog);
    }

    public boolean isShowing() {
        return shown;
    }

    @Override
    public int show(FragmentTransaction _transaction, String _tag) {
        return super.show(_transaction, _tag);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shown = false;
    }
}
