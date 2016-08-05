package cheipesh.homedelivery.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import cheipesh.homedelivery.com.R;

public class MenuAdapter extends BaseAdapter {

    private List<ParseObject> mData;
    private Context context;

    public MenuAdapter(Context context) {
        this.mData = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(getItem(position).getString("title"));
        return convertView;
    }


    class ViewHolder {
        private TextView itemName;

        public ViewHolder(View v) {
            itemName = (TextView) v.findViewById(R.id.tvNameItemMenu);
            itemName.setTypeface(Typer.set(context).getFont(Font.ROBOTO_REGULAR));
        }
    }

    public void setData(List<ParseObject> mData) {
        this.mData.clear();
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }
}
