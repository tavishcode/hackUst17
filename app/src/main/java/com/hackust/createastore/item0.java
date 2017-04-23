package com.hackust.createastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class item0 extends ArrayAdapter<ItemObj> {
    Context context;
    String url;
    public item0(Context context,ArrayList<ItemObj> users){
        super(context, 0, users);
        this.context=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ItemObj user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prod_list, parent, false);
        }
        // Lookup view for data population
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        final ImageView iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
        final SharedPreferences sharedPref = context.getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user",sharedPref.getString("name","sampleName"))
                .build();
        ApiClient.getItem(requestBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("item0","failed to post");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.i("item0",res);
                try {
                    JSONArray jsonArray= new JSONArray(res);
                    url="http://10.89.3.161:8082"+(jsonArray.getJSONObject(position).getString("imageLocation")).replace("\\","");
                    Log.i("item0",url);
                    Log.i("item0","LOADED IMAGE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Populate the data into the template view using the data object
        if(user!=null)
        {
            tv_name.setText(user.getName());
            tv_quantity.setText(String.valueOf(user.getQuantity()));
            tv_price.setText(String.valueOf(user.getPrice()));
            while (url==null)
            {}
            Picasso.with(context).load(url).into(iv_image);
        }
        return convertView;
    }

}
/*/ Construct the data source

*/