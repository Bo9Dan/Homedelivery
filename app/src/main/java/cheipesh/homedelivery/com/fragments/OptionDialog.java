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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.andexert.expandablelayout.library.ExpandableLayout;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

import org.json.JSONException;
import org.json.JSONObject;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.base.Constants;
import cheipesh.homedelivery.com.base.MainActivity;
import cheipesh.homedelivery.com.base.ParcelableParseObject;

public class OptionDialog extends DialogFragment {

    private ExpandableLayout elMenuDetail;
    private ImageView ivBackOptions;
    private RoundedImageView rivLogo;
    private WebView wvMenuRestaurant;
    private TextView tvWork, tvDelivery, tvCall;
    private String menuRestaurant, phoneRestaurant, workRestaurant, deliveryRestaurant;
    private ParcelableParseObject parseObject;

    public static OptionDialog newInstance(ParcelableParseObject object) {
        OptionDialog fragment = new OptionDialog();
        Bundle args = new Bundle();
        args.putParcelable("Object", object);
//        args.putString(Constants.TFG_OPTION_MENU_LIST, _menuList);
//        args.putString(Constants.TFG_OPTION_PHONE_NUMBER, _phoneNumber);
//        args.putString(Constants.TFG_OPTION_DELIVERY, _delivery);
//        args.putString(Constants.TFG_OPTION_WORK, _work);
        fragment.setArguments(args);
        return fragment;
    }

    private void getOptions(Bundle bundle) {
        parseObject = bundle.getParcelable("Object");
        menuRestaurant = parseObject.getMenu();
        phoneRestaurant = parseObject.getPhone();
        deliveryRestaurant = parseObject.getDelivery();
        workRestaurant = parseObject.getWork();
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
        ivBackOptions = (ImageView) dialog.findViewById(R.id.ivBackOption);
        elMenuDetail = (ExpandableLayout) dialog.findViewById(R.id.expandableLayout);
        rivLogo = (RoundedImageView) dialog.findViewById(R.id.ivOptionMenu);
        wvMenuRestaurant = (WebView) elMenuDetail.findViewById(R.id.tvMenuRestaurant);

        ivBackOptions.setImageBitmap(((MainActivity) getActivity()).getDrawableBack());
        ivBackOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
//        elMenuDetail.setOnClickListener(optionClickListener);
//        rivLogo.setOnClickListener(optionClickListener);
        wvMenuRestaurant.loadDataWithBaseURL("", menuRestaurant, "text/html", "UTF-8", "");
        if (((MainActivity) getActivity()).getDrawableOption() == null) {
            rivLogo.setVisibility(View.GONE);
        } else {

            rivLogo.setImageBitmap(((MainActivity) getActivity()).getDrawableOption());
        }

        tvWork = (TextView) dialog.findViewById(R.id.tvWorkingTime);
        tvWork.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        tvWork.setText(workRestaurant);

        tvDelivery = (TextView) dialog.findViewById(R.id.tvDeliveryTime);
        tvDelivery.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        tvDelivery.setText(deliveryRestaurant + " " + getResources().getString(R.string.delivery_time));

        tvCall = (TextView) dialog.findViewById(R.id.btnCall);
        tvCall.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        tvCall.setOnClickListener(callListener);



    }


    private View.OnClickListener optionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (wvMenuRestaurant.getVisibility()){
                case View.VISIBLE:
                    wvMenuRestaurant.setVisibility(View.GONE);
                    break;
                case View.GONE:
                    wvMenuRestaurant.setVisibility(View.VISIBLE);
                    break;
                case View.INVISIBLE:
                    wvMenuRestaurant.setVisibility(View.GONE);
                    break;
                default:
                    wvMenuRestaurant.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneRestaurant));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                getPermiss();
                return;
            }
            getContext().startActivity(intent);

            Amplitude.getInstance().logEvent(Constants.P_COLUMN_CALL, trackEventModel());
        }
    };

    public void getPermiss() {
        Ask.on(getActivity())
                .forPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET)
                .withRationales("Call permission need for call ")
                .go();
    }

    private JSONObject trackEventModel() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.P_COLUMN_CITY, parseObject.getCity());
            jsonObject.put(Constants.P_COLUMN_CATEGORY, parseObject.getCategory());
            jsonObject.put(Constants.P_COLUMN_PLACE, parseObject.getPlace());
            jsonObject.put(Constants.P_COLUMN_PLATFORM, "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @AskGranted(Manifest.permission.CALL_PHONE)
    public void fileAccessGranted() {
    }

    //optional
    @AskDenied(Manifest.permission.CALL_PHONE)
    public void fileAccessDenied() {
    }

    //optional
    @AskGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessGranted() {
    }

    //optional
    @AskDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessDenied() {
    }

}




