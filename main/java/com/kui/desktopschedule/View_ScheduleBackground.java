package com.kui.desktopschedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**课表视图
 * Created by Administrator on 2016/11/30.
 */

public class View_ScheduleBackground extends View {


    private Paint textPaint;
    private Paint drawRectPaint;
    private int width,height;
    private int roomWidth, roomHeight;

    public View_ScheduleBackground(Context context) {
        this(context, null);
    }

    public View_ScheduleBackground(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.textViewStyle);
    }

    public View_ScheduleBackground(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(3);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(28);

        drawRectPaint = new Paint();
        drawRectPaint.setStrokeWidth(1);
        drawRectPaint.setStyle(Paint.Style.FILL);
        drawRectPaint.setAntiAlias(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();//获取控件宽高
        height = getHeight();

        int left=80,top=60;
        drawRectPaint.setColor(0x80ffffff);
        canvas.drawRect(0, 0, width, top, drawRectPaint);
        canvas.drawRect(0, 0, left, height, drawRectPaint);
        drawRectPaint.setColor(0xff808080);
        roomWidth = (width - left) / 5;
        for (char i = 1; i < 5;i++ ) {//竖线
            canvas.drawLine(roomWidth * i + left, 0, roomWidth * i + left, height, drawRectPaint);
        }
        roomHeight= (height - top) / 5;
        for (char i = 1; i < 5;i++ ) {//横线
            canvas.drawLine(0, roomHeight * i + top, width, roomHeight * i + top, drawRectPaint);
        }
        drawRectPaint.setColor(0xff7100aa);
        canvas.drawLine(0, top, width, top, drawRectPaint);//横线
        canvas.drawLine(left, 0, left, height, drawRectPaint);//竖线

        String[] s1 = {"一", "二", "三", "四", "五"};
        int z = left + roomWidth / 2;
        for(char i=0;i<s1.length;i++) {
            canvas.drawText(s1[i], z + roomWidth * i, top - 20, textPaint);
        }
        String[] s2 = {"1-2", "3-4", "5-6", "7-8", "晚上"};
        z = top + roomHeight / 2 + 10;
        for(char i=0;i<s2.length;i++) {
            canvas.drawText(s2[i], left / 2, z + roomHeight * i, textPaint);
        }
    }
}
