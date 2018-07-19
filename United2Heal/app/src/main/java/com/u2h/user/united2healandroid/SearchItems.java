package com.u2h.user.united2healandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class SearchItems extends Fragment {
    ListView list;
    String[] items={"Item A","Item B", "Bandages","Bandage Kit","Dummy items"};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.search_items,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView emptyTextView=(TextView)getView().findViewById(R.id.empty);
        list = (ListView) getView().findViewById(R.id.itemListView);
        list.setEmptyView(emptyTextView);
        final CustomListAdapter listAdapter = new CustomListAdapter(getActivity(), items);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent loadItemDetails =new Intent(getContext(),ItemPage.class);
                String name= (String)list.getAdapter().getItem(i);
                loadItemDetails.putExtra("com.u2h.user.united2healandroid.ITEM_NAME",name);
                startActivity(loadItemDetails);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu,menu);

        final MenuItem searchItem=menu.findItem(R.id.action_search);

        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setQueryHint("Search for an item");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextChange(String s) {
                /*Iterates through the whole list of items and adds items containing
                the inputted string to a new list. A new ListAdapter is made with the
                new list inputted for it's values. The ListView displaying the search items
                is then updated by setting its adapter to the newly created one.*/
                ArrayList<String> tempList= new ArrayList<>();
                for(String item:items)
                {
                if(item.toLowerCase().contains(s.toLowerCase()))
                {
                    tempList.add(item);
                }
                }
                String[] newList= new String[tempList.size()];
                newList= tempList.toArray(newList);
                for(String i:newList)
                Log.i("name",i);

                CustomListAdapter newListAdapter= new CustomListAdapter(getActivity(),newList);
                list.setAdapter(newListAdapter);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String s) {

                //searchItem.collapseActionView();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }
}
