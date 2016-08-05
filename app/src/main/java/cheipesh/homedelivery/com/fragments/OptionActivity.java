package cheipesh.homedelivery.com.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.base.Constants;

public class OptionActivity extends AppCompatActivity {


    private WebView wvRestarauntMenuPrice;
    private RoundedImageView rivTopButton;
    private TextView btnCall, btnExpandMenu;
    private ImageView ivBackground;
    private String mPhoneNumber, mTextMenuPrice;
    private ExpandableRelativeLayout expandableLinearLayout;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_option);
        if (getIntent().getExtras() != null) {
            getOptions(getIntent().getExtras());
        }

        expandableLinearLayout = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        ivBackground = (ImageView) findViewById(R.id.ivOptionBackground);

        rivTopButton = (RoundedImageView) findViewById(R.id.ivOptionMenu);

        wvRestarauntMenuPrice = (WebView) findViewById(R.id.wvListMenu);

        btnCall = (TextView) findViewById(R.id.btnCall);
        btnExpandMenu = (TextView) findViewById(R.id.btnShowList);

//        Glide.with(this)
//                .load(mMap)
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .into(rivTopButton);

        wvRestarauntMenuPrice.loadDataWithBaseURL("", mTextMenuPrice, "text/html", "UTF-8", "");
        rivTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLinearLayout.toggle();
            }
        });
//        rivTopButton.setOnClickListener(optionClickListener);
//        btnExpandMenu.setOnClickListener(optionClickListener);
        btnCall.setOnClickListener(callListener);

    }

    private void getOptions(Bundle bundle) {
        mTextMenuPrice = bundle.getString(Constants.TFG_OPTION_MENU_LIST);
        mPhoneNumber = bundle.getString(Constants.TFG_OPTION_PHONE_NUMBER);
    }


    private View.OnClickListener optionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (wvRestarauntMenuPrice.getVisibility()){
                case View.VISIBLE:
                    wvRestarauntMenuPrice.setVisibility(View.GONE);
                    break;
                case View.GONE:
                    wvRestarauntMenuPrice.setVisibility(View.VISIBLE);
//                    wvRestarauntMenuPrice.loadDataWithBaseURL("", mTextMenuPrice, "text/html", "UTF-8", "");
                    break;
                case View.INVISIBLE:
                    wvRestarauntMenuPrice.setVisibility(View.GONE);
                    break;
                default:
                    wvRestarauntMenuPrice.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPhoneNumber));
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
            startActivity(intent);
        }
    };


    public void getPermiss() {
        Ask.on(this)
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
