package myahkota.homedelivery.com.present.city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.present.base.ExtendBaseAdapter;

public class CityAdapter extends ExtendBaseAdapter {

    CityAdapter(Context context) {
        super(context);
    }

    @Override
    public ParseObject getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getObjectId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityHolder cityHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);
            cityHolder = new CityHolder(convertView);
            convertView.setTag(cityHolder);
        } else {
            cityHolder = (CityHolder) convertView.getTag();
        }
        cityHolder.mCityName.setText(getItem(position).getString("title"));
        setColorView(convertView, position);

        return convertView;
    }

    private class CityHolder {
        private TextView mCityName;

        CityHolder(View v) {
            mCityName = (TextView) v.findViewById(R.id.tvCitName);
        }
    }

    private void setColorView(View view, int position) {
        if (position % 2 == 0) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.place1));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.place3));
        }
    }

}
