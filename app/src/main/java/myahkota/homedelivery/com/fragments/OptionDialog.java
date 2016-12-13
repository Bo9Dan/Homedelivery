package myahkota.homedelivery.com.fragments;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.base.Const;
import myahkota.homedelivery.com.base.MainActivity;
import myahkota.homedelivery.com.base.ParcelableParseObject;

public class OptionDialog extends DialogFragment {

    private ImageView ivDialogBackground;
    private RoundedImageView ivOptionLogo;

    private TextView tvWorking, tvDelivery;
    private TextView tvMenu, tvCall;
    private TextView bottomSpace;

    private View btnMenu, btnCall;
    private WebView wvMenuRestaurant;

    private ExpandableLayout erlTop, erlBottom;
    private FrameLayout frameLayout;
    private LinearLayout topLayout;
    private RelativeLayout relativeMenu, relativeCall;

    private ParcelableParseObject mRestaurantDetail;

    public static OptionDialog newInstance(ParcelableParseObject _placeDetail) {
        OptionDialog fragment = new OptionDialog();
        Bundle args = new Bundle();
        args.putParcelable(Const.P_COLUMN_OBJECT, _placeDetail);
        fragment.setArguments(args);
        return fragment;
    }

    private void getOptions(Bundle bundle) {
        mRestaurantDetail = bundle.getParcelable(Const.P_COLUMN_OBJECT);
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

        dialog.setContentView(R.layout.dialog_option_alternative);

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
        frameLayout = (FrameLayout) _dialog.findViewById(R.id.frameDialog);
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        frameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        setSizes(frameLayout.getHeight());
                        addEffects();
                    }
                });

        topLayout    = (LinearLayout) _dialog.findViewById(R.id.llTopContErl);
        relativeMenu = (RelativeLayout) _dialog.findViewById(R.id.rlMenuCont);
        relativeCall = (RelativeLayout) _dialog.findViewById(R.id.rlCallCont);

        erlTop      = (ExpandableLayout) _dialog.findViewById(R.id.expandableLayoutTop);
        erlBottom   = (ExpandableLayout) _dialog.findViewById(R.id.expandableLayoutBottom);

        wvMenuRestaurant    = (WebView) _dialog.findViewById(R.id.wvListMenu);

        ivDialogBackground  = (ImageView) _dialog.findViewById(R.id.ivOptionBackground);
        ivOptionLogo        = (RoundedImageView) _dialog.findViewById(R.id.ivOptionMenu);

        bottomSpace = (TextView) _dialog.findViewById(R.id.spaceBottom);

        tvWorking           = (TextView) _dialog.findViewById(R.id.tvWorkingTime);
        tvDelivery          = (TextView) _dialog.findViewById(R.id.tvDeliveryTime);

        tvMenu           = (TextView) _dialog.findViewById(R.id.btnShowList);
        tvCall          = (TextView) _dialog.findViewById(R.id.btnCall);

        btnMenu             = _dialog.findViewById(R.id.fakeButtonShow);
        btnCall             = _dialog.findViewById(R.id.fakeButtonCall);

        fillData(mRestaurantDetail);
        setListeners();
    }

    private void fillData(ParcelableParseObject model) {
        ivDialogBackground.setImageBitmap(((MainActivity) getActivity()).getDrawableBack());

        final Bitmap logoImage = ((MainActivity) getActivity()).getDrawableOption();
        if (logoImage == null)
            ivOptionLogo.setVisibility(View.GONE);
        else    ivOptionLogo.setImageBitmap(((MainActivity) getActivity()).getDrawableOption());

        tvWorking.setText(model.getWork());
        tvDelivery.setText(String.format("%s %s", model.getDelivery(), getResources().getString(R.string.delivery_time)));

        wvMenuRestaurant.loadDataWithBaseURL("", model.getMenu(), "text/html", "UTF-8", "");
    }

    private void setListeners() {
        wvMenuRestaurant.setOnTouchListener(new View.OnTouchListener() {

            static final int MIN_DISTANCE = 100;
            private float downX, downY, upX, upY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        //   return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        // swipe horizontal?
                        if(Math.abs(deltaX) > MIN_DISTANCE) {
                            // left or right
                            if(deltaX < 0) { return true; }
                            if(deltaX > 0) { return true; }
                        }

                        // swipe vertical?
                        if(Math.abs(deltaY) > MIN_DISTANCE) {
                            // top or down
                            if(deltaY > 0) { onSwipeTop(); return false; }
                            if(deltaY < 0) { onSwipeBottom(); return false; }
                        }
                    }
                }
                return false;
            }
        });

        ivDialogBackground.setOnClickListener(dialogListener);

        btnMenu.setOnClickListener(dialogListener);
        btnCall.setOnClickListener(dialogListener);

        ivOptionLogo.setOnClickListener(dialogListener);
    }

    public void onSwipeTop() {
        Log.d("TAG", "onSwipeTop");
        if (erlTop.isExpanded()) {
            erlTop.collapse();
            wvMenuRestaurant.scrollTo(wvMenuRestaurant.getScrollX(), 0);
        }
    }

    public void onSwipeBottom() {
        Log.d("TAG", "onSwipeBottom");
        if (!erlTop.isExpanded() && wvMenuRestaurant.getScrollY() ==0)
            erlTop.expand();
    }

    private void setSizes(int height) {

        ViewGroup.LayoutParams layoutParamsErlTop = topLayout.getLayoutParams();
        layoutParamsErlTop.height = (int) height/2;
        topLayout.setLayoutParams(layoutParamsErlTop);


        ViewGroup.LayoutParams layoutParamsMenu = relativeMenu.getLayoutParams();
        layoutParamsMenu.height = (int) 72;
        relativeMenu.setLayoutParams(layoutParamsMenu);


        ViewGroup.LayoutParams layoutParamsCall = relativeCall.getLayoutParams();
        layoutParamsCall.height = (int) 72;
        relativeCall.setLayoutParams(layoutParamsCall);


        ViewGroup.LayoutParams layoutParamsErlBottom = bottomSpace.getLayoutParams();
        layoutParamsErlBottom.height = (int) height/2 - 144;
        bottomSpace.setLayoutParams(layoutParamsErlBottom);
    }

    private void addEffects() {
        applyTypeFace(tvWorking);
        applyTypeFace(tvDelivery);
        applyTypeFace(tvMenu);
        applyTypeFace(tvCall);
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
                case R.id.fakeButtonCall:
                    callHim();
                    break;
                case R.id.ivOptionMenu:
                case R.id.fakeButtonShow:
                    if (erlTop.isExpanded())
                        erlBottom.toggle();
                    else {
                        erlTop.toggle();
                        erlBottom.toggle();
                    }
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

            Amplitude.getInstance().logEvent(Const.P_COLUMN_CALL, trackEventModel());
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
            jsonObject.put(Const.P_COLUMN_CITY, mRestaurantDetail.getCity());
            jsonObject.put(Const.P_COLUMN_CATEGORY, mRestaurantDetail.getCategory());
            jsonObject.put(Const.P_COLUMN_PLACE, mRestaurantDetail.getPlace());
            jsonObject.put(Const.P_COLUMN_PLATFORM, "Android");
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




