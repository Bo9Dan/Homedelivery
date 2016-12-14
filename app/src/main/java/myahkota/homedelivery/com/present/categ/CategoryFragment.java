package myahkota.homedelivery.com.present.categ;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.present.BaseGridFragment;
import myahkota.homedelivery.com.present.place.PlaceFragment;

public class CategoryFragment extends BaseGridFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void getData(ParseQuery<ParseObject> query) {
        query.orderByAscending(Const.P_COLUMN_ORDER);
        super.getData(query);
    }

    @Override
    protected String EntityName() {
        return Const.CATEGORY_KEY;
    }
}
