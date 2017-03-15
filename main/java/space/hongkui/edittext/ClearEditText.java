package space.hongkui.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.kui.desktopschedule.R;

/**
 * Created by Administrator on 2016/10/12.
 * 自定义带清除按钮和名字的输入框
 */

public class ClearEditText extends EditText {

    private Bitmap bitmap;//删除按钮的引用
    private boolean hasFocus=false;
    private String nameText;
    private Paint paint;
    private RectF r2;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int nameColor = Color.BLACK;
        int nameSize = 20;
        nameText = "";
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyControl);
            nameText=a.getString(R.styleable.MyControl_nameText);
            nameColor=a.getColor(R.styleable.MyControl_nameColor, Color.BLACK);//默认黑色
            nameSize = a.getInt(R.styleable.MyControl_nameSize, 20);
            a.recycle();
        }
        bitmap = ((BitmapDrawable)getResources().getDrawable(android.R.drawable.ic_notification_clear_all)).getBitmap();
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() > (getWidth() - bitmap.getWidth() - getPaddingRight())
                            && event.getX() < getWidth() && hasFocus) {
                        ClearEditText.this.setText("");
                    }
                }
                return false;
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ClearEditText.this.hasFocus = hasFocus;
                invalidate();
            }
        });
        this.setBackground(null);
        paint = new Paint();
        paint.setTextSize(nameSize * 2);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setColor(nameColor);
        paint.setTextAlign(Paint.Align.CENTER);//字体中心为基准
        paint.setAntiAlias(true); //消除锯齿
        paint.setStrokeWidth(2);
        r2=new RectF();//RectF对象
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        r2.left=10;
        r2.top=10;
        r2.right=getWidth()-10;
        r2.bottom=getHeight()-10;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(r2, 10, 10, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawLine(getWidth() / 4, 10, getWidth() / 4, getHeight() - 10, paint);
        canvas.drawText(nameText, getWidth() / 8, getHeight() / 2 + (paint.getFontMetrics().descent - paint.getFontMetrics().ascent) / 4, paint);
        if (hasFocus) {
            canvas.drawBitmap(bitmap, getWidth()-getPaddingRight()-bitmap.getWidth(),
                    (getHeight()-bitmap.getHeight())/2, paint);
        }
        this.setPadding(20 + getWidth() / 4, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }
}
