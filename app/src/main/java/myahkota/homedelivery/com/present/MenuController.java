package myahkota.homedelivery.com.present;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

public class MenuController implements View.OnClickListener{


    private Context context;
    private View rootView;
    private Toolbar toolbar;

    private ImageView menuBtn, searchBtn, backBtn;
    private TextView titleApp, cancelBtn;
    private EditText searchInput;

    private RelativeLayout basicBar;
    private LinearLayout searchBar;

    private ActionMenuListener actionListener;


    public MenuController(Context cntx) {
        this.context = cntx;
        initBar();
    }

    @SuppressLint("InflateParams")
    public void initBar() {
        rootView = LayoutInflater.from(context).inflate(R.layout.app_toolbar, null);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);


        basicBar    = (RelativeLayout) rootView.findViewById(R.id.rlBasicBar_AT);
        searchBar   = (LinearLayout) rootView.findViewById(R.id.llSearchBar_AT);

        menuBtn     = (ImageView) rootView.findViewById(R.id.ivMenuBtn_AT);
        searchBtn   = (ImageView) rootView.findViewById(R.id.ivSearchBtn_AT);
        backBtn     = (ImageView) rootView.findViewById(R.id.ivBackBtn_AT);

        titleApp    = (TextView) rootView.findViewById(R.id.tvToolbarTitle_AT);
        cancelBtn   = (TextView) rootView.findViewById(R.id.tvCancelBtb_AT);
        searchInput = (EditText) rootView.findViewById(R.id.etSearchInput_AT);
    }

    public void prepareBar() {
        menuBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(controllerListener);
        backBtn.setOnClickListener(controllerListener);

        titleApp.setOnClickListener(this);
        cancelBtn.setOnClickListener(controllerListener);
        searchInput.setOnEditorActionListener(searchActionListener);
        titleApp.setTypeface(Typer.set(context).getFont(Font.ROBOTO_REGULAR));
    }

    public Toolbar getToolbar() {
        return toolbar;
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
                    actionListener.onActiveSearch();
                    break;
                case R.id.ivBackBtn_AT:
                    actionListener.onBackClick();
                    break;
                case R.id.tvToolbarTitle_AT:
                    actionListener.onLogoClick();
                    break;
                case R.id.tvCancelBtb_AT:
                    setSearchVisible(false);
                    actionListener.onSearchCancelClick();
                    actionListener.ondDeactivatedSearch();
                    break;

            }
        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMenuBtn_AT:
                actionListener.onMenuClick();
                break;
            case R.id.ivSearchBtn_AT:
                setSearchVisible(true);
                actionListener.onActiveSearch();
                break;
            case R.id.ivBackBtn_AT:
                actionListener.onBackClick();
                break;
            case R.id.tvToolbarTitle_AT:
                actionListener.onLogoClick();
                break;
            case R.id.tvCancelBtb_AT:
                setSearchVisible(false);
                actionListener.onSearchCancelClick();
                actionListener.ondDeactivatedSearch();
                break;

        }
    }

    public void setActionListener(ActionMenuListener actionListener) {
        if (actionListener != null) {
            this.actionListener = actionListener;
            prepareBar();
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
                actionListener.onSearchClick(v.getText().toString());
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

        void onActiveSearch();

        void ondDeactivatedSearch();

        void onSearchClick(String searchWord);

        void onSearchCancelClick();


    }


}
