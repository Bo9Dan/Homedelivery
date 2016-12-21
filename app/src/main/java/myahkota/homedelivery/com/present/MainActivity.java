package myahkota.homedelivery.com.present;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
        implements RadioGroup.OnCheckedChangeListener, MenuController.ActionMenuListener {

    private MenuController menuController;
    private MenuAdapter menuAdapter;
    private DataProvider provider;

    private FrameLayout fragmentContainer;
    private DrawerLayout drawerLayout;
    private RadioGroup tabGroup;
    private ListView menuList;
    private LinearLayout fullLayout;

    protected int frameHeight;
    protected int frameWidth;


    private Bitmap drawableBack, drawableOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        menuController = new MenuController(this);
        menuAdapter = new MenuAdapter(this);
        provider = new DataProvider();

        findUI();
        setupUI();

        provider.getCategoriesOff(categoryCallBack);
        provider.getCitiesOff(cityCallBack);
        getNextFragment();

    }

    private void getNextFragment() {
        if (!SharedPrefManager.getInstance().retrieveCity().isEmpty()){
            replaceFragment(CategoryFragment.newInstance(SharedPrefManager.getInstance().retrieveCity()), false);
        } else {
            replaceFragment(new CityFragment(), false);
        }
    }

    private void findUI() {
        menuController.initBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout    = (DrawerLayout) findViewById(R.id.dlDrawer);
        fullLayout      = (LinearLayout) findViewById(R.id.mainContainer);
        tabGroup        = (RadioGroup) findViewById(R.id.rgTab);
        menuList        = (ListView) findViewById(R.id.lvMenu);

        fragmentContainer = (FrameLayout) findViewById(R.id.flBaseFrame);
        fragmentContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        fragmentContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        frameHeight = fragmentContainer.getHeight();
                        frameWidth = fragmentContainer.getWidth();
                    }
                });

    }

    private void setupUI() {
        tabGroup.setOnCheckedChangeListener(this);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(menuOnClickListener);
        menuController.setActionListener(this);
    }

    private AdapterView.OnItemClickListener menuOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toggleDrawer();
        String objectName = menuAdapter.getItem(position).getString(Const.P_COLUMN_TITLE);

        if (menuAdapter.isCategory()) {
            replaceFragment(PlaceFragment.newInstance(objectName),true);
        } else {
            replaceFragment(CategoryFragment.newInstance(objectName), true);
        }
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
        switch (checkedId){
            case R.id.rbTabCity:
                menuAdapter.shoesCities();
                break;
            case R.id.rbTabCat:
                menuAdapter.shoesCategories();
                break;
        }
    }

    @Override
    public void onMenuClick() {
        RadioButton rbCat = (RadioButton) tabGroup.findViewById(R.id.rbTabCat);
        rbCat.setChecked(true);
        menuAdapter.shoesCategories();
        toggleDrawer();
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onLogoClick() {
        RadioButton rbCat = (RadioButton) tabGroup.findViewById(R.id.rbTabCity);
        rbCat.setChecked(true);
        menuAdapter.shoesCities();
        toggleDrawer();
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(RIGHT)) {
            drawerLayout.closeDrawer(RIGHT); //CLOSE Nav Drawer!
        } else {
            drawerLayout.openDrawer(RIGHT); //OPEN Nav Drawer!
        }
    }

    public Bitmap getDrawableBack() {
        return drawableBack;
    }

    public Bitmap getDrawableOption() {
        return drawableOption;
    }

    public void takeScreen() {
        fullLayout.setDrawingCacheEnabled(true);
        this.drawableBack = NativeStackBlur.process(fullLayout.getDrawingCache(), 160);
    }

    public void takeItem(Bitmap drawable){
        this.drawableOption = drawable;
    }

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

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

}
