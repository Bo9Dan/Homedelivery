package myahkota.homedelivery.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.parse.ParseObject;

import java.util.List;

import myahkota.homedelivery.com.R;

public class MenuAdapter extends ExtendBaseAdapter {

    public MenuAdapter(Context context) {
        super(context);
    }

    @Override
    public ParseObject getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuHolder menuHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_menu, null);
            menuHolder = new MenuHolder(convertView);
            convertView.setTag(menuHolder);
        } else {
            menuHolder = (MenuHolder) convertView.getTag();
        }

        menuHolder.itemName.setText(getItem(position).getString("title"));
        return convertView;
    }

    private class MenuHolder {
        private TextView itemName;

        public MenuHolder(View v) {
            itemName = (TextView) v.findViewById(R.id.tvNameItemMenu);
            itemName.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        }
    }

    @Override
    public void setData(List<ParseObject> mData) {
        clear();
        super.setData(mData);
    }
}
