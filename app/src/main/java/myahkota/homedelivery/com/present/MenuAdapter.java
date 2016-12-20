package myahkota.homedelivery.com.present;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.present.base.ExtendBaseAdapter;

public class MenuAdapter extends ExtendBaseAdapter {

    private List<ParseObject> mCityDataList = new ArrayList<>();
    private List<ParseObject> mCategoryDataList = new ArrayList<>();
    private boolean isCategory = true;

    public MenuAdapter(Context context) {
        super(context);
    }

    @Override
    public ParseObject getItem(int position) {
        if (isCategory) return mCategoryDataList.get(position);
            else return mCityDataList.get(position);

    }

    @Override
    public int getCount() {
        if (isCategory) return mCategoryDataList.size();
            else return mCityDataList.size();
    }

    public void setListesData(List<ParseObject> categories, List<ParseObject> cities) {
        this.mCategoryDataList = categories;
        this.mCityDataList = cities;
    }

    public void setCityDataList(List<ParseObject> cities) {
        this.mCityDataList = cities;
        notifyDataSetChanged();
    }

    public void setCategoryDataList(List<ParseObject> categories) {
        this.mCategoryDataList = categories;
        notifyDataSetChanged();
    }

    public boolean isCategory() {
        return isCategory;
    }

    @Override
    public long getItemId(int position) {
        if (isCategory) {
            return mCategoryDataList.get(position).hashCode();
        } else {
            return mCityDataList.get(position).hashCode();
        }
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

    public void shoesCities() {
        isCategory = false;
        notifyDataSetInvalidated();
    }

    public void shoesCategories() {
        isCategory = true;
        notifyDataSetInvalidated();
    }

    private class MenuHolder {
        private TextView itemName;

        public MenuHolder(View v) {
            itemName = (TextView) v.findViewById(R.id.tvNameItemMenu);
            itemName.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
        }
    }


}
