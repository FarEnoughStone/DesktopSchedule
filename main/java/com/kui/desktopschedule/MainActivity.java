package com.kui.desktopschedule;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static android.view.animation.Animation.ABSOLUTE;

public class MainActivity extends AppCompatActivity {

    private String FILE_NAME;
    private int width, height, click = -1;
    private int left = 80, top = 60, roomWidth, roomHeight;
    private DataSchedule dataSchedule;
    private LinearLayout scheduleDisplay, scheduleSet;
    private RelativeLayout schedule;
    private TextView scheduleSetTitle, scheduleDisplayTitle;
    private EditText className, classRoom, teacherName;
    private Spinner frequency, week, classTime;
    private Button back, delete, save;
    private float clickPositionX, clickPositionY;
    private AnimationSet scheduleSetAnimationSetAppear, scheduleSetAnimationSetDisappear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        init();
        loadSchedule();
        setOnClick();

    }

    private void loadSchedule() {
        File file = new File(Environment.getExternalStorageDirectory(),"DesktopSchedule");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                (Toast.makeText(MainActivity.this,"建立文件路径错误",Toast.LENGTH_SHORT)).show();
            }
        }
        FILE_NAME = file.getAbsolutePath() + "/schedule";
        dataSchedule.readSchedule(FILE_NAME);
        refreshSchedule(schedule);
    }

    private void init() {
        dataSchedule = new DataSchedule(this);
        scheduleDisplay = (LinearLayout) findViewById(R.id.scheduleDisplay);
        scheduleDisplay.setVisibility(View.VISIBLE);
        scheduleSet = (LinearLayout) findViewById(R.id.scheduleSet);
        scheduleSet.setVisibility(View.INVISIBLE);
        schedule = (RelativeLayout) findViewById(R.id.schedule);
        scheduleDisplayTitle = (TextView) findViewById(R.id.scheduleDisplay_title);
        scheduleSetTitle = (TextView) findViewById(R.id.scheduleSet_title);
        className = (EditText) findViewById(R.id.className);
        classRoom = (EditText) findViewById(R.id.classRoom);
        teacherName = (EditText) findViewById(R.id.teacherName);
        frequency = (Spinner) findViewById(R.id.frequency);
        week = (Spinner) findViewById(R.id.week);
        classTime = (Spinner) findViewById(R.id.classTime);
        back = (Button) findViewById(R.id.back);
        delete = (Button) findViewById(R.id.delete);
        save = (Button) findViewById(R.id.save);
    }

    private void setOnClick() {
        scheduleSetAnimationSetAppear = new AnimationSet(true);
        scheduleSetAnimationSetDisappear = new AnimationSet(true);
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0f,1);
        alphaAnimation1.setDuration(500);
        alphaAnimation1.setFillAfter(true);
        scheduleSetAnimationSetAppear.addAnimation(alphaAnimation1);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, ABSOLUTE, width / 2, ABSOLUTE, height / 2);
        scaleAnimation1.setDuration(500);
        scaleAnimation1.setFillAfter(true);
        scheduleSetAnimationSetAppear.addAnimation(scaleAnimation1);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(1,0f);
        alphaAnimation2.setDuration(500);
        alphaAnimation2.setFillAfter(false);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, ABSOLUTE, width / 2, ABSOLUTE, height / 2);
        scaleAnimation2.setDuration(500);
        scaleAnimation2.setFillAfter(false);
        scheduleSetAnimationSetDisappear.addAnimation(alphaAnimation2);
        scheduleSetAnimationSetDisappear.addAnimation(scaleAnimation2);
        scheduleSetAnimationSetAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                scheduleSet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scheduleDisplay.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scheduleSetAnimationSetDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                scheduleDisplay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scheduleSet.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        schedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clickPositionX = event.getX();
                        clickPositionY = event.getY();
                        break;
                    case  MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if((Math.abs(clickPositionX-event.getX())<5) && ((clickPositionY-event.getY())<5)){
                            scheduleSet.startAnimation(scheduleSetAnimationSetAppear);
                            delete.setVisibility(View.GONE);
                            scheduleSetTitle.setText(getResources().getString(R.string.addClass));
                            className.setText(null);
                            classRoom.setText(null);
                            teacherName.setText(null);
                            classTime.setSelection((int)(clickPositionY-top)/roomHeight);
                            week.setSelection((int)(clickPositionX-left)/roomWidth);
                            click = -1;
                        }
                    default:
                        break;
                }
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleSet.startAnimation(scheduleSetAnimationSetDisappear);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleSet.startAnimation(scheduleSetAnimationSetDisappear);
                dataSchedule.classData.remove(click);
                refreshSchedule(schedule);
                dataSchedule.saveSchedule(FILE_NAME);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class c = new Class();
                if (!(c.className = className.getText().toString()).equals("")) {
                    c.teacherName = teacherName.getText().toString();
                    c.classRoom = classRoom.getText().toString();
                    c.frequency =  Frequency.values()[frequency.getSelectedItemPosition()];
                    c.week = Week.values()[week.getSelectedItemPosition()];
                    c.classTime = ClassTime.values()[ classTime.getSelectedItemPosition()];
                    if (click == -1) {
                        if (dataSchedule.addClass(c)) {
                            scheduleSet.startAnimation(scheduleSetAnimationSetDisappear);
                        }
                    }else {
                        dataSchedule.classData.remove(click);
                        if (dataSchedule.addClass(c)) {
                            scheduleSet.startAnimation(scheduleSetAnimationSetDisappear);
                        }
                    }
                    refreshSchedule(schedule);
                    dataSchedule.saveSchedule(FILE_NAME);
                }else {
                    (Toast.makeText(MainActivity.this,"课程不能为空",Toast.LENGTH_SHORT)).show();
                }

            }
        });
    }

    private void refreshSchedule(RelativeLayout view) {
        view.removeAllViews();
        width = this.getWindowManager().getDefaultDisplay().getWidth();
        height = this.getWindowManager().getDefaultDisplay().getHeight()-240;
        roomWidth = (width - left) / 5;
        roomHeight = (height - top) / 5;
        int positionTop, positionLeft;

        for(final Class d:dataSchedule.classData){
            RoundTextView t = new RoundTextView(this);
            t.setTextColor(Color.WHITE);
            t.setText(d.className+"\n"+d.classRoom);
            t.setGravity(Gravity.CENTER);
            t.setTextSize(20);
            t.setColor((int)(Math.random()*0xffffff));
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleSet.startAnimation(scheduleSetAnimationSetAppear);
                    delete.setVisibility(View.VISIBLE);
                    scheduleSetTitle.setText(getResources().getString(R.string.changeClass));
                    className.setText(d.className);
                    classRoom.setText(d.classRoom);
                    teacherName.setText(d.teacherName);
                    week.setSelection(d.week.ordinal());
                    classTime.setSelection(d.classTime.ordinal());
                    frequency.setSelection(d.frequency.ordinal());
                    click = dataSchedule.classData.indexOf(d);
                }
            });
            positionLeft = left + d.week.ordinal() * roomWidth;
            positionTop = top + roomHeight * d.classTime.ordinal();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.setMargins(positionLeft, positionTop,
                    width - positionLeft - roomWidth, height - positionTop - roomHeight);
            view.addView(t,layoutParams);
        }
    }

}
