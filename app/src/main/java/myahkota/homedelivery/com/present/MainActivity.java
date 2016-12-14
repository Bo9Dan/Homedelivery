package myahkota.homedelivery.com.present;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commit451.nativestackblur.NativeStackBlur;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.SplashDialog;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.categ.CategoryFragment;
import myahkota.homedelivery.com.present.city.CityFragment;
import myahkota.homedelivery.com.present.fragments.OldRestaurantFragment;
import myahkota.homedelivery.com.present.place.PlaceFragment;
import myahkota.homedelivery.com.present.view.LoadingDialog;

import static android.view.Gravity.END;

public class MainActivity extends AppCompatActivity
                implements RadioGroup.OnCheckedChangeListener{

    private LoadingDialog progressDialog;

    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    private RadioGroup mTabGroup;
    private ImageView toolbarMenu, search, menuBack;
    private TextView toolbarTitle;
    private ListView menuList;
    private FrameLayout frameCont;
    private LinearLayout layout;

    private RelativeLayout infoActionBar;
    private LinearLayout searchActionBar;
    private TextView cancel;
    private EditText inputView;


    private int frameHeight;
    private int frameWidth;

    private MenuAdapter menuAdapter;
    private Bitmap drawableBack, drawableOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getData(Const.CITY_KEY, true);
        getData(Const.CATEGORY_KEY, true);
        findUI();
        setupUI();
        getNextFragment();

    }

    private void getNextFragment() {

        final SplashDialog splashDialog = new SplashDialog();
        splashDialog.show(getSupportFragmentManager(), "Splash");

        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
            if (!SharedPrefManager.getInstance().retrieveCity().isEmpty()){
                replaceFragment(new CategoryFragment(), false);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                replaceFragment(new CityFragment(), false);
            }
            splashDialog.dismiss();
            }
        };
        worker.schedule(task, 2, TimeUnit.SECONDS);
    }

    private void findUI() {
        drawerLayout    = (DrawerLayout) findViewById(R.id.dlDrawer);
        mToolbar        = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle    = (TextView) /*mToolbar.*/findViewById(R.id.tvToolbarTitle);
        toolbarMenu     = (ImageView) mToolbar.findViewById(R.id.menu_icon);
        search          = (ImageView) mToolbar.findViewById(R.id.search_icon);
        menuBack        = (ImageView) mToolbar.findViewById(R.id.menu_back);
        layout          = (LinearLayout) findViewById(R.id.mainContainer);
        mTabGroup       = (RadioGroup) findViewById(R.id.rgTab);
        menuList        = (ListView) findViewById(R.id.lvMenu);

        toolbarTitle.setTypeface(Typer.set(this).getFont(Font.ROBOTO_REGULAR));
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


        infoActionBar = (RelativeLayout) findViewById(R.id.rlInfoCont);
        searchActionBar = (LinearLayout) findViewById(R.id.llSearchCont);
        cancel = (TextView) findViewById(R.id.tvSearchDisable);
        inputView = (EditText) findViewById(R.id.etSearch_input);

    }

    private void setupUI() {
        toolbarMenu.setOnClickListener(nawigateDrawerListener);
        toolbarTitle.setOnClickListener(nawigateDrawerListener);
        menuBack.setOnClickListener(backClick);
        mTabGroup.setOnCheckedChangeListener(this);
        menuAdapter = new MenuAdapter(this);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(cityOnItemClickListener);
        search.setOnClickListener(mSearchClickListener);
        cancel.setOnClickListener(mSearchClickListener);

        inputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    getData();
//                    ((BaseFragment) getSupportFragmentManager().findFragmentById(R.id.flBaseFrame)).search(inputView.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void setSearchVisible(boolean b) {
        search.setVisibility(b ? View.VISIBLE : View.GONE);
    }


    public void setMenuBack(Boolean isVisible) {
        menuBack.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    private AdapterView.OnItemClickListener cityOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            closeDriwer();
            SharedPrefManager.getInstance().saveCity(menuAdapter.getItem(position).getString(Const.P_COLUMN_TITLE));
            replaceFragment(new CategoryFragment(), true);
        }
    };

    private AdapterView.OnItemClickListener categoryOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            closeDriwer();
            replaceFragment(PlaceFragment
                            .newInstance(menuAdapter.getItem(position).getString(Const.P_COLUMN_TITLE))
                    , true);
        }
    };

    private View.OnClickListener mSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
           switch (v.getId()) {
               case R.id.search_icon:
                   infoActionBar.setVisibility(View.GONE);
                   searchActionBar.setVisibility(View.VISIBLE);
                   imm.showSoftInput(inputView, 0);
                   break;
               case R.id.tvSearchDisable:
                   imm.hideSoftInputFromWindow(inputView.getWindowToken(), 0);
                   infoActionBar.setVisibility(View.VISIBLE);
                   searchActionBar.setVisibility(View.GONE);
           }
        }
    };

    private void getData(String _key, boolean needOnline) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(_key);
        query.orderByAscending(Const.P_COLUMN_ORDER);
        if (isOnline() && needOnline) {
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
                    /*hideLoadingDialog();*/
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
        /*hideLoadingDialog();*/
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rbTabCity:
                getData(Const.CITY_KEY, false);
                menuList.setOnItemClickListener(cityOnItemClickListener);
                break;
            case R.id.rbTabCat:
                getData(Const.CATEGORY_KEY, false);
                menuList.setOnItemClickListener(categoryOnItemClickListener);
                break;
        }
    }

    private View.OnClickListener nawigateDrawerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvToolbarTitle:
                    getData(Const.CITY_KEY, false);
                    menuList.setOnItemClickListener(cityOnItemClickListener);
                    RadioButton rbCity = (RadioButton) mTabGroup.findViewById(R.id.rbTabCity);
                    rbCity.setChecked(true);
                    closeDriwer();
                    break;
                case R.id.menu_icon:
                    getData(Const.CATEGORY_KEY, false);
                    menuList.setOnItemClickListener(categoryOnItemClickListener);
                    RadioButton rbCat = (RadioButton) mTabGroup.findViewById(R.id.rbTabCat);
                    rbCat.setChecked(true);
                    closeDriwer();
                    break;
            }
        }
    };

    private void closeDriwer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(END); //CLOSE Nav Drawer!
        } else {
            drawerLayout.openDrawer(END); //OPEN Nav Drawer!
        }
    }

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

    public Bitmap getDrawableBack() {
        return drawableBack;
    }

    public Bitmap getDrawableOption() {
        return drawableOption;
    }

    public void takeScreen() {
        layout.setDrawingCacheEnabled(true);
        this.drawableBack = NativeStackBlur.process(layout.getDrawingCache(), 160);
    }

    public void setDrawableHamb(Bitmap drawable){
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



    public void setTitle(String _title) {
        toolbarTitle.setText(_title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menuBack.setVisibility(View.INVISIBLE);
    }
}
