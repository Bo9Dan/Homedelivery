package myahkota.homedelivery.com.present.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.present.base.ExtendBaseAdapter;

public class PlaceAdapter extends ExtendBaseAdapter {

    public PlaceAdapter(Context context) {
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
        PlaceHolder placeHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_venue, parent, false);
            placeHolder = new PlaceHolder(convertView);
            convertView.setTag(placeHolder);
        } else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }

        setColorView(placeHolder.view, position);

        if (getItem(position).getParseFile("image") != null) {

            Glide.with(getContext())
                    .load(getItem(position).getParseFile("image").getUrl())
                    .asBitmap()
                    .error(R.drawable.ic_room_service_white_48dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(placeHolder.mFoodImage);
        } else {
            placeHolder.mFoodImage.setImageDrawable(
                    getContext().getResources().getDrawable(R.drawable.ic_room_service_white_48dp));
        }

        return convertView;
    }

    private class PlaceHolder {
        private ImageView mFoodImage;
        private View view;

        public PlaceHolder(View v) {
            view = v;
            mFoodImage = (ImageView) v.findViewById(R.id.ivRestIcon);
        }
    }

    private void setColorView(View view, int position) {
        int indicator;
        if (position < 6 ) indicator = position;
            else indicator = position%6;

        switch (indicator) {
            case 0:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.place1));
                break;
            case 1:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.place2));
                break;
            case 2:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.place3));
                break;
            case 3:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.place4));
                break;
            case 4:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.place5));
                break;
            case 5:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.place6));
                break;
        }
    }
}
