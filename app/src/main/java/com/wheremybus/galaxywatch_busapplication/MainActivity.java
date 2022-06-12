package com.wheremybus.galaxywatch_busapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends WearableActivity {

    public static String mainTag = "mainTag";
    ListView listView;
    ListBusItemAdapter adapter;
    Button btnSetting;
    public String fileName = "myBusSaveItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        //readFile();

        btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(i);
            }
        });
        Log.d(mainTag,"onCreate");
        adapter = new ListBusItemAdapter();
//        adapter.addItem(new ListBusItem("8106","3번째 전 / 3분 25초 / 25석","7번째 전 / 13분 25초"));

        // String sb = apiExplorer.getsbSend();
        //adapter.addItem(new ListBusItem("8106","3번째 전 / 3분 25초 / 25석","7번째 전 / 13분 25초"));
        //adapter.addItem(new ListBusItem("1009","1번째 전 / 1분 25초","18번째 전 / 23분 25초"));
        //adapter.addItem(new ListBusItem("6-2","2번째 전 / 3분 22초","3번째 전 / 13분 25초"));
        //adapter.addItem(new ListBusItem("602-1","1번째 전 / 1분 25초","15번째 전 / 23분 25초"));
        //BusArriveTask("210000285","234001237","29");
        listView.setAdapter(adapter);

        adapter.setOnMyItemCheckedChanged(new ListBusItemAdapter.OnMyItemCheckedChanged() {
            @Override
            public void onItemCheckedChanged(ListBusItem item, int pos) {
                Log.d(mainTag,"Delete busNum : " + item.getBusID()+"/ stationName : " + item.getStationName());
                alertDilog(item.getBusID(),item.getStationName(),pos);
                readFile();
            }
        });

        setAmbientEnabled();
    }

    @Override
    protected void onPostResume() {
        Log.d(mainTag,"onResume");

        readFile();
        super.onPostResume();
    }

    public void readFile(){

        InputStream inputStream = null;
        try {
            inputStream = openFileInput(fileName);
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String temp = "";
                StringBuffer stringBuffer = new StringBuffer();
                while((temp = bufferedReader.readLine())!= null){
                    Log.d(mainTag,"temp : "+ temp);
                    adapter.setListView(temp);
                    adapter.notifyDataSetChanged();
                }
                inputStream.close();

            }else{
                Log.d(mainTag,"file not exists");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void alertDilog(final String busID, final String stationName, final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String strResult = "";
        final ArrayList<String> saveStr = new ArrayList<String>();
        builder.setTitle("BUS DELETE").setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                InputStream inputStream = null;
                try {
                    inputStream = openFileInput(fileName);
                    if(inputStream!=null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String temp = "";
                        StringBuffer stringBuffer = new StringBuffer();
                        while((temp = bufferedReader.readLine())!= null){
                            Log.d(mainTag,"temp : "+ temp);
                            saveStr.add(setListView(temp,busID,stationName)) ;

                        }
                        inputStream.close();

                    }else{
                        Log.d(mainTag,"file not exists");
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));
                    for(int i=0;i<saveStr.size();i++){
                        outputStreamWriter.write(saveStr.get(i));
                    }
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


    public String setListView(String temp,String d_busID,String d_stationName){
        String[] str = temp.split("/");

        String strResult = "";
        String busNum = "";
        String stationNum = "";
        String busID = "";
        String stationName = "";

        Log.d(mainTag,"select Bus ID : "+ d_busID+"/select statione name : " +d_stationName);

        int pos = 0;

        for(int i = 0; i < str.length/4;i++){
            busNum = str[4*i+0];
            stationName = str[4*i+1];
            busID = str[4*i+2];
            stationNum = str[4*i+3];

            Log.d(mainTag,"busNUM : "+ busNum+"/stationName : " + stationName+ "/ busID : " + busID + "/ stationNum : " + stationNum);

            if(d_busID.equals(busNum) && d_stationName.equals(stationName))
            {
                pos = i;
                Log.d(mainTag,"delete ITem : busNum "+ busNum + "/ stationName : " + stationName);
            }else{
                strResult+=((busNum)+"/"+(stationName)+"/"+(busID)+"/"+(stationNum)+"/");
                Log.d(mainTag,"not delete : " + strResult);
            }
        }
        Log.d(mainTag,"delete : " + strResult);

        adapter.removeItem(pos);
        adapter.notifyDataSetChanged();
        return strResult;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}