package cheipesh.homedelivery.com.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.base.Constants;
import cheipesh.homedelivery.com.base.MainActivity;

public class OptionDialog extends DialogFragment {
    private String propetryMenu, phoneNumber, map;
    private ExpandableLayout elMenuDetailList;
    private ImageView backOptions/*, hambMenu*/;
    private RoundedImageView hambMenu;
    private WebView menuRestaraunt;


    public static OptionDialog newInstance(final String _menuList, final String _phoneNumber, String bitmap) {
        OptionDialog fragment = new OptionDialog();
        Bundle args = new Bundle();
        args.putString(Constants.TFG_OPTION_MENU_LIST, _menuList);
        args.putString(Constants.TFG_OPTION_PHONE_NUMBER, _phoneNumber);
        args.putString("map", bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    private void getOptions(Bundle bundle) {
        propetryMenu = bundle.getString(Constants.TFG_OPTION_MENU_LIST);
        phoneNumber = bundle.getString(Constants.TFG_OPTION_PHONE_NUMBER);
        map = bundle.getString("map");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle _savedInstanceState) {
        if (getArguments() != null) {
            getOptions(getArguments());
        }

        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.option_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.transparent)));

        fillView(dialog);

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        fillView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void fillView(Dialog dialog) {
        backOptions = (ImageView) dialog.findViewById(R.id.ivBackOption);
        elMenuDetailList = (ExpandableLayout) dialog.findViewById(R.id.expandableLayout);
        hambMenu = (RoundedImageView) dialog.findViewById(R.id.ivOptionMenu);
        menuRestaraunt = (WebView) elMenuDetailList.findViewById(R.id.tvMenuRestaurant);

        backOptions.setImageBitmap(((MainActivity) getActivity()).getDrawableBack());
        backOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        elMenuDetailList.setOnClickListener(optionClickListener);
        hambMenu.setOnClickListener(optionClickListener);
        menuRestaraunt.loadDataWithBaseURL("", propetryMenu, "text/html", "UTF-8", "");
        if (((MainActivity) getActivity()).getDrawableOption() == null) {
            hambMenu.setVisibility(View.GONE);
        } else {

            hambMenu.setImageBitmap(((MainActivity) getActivity()).getDrawableOption());
        }
//        menuRestaraunt.setText(Html.fromHtml(propetryMenu));
//        Glide.with(this)
//                .load(map)
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .into(hambMenu);

        TextView callBtn = (TextView) dialog.findViewById(R.id.btnCall);
        callBtn.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        callBtn.setOnClickListener(callListener);
//
//        TextView showBTN = (TextView) dialog.findViewById(R.id.btnShowList);
//        showBTN.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
//        showBTN.setOnClickListener(optionClickListener);
    }


    private View.OnClickListener optionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (menuRestaraunt.getVisibility()){
                case View.VISIBLE:
                    menuRestaraunt.setVisibility(View.GONE);
                    break;
                case View.GONE:
                    menuRestaraunt.setVisibility(View.VISIBLE);
                    break;
                case View.INVISIBLE:
                    menuRestaraunt.setVisibility(View.GONE);
                    break;
                default:
                    menuRestaraunt.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                getPermiss();
                return;
            }
            getContext().startActivity(intent);
        }
    };

    public void getPermiss() {
        Ask.on(getActivity())
                .forPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET/*, Manifest.permission.WRITE_CALL_LOG*/)
                .withRationales("Call permission need for call ") //optional
                .go();
    }

    @AskGranted(Manifest.permission.CALL_PHONE)
    public void fileAccessGranted() {
//        Log.i(TAG, "FILE  GRANTED");
    }

    //optional
    @AskDenied(Manifest.permission.CALL_PHONE)
    public void fileAccessDenied() {
//        Log.i(TAG, "FILE  DENiED");
    }

    //optional
    @AskGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessGranted() {
//        Log.i(TAG, "MAP GRANTED");
    }

    //optional
    @AskDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessDenied() {
//        Log.i(TAG, "MAP DENIED");
    }

}




