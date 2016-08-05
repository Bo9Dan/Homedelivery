package cheipesh.homedelivery.com.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
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

    private ImageView ivDialogBackground;
    private RoundedImageView ivOptionLogo;
    private TextView tvWorking, tvDelivery;
    private TextView btnMenu, btnCall;
    private ExpandableRelativeLayout erlContainer;
    private WebView wvMenuRestaurant;

    ParcelableParseObject mRestaurantDetail;

    public static OptionDialog newInstance(ParcelableParseObject _placeDetail) {
        OptionDialog fragment = new OptionDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.P_COLUMN_OBJECT, _placeDetail);
        fragment.setArguments(args);
        return fragment;
    }

    private void getOptions(Bundle bundle) {
        mRestaurantDetail = bundle.getParcelable(Constants.P_COLUMN_OBJECT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle _savedInstanceState) {

        if (getArguments() != null) {
            getOptions(getArguments());
        }

        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                 ViewGroup.LayoutParams.MATCH_PARENT));


        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(), R.style.MyCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_option);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                     ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),
                                                                   R.color.transparent)));
        dialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;

        prepareDialog(dialog);

        return dialog;
    }

    private void prepareDialog(Dialog _dialog) {
        // findUI
        erlContainer        = (ExpandableRelativeLayout) _dialog.findViewById(R.id.expandableLayout);
        wvMenuRestaurant    = (WebView) _dialog.findViewById(R.id.wvListMenu);

        ivDialogBackground  = (ImageView) _dialog.findViewById(R.id.ivOptionBackground);
        ivOptionLogo        = (RoundedImageView) _dialog.findViewById(R.id.ivOptionMenu);

        tvWorking           = (TextView) _dialog.findViewById(R.id.tvWorkingTime);
        tvDelivery          = (TextView) _dialog.findViewById(R.id.tvDeliveryTime);

        btnMenu             = (TextView) _dialog.findViewById(R.id.btnShowList);
        btnCall             = (TextView) _dialog.findViewById(R.id.btnCall);

        // adEffectTypeface
        applyTypeFace(tvWorking);
        applyTypeFace(tvDelivery);
        applyTypeFace(btnMenu);
        applyTypeFace(btnCall);


        // fillData
        ivDialogBackground.setImageBitmap(((MainActivity) getActivity()).getDrawableBack());
        final Bitmap logoImage = ((MainActivity) getActivity()).getDrawableOption();
        if (logoImage == null)
                ivOptionLogo.setVisibility(View.GONE);
        else    ivOptionLogo.setImageBitmap(((MainActivity) getActivity()).getDrawableOption());

        tvWorking.setText(mRestaurantDetail.getWork());
        tvDelivery.setText(String.format("%s %s", mRestaurantDetail.getDelivery(), getResources().getString(R.string.delivery_time)));
        wvMenuRestaurant.loadDataWithBaseURL("", mRestaurantDetail.getMenu(), "text/html", "UTF-8", "");

        // setListeners
        ivDialogBackground.setOnClickListener(dialogListener);
        btnMenu.setOnClickListener(dialogListener);
        btnCall.setOnClickListener(dialogListener);
        ivOptionLogo.setOnClickListener(dialogListener);

    }

    private void applyTypeFace(TextView _v) {
        _v.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
    }

    private View.OnClickListener dialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ivOptionBackground:
                    getDialog().dismiss();
                    break;
                case R.id.btnCall:
                    callHim();
                    break;
                case R.id.ivOptionMenu:
                case R.id.btnShowList:
                    erlContainer.toggle();
                    break;
            }
        }
    };

    private void callHim() {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mRestaurantDetail.getPhone()));

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                getPermissions();
                return;
            }

            getContext().startActivity(intent);

            Amplitude.getInstance().logEvent(Constants.P_COLUMN_CALL, trackEventModel());
    }

    public void getPermissions() {
        Ask.on(getActivity())
                .forPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET)
                .withRationales("Call permission need for call ")
                .go();
    }

    private JSONObject trackEventModel() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.P_COLUMN_CITY, mRestaurantDetail.getCity());
            jsonObject.put(Constants.P_COLUMN_CATEGORY, mRestaurantDetail.getCategory());
            jsonObject.put(Constants.P_COLUMN_PLACE, mRestaurantDetail.getPlace());
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




