package myahkota.homedelivery.com.present;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

import myahkota.homedelivery.com.R;

public class MenuController {


    private static MenuController instance;

    private Context context;

    private ImageView menuBtn, searchBtn, backBtn;
    private TextView titleApp, cancelBtn;
    private EditText searchInput;

    private RelativeLayout basicBar;
    private LinearLayout searchBar;

    private ActionMenuListener actionListener;
    private SearchListener searchListener;


    public MenuController(Context cntx) {
        this.context = cntx;
        instance = this;
    }

    public static MenuController getInstance() {
        return instance;
    }

    @SuppressLint("InflateParams")
    public void initBar(Toolbar rootView) {
//        rootView = LayoutInflater.from(context).inflate(R.layout.app_toolbar, null);

        basicBar    = (RelativeLayout) rootView.findViewById(R.id.rlBasicBar_AT);
        searchBar   = (LinearLayout) rootView.findViewById(R.id.llSearchBar_AT);

        menuBtn     = (ImageView) rootView.findViewById(R.id.ivMenuBtn_AT);
        searchBtn   = (ImageView) rootView.findViewById(R.id.ivSearchBtn_AT);
        backBtn     = (ImageView) rootView.findViewById(R.id.ivBackBtn_AT);

        titleApp    = (TextView) rootView.findViewById(R.id.tvToolbarTitle_AT);
        cancelBtn   = (TextView) rootView.findViewById(R.id.tvCancelBtb_AT);
        searchInput = (EditText) rootView.findViewById(R.id.etSearchInput_AT);
    }

    private void connectActionListener() {
        menuBtn.setOnClickListener(controllerListener);

        backBtn.setOnClickListener(controllerListener);

        titleApp.setOnClickListener(controllerListener);


        titleApp.setTypeface(Typer.set(context).getFont(Font.ROBOTO_REGULAR));
    }

    private void connectSearchListener() {
        searchBtn.setOnClickListener(controllerListener);
        cancelBtn.setOnClickListener(controllerListener);
        searchInput.setOnEditorActionListener(searchActionListener);
    }

    private View.OnClickListener controllerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivMenuBtn_AT:
                    actionListener.onMenuClick();
                    break;
                case R.id.ivSearchBtn_AT:
                    setSearchVisible(true);
                    searchListener.onActiveSearch();
                    break;
                case R.id.ivBackBtn_AT:
                    actionListener.onBackClick();
                    break;
                case R.id.tvToolbarTitle_AT:
                    actionListener.onLogoClick();
                    break;
                case R.id.tvCancelBtb_AT:
                    setSearchVisible(false);
                    searchListener.onSearchCancelClick();
                    searchListener.ondDeactivatedSearch();
                    break;

            }
        }
    };

    public void setActionListener(ActionMenuListener actionListener) {
        if (actionListener != null) {
            this.actionListener = actionListener;
            connectActionListener();
        }
    }

    public void setSearchListener(SearchListener searchListener) {
        if (searchListener != null) {
            this.searchListener = searchListener;
            connectSearchListener();
        }
    }
    
    private void setSearchVisible(boolean searchState) {
        if (searchState) {
            searchBar.setVisibility(View.VISIBLE);
            basicBar.setVisibility(View.GONE);
        } else {
            searchBar.setVisibility(View.GONE);
            basicBar.setVisibility(View.VISIBLE);
        }
    }


    private TextView.OnEditorActionListener searchActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionListener!=null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchListener.onSearchClick(v.getText().toString());
                return true;
            }
            return false;
        }
    };

    public void setTitle(String title) {
        titleApp.setText(title);
    }

    public void setActiveSearch(boolean activated) {
        searchBtn.setVisibility(activated ? View.VISIBLE : View.GONE);
    }

    public void setActiveBackBtn(boolean activated) {
        backBtn.setVisibility(activated ? View.VISIBLE : View.GONE);
    }




    public interface ActionMenuListener {

        void onMenuClick();

        void onBackClick();

        void onLogoClick();
    }
    
    public interface SearchListener {
        
        void onActiveSearch();

        void ondDeactivatedSearch();

        void onSearchClick(String searchWord);

        void onSearchCancelClick();
    }

}
