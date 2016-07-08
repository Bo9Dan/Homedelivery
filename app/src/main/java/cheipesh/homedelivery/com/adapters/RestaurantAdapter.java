package cheipesh.homedelivery.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseObject;

import cheipesh.homedelivery.com.R;

public class RestaurantAdapter extends ExtendBaseAdapter{


    public RestaurantAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return getmData().size();
    }

    @Override
    public ParseObject getItem(int position) {
        return getmData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getmData().get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_venue, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position%2 == 0) {
            viewHolder.lineEnd.setVisibility(View.VISIBLE);
            viewHolder.lineStart.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.lineEnd.setVisibility(View.INVISIBLE);
            viewHolder.lineStart.setVisibility(View.VISIBLE);
        }
        setColorView(viewHolder.view, position);
        Glide.with(getContext())
                .load(getItem(position).getParseFile("image").getUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(viewHolder.mFoodImage);

        return convertView;
    }

    class ViewHolder {
        private ImageView mFoodImage;
        private View view, lineStart, lineEnd;

        public ViewHolder(View v) {
            view = v;
            mFoodImage = (ImageView) v.findViewById(R.id.ivFoodIcon);
            lineStart  = (View) v.findViewById(R.id.vSingleLineRestStart);
            lineEnd    = (View) v.findViewById(R.id.vSingleLineRestEnd);
        }
    }


    private void setColorView(View view, int position) {
        int indicator = 0;
        if (position < 6 ){
            indicator = position;
        } else {
            indicator = position%6;
        }

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
