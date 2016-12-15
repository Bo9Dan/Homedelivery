package myahkota.homedelivery.com.present;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import myahkota.homedelivery.com.App;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

    private DataProvider dataProvider = new DataProvider();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (hasNewData() && App.getInstance().isOnline()) {
            dataProvider.getCitiesOn(callbackCity);

//            dataProvider.getPlacesOn(callbackPlace);
        } else {
            openMain();
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

    private FindCallback<ParseObject> callbackCategory = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                ParseObject.pinAllInBackground(objects);
                dataProvider.getPlacesOn(callbackPlace);
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
        if (saveDate == -1) {
            return true;
        } else
            return nowDate - saveDate >  24 * 60 * 60 * 1000 * 2;
    }

}
