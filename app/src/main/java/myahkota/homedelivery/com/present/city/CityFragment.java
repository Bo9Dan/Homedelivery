package myahkota.homedelivery.com.present.city;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.parse.ParseObject;

import java.util.List;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.DataProvider;
import myahkota.homedelivery.com.present.base.BaseGridFragment;
import myahkota.homedelivery.com.present.categ.CategoryFragment;

public class CityFragment extends BaseGridFragment {

    private DataProvider provider = new DataProvider();

    @Override
    public void onViewCreated(View _view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        footerView.setVisibility(View.GONE);
        view.removeFooterView(footerView);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_select_city;
    }

    @Override
    protected void getData() {
        provider.getCitiesOff(callback);
    }

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
    public void setData(List<ParseObject> data) {
        getAdapter().clear();
        getAdapter().setData(data);
        hideLoadingDialog();
    }

    @Override
    protected void onClickItem(ParseObject model, View view) {
        replaceFragment(CategoryFragment.newInstance(model.getString(Const.P_COLUMN_TITLE)), null);
    }

    @Override
    protected void onClickFooter() {
        openBrowser("");
    }

}
