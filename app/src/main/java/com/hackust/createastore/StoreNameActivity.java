package com.hackust.createastore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Tavish on 22-Apr-17.
 */

public class StoreNameActivity extends AppCompatActivity {
    private EditText storeName;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_store_layout);
        storeName= (EditText)findViewById(R.id.editStoreName);
        next= (Button)findViewById(R.id.nextBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPref = StoreNameActivity.this.getSharedPreferences(
                        "userDetails", Context.MODE_PRIVATE);
                Intent i= new Intent(view.getContext(),StoreLocationActivity.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("storeName",storeName.getText().toString());
                editor.commit();
                startActivity(i);
            }
        });
    }
}
