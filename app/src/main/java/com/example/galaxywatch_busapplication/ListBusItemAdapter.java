package com.example.galaxywatch_busapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListBusItemAdapter extends BaseAdapter {

    ArrayList<ListBusItem> items = new ArrayList<ListBusItem>();
    Context context;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        ListBusItem listBusItem = items.get(position);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_busitem,parent,false);

        }
        TextView txt_busID = convertView.findViewById(R.id.busID);
        TextView txt_firstBus = convertView.findViewById(R.id.firstBus);
        TextView txt_secondBus = convertView.findViewById(R.id.secondBus);

        txt_busID.setText(listBusItem.getBusID());
        txt_firstBus.setText(listBusItem.getFirstBus());
        txt_secondBus.setText(listBusItem.getSecondBus());


        return convertView;
    }

    public void addItem(ListBusItem busItem)
    {
        items.add(busItem);
    }
}
