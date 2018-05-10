package myahkota.homedelivery.com.present.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

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

import static android.view.Gravity.END;

public class MainActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener, Root, View.OnClickListener {

    private DataProvider provider = new DataProvider();
    private MenuAdapter menuAdapter = new MenuAdapter(this);

    private DrawerLayout drawerLayout;
    private RadioGroup tabGroup;
    private ImageView menuView;
    private ListView menuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findUI();
        setupUI();

        provider.getCategoriesOff(categoryCallBack);
        provider.getCitiesOff(cityCallBack);
        getNextFragment();
    }

    private void getNextFragment() {
        final String city = SharedPrefManager.getInstance().retrieveCity();
        replaceFragment(city.isEmpty() ? new CityFragment() : CategoryFragment.newInstance(city), null);
    }

    private void findUI() {
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
                    null);
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
        if (drawerLayout.isDrawerOpen(END)) {
            drawerLayout.closeDrawer(END); //CLOSE Nav Drawer!
        } else {
            drawerLayout.openDrawer(END); //OPEN Nav Drawer!
        }
    }

    @Override
    public void replaceFragment(Fragment _fragment, View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("TAG")
                .replace(R.id.flBaseFrame, _fragment)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void replaceFragmentView(Fragment fragment, View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(view, ViewCompat.getTransitionName(view))
                .addToBackStack("TAG")
                .replace(R.id.flBaseFrame, fragment)
                .commit();
    }


    public Root getRoot() {
        return this;
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) finish();

        if (drawerLayout.isDrawerOpen(Gravity.END)) {
            toggleDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
