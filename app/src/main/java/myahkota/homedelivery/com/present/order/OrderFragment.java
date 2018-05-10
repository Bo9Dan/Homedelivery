package myahkota.homedelivery.com.present.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vistrav.ask.Ask;

import org.json.JSONException;
import org.json.JSONObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.ParcelableDTO;
import myahkota.homedelivery.com.present.view.CircleImageView;

public class OrderFragment extends AppCompatActivity implements View.OnClickListener {

    //region var UI
    private WebView webView;
    private FloatingActionButton fabCall;
    private TextView tvWorking, tvDelivery;

    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout behindBarView;
    private AppBarLayout appBarLayout;
    private CircleImageView icon;
    private ImageView backArrow;
    private Toolbar toolbar;
    private TextView title;
    //endregion

    private ParcelableDTO mRestaurantDetail;

    /*public static OrderFragment newInstance(ParcelableDTO _placeDetail) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putParcelable(Const.P_COLUMN_OBJECT, _placeDetail);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);
        findUI();
        setListeners();
        loadData();
    }

    private void loadData() {
        if (getIntent().getParcelableExtra(Const.P_COLUMN_OBJECT) != null) {
            mRestaurantDetail = getIntent().getParcelableExtra(Const.P_COLUMN_OBJECT);
            retrieveIcon(mRestaurantDetail);
            fillData(mRestaurantDetail);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setListeners() {
        webView.getSettings().setJavaScriptEnabled(true);
        backArrow.setOnClickListener(this);
        fabCall.setOnClickListener(this);
    }

    private void findUI() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        behindBarView = (FrameLayout) findViewById(R.id.info_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        backArrow = (ImageView) findViewById(R.id.ivBackBtn_AT);
        icon = (CircleImageView) findViewById(R.id.ivLogoIcon_AT);
        title = (TextView) findViewById(R.id.tvToolbarTitle_AT);

        tvWorking = (TextView) findViewById(R.id.tvWorkingTime);
        tvDelivery = (TextView) findViewById(R.id.tvDeliveryTime);
        fabCall = (FloatingActionButton) findViewById(R.id.fab);
        webView = (WebView) findViewById(R.id.wvListMenu);
    }

    private void fillData(ParcelableDTO model) {
        title.setText(model.getPlace());
        tvWorking.setText(model.getWork());
        tvDelivery.setText(String.format("%s %s", model.getDelivery(), getResources().getString(R.string.delivery_time)));
        webView.loadDataWithBaseURL(null, model.getMenu(), "text/html", "UTF-8", "");
        if (model.getCondition() != null && !model.getCondition().isEmpty()) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, model.getCondition(), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    // region Palette coloring UI
    private void retrieveIcon(ParcelableDTO model) {
        Glide.with(this)
                .load(model.getIcon())
                .asBitmap()
                .into(new BitmapImageViewTarget(icon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                applyPalette(palette);
                            }
                        });
                        super.setResource(resource);
                    }
                });
    }

    private void applyPalette(Palette palette) {
        final Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        int bgColor = ContextCompat.getColor(this, R.color.black_color);
        int txColor = ContextCompat.getColor(this, R.color.white_color);
        int acColor = ContextCompat.getColor(this, R.color.category5);

        if (vibrantSwatch != null) {
            bgColor = palette.getDarkMutedColor(bgColor);
            txColor = palette.getLightVibrantColor(txColor);
            acColor = palette.getLightMutedColor(acColor);
        }

        appBarLayout.setBackgroundColor(bgColor);
        toolbarLayout.setBackgroundColor(bgColor);
        toolbarLayout.setContentScrimColor(bgColor);
        toolbarLayout.setStatusBarScrimColor(bgColor);
        toolbar.setBackgroundColor(bgColor);
        behindBarView.setBackgroundColor(bgColor);

        fabCall.setBackgroundTintList(ColorStateList.valueOf(acColor));
        title.setTextColor(txColor);

        tvWorking.setTextColor(txColor);
        tvDelivery.setTextColor(txColor);
        setTextViewDrawableColor(tvWorking, txColor);
        setTextViewDrawableColor(tvDelivery, txColor);

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }
    //endregion

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackBtn_AT:
                onBackPressed();
                break;
            case R.id.fab:
                callHim();
                break;
        }
    }

    private void callHim() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mRestaurantDetail.getPhone()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
            return;
        }
        startActivity(intent);
        Amplitude.getInstance().logEvent(Const.P_COLUMN_CALL, trackEventModel());
    }

    public void getPermissions() {
        Ask.on(this)
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
}
