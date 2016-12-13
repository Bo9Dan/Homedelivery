package myahkota.homedelivery.com.present.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public abstract class ExtendBaseAdapter extends BaseAdapter {

    private List<ParseObject> mData;
    private Context context;

    public ExtendBaseAdapter(Context context) {
        this.context = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setData(List<ParseObject> mData) {
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
    }

    public List<ParseObject> getData() {
        return mData;
    }

    public Context getContext() {
        return context;
    }
}
