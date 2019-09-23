package com.u2h.user.united2healandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class PasswordPage extends Fragment {

    TextView passwordTextView;
    Toolbar toolbar;
    Button passwordButton;
    String passVal;
    UserInfo application;
    DrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.password_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        application = ((UserInfo) getActivity().getApplication());

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        passwordTextView = (TextView) view.findViewById(R.id.passwordTextView);
        passwordButton = (Button) view.findViewById(R.id.passwordSubmitButton);
        passwordButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                passVal = passwordTextView.getText().toString();
                Data data = new Data();
                data.execute();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public class Data extends AsyncTask<String, String, String> {
        Connection conn;
        Statement stmnt;
        String password = null;
        boolean success;

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn = DriverManager.getConnection(DatabaseStrings.DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);
                stmnt = conn.createStatement();
                String sql = "SELECT * from u2hdb.PasswordTable WHERE School='" + application.getSchoolName() + "'";
                ResultSet rs = stmnt.executeQuery(sql);
                ArrayList<String> passwordList = new ArrayList<String>();
                while (rs.next()) {
                    passwordList.add(rs.getString("Password"));
                }
                rs.close();

                for (String s : passwordList)
                    if (s.equals(passVal)) {
                        success = true;
                        application.setPassword(s);

                    }

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
        public void onPostExecute(String s) {
            if (success) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment = new choose_group();

                    fragmentTransaction.replace(R.id.MainFrame, fragment);
                    fragmentTransaction.commit();
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pick Group");

                }
            } else {
                Toast.makeText(getActivity(), "Error, Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
