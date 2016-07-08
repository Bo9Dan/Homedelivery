package cheipesh.homedelivery.com.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.parse.ParseObject;

import java.util.List;

import cheipesh.homedelivery.com.base.Constants;
import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.adapters.FoodAdapter;
import cheipesh.homedelivery.com.base.BaseFragment;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class FoodFragment extends BaseFragment {

    private GridViewWithHeaderAndFooter gridView;
    private View footerView;
    private String cityName;


    public static FoodFragment newInstance(final String _city) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CITY_KEY, _city);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected FoodAdapter initAdapter() {
        return new FoodAdapter(mCallingActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            cityName = getArguments().getString(Constants.CITY_KEY, "");
        }

        View view = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);
        footerView = inflater.inflate(R.layout.grid_foother, null);
        footerView.setVisibility(View.INVISIBLE);

        mCallingActivity.setMenuBack(false);

        gridView.addFooterView(footerView);
        gridView.setNumColumns(2);
        gridView.setAdapter(getAdapter());
        gridView.setOnItemClickListener(onCategoryClickListener);
        getData(Constants.CATEGORY_KEY, Constants.P_COLUMN_CITY, cityName);
        mCallingActivity.clickableMenu(true);

        return view;
    }



    private AdapterView.OnItemClickListener onCategoryClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != getAdapter().getCount() && id != -1){
                mCallingActivity.replaceFragment(RestaurantFragment
                                .newInstance(getAdapter().getItem(position).getObjectId(),
                                        getAdapter().getItem(position).getString("title"))
                        , true);
            }  else {
                String url = "http://www.google.com.ua";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    };




    @Override
    public void onResume() {
        super.onResume();
        mCallingActivity.setTitle(cityName);
    }


    @Override
    public void setData(List<ParseObject> data, boolean hasSave) {
        super.setData(data, hasSave);
        calculateFooter(data.size());
    }


    private void calculateFooter(int _size) {
        int dividFoot = mCallingActivity.getFrameHeight() -  Math.round(_size/2 + _size%2) * mCallingActivity.getFrameWidth()  ;
        footerView.setVisibility(View.VISIBLE);
        if (dividFoot > mCallingActivity.getFrameWidth()/2) {
            footerView.getLayoutParams().height = dividFoot;
        } else {
            footerView.getLayoutParams().height = mCallingActivity.getFrameWidth()/2;
        }

        footerView.requestLayout();

    }



}
