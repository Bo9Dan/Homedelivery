package myahkota.homedelivery.com;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amplitude.api.Amplitude;
import com.crashlytics.android.Crashlytics;
import com.parse.Parse;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import myahkota.homedelivery.com.data.Const;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Fabric.with(this, new Crashlytics());

        Amplitude.getInstance()
                .initialize(this, Const.AMPLITUDE_API_KEY)
                .enableForegroundTracking(this);

        Parse.enableLocalDatastore(this);

        deleteInstallationCache(this);

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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean deleteInstallationCache(Context context) {
        boolean deletedParseFolder = false;
        File cacheDir = context.getCacheDir();
        File parseApp;
        parseApp = new File(cacheDir.getParent(),"app_Parse");
        File installationId = new File(parseApp,"installationId");
        File currentInstallation = new File(parseApp,"currentInstallation");
        if(installationId.exists()) {
            deletedParseFolder = deletedParseFolder || installationId.delete();
        }
        if(currentInstallation.exists()) {
            deletedParseFolder = deletedParseFolder && currentInstallation.delete();
        }
        return deletedParseFolder;
    }
}
