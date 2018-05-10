package myahkota.homedelivery.com.present.main;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;

public interface Root {

    void replaceFragment(Fragment frg, View view);

    void screenShoot();

    void takeItem(Bitmap drawable);

    Bitmap fetchItemBitmap();

    Bitmap fetchBehind();

    void toggleDrawer();

    void back();

}
