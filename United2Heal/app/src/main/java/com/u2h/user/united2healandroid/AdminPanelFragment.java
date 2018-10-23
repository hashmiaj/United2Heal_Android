package com.u2h.user.united2healandroid;

import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AdminPanelFragment extends Fragment {
    TextView changePasswordTextView;
    Button changePasswordButton;
    String passValue;
    Spinner groupSpinner;
    Spinner boxNumberSpinner;
    String selectedGroupName;
    String selectedBox;
    Button openBoxButton;
    Button closeBoxButton;
    ArrayList<String> groupList=new ArrayList<>();
    ArrayList<String> boxList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_admin_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupSpinner=view.findViewById(R.id.groupSpinner);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroupName=groupSpinner.getAdapter().getItem(i).toString();
                GetBoxNumber getBoxNumber= new GetBoxNumber();
                getBoxNumber.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        boxNumberSpinner= view.findViewById(R.id.boxNumberSpinner);
        boxNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBox=boxNumberSpinner.getAdapter().getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        GetGroupData getGroupData= new GetGroupData();
        getGroupData.execute();
        openBoxButton=view.findViewById(R.id.openBoxButton);
        closeBoxButton=view.findViewById(R.id.closeBoxButton);
        closeBoxButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseBox closeBox=new CloseBox();
                closeBox.execute();
                updateButtons(0);
                Toast.makeText(getActivity(),"Box "+selectedGroupName+selectedBox+ " has been closed", Toast.LENGTH_SHORT).show();

            }
        });
        openBoxButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBox openBox=new OpenBox();
                openBox.execute();
                updateButtons(1);
                Toast.makeText(getActivity(),"Box "+selectedGroupName+selectedBox+ " has been opened", Toast.LENGTH_SHORT).show();

            }
        });        changePasswordTextView=view.findViewById(R.id.passwordTextView);
        changePasswordButton=view.findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changePasswordTextView.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getActivity(),"Error, Invalid Password", Toast.LENGTH_SHORT).show();
                }
                else if(!changePasswordTextView.getText().toString().trim().equals(changePasswordTextView.getText().toString())){
                    Toast.makeText(getActivity(),"Error, Invalid Password, remove spaces at beginning or end", Toast.LENGTH_SHORT).show();

                }
                else{
                    passValue=changePasswordTextView.getText().toString().trim();
                    PassData data=new PassData();
                    data.execute();
                }
            }
        });
    }
    public class PassData extends AsyncTask<String,String,String> {
        Connection conn;
        Statement stmnt;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "UPDATE u2hdb.PasswordTable SET Password='" + passValue+"'";
                stmnt.executeUpdate(sql);
                stmnt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmnt != null)
                        stmnt.close();
                } catch (SQLException e) {

                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {

                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            Toast.makeText(getActivity(),"Password changed to \""+passValue+"\"", Toast.LENGTH_SHORT).show();

        }
    }
    public class CloseBox extends AsyncTask<String,String,String> {
        Connection conn;
        Statement stmnt;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "UPDATE u2hdb.BoxTable SET IsOpen=0 WHERE BoxNumber='"+selectedBox+"'  AND GroupName='"+selectedGroupName+"'";
                stmnt.executeUpdate(sql);
                stmnt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmnt != null)
                        stmnt.close();
                } catch (SQLException e) {

                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {

                }

            }

            return null;
        }


    }
    public class OpenBox extends AsyncTask<String,String,String> {
        Connection conn;
        Statement stmnt;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "UPDATE u2hdb.BoxTable SET IsOpen=1 WHERE BoxNumber='"+selectedBox+"'  AND GroupName='"+selectedGroupName+"'";
                stmnt.executeUpdate(sql);
                stmnt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmnt != null)
                        stmnt.close();
                } catch (SQLException e) {

                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {

                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {

        }
    }
    public class GetGroupData extends AsyncTask<String,String,String>
    {
        Connection conn;
        Statement stmnt;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                Connection conn= DriverManager.getConnection(DatabaseStrings.DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                String sql= "Select DISTINCT GroupName from u2hdb.BoxTable";
                stmnt=conn.createStatement();
                ResultSet rs= stmnt.executeQuery(sql);
                groupList.clear();
                while(rs.next())
                {
                    groupList.add(rs.getString("GroupName"));

                }
                rs.close();
                stmnt.close();
                conn.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try{
                    if(stmnt!=null)
                        stmnt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try{
                    if(conn!=null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }
        @Override
        public void onPostExecute(String msg)
        {
            if(selectedGroupName==null)
            selectedGroupName=groupList.get(0);
            ArrayAdapter<String> groupAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_item,groupList);
            groupSpinner.setAdapter(groupAdapter);
            GetBoxNumber getBoxNumber=new GetBoxNumber();
            getBoxNumber.execute();
        }
    }
    public class GetBoxNumber extends AsyncTask<String,String,String>
    {
        Connection conn;
        Statement stmnt;
        ArrayList<Integer> isBoxOpen=new ArrayList<Integer>();
        @Override
        protected String doInBackground(String... strings) {
            try {

                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn=DriverManager.getConnection(DatabaseStrings.DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                stmnt=conn.createStatement();
                String sql="Select * from u2hdb.BoxTable WHERE GroupName='"+selectedGroupName+"'";
                ResultSet rs= stmnt.executeQuery(sql);
                boxList.clear();
                while(rs.next())
                {
                boxList.add(rs.getString("BoxNumber"));
                isBoxOpen.add(rs.getInt("IsOpen"));
                }
                rs.close();
                stmnt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                try{
                    if(stmnt!=null)
                        stmnt.close();
                }
                catch (SQLException e)
                {

                }
                try{
                    if(conn!=null)
                        conn.close();
                }
                catch (SQLException e)
                {

                }
            }
           return null;
        }
        @Override
        public void onPostExecute(String msg)
        {
            selectedBox=boxList.get(0);
           updateButtons(isBoxOpen.get(0));
            ArrayAdapter<String> boxNumberAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_item,boxList);
            boxNumberSpinner.setAdapter(boxNumberAdapter);
        }
    }
    public void updateButtons(int value)
    {
        if(value==0)
        {
            closeBoxButton.setEnabled(false);
            closeBoxButton.setTextColor(getResources().getColor(R.color.darkerGray));
            openBoxButton.setEnabled(true);
            openBoxButton.setTextColor(Color.BLACK);
        }
        else
        {
            openBoxButton.setEnabled(false);
            openBoxButton.setTextColor(getResources().getColor(R.color.darkerGray));
            closeBoxButton.setEnabled(true);
            closeBoxButton.setTextColor(Color.BLACK);
        }
    }
}
