package myahkota.homedelivery.com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.parse.ParseObject;

import java.util.List;

import myahkota.homedelivery.com.base.Constants;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.util.SharedPrefManager;
import myahkota.homedelivery.com.adapters.CityAdapter;
import myahkota.homedelivery.com.base.BaseFragment;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class CityFragment extends BaseFragment {

    private GridViewWithHeaderAndFooter gridView;
    private View footerView;


    @Override
    protected CityAdapter initAdapter() {
        return new CityAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);
        footerView = inflater.inflate(R.layout.grid_foother, null);

        activity.setMenuBack(false);

        gridView.addFooterView(footerView);
        gridView.setNumColumns(1);
        gridView.setAdapter(getAdapter());
        gridView.setOnItemClickListener(onCityClickListener);
        getData(Constants.CITY_KEY, "", "");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.setTitle(activity.getString(R.string.app_title));
        activity.clickableMenu(false);
    }

    private AdapterView.OnItemClickListener onCityClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != getAdapter().getCount() && position >= 0){
            activity.replaceFragment(new CategoryFragment (), true);
            SharedPrefManager.getInstance().saveCity(getAdapter().getItem(position).getString(Constants.P_COLUMN_TITLE));
        } else {
            activity.openBrowser("");
        }
        }
    };


    @Override
    public void setData(List<ParseObject> data, boolean hasSave) {
        super.setData(data, hasSave);
        calculateCityFooter(data.size());
    }

    private void calculateCityFooter(int _size) {
        int listItemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        getResources().getDisplayMetrics());

        int diffFoot = activity.getFrameHeight() - listItemHeight * _size;

        if (diffFoot > activity.getFrameWidth()/2) {
            footerView.getLayoutParams().height = diffFoot;
        } else {
            footerView.getLayoutParams().height = activity.getFrameWidth()/2;
        }

        footerView.requestLayout();

    }
}
