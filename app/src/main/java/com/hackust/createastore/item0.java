package com.hackust.createastore;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;


public class item0 extends ArrayAdapter<ItemObj> {
    public item0(Context context,ArrayList<ItemObj> users){
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemObj user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prod_list, parent, false);
        }
        // Lookup view for data population
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        ImageView iv_image = (ImageView) convertView.findViewById(R.id.iv_image);

        // Populate the data into the template view using the data object
        tv_name.setText(user.getName());
        tv_quantity.setText(user.getQuantity());
        tv_price.setText(String.valueOf(user.getPrice()));
        iv_image.setImageResource(R.mipmap.ic_launcher);
        // Return the completed view to render on screen
        return convertView;
    }
}
// Construct the data source
/*   ArrayList<ItemObj> arrayOfUsers = new ArrayList<ItemObj>();
    // Create the adapter to convert the array to views
    item0 adapter = new item0(this, arrayOfUsers);
    // Attach the adapter to a ListView
    ListView activity_item0 = (ListView) findViewById(R.id.lvItems);
    activity_item0.setAdapter(adapter);
*/