package com.wheremybus.galaxywatch_busapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

import static android.widget.Toast.makeText;

public class SettingActivity extends WearableActivity {

    private Button btnFind;
    private Button btnBack;
    private ListView listView;
    ArrayList<String> items = new ArrayList<String>() ;
    ArrayAdapter adapter;
    private EditText editBusNum;
    private String busNum;
    private String busID;
    ArrayList<String> stationID;
    ArrayList<String> stationName;
    public String fileName = "myBusSaveItem";
    //File file;
    findBusRouteThread findThread;
    findBusNumThread findBusID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //file  = new File(this.getFilesDir(), fileName);
        editBusNum = findViewById(R.id.editBus);
        btnFind = findViewById(R.id.btnBus);

        listView = findViewById(R.id.set_itemList);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);
        stationID = new ArrayList<String>();
        stationName = new ArrayList<String>();
        listView.setAdapter(adapter);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initItems();
                busNum = editBusNum.getText().toString();
                Log.d("SettingTag", "busNum :"+busNum);
                if(!busNum.isEmpty()){
                    Toast.makeText(SettingActivity.this, "FIND...", Toast.LENGTH_LONG).show();
                    findBusID = new findBusNumThread();
                    findBusID.start();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                alertDilog(stationID.get(a_position),stationName.get(a_position));
            }
        });
        // Enables Always-on
        setAmbientEnabled();
    }

    public void initItems(){
        busNum = "";
        busID = "";
        adapter.clear();
        stationID.clear();
        stationName.clear();
    }

    public void alertDilog(final String stationID, final String busStationName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(busNum+"번 "+ busStationName);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName,Context.MODE_APPEND));
                    outputStreamWriter.write(busNum);
                    outputStreamWriter.write("/");
                    outputStreamWriter.write(busStationName);
                    outputStreamWriter.write("/");
                    outputStreamWriter.write(busID);
                    outputStreamWriter.write("/");
                    outputStreamWriter.write(stationID);
                    outputStreamWriter.write("/");
                    Log.d("SettingTag","busID : "+ busID + " / stationID : "+ stationID);
                    outputStreamWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                //
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    public void readBusRouteExcel(){
        //Read File
        try{
            InputStream is = getBaseContext().getResources().getAssets().open("GGD_RouteInfo_M.xls");
            //Excel File
            Workbook wb = Workbook.getWorkbook(is);

            if(wb!=null){
                Sheet sheet = wb.getSheet(0);

                int colTotal = sheet.getColumns();//total column
                int rowIndexStart = 1;
                int rowTotal = sheet.getColumn(colTotal-1).length;

                StringBuilder sb;
                for(int row = rowIndexStart;row<rowTotal;row++){
                    sb = new StringBuilder();
                    busNum = editBusNum.getText().toString();
                    String strRoute = sheet.getCell(3,row).getContents();
                    if(busNum.length()!=0){
                        //Log.d("SettingTag", "strRoute :"+strRoute);
                        if(busNum.equals(strRoute)){
                            busID = sheet.getCell(4,row).getContents();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBusRouteList(){
//Read File
        try{
            InputStream is = getBaseContext().getResources().getAssets().open("GGD_RouteStationInfo_M.xls");

            String busStation;
            //Excel File
            Workbook wb = Workbook.getWorkbook(is);

 //           Log.d("SettingTag", "busNum :"+busNum);
 //           Log.d("SettingTag", "busID :"+busID);
            Sheet sheet;
            int colTotal=0;//total column
            int rowIndexStart = 0;
            int rowTotal = 0;

            for(int i=0;i<3;i++){
                findThread = new findBusRouteThread(i);
                findThread.start();
            }

/*
            if(wb!=null){
                for(int i=0; i< 3;i++){
                    sheet = wb.getSheet(i);
                    colTotal = sheet.getColumns();//total column
                    rowIndexStart = 1;
                    rowTotal = sheet.getColumn(colTotal-1).length;
                    StringBuilder sb;
                    for(int row = rowIndexStart;row<rowTotal;row++){
                        sb = new StringBuilder();
                        String strRoute = sheet.getCell(1,row).getContents();
                        if(busID.equals(strRoute)){
                            busStation = sheet.getCell(4,row).getContents();
                            stationID.add(sheet.getCell(5,row).getContents());
                            stationName.add(busStation);
                            adapter.add(busStation);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class findBusRouteThread extends Thread{
        private int sheetNum = 0;
        Sheet sheet;
        int colTotal=0;//total column
        int rowIndexStart = 0;
        int rowTotal = 0;
        String busStation;

        public  findBusRouteThread(int sheetNum){
            this.sheetNum = sheetNum;
        }
        public void run(){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            InputStream is = getBaseContext().getResources().getAssets().open("GGD_RouteStationInfo_M.xls");
                            //Excel File
                            Workbook wb = Workbook.getWorkbook(is);

                            if(wb!=null){
                                sheet = wb.getSheet(sheetNum);
                                colTotal = sheet.getColumns();//total column
                                rowIndexStart = 1;
                                rowTotal = sheet.getColumn(colTotal-1).length;
                                StringBuilder sb;
                                Log.d("SettingTag", "sheetNum : "+ sheetNum);
                                for(int row = rowIndexStart;row<rowTotal;row++){
                                    Log.d("SettingTag", "saveList");
                                    sb = new StringBuilder();
                                    String strRoute = sheet.getCell(1,row).getContents();
                                    if(busID.equals(strRoute)){
                                        busStation = sheet.getCell(4,row).getContents();
                                        stationID.add(sheet.getCell(5,row).getContents());
                                        stationName.add(busStation);
                                        adapter.add(busStation);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        }
                });



        }
    }

    public class findBusNumThread extends Thread{

        boolean bFindBus=true;
        public  findBusNumThread(){
        }

        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Read File
                    try{
                        InputStream is = getBaseContext().getResources().getAssets().open("GGD_RouteInfo_M.xls");
                        //Excel File
                        Workbook wb = Workbook.getWorkbook(is);

                        if(wb!=null){
                            Sheet sheet = wb.getSheet(0);

                            int colTotal = sheet.getColumns();//total column
                            int rowIndexStart = 1;
                            int rowTotal = sheet.getColumn(colTotal-1).length;

                            StringBuilder sb;
                            for(int row = rowIndexStart;row<rowTotal;row++){
                                sb = new StringBuilder();
                                //busNum = editBusNum.getText().toString();
                                String strRoute = sheet.getCell(3,row).getContents();
                                if(busNum.length()!=0 && bFindBus){
                                    if(busNum.equals(strRoute)){
                                        //Log.d("SettingTag", "find Bus : "+ strRoute);
                                        busID = sheet.getCell(4,row).getContents();
                                        getBusRouteList();
                                        bFindBus = false;
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

}