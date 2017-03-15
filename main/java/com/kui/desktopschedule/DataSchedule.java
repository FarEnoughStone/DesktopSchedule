package com.kui.desktopschedule;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**课表数据
 * Created by Administrator on 2016/11/30.
 */
enum Frequency{
    always,single,twin
}
enum  Week{
    one,two,three,four,five
}
enum ClassTime{
    one,two,three,four,five
}

class DataSchedule{
    ArrayList<Class> classData;
    private Context context;
    DataSchedule(Context c){
        context = c;
        classData = new ArrayList<>();
    }

    void  readSchedule(String fName){
        File file = new File(fName);
        if (file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(fName));
                this.classData=(ArrayList<Class>) in.readObject();
                in.close();
            } catch (FileNotFoundException e) {
                (Toast.makeText(context,"未找到文件",Toast.LENGTH_SHORT)).show();
                this.classData=new ArrayList<>();
            } catch (IOException e) {
                (Toast.makeText(context,"IO端口出错-读",Toast.LENGTH_SHORT)).show();
                this.classData=new ArrayList<>();
            } catch (ClassNotFoundException e) {
                (Toast.makeText(context,"未找到类",Toast.LENGTH_SHORT)).show();
                this.classData=new ArrayList<>();
            }
        }
    }

    boolean saveSchedule(String fName) {
        File file = new File(fName);
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    (Toast.makeText(context, "无法创建文件", Toast.LENGTH_SHORT)).show();
                    return false;
                }
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(this.classData);
            out.close();
            return true;
        } catch (IOException e) {
            (Toast.makeText(context,"IO端口出错-写",Toast.LENGTH_SHORT)).show();
            return false;
        }
    }

    boolean addClass(Class newClass) {
        if (classData.size() == 0) {
            classData.add(newClass);
            return true;
        }else {
            Class c;
            int index = classData.size();
            for (int i=0;i<index;i++) {
                c = classData.get(i);
                if (i == 0) {
                    if ((c.week.ordinal() == newClass.week.ordinal() &&
                            c.classTime.ordinal() > newClass.classTime.ordinal())
                            ||c.week.ordinal() > newClass.week.ordinal()) {
                        classData.add(0,newClass);
                        return true;
                    }
                }
                if ((c.week.ordinal() == newClass.week.ordinal() &&
                        c.classTime.ordinal() < newClass.classTime.ordinal())
                        ||c.week.ordinal() < newClass.week.ordinal()) {
                    if (i == index - 1) {
                        classData.add(newClass);
                        return true;
                    } else {
                        c = classData.get(i+1);
                        if ((c.week.ordinal() == newClass.week.ordinal() &&
                                c.classTime.ordinal() > newClass.classTime.ordinal())
                                ||c.week.ordinal() > newClass.week.ordinal()) {
                            classData.add(i+1, newClass);
                            return true;
                        } else {
                            continue;
                        }
                    }
                } else  {
                    (Toast.makeText(context,"与现有课程冲突",Toast.LENGTH_SHORT)).show();
                    return false;
                }
            }
            return false;
        }
    }
}

class Class implements Serializable {
    Week week;
    ClassTime classTime;
    String className = null,
            teacherName = null,
            classRoom = null;
    Frequency frequency;
}

