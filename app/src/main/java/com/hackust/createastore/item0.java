package com.hackust.createastore;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class item0 extends AppCompatActivity {

    FloatingActionButton mfab;
    ListView listView;
    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item0);
        mfab = (FloatingActionButton) findViewById(R.id.add_fab);
        listView = (ListView) findViewById(R.id.listview_product_item0);



        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //add logic to add item to list
            }
        });
    }
}