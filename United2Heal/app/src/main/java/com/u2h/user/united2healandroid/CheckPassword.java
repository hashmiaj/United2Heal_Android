package com.u2h.user.united2healandroid;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class CheckPassword extends IntentService {
    Handler mHandler;
    Boolean passFailed=false;
     Context context;
    public CheckPassword(){
super("CheckPassword");
}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
  context=this;
        super.onCreate();
        mHandler=new Handler(Looper.getMainLooper());
        Timer timer= new Timer();
        TimerTask checkPassword= new TimerTask() {
            @Override
            public void run() {
                GetData data=new GetData();
                data.execute();
            }
        };
        timer.schedule(checkPassword,1000*60*60,1000*60*60);
}

    public class GetData extends AsyncTask<String,String,String>{
        Connection conn;
        Statement stmnt;
        String password;
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName(DatabaseStrings.JDBC_DRIVER);
                conn= DriverManager.getConnection(DatabaseStrings.DB_URL,DatabaseStrings.USERNAME,DatabaseStrings.PASSWORD);
                String sql="Select * from u2hdb.PasswordTable";
                stmnt=conn.createStatement();
                ResultSet rs= stmnt.executeQuery(sql);
                rs.next();
                password=rs.getString("Password");
                rs.close();
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
                    {
                        stmnt.close();
                    }
                }
                catch (SQLException e)
                {

                }
                try{
                    if(conn!=null)
                    {
                        conn.close();
                    }
                }
                catch (SQLException e)
                {

                }
            }
            return null;
        }
        @Override
        public void onPostExecute(String msg)
        {
            if(!password.equals(((UserInfo)getApplication()).getPassword()))
            {

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }
}
