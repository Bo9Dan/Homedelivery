package myahkota.homedelivery.com.present.categ;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.base.BaseGridFragment;
import myahkota.homedelivery.com.present.place.PlaceFragment;
import myahkota.homedelivery.com.present.search.place.SearchPlaceFragment;

public class CategoryFragment extends BaseGridFragment implements View.OnClickListener {

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
    public int getLayoutResource() {
        return R.layout.fragment_select_category;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView searchIcon = (ImageView) rootView.findViewById(R.id.ivSearchBtn_AT);
        TextView title = (TextView) rootView.findViewById(R.id.tvToolbarTitle_AT);
        title.setText(mCity);
        searchIcon.setOnClickListener(this);
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
    public void onClick(View v) {
        replaceFragment(new SearchPlaceFragment(), true);
    }
}
