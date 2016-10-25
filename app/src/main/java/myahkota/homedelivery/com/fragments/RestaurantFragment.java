package myahkota.homedelivery.com.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.adapters.RestaurantAdapter;
import myahkota.homedelivery.com.base.BaseFragment;
import myahkota.homedelivery.com.base.Constants;
import myahkota.homedelivery.com.base.ParcelableParseObject;
import myahkota.homedelivery.com.util.SharedPrefManager;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class RestaurantFragment extends BaseFragment {

    private String mCategoryId, mCategoryName;
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
        return new RestaurantAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mCategoryId = getArguments().getString(Constants.PLACE_KEY, "");
            mCategoryName = getArguments().getString(Constants.CITY_TITLE, "");
        }

        View view = inflater.inflate(R.layout.grid_layout, container, false);
        headerAndFooter = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);

        footerView = inflater.inflate(R.layout.grid_foother, null);
        footerView.setVisibility(View.INVISIBLE);
        headerAndFooter.addFooterView(footerView);
        headerAndFooter.setNumColumns(2);
        headerAndFooter.setAdapter(getAdapter());
        headerAndFooter.setOnItemClickListener(restaurantOnClickListener);

        getData(Constants.PLACE_KEY, Constants.P_COLUMN_CITY, SharedPrefManager.getInstance().retrieveCity());

        activity.setMenuBack(true);

        return view;
    }

    private AdapterView.OnItemClickListener restaurantOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position != getAdapter().getCount() && id >= 0) {

                ParseObject place = getAdapter().getItem(position).getParseObject("object");
                saveDrawable(view);
                activity.setDrawableBack();
                ParcelableParseObject parseObject = new ParcelableParseObject(place, mCategoryName);

                OptionDialog placeDetail = OptionDialog.newInstance(parseObject);
                placeDetail.show(getFragmentManager(), Constants.PLACE_KEY);
            }  else {
                activity.openBrowser("");
            }
        }
    };

    private void saveDrawable(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.ivRestIcon);
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            activity.setDrawableHamb(bitmap);
        } else {
            activity.setDrawableHamb(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setData(List<ParseObject> data, boolean hasSave) {
        List<ParseObject> sortData = new ArrayList<>();
        for (ParseObject object :data) {

            for (Object category: object.getList("categories")) {
                if (category.toString().equals(mCategoryName)){
                    List<String> orderList = object.getList("order");

                    if(orderList != null && !orderList.isEmpty()){
                        String order = "10000";
                        if (orderList.size() >=  object.getList("categories").indexOf(category))
                            order = orderList.get(object.getList("categories").indexOf(category));
//                        object.add("myorder", order);

                        sortData.add(createNewSortItem(object, order));
                    } else {
                        String order = "10000";
//                        object.add("myorder", order);
                        sortData.add(createNewSortItem(object, order));
                    }

                    break;
                }
            }
        }

        Collections.sort(sortData, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject lhs, ParseObject rhs) {
                return lhs.getString("mOrder").compareTo(rhs.getString("mOrder"));
            }
        });
        activity.hideLoadingDialog();
        getAdapter().setData(sortData);
        if (hasSave){
            ParseObject.pinAllInBackground(data);
            SharedPrefManager.getInstance().saveLong(Constants.PLACE_KEY + SharedPrefManager.getInstance().retrieveCity(), System.currentTimeMillis());
        }
        calculateFooter(sortData.size());
    }

    private ParseObject createNewSortItem(ParseObject object, String positionOrder) {
        ParseObject parseObject = new ParseObject("SortPlace");
        parseObject.put("mOrder", positionOrder);
        parseObject.put("object", object);
        return parseObject;
    }

    private void calculateFooter(int _size) {
        int dividFoot = activity.getFrameHeight() - Math.round(_size/2 + _size%2) * activity.getFrameWidth()/2;
        footerView.setVisibility(View.VISIBLE);
        if (dividFoot > activity.getFrameWidth()/2) {
            footerView.getLayoutParams().height = dividFoot;
        } else {
            footerView.getLayoutParams().height = activity.getFrameWidth() / 2;
        }
        footerView.requestLayout();

    }










}

