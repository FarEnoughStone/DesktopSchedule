package space.hongkui.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.kui.desktopschedule.R;

/**自定义圆角响应按钮
 * Created by Administrator on 2016/10/17.
 */

public class RoundButton extends Button {

    private Paint paint,p;
    private RectF rectF;
    private int corners;
    private Shader mShader;
    private boolean downFirst = true;
    private boolean down = false;

    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int color = 0xff777777;
        corners = 20;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyControl);
            color = a.getColor(R.styleable.MyControl_backgroundColor, color);
            corners = a.getInt(R.styleable.MyControl_corners,20);
            a.recycle();
        }
        setOnTouchListener(new OnTouchListener() {
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
                    default:
                        break;
                }
                return false;
            }
        });
        setBackground(null);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setShadowLayer(15,10,10, Color.GRAY);
        //阴影扩散半径，阴影在X和Y方向的偏移量，最后一个参数是颜色
        p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        rectF = new RectF();
        rectF.left = 15;
        rectF.top = 15;
    }

    public void onDraw(Canvas canvas) {
        rectF.bottom = getHeight()-15;
        rectF.right = getWidth()-15;
        canvas.drawRoundRect(rectF,corners,corners,paint);
        super.onDraw(canvas);
        if (down) {
            if (downFirst) {
                mShader = new RadialGradient(getWidth()/2,getHeight()/2,Math.max(getWidth()/2,getHeight()/2),
                        0x00ffffff,0x22000000,Shader.TileMode.CLAMP);
                downFirst = false;
            }
            p.setShader(mShader);
            canvas.drawRoundRect(rectF,corners,corners,p);
        }
    }
}
