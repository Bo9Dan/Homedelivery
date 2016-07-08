package cheipesh.homedelivery.com.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.base.Constants;
import cheipesh.homedelivery.com.base.MainActivity;

public class OptionDialog extends DialogFragment {
    private String menuList, phoneNumber, map;
    private ExpandableLayout elMenuDetailList;
    private ImageView backOptions/*, hambMenu*/;
    private RoundedImageView hambMenu;
    private TextView menuRestaraunt;


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
        menuList = bundle.getString(Constants.TFG_OPTION_MENU_LIST);
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


    private void fillView(Dialog dialog) {
        backOptions = (ImageView) dialog.findViewById(R.id.ivBackOption);
        elMenuDetailList = (ExpandableLayout) dialog.findViewById(R.id.expandableLayout);
        hambMenu = (RoundedImageView) dialog.findViewById(R.id.ivOptionMenu);
        menuRestaraunt = (TextView) elMenuDetailList.findViewById(R.id.tvMenuRestaurant);

        backOptions.setImageBitmap(((MainActivity) getActivity()).getDrawable());
        backOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        elMenuDetailList.setOnClickListener(optionClickListener);
        hambMenu.setOnClickListener(optionClickListener);
        menuRestaraunt.setText(menuList);

        Glide.with(this)
                .load(map)
                .asBitmap()
//                .override(120,120)
//                .centerCrop()
//                .transform(new RoundedCornersTransformation(getContext(), 16, 2))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(hambMenu);

        TextView callBtn = (TextView) dialog.findViewById(R.id.btnCall);
        callBtn.setOnClickListener(callListener);
    }


    private View.OnClickListener optionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (elMenuDetailList.isOpened())
                elMenuDetailList.hide();
            else elMenuDetailList.show();
        }
    };

    private View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                getPermiss();
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            getContext().startActivity(intent);
        }
    };

//    private void call() {
//        PhoneCallListener phoneListener = new PhoneCallListener();
//        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(phoneListener,
//                PhoneStateListener.LISTEN_CALL_STATE);
//
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:0377778888"));
//        startActivity(callIntent);
//
//    }

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

//    public void deleteCallLogByNumber(String number) {
//        String queryString = "NUMBER=" + number;
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        getActivity().getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);
//    }
//    class PhoneCallListener extends PhoneStateListener {
//
//        private boolean isPhoneCalling = false;
//
//        String LOG_TAG = "LOGGING 123";
//
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            if (TelephonyManager.CALL_STATE_RINGING == state) {
//                // phone ringing
//                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
//            }
//
//            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
//                // active
//                Log.i(LOG_TAG, "OFFHOOK");
//
//                isPhoneCalling = true;
//            }
//
//            if (TelephonyManager.CALL_STATE_IDLE == state) {
//                // run when class initial and phone call ended, need detect flag
//                // from CALL_STATE_OFFHOOK
//                Log.i(LOG_TAG, "IDLE");
//
//                if (isPhoneCalling) {
//
//                    Log.i(LOG_TAG, "restart app");
//                    deleteCallLogByNumber(incomingNumber);
////                    // restart app
////                    Intent i = App.getAppContext().getPackageManager()
////                            .getLaunchIntentForPackage(
////                                    App.getAppContext().getPackageName());
////                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    startActivity(i);
//
//                    isPhoneCalling = false;
//                }
//
//            }
//        }
//
//        private void deleteCallLogByNumber(String queryString) {
//
//            if (ActivityCompat.checkSelfPermission(App.getAppContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            App.getAppContext().getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);
//        }
//    }
}




