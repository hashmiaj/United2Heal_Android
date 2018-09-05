package com.u2h.user.united2healandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminPanelFragment extends Fragment {
    TextView changePasswordTextView;
    Button changePasswordButton;
    String passValue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_admin_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changePasswordTextView=view.findViewById(R.id.passwordTextView);
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
                    Data data=new Data();
                    data.execute();
                }
            }
        });
    }
    public class Data extends AsyncTask<String,String,String> {
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
}
