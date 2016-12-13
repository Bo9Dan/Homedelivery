package myahkota.homedelivery.com.present.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.parse.ParseObject;

import myahkota.homedelivery.com.R;
import myahkota.homedelivery.com.present.base.ExtendBaseAdapter;

public class CategoryAdapter extends ExtendBaseAdapter {

    public CategoryAdapter(Context context) {
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
        CategoryHolder categoryHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food_bord, null);
            categoryHolder = new CategoryHolder(convertView);
            convertView.setTag(categoryHolder);
        } else {
            categoryHolder = (CategoryHolder) convertView.getTag();
        }

        categoryHolder.mFoodName.setText(getItem(position).getString("title"));
        setColorView(categoryHolder.view, position);

        if (position%2 == 0) {
            categoryHolder.lineEnd.setVisibility(View.VISIBLE);
            categoryHolder.lineStart.setVisibility(View.INVISIBLE);
        } else {
            categoryHolder.lineEnd.setVisibility(View.INVISIBLE);
            categoryHolder.lineStart.setVisibility(View.VISIBLE);
        }

        Glide.with(getContext())
                .load(getItem(position).getParseFile("image").getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(categoryHolder.mFoodImage);

        return convertView;
    }

    private class CategoryHolder {
        private ImageView mFoodImage;
        private TextView  mFoodName;
        private View view, lineStart, lineEnd;

        public CategoryHolder(View v) {
            view = v;
            mFoodImage = (ImageView) v.findViewById(R.id.ivFoodIcon);
            mFoodName = (TextView) v.findViewById(R.id.tvFoodName);
            mFoodName.setTypeface(Typer.set(getContext()).getFont(Font.ROBOTO_REGULAR));
            lineStart    = v.findViewById(R.id.vSingleLineStart);
            lineEnd    = v.findViewById(R.id.vSingleLineEnd);
        }
    }

    private void setColorView(View view, int position) {
        int indicator;
        if (position < 6 ) indicator = position;
            else indicator = position%6;

        switch (indicator) {
            case 0:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.category1));
                break;
            case 1:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.category2));
                break;
            case 2:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.category3));
                break;
            case 3:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.category4));
                break;
            case 4:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.category5));
                break;
            case 5:
                view.setBackgroundColor(getContext().getResources().getColor(R.color.category6));
                break;
        }
    }
}
