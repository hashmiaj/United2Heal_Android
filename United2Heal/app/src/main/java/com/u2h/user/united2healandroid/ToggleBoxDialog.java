package com.u2h.user.united2healandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ToggleBoxDialog extends DialogFragment {
    public interface ToggleBoxDialogListener{
        public void onSubmitted(int isOpen);
    }
    private String passVal;
    private int isBoxOpen;
    private int boxID;
    ToggleBoxDialogListener toggleBoxDialogListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= requireActivity().getLayoutInflater();
        View mainView= inflater.inflate(R.layout.toggle_box_dialog,null);
        final TextView passwordInput=(TextView) mainView.findViewById(R.id.passwordInput);
        isBoxOpen=getArguments().getInt("IsBoxOpen");
        boxID=getArguments().getInt("BoxID");
        Button submitButton= (Button) mainView.findViewById(R.id.submitToggleBoxButton);
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
            passVal=passwordInput.getText().toString();
            Data data= new Data();
            data.execute();
            }
        });
        builder.setView(mainView).setTitle("Confirm box change");
        return builder.create();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            toggleBoxDialogListener=(ToggleBoxDialogListener) context;
        }catch (ClassCastException e)
        {

        }
    }
    public class Data extends AsyncTask<String,String,String> {
        Connection conn;
        Statement stmnt;
        String password=null;
        boolean success;
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn= DriverManager.getConnection(DatabaseStrings.DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                stmnt=conn.createStatement();
                String sql="SELECT * from u2hdb.PasswordTable WHERE School='"+((UserInfo)getActivity().getApplication()).getSchoolName()+"'";
                ResultSet rs=stmnt.executeQuery(sql);
                while (rs.next())
                {
                    password=rs.getString("Password");
                }
                rs.close();
                if(password!=null)
                {
                    if(password.equals(passVal))
                    {
                        success=true;
                        Log.e("ID",boxID+"");
                        sql="UPDATE u2hdb.BoxTable SET IsOpen="+(isBoxOpen == 1 ? 0:1)+" WHERE BoxID="+boxID;
                        stmnt.executeUpdate(sql);
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                toggleBoxDialogListener.onSubmitted(isBoxOpen == 1 ? 0:1);
                            }
                        });
                    }
                }
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
        public void onPostExecute(String s)
        {
            if(success)
            {
                if(getActivity()!=null) {
                    Toast.makeText(getActivity(), "Success! Box "+(isBoxOpen==1? "closed":"opened"), Toast.LENGTH_SHORT).show();
                    dismiss();

                }
            }
            else{
                Toast.makeText(getActivity(),"Error, Try again",Toast.LENGTH_SHORT).show();
            }
        }
    }
}


