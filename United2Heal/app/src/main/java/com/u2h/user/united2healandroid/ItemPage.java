package com.u2h.user.united2healandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
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
    Button submitButton;
    String itemQuantity;
    String selectedBox;
    Boolean connectedToDB=false;
    ArrayList<String> boxList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        TextView itemNameTextView = (TextView) findViewById(R.id.itemNameTextView);
        itemCategoryTextView = (TextView) findViewById(R.id.itemCategoryTextView);
        itemIdTextView = (TextView) findViewById(R.id.codeTextView);
        itemBoxSpinner = (Spinner) findViewById(R.id.itemBoxSpinner);
        submitButton = (Button) findViewById(R.id.submitButton);
        final TextView quantityTextView = (TextView) findViewById(R.id.quantityEdit);
        quantityTextView.setText("");
        itemBoxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBox = itemBoxSpinner.getAdapter().getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemQuantity = quantityTextView.getText().toString();
                if(itemQuantity.equals("")) {
                    Toast.makeText(getApplicationContext(),"Error: Enter a quantity ",Toast.LENGTH_SHORT).show();
                }
                else if(!connectedToDB)
                {
                    Toast.makeText(getApplicationContext(),"Wait for Database to connect",Toast.LENGTH_SHORT).show();

                }
                else  {
                    PostData postData = new PostData();
                    postData.execute();
                }
            }
        });
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
            boxList.clear();
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
                sql = "Select * from u2hdb.BoxTable WHERE IsOpen=1 and CategoryName='" + itemCategory + "'";
                rs = stmnt.executeQuery(sql);
                while (rs.next()) {
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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ItemPage.this, R.layout.spinner_item, boxList);
            itemBoxSpinner.setAdapter(dataAdapter);
            connectedToDB=true;
        }
    }

    private class PostData extends AsyncTask<String, String, String> {
        Connection conn;
        Statement stmnt;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();

                ArrayList<String> itemBoxEntries = new ArrayList<>();
                ArrayList<Integer> itemBoxID = new ArrayList<>();
                int random = (int) (Math.random() * 100000);
                String sql = "Select Count(*) AS length from u2hdb.ItemBox where ItemName='" + selectedItem + "' AND BoxName='" + selectedBox + "'";
                ResultSet rs = stmnt.executeQuery(sql);
                rs.next();
                int count=rs.getInt("length");
                rs.close();
                if(count>0)
                {
                    sql="UPDATE u2hdb.ItemBox SET ItemQuantity=ItemQuantity+"+itemQuantity+" where ItemName='" + selectedItem + "' AND BoxName='" + selectedBox + "'";
                    stmnt.executeUpdate(sql);

                }else {
                    sql = "INSERT INTO u2hdb.ItemBox\n" +
                            "Values (" + random + ",'','" + itemID + "','" + itemQuantity + "','" + selectedBox + "','" + selectedItem + "')";
                    stmnt.executeUpdate(sql);
                }
                conn.close();
                stmnt.close();
            } catch (ClassNotFoundException e) {

            } catch (SQLException e) {
                if(e.getErrorCode()==23505)
                {
                    Toast.makeText(getApplicationContext(),"TRY AGAIN",Toast.LENGTH_SHORT).show();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (stmnt != null)
                        stmnt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(getApplicationContext(),itemQuantity+" "+selectedItem+ " added to box "+selectedBox,Toast.LENGTH_SHORT).show();
            finish();

        }
    }
}
