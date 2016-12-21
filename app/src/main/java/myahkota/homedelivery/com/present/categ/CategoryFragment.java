package myahkota.homedelivery.com.present.categ;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.ParcelableDTO;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.BaseGridFragment;
import myahkota.homedelivery.com.present.MenuController;
import myahkota.homedelivery.com.present.place.PlaceAdapter;
import myahkota.homedelivery.com.present.place.PlaceFragment;
import myahkota.homedelivery.com.present.view.OptionDialog;

public class CategoryFragment extends BaseGridFragment {

    private String mCity;
    private DataProvider provider = new DataProvider();
    private boolean isDefaultCategory = true;
    private PlaceAdapter placeAdapter;

    public static CategoryFragment newInstance(final String city) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(Const.CITY_KEY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MenuController.getInstance().setActiveSearch(true);
    }

    @Override
    public void onBaseResume() {
        MenuController.getInstance().setActiveSearch(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCity = getArguments().getString(Const.CITY_KEY, "");
        }
        SharedPrefManager.getInstance().saveCity(mCity);
        MenuController.getInstance().setTitle(SharedPrefManager.getInstance().retrieveCity());
        MenuController.getInstance().setSearchListener(toolListener);
        placeAdapter = new PlaceAdapter(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void getData() {
        provider.getCategoriesOff(callback);
    }

    @Override
    protected CategoryAdapter initAdapter(Fragment _context) {
        return new CategoryAdapter(_context.getContext());
    }

    @Override
    protected void onClickItem(ParseObject model) {
        if (isDefaultCategory) {
            Fragment frgPlace = PlaceFragment.newInstance(model.getString(Const.P_COLUMN_TITLE));
            replaceFragment(frgPlace, true);
        } else {
            getMain().takeScreen();
            OptionDialog placeDetail = OptionDialog.newInstance(getDTO(model));
            placeDetail.show(getFragmentManager(), Const.PLACE_KEY);
        }
    }

    @Override
    protected void onClickFooter() {
        openBrowser("");
    }

    @Override
    protected String EntityName() {
        return Const.CATEGORY_KEY;
    }

    @Override
    public void onBasePause() {
        MenuController.getInstance().setActiveSearch(false);
    }

    private ParcelableDTO getDTO(ParseObject object) {
        return new ParcelableDTO(object, "");
    }

    private FindCallback<ParseObject> callbackPlace = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                placeAdapter.setData(objects);
            }
        }
    };

    private MenuController.SearchListener toolListener = new MenuController.SearchListener() {
        @Override
        public void onActiveSearch() {
            view.setAdapter(placeAdapter);
            isDefaultCategory = false;
        }

        @Override
        public void ondDeactivatedSearch() {
            isDefaultCategory = true;
            view.setAdapter(getAdapter());
        }

        @Override
        public void onSearchClick(String searchWord) {
            Toast.makeText(getContext(), searchWord, Toast.LENGTH_LONG).show();
            provider.getSearchPlaces(callbackPlace, mCity, searchWord);
        }

        @Override
        public void onSearchCancelClick() {

        }
    };
}
