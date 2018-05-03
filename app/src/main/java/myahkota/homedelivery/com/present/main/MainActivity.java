package myahkota.homedelivery.com.present.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.commit451.nativestackblur.NativeStackBlur;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.categ.CategoryFragment;
import myahkota.homedelivery.com.present.city.CityFragment;
import myahkota.homedelivery.com.present.place.PlaceFragment;

import static android.view.Gravity.RIGHT;

public class MainActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener, Root, View.OnClickListener {

    private DataProvider provider = new DataProvider();
    private MenuAdapter menuAdapter;

    private ImageView menuView;
    private FrameLayout fragmentContainer;
    private DrawerLayout drawerLayout;
    private RadioGroup tabGroup;
    private ListView menuList;

    private Bitmap drawableBack, drawableOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        menuAdapter = new MenuAdapter(this);
        findUI();
        setupUI();

        provider.getCategoriesOff(categoryCallBack);
        provider.getCitiesOff(cityCallBack);
        getNextFragment();

    }

    private void getNextFragment() {
        final String city = SharedPrefManager.getInstance().retrieveCity();
        replaceFragment(city.isEmpty() ? new CityFragment() : CategoryFragment.newInstance(city), false);
    }

    private void findUI() {
        fragmentContainer = (FrameLayout) findViewById(R.id.flBaseFrame);
        drawerLayout = (DrawerLayout) findViewById(R.id.dlDrawer);
        menuView = (ImageView) findViewById(R.id.ivMenuBtn_AT);
        tabGroup = (RadioGroup) findViewById(R.id.rgTab);
        menuList = (ListView) findViewById(R.id.lvMenu);
    }

    private void setupUI() {
        tabGroup.setOnCheckedChangeListener(this);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(menuOnClickListener);
        menuView.setOnClickListener(this);
    }

    private AdapterView.OnItemClickListener menuOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            toggleDrawer();

            final String objectName = menuAdapter.getItem(position).getString(Const.P_COLUMN_TITLE);
            final boolean isCategory = menuAdapter.isCategory();
            replaceFragment(isCategory ? PlaceFragment.newInstance(objectName)
                            : CategoryFragment.newInstance(objectName),
                    false);
        }
    };

    protected FindCallback<ParseObject> cityCallBack = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) menuAdapter.setCityDataList(objects);
        }
    };

    protected FindCallback<ParseObject> categoryCallBack = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) menuAdapter.setCategoryDataList(objects);
        }
    };


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbTabCity:
                menuAdapter.choosingCities();
                break;
            case R.id.rbTabCat:
                menuAdapter.shoesCategories();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        toggleDrawer();
    }

    @Override
    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(RIGHT)) {
            drawerLayout.closeDrawer(RIGHT); //CLOSE Nav Drawer!
        } else {
            drawerLayout.openDrawer(RIGHT); //OPEN Nav Drawer!
        }
    }

    @Override
    public void takeItem(Bitmap drawable) {
        this.drawableOption = drawable;
    }

    @Override
    public Bitmap fetchItemBitmap() {
        return drawableOption;
    }

    @Override
    public void replaceFragment(Fragment _fragment, boolean _addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (_addToBackStack) fragmentTransaction.addToBackStack(null);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flBaseFrame);
        if (fragment != null)
            fragmentTransaction.hide(fragment);
        if (_addToBackStack) {
            fragmentTransaction.add(R.id.flBaseFrame, _fragment, _fragment.getClass().getName());
        } else {
            fragmentTransaction.replace(R.id.flBaseFrame, _fragment, _fragment.getClass().getName());
        }
        fragmentTransaction.show(_fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void screenShoot() {
        fragmentContainer.setDrawingCacheEnabled(true);
        this.drawableBack = NativeStackBlur.process(fragmentContainer.getDrawingCache(), 110);
    }

    @Override
    public Bitmap fetchBehind() {
        return drawableBack;
    }

    public Root getRoot() {
        return this;
    }

    @Override
    public void back() {
        onBackPressed();
    }


}
