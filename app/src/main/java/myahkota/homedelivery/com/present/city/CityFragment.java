package myahkota.homedelivery.com.present.city;

import android.support.v4.app.Fragment;

import com.parse.ParseObject;

import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.present.BaseGridFragment;
import myahkota.homedelivery.com.present.categ.CategoryFragment;

public class CityFragment extends BaseGridFragment {

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getTypeFooter() {
        return 125;
    }

    @Override
    protected CityAdapter initAdapter(Fragment _context) {
        return new CityAdapter(_context.getContext());
    }

    @Override
    protected void onClickItem(ParseObject model) {
        replaceFragment(new CategoryFragment(), true);
    }

    @Override
    protected void onClickFooter() {
        openBrowser("");
    }

    @Override
    protected String EntityName() {
        return Const.CITY_KEY;
    }
}
