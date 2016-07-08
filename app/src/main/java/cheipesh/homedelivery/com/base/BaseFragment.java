package cheipesh.homedelivery.com.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

import java.util.List;

import cheipesh.homedelivery.com.R;
import cheipesh.homedelivery.com.adapters.ExtendBaseAdapter;
import cheipesh.homedelivery.com.util.SharedPrefManager;

public abstract class BaseFragment extends Fragment {

    protected MainActivity mCallingActivity;
    private ExtendBaseAdapter mAdapter;
    private String saveKey;

    private String ent, name, key;


    protected abstract ExtendBaseAdapter initAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallingActivity = (MainActivity) context;
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
        mCallingActivity.showLoadingDialog();
        saveKey = _entityKey + _columnKey;
        if (!_columnKey.isEmpty())
            query.whereEqualTo(_columnName, _columnKey);

        if (mCallingActivity.isOnline()) {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(mCallingActivity);
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
        _query.orderByAscending(Constants.P_COLUMN_ORDER);
        _query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    setData(objects, false);
                    Log.d("SyncDel", "get offline data");
                } else {
                    Log.d("SyncDel", "error offline data" );
                    mCallingActivity.hideLoadingDialog();
                   /* Toast.makeText(mCallingActivity, "??????? ???????? ?? ????????????? ?????", Toast.LENGTH_LONG).show();*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(mCallingActivity);
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
        mCallingActivity.hideLoadingDialog();
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

    public void getPermiss() {
        Ask.on(mCallingActivity)
                .forPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET)
                .withRationales("Call permission need for call ") //optional
                .go();
    }

    @AskGranted(Manifest.permission.CALL_PHONE)
    public void fileAccessGranted() {
//        Log.i(TAG, "FILE  GRANTED");
    }

    //optional
    @AskDenied(Manifest.permission.CALL_PHONE)
    public void fileAccessDenied() {
//        Log.i(TAG, "FILE  DENiED");
    }

    //optional
    @AskGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessGranted() {
//        Log.i(TAG, "MAP GRANTED");
    }

    //optional
    @AskDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessDenied() {
//        Log.i(TAG, "MAP DENIED");
    }

}
