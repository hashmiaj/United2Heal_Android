package com.u2h.user.united2healandroid;

import android.content.Intent;
import android.os.AsyncTask;
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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchItems extends Fragment {
    ListView list;
    ArrayList<String> itemList = new ArrayList<>();
    TextView emptyTextView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.search_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetData data= new GetData();
        data.execute();
        emptyTextView = (TextView) getView().findViewById(R.id.empty);
        list = (ListView) getView().findViewById(R.id.itemListView);
        list.setEmptyView(emptyTextView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent loadItemDetails = new Intent(getContext(), ItemPage.class);
                String name = (String) list.getAdapter().getItem(i);
                loadItemDetails.putExtra("com.u2h.user.united2healandroid.ITEM_NAME", name);
                startActivity(loadItemDetails);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search for an item");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextChange(String s) {
                /*Iterates through the whole list of items and adds items containing
                the inputted string to a new list. A new ListAdapter is made with the
                new list inputted for it's values. The ListView displaying the search items
                is then updated by setting its adapter to the newly created one.*/
                ArrayList<String> tempList = new ArrayList<>();
                for (String item : itemList) {
                    if (item.toLowerCase().contains(s.toLowerCase())) {
                        tempList.add(item);
                    }
                }
                String[] newList = new String[tempList.size()];
                newList = tempList.toArray(newList);
                for (String i : newList)
                    Log.i("name", i);

                CustomListAdapter newListAdapter = new CustomListAdapter(getActivity(), newList);
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

    private class GetData extends AsyncTask<String, String, String> {
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        Boolean isEmpty = false;
        static final String DB_URL = "jdbc:mysql://" + DatabaseStrings.DATABASE_URL + "/" + DatabaseStrings.DATABASE_NAME;
        @Override
        protected void onPreExecute()
        {
itemList.clear();

        }
        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmnt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "SELECT * FROM u2hdb.ItemTable";
                ResultSet rs = stmnt.executeQuery(sql);
                while (rs.next()) {
                    itemList.add(rs.getString("ItemName"));
                }
                if (itemList.size() == 0) {
                    isEmpty = true;
                }
                rs.close();
                stmnt.close();
                conn.close();
            } catch (SQLException e) {

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (stmnt != null)
                        stmnt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            String[] newList = new String[itemList.size()];
            CustomListAdapter listAdapter = new CustomListAdapter(getContext(), itemList.toArray(newList));
            list.setAdapter(listAdapter);
            if (isEmpty) {
                emptyTextView.setText("No boxes in this category");
            }
        }
    }
}
