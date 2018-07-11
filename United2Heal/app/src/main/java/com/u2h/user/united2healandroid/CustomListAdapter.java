package com.u2h.user.united2healandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] items;
    public CustomListAdapter(Context c, String[] i)
    {
        items=i;
        mInflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=mInflater.inflate(R.layout.custom_list_view,null);
        TextView itemNameTextView= v.findViewById(R.id.itemNameTextView);
        itemNameTextView.setText(items[i]);


        return v;
    }
}
