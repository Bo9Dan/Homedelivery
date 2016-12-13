package myahkota.homedelivery.com.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SquareView extends LinearLayout {

    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        this(context);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
