package myahkota.homedelivery.com.present;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.present.base.ExtendBaseAdapter;
import myahkota.homedelivery.com.present.view.LoadingDialog;

public abstract class BaseGridFragment extends Fragment {

    private MainActivity activity;
    private LoadingDialog progressDialog;
    private ExtendBaseAdapter adapter;
    private View footerView;
    protected GridViewWithHeaderAndFooter view;

//    protected abstract void findUI(View rootView);
//
//    protected abstract void setupUI();

    protected abstract ExtendBaseAdapter initAdapter(Fragment _context);

    protected abstract void onClickItem(ParseObject model);

    protected abstract void onClickFooter();

    protected abstract String EntityName();

    public int getLayoutResource() {
        return R.layout.grid_layout;
    }

    public int getCount() {
        return 2;
    }

    public int getTypeFooter() {
        return 0;
    }

    private void calculateFooter() {
        int countData = getAdapter().getCount();
        int rootHeight = activity.getFrameHeight();
        int rootWidth = activity.getFrameWidth();
        int defSize;
        footerView.setVisibility(View.VISIBLE);

        if (getTypeFooter() == 0) {
            defSize = rootHeight - Math.round(countData / 2 + countData % 2) * rootWidth / 2;

        } else {
            int dimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                    getResources().getDisplayMetrics());
            defSize = rootHeight - dimension * countData;
        }

        if (defSize > rootWidth / 2) {
            footerView.getLayoutParams().height = defSize;
        } else {
            footerView.getLayoutParams().height = rootWidth / 2;
        }

        footerView.requestLayout();

    }

    public ExtendBaseAdapter getAdapter() {
        return adapter;
    }

    public MainActivity getMain() {
        return activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        adapter = initAdapter(this);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResource(), container, false);
        footerView = inflater.inflate(R.layout.grid_foother, null);
        findDataView(rootView);
//        findUI(rootView);
//        setupUI();
        progressDialog = new LoadingDialog();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingDialog();
        getData();
    }

    private void findDataView(View _rootView) {
        view = (GridViewWithHeaderAndFooter) _rootView.findViewById(R.id.gvGridView);
        footerView.setVisibility(View.INVISIBLE);
        view.addFooterView(footerView);
        view.setNumColumns(getCount());
        view.setAdapter(getAdapter());
        view.setOnItemClickListener(onClickListener);
    }

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < getAdapter().getCount() && position >= 0)
                onClickItem(getAdapter().getData().get(position));
            else onClickFooter();

        }
    };

    protected FindCallback<ParseObject> callback = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null) {
                setData(objects);
            }
        }
    };

    protected abstract void getData();

    public void setData(List<ParseObject> data) {
        getAdapter().setData(data);
        calculateFooter();
        hideLoadingDialog();
    }

    public void openBrowser(String customUrl) {
        String defaultUrl = Const.FOOTER_LINK;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(customUrl.isEmpty() ? defaultUrl : customUrl));
        startActivity(i);
    }

    public void onBaseResume() {
    }

    public void onBasePause() {

    }


    public void replaceFragment(Fragment frg, boolean isAdd) {
        activity.replaceFragment(frg, isAdd);
    }

    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new LoadingDialog();
        }
        if (!progressDialog.isShowing())
            progressDialog.show(getFragmentManager(), "");
    }

    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) onBasePause();
        else onBaseResume();
    }


}
