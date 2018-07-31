package com.u2h.user.united2healandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

public class AddItemPage extends Fragment {
    ArrayList<String> categoryList=new ArrayList<>();
    ArrayList<Item> itemArrayList=new ArrayList<>();
    Spinner itemCategorySpinner;
    Button generateCodeButton;
    TextView codeInputTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_item_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemCategorySpinner=(Spinner)view.findViewById(R.id.itemCategorySpinner);
        codeInputTextView=(TextView) view.findViewById(R.id.itemCodeInput);
        generateCodeButton=(Button) view.findViewById(R.id.generateCodeButton);
        generateCodeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rand= (int)(Math.random()*100000);
                codeInputTextView.setText(String.valueOf(rand));
            }
        });
        GetData data= new GetData();
        data.execute();
    }
    private class GetData extends AsyncTask<String,String,String>
    {
        Connection conn= null;
        Statement stmnt=null;

        @Override
        protected void onPreExecute(){
            categoryList.clear();
            itemArrayList.clear();
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn= DriverManager.getConnection(DatabaseStrings.DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                stmnt=conn.createStatement();
                String sql="Select * from u2hdb.ItemTable";
                ResultSet rs=stmnt.executeQuery(sql);
                while (rs.next())
                {
                    categoryList.add(rs.getString("CategoryName"));
                    Item item= new Item(rs.getString("CategoryName"),rs.getInt("ItemID"),rs.getString("ItemName"));
                    itemArrayList.add(item);
                }
                ArrayList<String> temp= new ArrayList<>();
                HashSet<String> hashSet=new HashSet<>();
                for(String s:categoryList)
                {
                    if(!hashSet.contains(s))
                    {
                        temp.add(s);
                        hashSet.add(s);
                    }
                }
                categoryList=temp;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String msg)
        {
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getActivity(),R.layout.spinner_item_small,categoryList);
            itemCategorySpinner.setAdapter(arrayAdapter);
        }
    }
}
