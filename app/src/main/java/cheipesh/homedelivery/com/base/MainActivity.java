package cheipesh.homedelivery.com.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.commit451.nativestackblur.NativeStackBlur;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.adapters.MenuAdapter;
import cheipesh.homedelivery.com.fragments.FoodFragment;
import cheipesh.homedelivery.com.fragments.CityFragment;
import cheipesh.homedelivery.com.fragments.RestaurantFragment;
import cheipesh.homedelivery.com.fragments.SplashDialog;
import cheipesh.homedelivery.com.fragments.LoadingDialog;
import cheipesh.homedelivery.com.util.SharedPrefManager;

public class MainActivity extends AppCompatActivity
implements RadioGroup.OnCheckedChangeListener{

    private LoadingDialog progressDialog;

    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    private RadioGroup mTabGroup;
    private ImageView toolbarMenu, drawerMenu, menuBack;
    private TextView toolbarTitle;
    private ListView menuList;
    private FrameLayout frameCont;
    private LinearLayout layout;

    private int frameHeight;
    private int frameWidth;

    private List<ParseObject> mCities;
    private List<ParseObject> mCategories;

    private MenuAdapter menuAdapter;
    Bitmap drawable;

    public Bitmap getDrawable() {
        return drawable;
    }

    public void setDrawable() {
        layout.setDrawingCacheEnabled(true);
         this.drawable = NativeStackBlur.process(layout.getDrawingCache(), 36);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findUI();
        setupUI();
        getNextFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    private void getNextFragment() {

        final SplashDialog splashDialog = new SplashDialog();
        splashDialog.show(getSupportFragmentManager(), "Splash");

        ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                if (!SharedPrefManager.getInstance().retrieveCity().isEmpty()){
                    replaceFragment(FoodFragment.newInstance(
                            SharedPrefManager.getInstance().retrieveCity()),
                            false);
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    replaceFragment(new CityFragment(), false);
                }
                splashDialog.dismiss();
            }
        };
        worker.schedule(task, 2, TimeUnit.SECONDS);
        getData(Constants.CATEGORY_KEY);
    }




    private void findUI() {
        drawerLayout    = (DrawerLayout) findViewById(R.id.dlDrawer);
        mToolbar        = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle    = (TextView) /*mToolbar.*/findViewById(R.id.tvToolbarTitle);
        toolbarMenu     = (ImageView) mToolbar.findViewById(R.id.menu_icon);
        menuBack        = (ImageView) mToolbar.findViewById(R.id.menu_back);
        layout          = (LinearLayout) findViewById(R.id.mainContainer);
        mTabGroup       = (RadioGroup) findViewById(R.id.rgTab);
        menuList        = (ListView) findViewById(R.id.lvMenu);

        frameCont = (FrameLayout) findViewById(R.id.flBaseFrame);
        frameCont.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        frameCont.getViewTreeObserver().removeGlobalOnLayoutListener( this );
                        frameHeight = frameCont.getHeight();
                        frameWidth = frameCont.getWidth();
                    }
                });
    }


    private void setupUI() {
        toolbarMenu.setOnClickListener(nawigateDrawerListener);
        menuBack.setOnClickListener(backClick);
        mTabGroup.setOnCheckedChangeListener(this);
        menuAdapter = new MenuAdapter(this);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(cityOnItemClickListener);
    }

    public void setMenuBack(Boolean isVisible) {
        menuBack.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    private AdapterView.OnItemClickListener cityOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(Gravity.END);
            SharedPrefManager.getInstance().saveCity(menuAdapter.getItem(position).getString(Constants.P_COLUMN_TITLE));
            replaceFragment(FoodFragment
                            .newInstance(menuAdapter.getItem(position).getString("title"))
                    , true);
        }
    };

    private AdapterView.OnItemClickListener categoryOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(Gravity.END);
            replaceFragment(RestaurantFragment
                            .newInstance(menuAdapter.getItem(position).getObjectId(),
                                    menuAdapter.getItem(position).getString(Constants.P_COLUMN_TITLE))
                    , true);
        }
    };


    private void getData(String _key) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(_key);
        if (_key.equals(Constants.CATEGORY_KEY)){
            query.whereEqualTo(Constants.P_COLUMN_CITY,
                    SharedPrefManager.getInstance().retrieveCity());
        }

        if (isOnline()) {
            onlineData(query);
        } else {
            offlineData(query);
        }
    }



    private void onlineData(final ParseQuery<ParseObject> _query) {
        _query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    setDate(objects, false);
                } else {
                    offlineData(_query);
                    Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void offlineData(ParseQuery<ParseObject> _query) {
        _query.fromLocalDatastore();
        _query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    setDate(objects, true);
                } else {
                    hideLoadingDialog();
                    Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void setDate(List<ParseObject> _data, boolean isLocal) {
        if (!isLocal) {
            ParseObject.pinAllInBackground(_data);
        }
        menuAdapter.setData(_data);
        hideLoadingDialog();
    }



    private View.OnClickListener nawigateDrawerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(Gravity.END); //CLOSE Nav Drawer!
            }else{
                drawerLayout.openDrawer(Gravity.END); //OPEN Nav Drawer!
            }
        }
    };

    private View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };


    public void clickableMenu(boolean b) {
        toolbarMenu.setClickable(b);
    }

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    public void replaceFragment(Fragment _fragment, boolean _addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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

    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new LoadingDialog();
        }
        if (!progressDialog.isShowing())
            progressDialog.show(getSupportFragmentManager(), "");
    }

    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }



    public void setTitle(String _title) {
        toolbarTitle.setText(_title);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rbTabCity:
                getData(Constants.CITY_KEY);
                menuList.setOnItemClickListener(cityOnItemClickListener);
                break;
            case R.id.rbTabCat:
                getData(Constants.CATEGORY_KEY);
                menuList.setOnItemClickListener(categoryOnItemClickListener);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menuBack.setVisibility(View.INVISIBLE);
    }
}
