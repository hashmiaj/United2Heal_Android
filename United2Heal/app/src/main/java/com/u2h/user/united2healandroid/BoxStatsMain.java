package com.u2h.user.united2healandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BoxStatsMain extends Fragment {
    private ArrayList<String> groupList = new ArrayList<>();
    ListView mainListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.box_stats_listview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetData retrieveData = new GetData();
        retrieveData.execute();
        mainListView = (ListView) view.findViewById(R.id.boxStatsListView);
        TextView emptyTextView = (TextView) getView().findViewById(R.id.emptyTextView);

        mainListView.setEmptyView(emptyTextView);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new BoxStatsBoxes();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle data = new Bundle();
                data.putString("com.u2h.user.united2healandroid.SELECTED_GROUP", mainListView.getAdapter().getItem(i).toString());
                fragment.setArguments(data);
                fragmentTransaction.replace(R.id.MainFrame, fragment, "BoxList").addToBackStack(null);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Boxes");
                fragmentTransaction.commit();
            }
        });
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groups");


    }

    private class GetData extends AsyncTask<String, String, String> {
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://" + DatabaseStrings.DATABASE_URL + "/" + DatabaseStrings.DATABASE_NAME;

        @Override
        protected void onPreExecute() {
            groupList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmnt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "SELECT * FROM u2hdb.BoxTable";
                ResultSet rs = stmnt.executeQuery(sql);
                while (rs.next()) {
                    groupList.add(rs.getString("GroupName"));
                }
                ArrayList<String> tempList = new ArrayList<>();
                HashSet tempSet = new HashSet<>();
                for (String s : groupList) {
                    if (!tempSet.contains(s)) {
                        tempSet.add(s);
                        tempList.add(s);
                    }
                }
                groupList = tempList;
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
            String[] newList = new String[groupList.size()];
            if (getActivity() != null) {
                CustomListAdapter listAdapter = new CustomListAdapter(getActivity(), groupList.toArray(newList));

                mainListView.setAdapter(listAdapter);
            }
        }
    }
}
