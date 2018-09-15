package com.u2h.user.united2healandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wutka.dtd.DTDAttlist;

import org.apache.commons.lang3.StringUtils;
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
    String[] hardcodedArray={"Lab" ,
            "Surgical Instruments" ,
            "Rehab" ,
            "Sterilization" ,
            "Furnishing Textiles" ,
            "Diagnostic Supplies" ,
            "Pharmaceuticals" ,
            "Collection Drainage Suction" ,
            "Incontinence" ,
            "GI" ,
            "Respiratory" ,
            "Apparel" ,
            "Housekeeping" ,
            "Wound Care" ,
            "Utensils" ,
            "Wound Closure" ,
            "Patient Comfort" ,
            "I.V. Therapy" ,
            "Personal Hygiene" ,
            "Needles and Syringes" ,
            "Implants" ,
            "Gloves"};
    TextView nameInputTextView;
    Spinner itemCategorySpinner;
    Button generateCodeButton;
    TextView codeInputTextView;

    String itemName;
    String code;
    String categoryName;

    Boolean connectedToDB=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_item_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //itemCategorySpinner=(Spinner)view.findViewById(R.id.itemCategorySpinner);
        codeInputTextView=(TextView) view.findViewById(R.id.itemCodeInput);
        nameInputTextView=(TextView) view.findViewById(R.id.itemNameInput);
        generateCodeButton=(Button) view.findViewById(R.id.generateCodeButton);
        /*itemCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryName=itemCategorySpinner.getAdapter().getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        Button submitItemButton=(Button) view.findViewById(R.id.submitItemButton);
        submitItemButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                code=codeInputTextView.getText().toString();
                itemName = nameInputTextView.getText().toString();
                if(itemName.equals(""))
                {
                    Toast.makeText(getActivity(),"Error: Enter a Name",Toast.LENGTH_SHORT).show();

                }
                else if(!StringUtils.isNumeric(code) ) {
                    Toast.makeText(getActivity(), "Error: Invalid Code", Toast.LENGTH_SHORT).show();

                }
                else{
                    PostData data = new PostData();
                    data.execute();
                }
            }
        });
        generateCodeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rand= (int)(Math.random()*100000);
                codeInputTextView.setText(String.valueOf(rand));
            }
        });
        /*GetData data= new GetData();
        data.execute();*/
    }
    /*private class GetData extends AsyncTask<String,String,String>
    {
        Connection conn= null;
        Statement stmnt=null;

        @Override
        protected void onPreExecute(){
            categoryList.clear();
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
                }
                for(String s: hardcodedArray)
                {
                    categoryList.add(s);
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
            arrayAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown_resource);
            itemCategorySpinner.setAdapter(arrayAdapter);
            if(categoryList.size()>0)
            {
                connectedToDB=true;
            }
        }
    }*/
    private class PostData extends AsyncTask<String,String,String>
    {
        Connection conn;
        Statement stmnt;
        Boolean success=false;
        Boolean duplicate=false;
        Boolean duplicateKey=false;
        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn=DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                String sql="Select * from u2hdb.ItemTable";
                stmnt=conn.createStatement();
                ResultSet rs=stmnt.executeQuery(sql);
                ArrayList<String> existingNames=new ArrayList<>();
                while(rs.next())
                {
                    existingNames.add(rs.getString("ItemName").toLowerCase());
                }
                rs.close();
             for(String s:existingNames)
             {
                 if(s.toLowerCase().equals(itemName.toLowerCase()))
                 {
                     duplicate=true;

                 }
             }
             if(!duplicate)
             {
                 sql="INSERT INTO u2hdb.ItemTable \n"+
                         "values ("+code+",'','"+itemName+"')";
                 stmnt.executeUpdate(sql);
             }
             success=true;
             stmnt.close();
             conn.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                if(e.getSQLState().equals("23000"))
                {
                    duplicateKey=true;
                }

                    e.printStackTrace();

            }
            finally {

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
        protected void onPostExecute(String msg)
        {
            if(duplicate)
            {Toast.makeText(getActivity(),"Error, item name already exists",Toast.LENGTH_SHORT).show();

            }
            else if(duplicateKey)
            {
                Toast.makeText(getActivity(),"Error: Code already exists",Toast.LENGTH_SHORT).show();

            }
            else if(success){
                Toast.makeText(getActivity(),"Succes! Added Item",Toast.LENGTH_SHORT).show();
                nameInputTextView.setText("");
                codeInputTextView.setText("");
            }
            else if(!success)
            {
                Toast.makeText(getActivity(),"Failed to add",Toast.LENGTH_SHORT).show();

            }
        }
    }

}
