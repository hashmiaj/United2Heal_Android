package com.u2h.user.united2healandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class BoxPage extends Fragment {
    String[] categories={"Category A","Category B","Category C"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.box_page,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView boxCategoriesListView = (ListView) view.findViewById(R.id.boxCategoriesListView);
        final CustomListAdapter listAdapter= new CustomListAdapter(getActivity(), categories);
        boxCategoriesListView.setAdapter(listAdapter);
        boxCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent loadCloseBoxPage=new Intent(getContext(),CloseBoxPage.class);
                String categoryName=(String)boxCategoriesListView.getAdapter().getItem(i);
                loadCloseBoxPage.putExtra("com.u2h.user.united2healandroid.CATEGORY_NAME",categoryName);
                startActivity(loadCloseBoxPage);
            }
        });

    }
}
