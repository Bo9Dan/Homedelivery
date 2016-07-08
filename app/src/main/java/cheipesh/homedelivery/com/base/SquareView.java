package cheipesh.homedelivery.com.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SquareView extends LinearLayout {
    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // вызываем метод onMeasure класса ImageButton, чтобы расcчитать размеры
        // кнопки стандартным образом
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // сейчас наша кнопка имеет такие же размеры как если бы
        // она была экземпляром класса ImageButton

        // начинаем добавлять новую логику расчета размера

        // получаем рассчитанные размеры кнопки
        final int height = getMeasuredHeight();	// высота
        final int width = getMeasuredWidth();	// ширина

        // теперь задаем новый размер
        // ширину оставляем такую же как у стандартной кнопки
        // высоту выбираем как максимум между стандартной высотой и шириной
        setMeasuredDimension(width, Math.max(width, height));
    }
}
