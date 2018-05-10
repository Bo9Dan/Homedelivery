package myahkota.homedelivery.com.present.search.place;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.data.ParcelableDTO;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.OptionDialog;
import myahkota.homedelivery.com.present.base.BaseGridFragment;
import myahkota.homedelivery.com.present.order.OrderFragment;

public class SearchPlaceFragment extends BaseGridFragment {

    private String mCity;
    private DataProvider provider = new DataProvider();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCity = SharedPrefManager.getInstance().retrieveCity();
        EditText input = (EditText) rootView.findViewById(R.id.etSearchInput_AT);
        TextView cancel = (TextView) rootView.findViewById(R.id.tvCancelBtb_AT);
        input.setOnEditorActionListener(searchActionListener);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.back();
            }
        });
    }

    private TextView.OnEditorActionListener searchActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                final String text = v.getText().toString();
                if (text.trim().length() > 2) {
                    provider.getSearchPlaces(callback, mCity, v.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                } else {
                    Snackbar snackbar = Snackbar.make(rootView, getResources().getString(R.string.too_short_word), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                return true;
            }
            return true;
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_search_place;
    }

    @Override
    protected void getData() {
//        provider.getPlacesOff(callback, mCity, mCategory);
    }

    @Override
    protected SearchPlaceAdapter initAdapter(Fragment _context) {
        return new SearchPlaceAdapter(_context.getContext());
    }

    @Override
    protected void onClickItem(ParseObject model, View view) {
        Intent intent = new Intent(getActivity(), OrderFragment.class);
        intent.putExtra(Const.P_COLUMN_OBJECT, getDTO(model));
        startActivity(intent);
    }

    @Override
    protected void onClickFooter() {
        openBrowser("");
    }

    private ParcelableDTO getDTO(ParseObject object) {
        return new ParcelableDTO(object, "");
    }

    @Override
    public void setData(List<ParseObject> data) {
        getAdapter().clear();
        super.setData(getSortDataOrder(data));
        if (data.size() == 0) {
            Snackbar snackbar = Snackbar.make(rootView, getResources().getString(R.string.empty_result), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private List<ParseObject> getSortDataOrder(final List<ParseObject> objectsList) {
        List<ParseObject> sortDataList = new ArrayList<>();
        List<ParseObject> sortNewDataList = new ArrayList<>();
        List<Object> categoriesList;
        List<String> orderList;
        String defOrder = "1000000";

        for (ParseObject object : objectsList) {

            categoriesList = object.getList(Const.P_COLUMN_CATEGORIES);

            for (Object category : categoriesList) {

                orderList = object.getList(Const.P_COLUMN_ORDER);

                if (orderList == null || orderList.isEmpty()) {

                    sortDataList.add(createNewSortItem(object, defOrder));

                } else {

                    String ladder = defOrder;

                    if (orderList.size() >= categoriesList.indexOf(category))
                        ladder = orderList.get(categoriesList.indexOf(category));

                    sortDataList.add(createNewSortItem(object, ladder));
                }

                break;
            }
        }

        Collections.sort(sortDataList, new Comparator<ParseObject>() {
            @Override
            public int compare(ParseObject lhs, ParseObject rhs) {
                return lhs.getString("mOrder").compareTo(rhs.getString("mOrder"));
            }
        });

        for (ParseObject parseObject : sortDataList) {
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
