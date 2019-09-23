package com.u2h.user.united2healandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BoxStats extends AppCompatActivity implements  ToggleBoxDialog.ToggleBoxDialogListener {
    private String selectedBox;
    private String selectedGroup;
    private int boxID;
    private Button boxOpenOrCloseButton;
    private int isBoxOpen;
    File csvFile;
    private TextView emptyTextView;
    private ArrayList<BoxStatsDataPoint> boxStats = new ArrayList<>();

    ListView boxStatsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_stats);
        boxStatsListView = (ListView) findViewById(R.id.boxStatsListView);
        boxOpenOrCloseButton = (Button) findViewById(R.id.openOrCloseBoxButton);

        Button exportButton = (Button) findViewById(R.id.exportBoxStatsButton);
        exportButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToCSV();
            }
        });
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        boxStatsListView.setEmptyView(emptyTextView);
        if (getIntent().getExtras() != null) {
            selectedBox = getIntent().getExtras().getString("com.u2h.user.united2healandroid.BOX_NAME_PICKED");
            boxID = getIntent().getExtras().getInt("BoxID");
            selectedGroup = getIntent().getExtras().getString("com.u2h.user.united2healandroid.BOX_CATEGORY_PICKED");

            GetData data = new GetData();
            data.execute();
        }
        boxOpenOrCloseButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment confirm = new ToggleBoxDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("BoxID", boxID);
                bundle.putInt("IsBoxOpen", isBoxOpen);
                confirm.setArguments(bundle);
                confirm.show(getSupportFragmentManager(), "confirm");
            }
        });

    }

    public void emailCSV() {
        Context context = BoxStats.this;
        Uri pathToFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.u2h.user.united2healandroid.provider", csvFile);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, pathToFile);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, selectedGroup + " Box " + selectedBox + " Datatable CSV");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void exportToCSV() {
        Context context = BoxStats.this;
        try {
            csvFile = new File(context.getExternalCacheDir().toString() + "/" + selectedGroup + " Box " + selectedBox + ".csv");
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));
            csvWriter.writeNext(new String[]{"ItemName", "ItemQuantity", "ExpirationDate"});
            for (int i = 0; i < boxStats.size(); i++) {
                csvWriter.writeNext(new String[]{boxStats.get(i).getItemName(), boxStats.get(i).getQuantity() + "", boxStats.get(i).getExpirationDate()});
            }
            csvWriter.close();
            emailCSV();
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
    }

    @Override
    public void onSubmitted(int isOpen) {
        isBoxOpen=isOpen;
        boxOpenOrCloseButton.setText(isBoxOpen == 1 ? "Close Box" : "Open Box");

    }

    private class GetData extends AsyncTask<String, String, String> {
        String msg = "Connecting to database";
        boolean isEmpty = false;

        @Override
        protected void onPreExecute() {
            boxStats.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmnt = null;
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "SELECT * from u2hdb.ItemBox where BoxNumber='" + selectedBox + "' AND GroupName='" + selectedGroup + "' AND School='" + ((UserInfo) getApplication()).getSchoolName() + "'";
                ResultSet rs = stmnt.executeQuery(sql);
                while (rs.next()) {
                    BoxStatsDataPoint itemData = new BoxStatsDataPoint(rs.getString("ItemName"), rs.getInt("ItemQuantity"), rs.getString("ExpirationDate"));
                    boxStats.add(itemData);

                }
                if (boxStats.size() == 0) {
                    isEmpty = true;
                }
                sql="Select * from u2hdb.BoxTable where BoxID="+boxID;
                rs=stmnt.executeQuery(sql);
                while(rs.next())
                {
                    isBoxOpen=rs.getInt("IsOpen");
                }
                conn.close();
                stmnt.close();
                rs.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {

            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {

                }
                try {
                    if (stmnt != null)
                        stmnt.close();
                } catch (SQLException e) {

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            Context context = BoxStats.this;
            BoxStatsListAdapter listAdapter = new BoxStatsListAdapter(context, boxStats);
            boxStatsListView.setAdapter(listAdapter);
            if (isEmpty) {
                emptyTextView.setText("No data in this box");
            }
            boxOpenOrCloseButton.setText(isBoxOpen == 1 ? "Close Box" : "Open Box");


        }
    }
}
