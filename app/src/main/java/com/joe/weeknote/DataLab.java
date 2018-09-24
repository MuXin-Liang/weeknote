package com.joe.weeknote;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Joe on 2018/4/22.
 */

public class DataLab {
    private static DataLab sDataLab;
    private static Context mAppContext;
    private static String[][] mcontents;
    public static boolean editable=false;
    public static int MonthDate;
    public static int Month;
    public static int Week;
    public static int Hour;
    public static int CourseNum;
    public static int WeekDay;
    public static int SelectWeek;


    public static int rand[]={2,5,3,6,7,4,0,1};

    //初始化,按当周
    private DataLab(Context appContext){
        mAppContext = appContext.getApplicationContext();
        mcontents = new String[14][7];
        setTime();
        getContentFromShare(Week);//按当前周将数据提取出来
    }

    public void changeWeek(int week){
        SelectWeek=week;
        getContentFromShare(SelectWeek);
    }

    //更新内容
    public void changeContent(int row,int col,String str) {
        DataLab.mcontents[row][col] = str;
        saveContent(SelectWeek);
    }

    public void changeContentAllWeek(int row,int col,String str) {
        for(int i = 0; i < 20; i++) {
            getContentFromShare(i);
            DataLab.mcontents[row][col] = str;
            saveContent(i);
        }
        getContentFromShare(SelectWeek);
    }

    //更新页面并保存
    public void viewNotify(){
        MainActivity.viewNotify();
    }

    public static DataLab get(Context c) {
        if (sDataLab == null) {
            sDataLab = new DataLab(c.getApplicationContext());
        }
        return sDataLab;
    }

    public static DataLab get() {
        return sDataLab;
    }

    public static String[][] getContents(){
        return mcontents;
    }

    public void setContents(String[][] mcontents) {
        DataLab.mcontents = mcontents;
    }


    //使用SharedPreference保存增长备注
    //依照当前选择的周数保存
    public boolean saveContent(int week) {

        try {
            SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("base64", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(mcontents);
            String base64Product = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            Log.e("正在保存","classs"+String.valueOf(week));
            editor.putString("classs"+String.valueOf(week), base64Product);
            editor.commit();
            baos.close();
            oos.close();

            return true;
        }
        catch (Exception e) {
            Toast.makeText(mAppContext, "保存出错了！！！" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    //从SharePreference中将保存的classs取出
    public void getContentFromShare(int week) {
        if (isFristRun(week)) {
            fillStringArray();
            saveContent(week);
            Log.e("第一次","classs"+String.valueOf(week));
        }
        else {
            try {
                Log.e("正在读取","classs"+String.valueOf(week));
                SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("base64", MODE_PRIVATE);
                String productString = sharedPreferences.getString("classs" + String.valueOf(week), "");
                byte[] base64Product = Base64.decode(productString, Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Product);
                ObjectInputStream ois = new ObjectInputStream(bais);
                mcontents = (String[][]) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("步行啊啊啊啊啊啊");
            }
        }
    }

    //判断是否第一次运行
    private boolean isFristRun(int week) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences sharedPreferences = mAppContext.getSharedPreferences(
                "share", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun"+String.valueOf(week), true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!isFirstRun) {
            Log.e("是否第一？","不是");
            return false;
        } else {
            Log.e("是否第一次？","是");
            //保存数据 （第三步）
            editor.putBoolean("isFirstRun"+String.valueOf(week), false);
            //提交当前数据 （第四步）
            editor.commit();
            return true;
        }
    }


    private void setTime(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        Month = t.month;
        MonthDate = t.monthDay;

        //计算当前周
        String dbtime1 = "2018-02-26";  //第二个日期
        //算两个日期间隔多少天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(dbtime1);
            Date date2 = new Date(System.currentTimeMillis());
            Week = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24)/7)-1;

        }catch (java.text.ParseException e){
            e.printStackTrace();
        }
        SelectWeek=Week;

        Hour = t.hour;    // 0-23
        if(Hour<12)
            CourseNum=(Hour-8);
        else if(Hour>=14)
            CourseNum=(Hour-10);
        else
            CourseNum=100;
        Calendar c = Calendar.getInstance();
        WeekDay=c.get(Calendar.DAY_OF_WEEK)-2;
        if(WeekDay==-1)  WeekDay=6;
    }



    public static void fillStringArray() {
        mcontents[0][0] = "";
        mcontents[1][0] = "";
        mcontents[2][0] = "";
        mcontents[3][0] = "";
        mcontents[4][0] = "";
        mcontents[5][0] = "";
        mcontents[6][0] = "";
        mcontents[7][0] = "";
        mcontents[8][0] = "";
        mcontents[9][0] = "";
        mcontents[10][0] = "";
        mcontents[11][0] = "";
        mcontents[12][0] = "";
        mcontents[13][0] = "";


        mcontents[0][1] = "数据结构与算法\nB211";
        mcontents[1][1] = "";
        mcontents[2][1] = "";
        mcontents[3][1] = "";
        mcontents[4][1] = "";
        mcontents[5][1] = "";
        mcontents[6][1] = "";
        mcontents[7][1] = "";
        mcontents[8][1] = "";
        mcontents[9][1] = "";
        mcontents[10][1] = "";
        mcontents[11][1] = "";
        mcontents[12][1] = "";
        mcontents[13][1] = "";

        mcontents[0][2] = "微机原理及应用\nE203";
        mcontents[1][2] = "";
        mcontents[2][2] = "";
        mcontents[3][2] = "";
        mcontents[4][2] = "";
        mcontents[5][2] = "";
        mcontents[6][2] = "";
        mcontents[7][2] = "";
        mcontents[8][2] = "";
        mcontents[9][2] = "";
        mcontents[10][2] = "";
        mcontents[11][2] = "";
        mcontents[12][2] = "";
        mcontents[13][2] = "";

        mcontents[0][3] = "面向对象程序设计\nA309";
        mcontents[1][3] = "";
        mcontents[2][3] = "";
        mcontents[3][3] = "";
        mcontents[4][3] = "";
        mcontents[5][3] = "";
        mcontents[6][3] = "";
        mcontents[7][3] = "";
        mcontents[8][3] = "";
        mcontents[9][3] = "";
        mcontents[10][3] = "";
        mcontents[11][3] = "";
        mcontents[12][3] = "";
        mcontents[13][3] = "";

        mcontents[0][4] = "";
        mcontents[1][4] = "";
        mcontents[2][4] = "";
        mcontents[3][4] = "";
        mcontents[4][4] = "";
        mcontents[5][4] = "";
        mcontents[6][4] = "";
        mcontents[7][4] = "";
        mcontents[8][4] = "";
        mcontents[9][4] = "";
        mcontents[10][4] = "";
        mcontents[11][4] = "";
        mcontents[12][4] = "";
        mcontents[13][4] = "";

        mcontents[0][5] = "";
        mcontents[1][5] = "";
        mcontents[2][5] = "";
        mcontents[3][5] = "";
        mcontents[4][5] = "";
        mcontents[5][5] = "";
        mcontents[6][5] = "";
        mcontents[7][5] = "";
        mcontents[8][5] = "";
        mcontents[9][5] = "";
        mcontents[10][5] = "";
        mcontents[11][5] = "";
        mcontents[12][5] = "";
        mcontents[13][5] = "";

        mcontents[0][6] = "";
        mcontents[1][6] = "";
        mcontents[2][6] = "";
        mcontents[3][6] = "";
        mcontents[4][6] = "";
        mcontents[5][6] = "";
        mcontents[6][6] = "";
        mcontents[7][6] = "";
        mcontents[8][6] = "";
        mcontents[9][6] = "";
        mcontents[10][6] = "";
        mcontents[11][6] = "";
        mcontents[12][6] = "";
        mcontents[13][6] = "";


    }


}
