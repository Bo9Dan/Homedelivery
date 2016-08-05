package cheipesh.homedelivery.com;

import android.app.Application;
import android.content.Context;

import com.amplitude.api.Amplitude;
import com.parse.Parse;

import cheipesh.homedelivery.com.base.Constants;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Amplitude.getInstance().initialize(this, Constants.AMPLITUDE_API_KEY).enableForegroundTracking(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .server(Constants.PROJECT_URL)
                .applicationId(Constants.PROJECT_ID)
                .clientKey(Constants.CLIENT_ID)
                .enableLocalDataStore()
                .build()
        );

    }


    public static Context getAppContext() {
        return mContext;
    }
}
