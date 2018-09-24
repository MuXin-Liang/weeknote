package com.joe.weeknote.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joe.weeknote.DataLab;
import com.joe.weeknote.R;

import java.util.ArrayList;

/**
 * Created by wan on 2016/10/16.
 * GridView的适配器
 */
public class AbsGridAdapter extends BaseAdapter {

    private Context mContext;

    private String[][] contents;

    private int rowTotal;

    private int columnTotal;

    private int positionTotal;

    private ArrayAdapter<String> spinnerAdapter;

    private ArrayList<String> dataList;

    public AbsGridAdapter(Context context) {
        this.mContext = context;
        dataList=new ArrayList<String>();
        dataList.add("仅保存为本周");
        dataList.add("保存为所有周");
        spinnerAdapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public int getCount() {
        return positionTotal;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        //求余得到二维索引
        int column = position % columnTotal;
        //求商得到二维索引
        int row = position / columnTotal;
        return contents[row][column];
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grib_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.text);
        int col = position % columnTotal;
        int row = position / columnTotal;

        //如果有课,那么添加数据
        if( !getItem(position).equals("")) {
            textView.setText((String)getItem(position));
            textView.setTextColor(Color.WHITE);
            //变换颜色
            //int color=(int)(Math.random()*8);
            int rand=(row/2+col+DataLab.rand[col])%8;
            switch( rand ) {
                case 0:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.grid_item_bg));
                    break;
                case 1:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_12));
                    break;
                case 2:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_13));
                    break;
                case 3:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_14));
                    break;
                case 4:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_15));
                    break;
                case 5:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_16));
                    break;
                case 6:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_17));
                    break;
                case 7:
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_18));
                    break;

            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int row = position / columnTotal;
                    int column = position % columnTotal;
                    if(DataLab.get().editable)
                        newDialog(row,column);
                }
            });
        }
        else {
            textView.setText("");
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_origin));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int row = position / columnTotal;
                    int column = position % columnTotal;
                    if(DataLab.get().editable)
                        newDialog(row,column);
                }
            });
        }

        if(DataLab.CourseNum==row&&DataLab.WeekDay==col){
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_today));
        }
        return convertView;
    }

    /**
     * 设置内容、行数、列数
     */
    public void setContent(String[][] contents, int row, int column) {
        this.contents = contents;
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }


    public void newDialog(final int row, final int col){
        //代码生成布局样式
        final LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText et1 = new EditText(mContext);
        final EditText et2 = new EditText(mContext);
        final Spinner sp1=new Spinner(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 20, 30, 10);
        et1.setLayoutParams(lp);
        et2.setLayoutParams(lp);
        sp1.setLayoutParams(lp);
        sp1.setAdapter(spinnerAdapter);
        et1.setBackground(null);
        et2.setBackground(null);
        et1.setHint("课程名");
        et2.setHint("地点");
        String[] s=contents[row][col].split("\n");
        if(s.length==2)
        {
            et1.setText(s[0]);
            et2.setText(s[1]);
        }
        else if(s.length==1)
            et1.setText(s[0]);

        layout.addView(et1);
        layout.addView(et2);
        layout.addView(sp1);

        new AlertDialog.Builder(mContext).setTitle("修改课程")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input1 = et1.getText().toString();
                        String input2 = et2.getText().toString();

                        if(sp1.getSelectedItemPosition()==0) {
                            if (input1.equals("") && input2.equals("")) {
                                DataLab.get().changeContent(row, col, "");
                                DataLab.get().viewNotify();
                            } else {
                                //执行修改
                                DataLab.get().changeContent(row, col, input1 + "\n" + input2);
                                DataLab.get().viewNotify();
                            }
                        }
                        else {
                            if (input1.equals("") && input2.equals("")) {
                                DataLab.get().changeContentAllWeek(row, col, "");
                                DataLab.get().viewNotify();
                            } else {
                                //执行修改
                                DataLab.get().changeContentAllWeek(row, col, input1 + "\n" + input2);
                                DataLab.get().viewNotify();
                            }
                        }
                    }

                })
                .setNegativeButton("取消", null)
                .show();
    }


}
