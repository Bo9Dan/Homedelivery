package cheipesh.homedelivery.com.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.adapters.ExtendBaseAdapter;
import cheipesh.homedelivery.com.util.SharedPrefManager;

public abstract class BaseFragment extends Fragment {

    protected MainActivity activity;
    private ExtendBaseAdapter mAdapter;
    private String saveKey;

    private String ent, name, key;


    protected abstract ExtendBaseAdapter initAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mAdapter = initAdapter();
    }

    public ExtendBaseAdapter getAdapter() {
        return mAdapter;
    }


    protected void getData(String _entityKey,
                           String _columnName,
                           String _columnKey) {
        ent = _entityKey;
        name = _columnName;
        key = _columnKey;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(_entityKey);
//        query.orderByAscending(Constants.P_COLUMN_ORDER);
        activity.showLoadingDialog();
        saveKey = _entityKey + _columnKey;
        if (!_columnKey.isEmpty()) {
            query.whereEqualTo(_columnName, _columnKey);
        } else {
            query.orderByAscending(Constants.P_COLUMN_ORDER);
        }

        if (activity.isOnline()) {
            if (hasNewData(saveKey)){
//                saveKey = _entityKey + _columnKey;
                onlineData(query );
            } else {
                offlineData(query);
            }

        } else {
            offlineData(query/*, _callback*/);
        }
    }


    private void onlineData(final ParseQuery<ParseObject> _query) {
        _query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){

                    setData(objects, !objects.isEmpty());
                    Log.d("SyncDel", "get online data");
                } else {
//                    offlineData(_query);

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(R.string.error)
                            .setMessage(R.string.error_msg)
                            .setIcon(R.mipmap.appicon)
                            .setCancelable(false)
                            .setNegativeButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            offlineData(_query);
                                            dialog.cancel();
                                        }
                                    })
                            .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getData(ent, name, key);
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                    Log.d("SyncDel", "error online data");
                }
            }
        });
    }

    private void offlineData(ParseQuery<ParseObject> _query) {
        _query.fromLocalDatastore();
//        _query.orderByAscending(Constants.P_COLUMN_ORDER);
        _query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    setData(objects, false);
                    Log.d("SyncDel", "get offline data");
                } else {
                    Log.d("SyncDel", "error offline data" );
                    activity.hideLoadingDialog();
                   /* Toast.makeText(activity, "??????? ???????? ?? ????????????? ?????", Toast.LENGTH_LONG).show();*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(R.string.error)
                            .setMessage(R.string.error_msg)
                            .setIcon(R.mipmap.appicon)
                            .setCancelable(false)
                            .setNegativeButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getData(ent, name, key);
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();



                }
            }
        });
    }

    public void setData(List<ParseObject> data, boolean hasSave) {
        activity.hideLoadingDialog();
        mAdapter.setData(data);
        if (hasSave){
            ParseObject.pinAllInBackground(data);
            SharedPrefManager.getInstance().saveLong(saveKey, System.currentTimeMillis());
        }
    }


    private boolean hasNewData(String _key) {
        long saveDate = SharedPrefManager.getInstance().retrieveLong(_key);
        long nowDate = System.currentTimeMillis();
        if (saveDate == -1) {
            return true;
        } else
            return nowDate - saveDate > 24 * 60 * 60 * 1000 * 7;

    }
}
