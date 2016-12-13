package myahkota.homedelivery.com;

import android.app.Application;
import com.amplitude.api.Amplitude;
import com.parse.Parse;

import myahkota.homedelivery.com.data.Const;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Amplitude.getInstance()
                .initialize(this, Const.AMPLITUDE_API_KEY)
                .enableForegroundTracking(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .server(Const.PROJECT_URL)
                .applicationId(Const.PROJECT_ID)
                .clientKey(Const.CLIENT_ID)
                .enableLocalDataStore()
                .build()
        );

    }

    public static App getInstance() {
        return instance;
    }
}
