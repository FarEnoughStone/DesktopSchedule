package com.kui.desktopschedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**自定义彩色圆角矩形文本框
 * Created by Administrator on 2016/12/1.
 */

public class RoundTextView extends TextView {

    private Paint paint;
    private int color=0xfffffff;
    private RectF rectF;
    private Shader shader;
    private boolean colorIsChange = false;

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.textViewStyle);
    }

    public RoundTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
    }

    public void setColor(int color) {
        this.color = (0x50000000 + color % 0x1000000);
        colorIsChange = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        rectF.set(0, 0, width, height);

        if (colorIsChange) {
            shader = new RadialGradient(width/2,height/2,height/2+100,new int[] {(int)(color*1.1),color,},null,Shader.TileMode.REPEAT);
            colorIsChange = false;
        }
        paint.setShader(shader);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, 30, 30, paint);

        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, 30, 30, paint);
        super.onDraw(canvas);
    }
}
