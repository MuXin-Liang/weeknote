package com.joe.weeknote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.joe.weeknote.UI.AbsGridAdapter;
import com.joe.weeknote.UI.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private GridView detailCource;
    private MyAdapter adapter;
    private List<String> dataList;
    private static AbsGridAdapter secondAdapter;
    private ArrayAdapter<String> spinnerAdapter;

    public static void viewNotify(){
        secondAdapter.setContent(DataLab.get().getContents(), 14, 7);
        secondAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局
        setContentView(R.layout.cource);
        spinner = (Spinner)findViewById(R.id.switchWeek);
        detailCource = (GridView)findViewById(R.id.courceDetail);

        //(课程)
        //contents= DataLab.get(MainActivity.this).getContents();
        secondAdapter = new AbsGridAdapter(this);
        secondAdapter.setContent(DataLab.get(MainActivity.this).getContents(), 14, 7);
        detailCource.setAdapter(secondAdapter);


        //////////////创建Spinner数据（周数）
        fillDataList();
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DataLab.get().changeWeek(i);
                DataLab.get().viewNotify();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.e("666",""+DataLab.Week);
        spinner.setSelection(DataLab.Week);

    }

    public void fillDataList() {
        dataList = new ArrayList<>();
        for(int i = 1; i < 21; i++) {
            dataList.add("第" + i + "周");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu_normal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.file_edit:
                if(DataLab.get().editable==true) {
                    DataLab.get().editable = false;
                    item.setTitle("编辑");
                }
                else
                {
                    DataLab.get().editable = true;
                    item.setTitle("保存");
                }



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
