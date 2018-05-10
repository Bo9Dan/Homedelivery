package myahkota.homedelivery.com.present.base;

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
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.data.Const;
import myahkota.homedelivery.com.data.SharedPrefManager;
import myahkota.homedelivery.com.present.main.MainActivity;
import myahkota.homedelivery.com.present.main.Root;
import myahkota.homedelivery.com.present.view.LoadingDialog;

public abstract class BaseGridFragment extends Fragment {

    public Root root;
    public View rootView;
    public View footerView;
    private ExtendBaseAdapter adapter;
    private LoadingDialog progressDialog;
    protected GridViewWithHeaderAndFooter view;

    protected abstract ExtendBaseAdapter initAdapter(Fragment _context);

    protected abstract void onClickItem(ParseObject model, View view);

    protected abstract void onClickFooter();

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
        int rootHeight = SharedPrefManager.getInstance().retrieveFrameHeight();
        int rootWidth = SharedPrefManager.getInstance().retrieveWidth();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = initAdapter(this);
        root = ((MainActivity) getActivity()).getRoot();
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResource(), container, false);
        footerView = inflater.inflate(R.layout.grid_foother, null);
        findDataView(rootView);
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
        ImageView menuView = (ImageView) _rootView.findViewById(R.id.ivMenuBtn_AT);
        ImageView appView = (ImageView) _rootView.findViewById(R.id.ivLogoIcon_AT);
        footerView.setVisibility(View.INVISIBLE);
        view.addFooterView(footerView);
        view.setNumColumns(getCount());
        view.setAdapter(getAdapter());
        view.setOnItemClickListener(onClickListener);
        if (menuView != null && appView != null) {
            menuView.setOnClickListener(menuOpenListener);
            appView.setOnClickListener(menuOpenListener);
        }
    }

    private View.OnClickListener menuOpenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            root.toggleDrawer();
        }
    };

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < getAdapter().getCount() && position >= 0)
                onClickItem(getAdapter().getData().get(position), view);
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
        getAdapter().clear();
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


    public void replaceFragment(Fragment frg, View view) {
        root.replaceFragment(frg, view);
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

}
