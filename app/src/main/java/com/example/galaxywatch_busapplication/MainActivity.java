package com.example.galaxywatch_busapplication;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    ListView listView;
    ListBusItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        adapter = new ListBusItemAdapter();

        adapter.addItem(new ListBusItem("8106","3번째 전 / 3분 25초","7번째 전 / 13분 25초"));
        adapter.addItem(new ListBusItem("1009","1번째 전 / 1분 25초","18번째 전 / 23분 25초"));
        adapter.addItem(new ListBusItem("6-2","2번째 전 / 3분 22초","3번째 전 / 13분 25초"));
        adapter.addItem(new ListBusItem("602-1","1번째 전 / 1분 25초","15번째 전 / 23분 25초"));

        listView.setAdapter(adapter);
        // Enables Always-on
        setAmbientEnabled();
    }
}