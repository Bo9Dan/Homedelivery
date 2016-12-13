package myahkota.homedelivery.com.present.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.parse.ParseObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.present.base.ExtendBaseAdapter;

public class CityAdapter extends ExtendBaseAdapter {

    public CityAdapter(Context context) {
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
        private ImageView ivCityIcon;
        private TextView mCityName;
        private View view;

        public CityHolder(View v) {
            view = v;
            ivCityIcon = (ImageView) v.findViewById(R.id.ivCityPictogram);
            mCityName = (TextView) v.findViewById(R.id.tvCitName);
            mCityName.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        }
    }

    private void setColorView(View view, int position) {
        view.getLayoutParams().height =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                                getContext().getResources().getDisplayMetrics());

        if (position%2 == 0) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.place1));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.place3));
        }
    }

}
