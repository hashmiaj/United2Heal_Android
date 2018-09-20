package com.u2h.user.united2healandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoxStatsListAdapter extends BaseAdapter{
    LayoutInflater mInflator;
    private ArrayList<BoxStatsDataPoint> dataArray;
    public BoxStatsListAdapter(Context c, ArrayList<BoxStatsDataPoint> dataArray)
    {
       this.dataArray=dataArray;
        mInflator=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Object getItem(int i) {
        return dataArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=mInflator.inflate(R.layout.spreadsheet_list_adapter,null);
        TextView itemNameTextView=v.findViewById(R.id.itemNameTextView);
        TextView quantitiyTextView=v.findViewById(R.id.quantityTextView);
        itemNameTextView.setText(dataArray.get(i).getItemName());
        quantitiyTextView.setText("x"+dataArray.get(i).getQuantity());

        return v;
    }
}