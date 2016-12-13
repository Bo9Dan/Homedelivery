package myahkota.homedelivery.com;

import android.app.Application;
import android.content.Context;

import com.amplitude.api.Amplitude;
import com.parse.Parse;

import myahkota.homedelivery.com.base.Const;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Amplitude.getInstance().initialize(this, Const.AMPLITUDE_API_KEY).enableForegroundTracking(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .server(Const.PROJECT_URL)
                .applicationId(Const.PROJECT_ID)
                .clientKey(Const.CLIENT_ID)
                .enableLocalDataStore()
                .build()
        );

    }


    public static Context getAppContext() {
        return mContext;
    }
}
