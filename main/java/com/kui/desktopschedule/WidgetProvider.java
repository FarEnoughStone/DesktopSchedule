package com.kui.desktopschedule;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider {
    static final String broadCastString = "com.kui.WidgetUpdate";//定义我们要发送的事件
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // TODO Auto-generated method stub
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ReceiverService.context = context;
        Intent i = new Intent(context, ReceiverService.class);
        context.startService(i);
        // TODO Auto-generated method stub
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //当判断到是该事件发过来时， 我们就获取插件的界面， 然后将index自加后传入到textView中
        if(intent.getAction().equals(broadCastString)) {
            new Thread(new DisplayRunnable(context)).start();
        }
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
    }
}

class DisplayRunnable implements Runnable {
    private Context context;
    private String overTime,nowClassName,nowClassRoom,beginTime,nextClassName,nextClassRoom;

    DisplayRunnable(Context c) {
        context = c;
    }

    @Override
    public void run() {
        try {
            init();
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            rv.setTextViewText(R.id.overTime,overTime);
            rv.setTextViewText(R.id.nowClassName,nowClassName);
            rv.setTextViewText(R.id.nowClassRoom,nowClassRoom);
            rv.setTextViewText(R.id.beginTime,beginTime);
            rv.setTextViewText(R.id.nextClassName,nextClassName);
            rv.setTextViewText(R.id.nextClassRoom,nextClassRoom);
            //将该界面显示到插件中
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,WidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, rv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        DataSchedule dataSchedule = new DataSchedule(context);
        File file = new File(Environment.getExternalStorageDirectory(),"DesktopSchedule");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                (Toast.makeText(context,"建立文件路径错误",Toast.LENGTH_SHORT)).show();
            }
        }
        dataSchedule.readSchedule(file.getAbsolutePath() + "/schedule");
        int size = dataSchedule.classData.size();
        int second = c.get(Calendar.SECOND);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = c.get(Calendar.MINUTE);
        int minuteOfDay = hourOfDay * 60 + minuteOfHour;
        int classBeginTime[] = {480, 615, 870, 995, 1140};
        int classOverTime[] = {585, 720, 975, 1100, 1260};
        overTime = nowClassName = nowClassRoom = beginTime = nextClassName = nextClassRoom = null;
        if (dayOfWeek == 1) {
            nextClassName = "明天课程：" + dataSchedule.classData.get(0).className;
        } else if (dayOfWeek == 7) {
            nextClassName = "后天课程：" + dataSchedule.classData.get(0).className;
        } else if (size > 0) {
            for(int i=0;i<size;i++) {
                Class theClass = dataSchedule.classData.get(i);
                if (theClass.week.ordinal() == (dayOfWeek - 2)) {
                    if ( minuteOfDay > classBeginTime[theClass.classTime.ordinal()]
                            && minuteOfDay < classOverTime[theClass.classTime.ordinal()]) {
                        overTime = "距下课时间还有：" +
                                String.format(Locale.CHINA, "%02d", (classOverTime[theClass.classTime.ordinal()] - 1 - minuteOfDay) / 60) + ":"
                                + String.format(Locale.CHINA, "%02d", (classOverTime[theClass.classTime.ordinal()] - 1 - minuteOfDay) % 60) + ":"
                                + String.format(Locale.CHINA, "%02d", (60 - second));
                        nowClassName = "当前课程：" + theClass.className;
                        nowClassRoom = theClass.classRoom;
                    }
                }
                if ((theClass.week.ordinal() > (dayOfWeek - 2)) || ((theClass.week.ordinal() == (dayOfWeek - 2))
                        && minuteOfDay < classBeginTime[theClass.classTime.ordinal()])) {
                    beginTime = "距上课时间还有：" +
                            String.format(Locale.CHINA, "%02d", (classBeginTime[theClass.classTime.ordinal()] - 1 - minuteOfDay + 1440) % 1440 / 60) + ":"
                            + String.format(Locale.CHINA, "%02d", (classBeginTime[theClass.classTime.ordinal()] - 1 - minuteOfDay + 1440) % 1440 % 60) + ":"
                            + String.format(Locale.CHINA, "%02d", (60 - second));
                    nextClassName = "下节课程：" + theClass.className;
                    nextClassRoom = theClass.classRoom;
                    break;
                }
            }
        }
    }
}