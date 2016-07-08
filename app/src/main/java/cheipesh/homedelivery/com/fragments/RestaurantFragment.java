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

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.adapters.RestaurantAdapter;
import cheipesh.homedelivery.com.base.BaseFragment;
import cheipesh.homedelivery.com.base.Constants;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class RestaurantFragment extends BaseFragment {

    private String CategoryId, CityName;
    private GridViewWithHeaderAndFooter headerAndFooter;
    private View footerView;


    public static RestaurantFragment newInstance(final String _categoryID, final String _title) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PLACE_KEY, _categoryID);
        args.putString(Constants.CITY_TITLE, _title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected RestaurantAdapter initAdapter() {
        return new RestaurantAdapter(mCallingActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            CategoryId = getArguments().getString(Constants.PLACE_KEY, "");
            CityName = getArguments().getString(Constants.CITY_TITLE, "");
        }

        View view = inflater.inflate(R.layout.grid_layout, container, false);
        headerAndFooter = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);

        footerView = inflater.inflate(R.layout.grid_foother, null);
        footerView.setVisibility(View.INVISIBLE);
        headerAndFooter.addFooterView(footerView);
        headerAndFooter.setNumColumns(2);
        headerAndFooter.setAdapter(getAdapter());
        headerAndFooter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != getAdapter().getCount() && id != -1){

                    mCallingActivity.setDrawable();
                    OptionDialog placeDetail = OptionDialog
                            .newInstance(getAdapter().getItem(position).getString(Constants.P_COLUMN_MENU),
                                    getAdapter().getItem(position).getString(Constants.P_COLUMN_PHONE),
                                    getAdapter().getItem(position).getParseFile("image").getUrl());
                    placeDetail.show(getFragmentManager(), Constants.PLACE_KEY);
                }  else {
                    String url = "http://www.google.com.ua";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        getData(Constants.PLACE_KEY, Constants.P_COLUMN_CATEGORY_ID, CategoryId);

        mCallingActivity.setMenuBack(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setData(List<ParseObject> data, boolean hasSave) {
        super.setData(data, hasSave);
        calculateFooter(data.size());
    }

    private void calculateFooter(int _size) {
        int mSize = _size%4;

        int dividFoot = mCallingActivity.getFrameHeight() - Math.round(_size/2 + _size%2) * mCallingActivity.getFrameWidth()/2;
        footerView.setVisibility(View.VISIBLE);
        if (dividFoot > mCallingActivity.getFrameWidth()/2) {
            footerView.getLayoutParams().height = dividFoot;
        } else {
            footerView.getLayoutParams().height = mCallingActivity.getFrameWidth() / 2;
        }
        footerView.requestLayout();

    }










}

