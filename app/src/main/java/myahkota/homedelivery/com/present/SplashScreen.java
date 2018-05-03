package myahkota.homedelivery.com.present;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
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
import myahkota.homedelivery.com.present.view.LoadingDialog;

public class SplashScreen extends AppCompatActivity {

    private DataProvider dataProvider = new DataProvider();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (hasNewData() && App.getInstance().isOnline()) {
            dataProvider.clearParse();
            dataProvider.getCitiesOn(callbackCity);
        } else {
            ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                    public void run() {
                        openMain();
                    }
            };
            worker.schedule(task, 1, TimeUnit.SECONDS);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final int value = SharedPrefManager.getInstance().retrieveHeight();
//        if (value == -1) {
            final ImageView view = (ImageView) findViewById(R.id.ivSplashIcon);
            view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        SharedPrefManager.getInstance().saveHeight(view.getHeight());
                        SharedPrefManager.getInstance().saveWidth(view.getWidth());
                    }
                });
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener(){
                        @Override
                        public void onGlobalLayout() {
                            toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            SharedPrefManager.getInstance().saveFrameHeight(SharedPrefManager.getInstance().retrieveHeight() - toolbar.getHeight());
                        }
                    });

//        }
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
        supportFinishAfterTransition();
        startActivity(new Intent(this, MainActivity.class));
    }

    private boolean hasNewData() {
        long saveDate = SharedPrefManager.getInstance().retrievePinDate();
        long nowDate = System.currentTimeMillis();
        return saveDate == -1 || nowDate - saveDate > TimeUnit.DAYS.toMillis(1);
    }

}
