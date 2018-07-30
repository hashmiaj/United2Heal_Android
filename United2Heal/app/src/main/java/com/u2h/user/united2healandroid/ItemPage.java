package com.u2h.user.united2healandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ItemPage extends AppCompatActivity {
    String itemCategory;
    String itemID;
    TextView itemCategoryTextView;
    TextView itemIdTextView;
    String selectedItem;
    Spinner itemBoxSpinner;
    ArrayList<String> boxList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        TextView itemNameTextView = (TextView) findViewById(R.id.itemNameTextView);
        itemCategoryTextView = (TextView) findViewById(R.id.itemCategoryTextView);
        itemIdTextView = (TextView) findViewById(R.id.codeTextView);
        itemBoxSpinner = (Spinner) findViewById(R.id.itemBoxSpinner);
        if (getIntent().getExtras() != null) {
            selectedItem = getIntent().getExtras().getString("com.u2h.user.united2healandroid.ITEM_NAME");
            itemNameTextView.setText(selectedItem);

        }
        GetData data = new GetData();
        data.execute();
    }

    private class GetData extends AsyncTask<String, String, String> {
        boolean isEmpty = false;
        Connection conn = null;
        Statement stmnt = null;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                String sql = "Select * from u2hdb.ItemTable WHERE ItemName='" + selectedItem + "'";
                stmnt = conn.createStatement();
                ResultSet rs = stmnt.executeQuery(sql);

                while (rs.next()) {
                    itemCategory = rs.getString("CategoryName");
                    itemID = rs.getInt("ItemID") + "";
                }
                sql="Select * from u2hdb.BoxTable WHERE IsOpen=1 and CategoryName='"+itemCategory+"'";
                rs=stmnt.executeQuery(sql);
                while (rs.next())
                {
                    boxList.add(rs.getString("BoxName"));
                }
                conn.close();
                stmnt.close();
                rs.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (stmnt != null) {
                        stmnt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            itemCategoryTextView.setText(itemCategory);
            itemIdTextView.setText(itemID);
            ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(ItemPage.this,R.layout.spinner_item,boxList);
            itemBoxSpinner.setAdapter(dataAdapter);
        }
    }
}
