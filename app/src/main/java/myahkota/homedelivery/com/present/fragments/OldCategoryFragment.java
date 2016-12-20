package myahkota.homedelivery.com.present.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.parse.ParseObject;

import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.base.BaseFragment;
import myahkota.homedelivery.com.present.categ.CategoryAdapter;

public class OldCategoryFragment extends BaseFragment {

    private GridViewWithHeaderAndFooter gridView;
    private View footerView;
    private String cityName;

    @Override
    protected CategoryAdapter initAdapter() {
        return new CategoryAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        cityName = SharedPrefManager.getInstance().retrieveCity();

        View view = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);
        footerView = inflater.inflate(R.layout.grid_foother, null);
        footerView.setVisibility(View.INVISIBLE);

//        activity.setMenuBack(false);
//
//        gridView.addFooterView(footerView);
//        gridView.setNumColumns(2);
//        gridView.setAdapter(getAdapter());
//        gridView.setOnItemClickListener(onCategoryClickListener);
//        getData(Const.CATEGORY_KEY, "", "");
//        activity.clickableMenu(true);


        return view;
    }

    @Override
    protected void search(String text) {
        super.search(text);
        getData(Const.PLACE_KEY, Const.P_COLUMN_MENU, text);
    }

    private AdapterView.OnItemClickListener onCategoryClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != getAdapter().getCount() && id >= 0){
            activity.replaceFragment(OldRestaurantFragment.newInstance(
                    getAdapter().getItem(position).getObjectId(),
                    getAdapter().getItem(position).getString("title"))
                    , true);
        }  else {
           /*activity.openBrowser("");*/
        }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(cityName);
    }

    @Override
    public void setData(List<ParseObject> data, boolean hasSave) {
        super.setData(data, hasSave);
        calculateFooter(data.size());
    }

    private void calculateFooter(int _size) {
        int dividFoot = activity.getFrameHeight() -  Math.round(_size/2 + _size%2) * activity.getFrameWidth()  ;
        footerView.setVisibility(View.VISIBLE);
        if (dividFoot > activity.getFrameWidth()/2) {
            footerView.getLayoutParams().height = dividFoot;
        } else {
            footerView.getLayoutParams().height = activity.getFrameWidth()/2;
        }

        footerView.requestLayout();
    }

}
