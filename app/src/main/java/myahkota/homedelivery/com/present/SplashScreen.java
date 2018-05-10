package myahkota.homedelivery.com.present;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import myahkota.homedelivery.com.App;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.main.MainActivity;

public class SplashScreen extends AppCompatActivity {

    private DataProvider dataProvider = new DataProvider();
    private Integer width, toolbarHeight, height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ImageView appIcon = (ImageView) findViewById(R.id.ivSplashIcon);
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1800);
        rotate.setRepeatCount(10);
        rotate.setRepeatMode(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        appIcon.startAnimation(rotate);


        if (hasNewData() && App.getInstance().isOnline()) {
            clearPrevData();
            dataProvider.getCitiesOn(callbackCity);
        } else {
            ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                public void run() {
                    openMain();
                }
            };
            worker.schedule(task, 3700, TimeUnit.MILLISECONDS);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final int value = SharedPrefManager.getInstance().retrieveHeight();
        if (value == -1) {
            final ImageView view = (ImageView) findViewById(R.id.fullView);
            view.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            height = view.getHeight();
                            width = view.getWidth();
                        }
                    });
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            toolbarHeight = toolbar.getHeight();
                        }
                    });

        }
    }

    private FindCallback<ParseObject> callbackCity = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                ParseObject.pinAllInBackground(objects);
                dataProvider.getCategoriesOn(callbackCategory);
            }
        }
    };

    private FindCallback<ParseObject> callbackCategory = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                ParseObject.pinAllInBackground(objects);
                dataProvider.getPlacesOn(callbackPlace);
            }
        }
    };

    private FindCallback<ParseObject> callbackPlace = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                ParseObject.pinAllInBackground(objects);
                SharedPrefManager.getInstance().savePinDate();
                openMain();
            }
        }
    };

    private void openMain() {
        if (toolbarHeight != null) {
            SharedPrefManager.getInstance().saveWidth(width);
            SharedPrefManager.getInstance().saveHeight(height);
            SharedPrefManager.getInstance().saveFrameHeight(height - toolbarHeight);
        }
        supportFinishAfterTransition();
        startActivity(new Intent(this, MainActivity.class));
    }

    @WorkerThread
    private void clearPrevData() {
        dataProvider.clearParse();
//        Glide.get(this).clearDiskCache();
//        Glide.get(this).clearMemory();
    }

    private boolean hasNewData() {
        long saveDate = SharedPrefManager.getInstance().retrievePinDate();
        long nowDate = System.currentTimeMillis();
        return saveDate == -1 || nowDate - saveDate > TimeUnit.HOURS.toMillis(4);
//        return false;
    }

}
