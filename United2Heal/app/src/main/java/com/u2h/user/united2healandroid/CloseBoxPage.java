package com.u2h.user.united2healandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CloseBoxPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_box_page);

        TextView categoryTextView= (TextView)findViewById(R.id.categoryTextView);
        if(getIntent().getExtras()!=null)
        {
            categoryTextView.setText(getIntent().getExtras().getString("com.u2h.user.united2healandroid.CATEGORY_NAME"));
        }
    }
}
