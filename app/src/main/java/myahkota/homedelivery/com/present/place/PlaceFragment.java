package myahkota.homedelivery.com.present.place;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.ParcelableDTO;
import myahkota.homedelivery.com.present.BaseGridFragment;
import myahkota.homedelivery.com.present.view.OptionDialog;

public class PlaceFragment extends BaseGridFragment {

    private String mCategory, mCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCategory = getArguments().getString(Const.CATEGORY_KEY, "");
            mCity = getArguments().getString(Const.CITY_TITLE, "");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static PlaceFragment newInstance(final String category) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putString(Const.CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PlaceAdapter initAdapter(Fragment _context) {
        return new PlaceAdapter(_context.getContext());
    }

    @Override
    protected void onClickItem(ParseObject model) {
        getMain().takeScreen();
        OptionDialog placeDetail = OptionDialog.newInstance(getDTO(model));
        placeDetail.show(getFragmentManager(), Const.PLACE_KEY);
    }

    @Override
    protected void onClickFooter() {
        openBrowser("");
    }

    @Override
    protected String EntityName() {
        return Const.PLACE_KEY;
    }

    private ParcelableDTO getDTO(ParseObject object) {
        return new ParcelableDTO(object, "tudu");
    }

    @Override
    public void setData(List<ParseObject> data) {
        super.setData(getSortDataOrder(data));
    }

    private List<ParseObject> getSortDataOrder(final List<ParseObject> objectsList) {
        List<ParseObject> sortDataList = new ArrayList<>();
        List<ParseObject> sortNewDataList = new ArrayList<>();
        List<Object>      categoriesList;
        List<String>      orderList;
        String defOrder = "1000000";

        for (ParseObject object : objectsList) {

            categoriesList = object.getList(Const.P_COLUMN_CATEGORIES);

            for (Object category: categoriesList) {

                if (category.toString().equals(mCategory)) {

                    orderList = object.getList(Const.P_COLUMN_ORDER);

                    if(orderList == null || orderList.isEmpty()) {

                        sortDataList.add(createNewSortItem(object, defOrder));

                    } else {

                        String ladder = defOrder;

                        if (orderList.size() >=  categoriesList.indexOf(category))
                            ladder = orderList.get(categoriesList.indexOf(category));

                        sortDataList.add(createNewSortItem(object, ladder));
                    }

                    break;
                }
            }
        }

        Collections.sort(sortDataList, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject lhs, ParseObject rhs) {
                return lhs.getString("mOrder").compareTo(rhs.getString("mOrder"));
            }
        });

        for(ParseObject parseObject : sortDataList) {
            sortNewDataList.add(parseObject.getParseObject("object"));
        }



        return sortNewDataList;
    }

    private ParseObject createNewSortItem(ParseObject object, String positionOrder) {
        ParseObject parseObject = new ParseObject("SortPlace");
        parseObject.put("mOrder", positionOrder);
        parseObject.put("object", object);
        return parseObject;
    }
}
