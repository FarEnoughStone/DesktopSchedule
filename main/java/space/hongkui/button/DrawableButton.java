package space.hongkui.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.kui.desktopschedule.R;

/**
 * Created by Administrator on 2016/10/13.
 */

public class DrawableButton extends Button {
    private int drawablePosition;
    private Drawable drawable;
    private boolean down = false;

    public DrawableButton(Context context) {
        this(context, null);
    }

    public DrawableButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public DrawableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyControl);
            drawable = a.getDrawable(R.styleable.MyControl_drawable);
            drawablePosition = a.getInt(R.styleable.MyControl_drawablePosition, 1);
            a.recycle();
        }
        this.setBackground(null);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        down = true;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        down = false;
                        invalidate();
                        break;
                    default:break;
                }
                return true;
            }
        });
    }

    public void onDraw(Canvas canvas) {

        int width = getWidth(),
                height = getHeight();
        switch (drawablePosition) {
            case 0:
                drawableChangeInProportion(width * 3 / 5, height);
                setCompoundDrawables(drawable,null,null,null);
                break;
            case 1:
                drawableChangeInProportion(width, height * 3 / 5);
                setCompoundDrawables(null,drawable,null,null);
                break;
            case 2:
                drawableChangeInProportion(width * 3 / 5, height);
                setCompoundDrawables(null,null,drawable,null);
                break;
            case 3:
                drawableChangeInProportion(width, height * 3 / 5);
                setCompoundDrawables(null,null,null,drawable);
                break;
        }
        super.onDraw(canvas);
        if (down) {
            canvas.drawColor(0x15000000);
        }
    }

    /**
     * 按比例缩放图片
     */
    private void drawableChangeInProportion(int w, int h) {
        int width = drawable.getMinimumWidth();
        int height = drawable.getMinimumHeight();
        float m = Math.min((float) w / width, (float) h / height);
        drawable.setBounds(0, 0, (int) (width * m), (int) (height * m));
    }
}
