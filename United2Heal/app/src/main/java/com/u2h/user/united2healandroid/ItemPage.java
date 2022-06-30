package com.u2h.user.united2healandroid;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemPage extends AppCompatActivity implements CalendarDialog.DialogListener, ItemConfirmDialog.ConfirmDialogListener{
    String itemCategory;
    String itemID;
    TextView groupNameTextView;
    TextView itemIdTextView;
    String selectedItem;
    Spinner itemBoxSpinner;
    Button submitButton;
    CheckBox hasExpirationCheckBox;
    String expirationDate="None";
    Boolean hasExpiration=false;
    Boolean dateSet=false;
    String itemQuantity;
    String schoolName;
    String selectedBox;
    Boolean connectedToDB=false;
    ArrayList<String> boxList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        schoolName=((UserInfo)getApplication()).getSchoolName();
        final TextView itemNameTextView = (TextView) findViewById(R.id.itemNameTextView);
        final CalendarDialog calendar= new CalendarDialog();
        final Button expirationDateButton= (Button) findViewById(R.id.chooseExpirationDate);
        expirationDateButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager= getSupportFragmentManager();
                calendar.show(manager,"");
            }
        });
        groupNameTextView = (TextView) findViewById(R.id.groupNameTextView);
        itemIdTextView = (TextView) findViewById(R.id.codeTextView);
        hasExpirationCheckBox= (CheckBox) findViewById(R.id.expirationCheckBox);
        hasExpirationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    expirationDateButton.setTextColor(Color.BLACK);
                    hasExpiration=true;
                    expirationDateButton.setEnabled(true);
                }
                else{
                    expirationDateButton.setTextColor(getResources().getColor(R.color.darkerGray));
                    hasExpiration=false;
                    dateSet=false;
                    expirationDateButton.setEnabled(false);
                    expirationDate="None";

                }

            }
        });
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
                else if(!dateSet && hasExpiration)
                {
                    Toast.makeText(getApplicationContext(),"Error: Set Expiration Date ",Toast.LENGTH_SHORT).show();

                }
                else  {
                    ItemConfirmDialog dialog = new ItemConfirmDialog();
                    Bundle bundle= new Bundle();
                    bundle.putString("ITEM_NAME",itemNameTextView.getText().toString());
                    bundle.putString("ITEM_QUANTITY",itemQuantity);
                    bundle.putString("BOX_NUMBER",selectedBox);
                    bundle.putString("EXPIRATION",expirationDate);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(),null);
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

    @Override
    public void onPositiveClick(CalendarDialog dialog) {
        expirationDate=dialog.date;
        dateSet=true;
        Toast.makeText(this, "Expiration date set to "+dialog.date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeClick(CalendarDialog dialog) {

    }

    @Override
    public void onConfirm() {
        PostData data= new PostData();
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
                    itemID = rs.getInt("ItemID") + "";
                }
                sql = "Select * from u2hdb.BoxTable WHERE IsOpen=1 and GroupName='" + ((UserInfo)getApplication()).getGroupName() + "' and School='"+schoolName+"' ORDER BY BoxNumber ASC";
                rs = stmnt.executeQuery(sql);
                while (rs.next()) {
                    boxList.add(rs.getString("BoxNumber"));
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
            groupNameTextView.setText(((UserInfo)getApplication()).getGroupName());
            itemIdTextView.setText(itemID);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ItemPage.this, R.layout.spinner_item, boxList);
            dataAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown_resource);
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
                String sql = "Select Count(*) AS length from u2hdb.ItemBox where ItemName='" + selectedItem + "' AND BoxNumber='" + selectedBox+ "' and ExpirationDate='"+expirationDate+"' and GroupName='"+((UserInfo)getApplication()).getGroupName()+"' and School='"+schoolName+"'";
                ResultSet rs = stmnt.executeQuery(sql);
                rs.next();
                int count=rs.getInt("length");
                rs.close();
                if(count>0)
                {
                    sql="UPDATE u2hdb.ItemBox SET ItemQuantity=ItemQuantity+"+itemQuantity+" where ItemName='" + selectedItem + "' AND BoxNumber='" + selectedBox + "' and ExpirationDate='"+expirationDate+"' and GroupName='"+((UserInfo)getApplication()).getGroupName()+"' and School='"+schoolName+"'";
                    stmnt.executeUpdate(sql);

                }else {
                    sql = "Select MAX(ItemBoxID) AS max from u2hdb.ItemBox";
                    rs=stmnt.executeQuery(sql);
                    rs.next();
                    int id=rs.getInt("max")+1;
                    sql = "INSERT INTO u2hdb.ItemBox\n" +
                            "Values (" + id + ",'" + itemID + "','"+((UserInfo)getApplication()).getGroupName()+"','"+ selectedBox +  "','"+selectedItem + "','" + itemQuantity+"','"+expirationDate+"','"+schoolName+"', '" +Calendar.getInstance().getTimeInMillis()+ "')";
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
