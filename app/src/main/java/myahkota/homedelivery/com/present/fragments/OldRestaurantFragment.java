package myahkota.homedelivery.com.present.fragments;

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

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.present.place.PlaceAdapter;
import myahkota.homedelivery.com.present.base.BaseFragment;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.ParcelableDTO;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.view.OptionDialog;

public class OldRestaurantFragment extends BaseFragment {

    private String mCategoryId, mCategoryName;
    private GridViewWithHeaderAndFooter headerAndFooter;
    private View footerView;


    public static OldRestaurantFragment newInstance(final String _categoryID, final String _title) {
        OldRestaurantFragment fragment = new OldRestaurantFragment();
        Bundle args = new Bundle();
        args.putString(Const.PLACE_KEY, _categoryID);
        args.putString(Const.CITY_TITLE, _title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected PlaceAdapter initAdapter() {
        return new PlaceAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mCategoryId = getArguments().getString(Const.PLACE_KEY, "");
            mCategoryName = getArguments().getString(Const.CITY_TITLE, "");
        }

        View view = inflater.inflate(R.layout.grid_layout, container, false);
        headerAndFooter = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);

        footerView = inflater.inflate(R.layout.grid_foother, null);
        footerView.setVisibility(View.INVISIBLE);
        headerAndFooter.addFooterView(footerView);
        headerAndFooter.setNumColumns(2);
        headerAndFooter.setAdapter(getAdapter());
        headerAndFooter.setOnItemClickListener(restaurantOnClickListener);

        getData(Const.PLACE_KEY, Const.P_COLUMN_CITY, SharedPrefManager.getInstance().retrieveCity());


        return view;
    }

    private AdapterView.OnItemClickListener restaurantOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position != getAdapter().getCount() && id >= 0) {

                ParseObject place = getAdapter().getItem(position).getParseObject("object");
                saveDrawable(view);
                activity.takeScreen();
                ParcelableDTO parseObject = new ParcelableDTO(place, mCategoryName);

                OptionDialog placeDetail = OptionDialog.newInstance(parseObject);
                placeDetail.show(getFragmentManager(), Const.PLACE_KEY);
            }  else {
                /*activity.openBrowser("");*/
            }
        }
    };

    private void saveDrawable(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.ivRestIcon);
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            activity.takeItem(bitmap);
        } else {
            activity.takeItem(null);
        }
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

                        sortData.add(createNewSortItem(object, order));
                    } else {
                        String order = "10000";
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
        /*activity.hideLoadingDialog();*/
        getAdapter().setData(sortData);
        if (hasSave){
            ParseObject.pinAllInBackground(data);
            SharedPrefManager.getInstance().savePinDate();
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

