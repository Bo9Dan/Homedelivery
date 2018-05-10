package myahkota.homedelivery.com.present.main;

import android.support.v4.app.Fragment;
import android.view.View;

public interface Root {

    void replaceFragment(Fragment frg, View view);

    void toggleDrawer();

    void back();

}
