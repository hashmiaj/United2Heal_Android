package com.u2h.user.united2healandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ExportExcel extends Fragment {
    TextView progressTextView;
    TextView enterEmailTextView;
    ArrayList<Item> itemList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemList=new ArrayList<>();
        return inflater.inflate(R.layout.export_excel,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.e("itemList Length",itemList.size()+"");
        super.onViewCreated(view, savedInstanceState);
        enterEmailTextView= (TextView) view.findViewById(R.id.enterEmailTextView);
        progressTextView=(TextView) view.findViewById(R.id.progressTextView);
        Button getDataButton= (Button) view.findViewById(R.id.exportExcelButton);
        getDataButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData retrieveData= new GetData();
                retrieveData.execute();
            }
        });
    }
    public void exportCSV()
    {
        try
        {
            File file=new File(getContext().getExternalCacheDir().toString()+"/database.csv");
            CSVWriter writer= new CSVWriter(new FileWriter(file));
            writer.writeNext(new String[]{"ItemID","ItemCategory","ItemName"});
            for(Item i:itemList)
            {
                writer.writeNext(new String[]{Integer.toString(i.getItemID()),i.getItemCategory(),i.getItemName()});

            }
            writer.close();
            Uri path = FileProvider.getUriForFile(getContext(),getContext().getApplicationContext().getPackageName()+".com.u2h.user.united2healandroid.provider",file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("vnd.android.cursor.dir/email");
            String to[]= {enterEmailTextView.getText().toString()};
            emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "U2H Database CSV");
            startActivity(Intent.createChooser(emailIntent , "Send email..."));

        }
        catch (IOException e)
        {
        Log.e("IOERROR",e.getMessage());
        }

    }
    private class GetData extends AsyncTask<String,String,String>
    {
        Boolean success;
        String msg="";
        static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
        static final String DB_URL="jdbc:mysql://"+DatabaseStrings.DATABASE_URL+"/"+DatabaseStrings.DATABASE_NAME;
        @Override
        protected void onPreExecute()
        {
            itemList.clear();
            progressTextView.setText("Connecting to database");
        }
        @Override
        protected String doInBackground(String... strings) {
            Connection conn=null;
            Statement stmnt=null;

            try{
                Class.forName(JDBC_DRIVER);
                conn= DriverManager.getConnection(DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                stmnt=conn.createStatement();
                String sql="SELECT * FROM u2hdb.ItemTable";
                ResultSet rs=stmnt.executeQuery(sql);
                while(rs.next())
                {
                    int itemId=rs.getInt("ItemID");
                    String itemName=rs.getString("ItemName");
                    String itemCategory=rs.getString("CategoryName");

                    itemList.add(new Item(itemCategory,itemId,itemName));

                }

                if(itemList.size()>0)
                {   msg="";
                    exportCSV();
                }
                success=true;

                rs.close();
                stmnt.close();
                conn.close();
            }
            catch(SQLException connError) {
msg=connError.getMessage();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                msg="Class not found";
            }
            finally {
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
            if(progressTextView!=null)
            progressTextView.setText(this.msg);
        }
    }
}
