package com.u2h.user.united2healandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ItemPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        TextView itemNameTextView= (TextView) findViewById(R.id.itemNameTextView);
        if(getIntent().getExtras()!=null)
        {
            itemNameTextView.setText(getIntent().getExtras().getString("com.u2h.user.united2healandroid.ITEM_NAME"));
        }
    }
}
