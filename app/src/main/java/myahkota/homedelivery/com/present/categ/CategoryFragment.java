package myahkota.homedelivery.com.present.categ;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;

import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.BaseGridFragment;
import myahkota.homedelivery.com.present.place.PlaceFragment;

public class CategoryFragment extends BaseGridFragment {

    private String mCity;
    private DataProvider provider = new DataProvider();

    public static CategoryFragment newInstance(final String city) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(Const.CITY_KEY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCity = getArguments().getString(Const.CITY_KEY, "");
        }
        SharedPrefManager.getInstance().saveCity(mCity);
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
        Fragment frgPlace = PlaceFragment.newInstance(model.getString(Const.P_COLUMN_TITLE));
        replaceFragment(frgPlace, true);
    }

    @Override
    protected void onClickFooter() {
        openBrowser("");
    }

    @Override
    protected String EntityName() {
        return Const.CATEGORY_KEY;
    }
}
