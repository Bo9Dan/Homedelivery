package cheipesh.homedelivery.com.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.parse.ParseObject;

import java.util.List;

import cheipesh.homedelivery.com.base.Constants;
import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.util.SharedPrefManager;
import cheipesh.homedelivery.com.adapters.CityAdapter;
import cheipesh.homedelivery.com.base.BaseFragment;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class CityFragment extends BaseFragment {

    private static final int ITEM_HEIGHT = 68;
    private GridViewWithHeaderAndFooter gridView;
    private View footerView;


    @Override
    protected CityAdapter initAdapter() {
        return new CityAdapter(mCallingActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gvGridView);
        footerView = inflater.inflate(R.layout.grid_foother, null);

        mCallingActivity.setMenuBack(false);

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
        mCallingActivity.setTitle(mCallingActivity.getString(R.string.app_title));
        mCallingActivity.clickableMenu(false);
    }

    private AdapterView.OnItemClickListener onCityClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != getAdapter().getCount() && position >= 0){
                mCallingActivity.replaceFragment(FoodFragment
                        .newInstance(getAdapter().getItem(position)
                                .getString(Constants.P_COLUMN_TITLE))
                        , true);
                SharedPrefManager.getInstance().saveCity(getAdapter().getItem(position).getString(Constants.P_COLUMN_TITLE));
            } else {
                String url = "http://www.google.com.ua";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    };



    @Override
    public void setData(List<ParseObject> data, boolean hasSave) {
        super.setData(data, hasSave);
        calculateCityFooter(data.size());
    }


    private void calculateCityFooter(int _size) {
//        int hhh = (int) getResources().getDimension(R.dimen._48sdp);
        int listItemHeight = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        getResources().getDisplayMetrics());

        int diffFoot = mCallingActivity.getFrameHeight() - listItemHeight * _size;

        if (diffFoot > mCallingActivity.getFrameWidth()/2) {
            footerView.getLayoutParams().height = diffFoot;
        } else {
            footerView.getLayoutParams().height = mCallingActivity.getFrameWidth()/2;
        }


        footerView.requestLayout();

    }
}
