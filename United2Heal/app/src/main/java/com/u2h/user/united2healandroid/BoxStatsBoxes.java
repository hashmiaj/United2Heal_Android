package com.u2h.user.united2healandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BoxStatsBoxes extends Fragment {
    private String groupClicked;
    TextView emptyTextView;
    private ArrayList<String> boxList=new ArrayList<>();
    ListView mainListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.box_stats_listview,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((UserInfo)getActivity().getApplication()).allowAsync=true;
        GetData retrieveData=new GetData();
        retrieveData.execute();

        Bundle data=this.getArguments();
        if(data!=null)
        {
            groupClicked=data.getString("com.u2h.user.united2healandroid.SELECTED_GROUP");
        }
        mainListView=(ListView)view.findViewById(R.id.boxStatsListView);
        emptyTextView=(TextView)getView().findViewById(R.id.emptyTextView);

        mainListView.setEmptyView(emptyTextView);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),BoxStats.class);
                intent.putExtra("com.u2h.user.united2healandroid.BOX_NAME_PICKED",mainListView.getAdapter().getItem(i).toString());
                intent.putExtra("com.u2h.user.united2healandroid.BOX_CATEGORY_PICKED",groupClicked);

                startActivity(intent);
            }
        });
    }
    private class GetData extends AsyncTask<String,String,String> {
        static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
        Boolean isEmpty=false;
        static final String DB_URL="jdbc:mysql://"+DatabaseStrings.DATABASE_URL+"/"+DatabaseStrings.DATABASE_NAME;
        @Override
        protected String doInBackground(String... strings) {
            Connection conn=null;
            Statement stmnt=null;
            try{
                Class.forName(JDBC_DRIVER);
                conn= DriverManager.getConnection(DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                stmnt=conn.createStatement();
                String sql="SELECT * FROM u2hdb.BoxTable where GroupName='"+groupClicked+"' ORDER BY BoxNumber ASC";
                ResultSet rs= stmnt.executeQuery(sql);
                while(rs.next())
                {
                    boxList.add(rs.getString("BoxNumber"));
                    Log.e("String",rs.getString("BoxNumber"));
                }
                if(boxList.size()==0)
                {
                    isEmpty=true;
                }
                rs.close();
                stmnt.close();
                conn.close();
            }
            catch (SQLException e)
            {

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally{

                try {
                    if(stmnt!=null)
                        stmnt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
                try {
                    if(conn!=null)
                        conn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String msg)
        {
            String[] newList=new String[boxList.size()];
            CustomListAdapter listAdapter=new CustomListAdapter(getContext(),boxList.toArray(newList));
            mainListView.setAdapter(listAdapter);
            if(isEmpty)
            {
                emptyTextView.setText("No boxes in this category");
            }
        }
    }
}


