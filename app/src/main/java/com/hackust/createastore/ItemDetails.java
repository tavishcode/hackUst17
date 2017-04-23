package com.hackust.createastore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Tavish on 23-Apr-17.
 */

public class ItemDetails extends AppCompatActivity {
    Button next;
    EditText quantity;
    EditText price;
    EditText name;
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        next= (Button)findViewById(R.id.nexBtn);
        quantity= (EditText)findViewById(R.id.quantity);
        price= (EditText)findViewById(R.id.price);
        name= (EditText)findViewById(R.id.name);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPref = ItemDetails.this.getSharedPreferences(
                        "userDetails", Context.MODE_PRIVATE);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user",sharedPref.getString("name","sampleName"))
                        .addFormDataPart("itemName",name.getText().toString())
                        .addFormDataPart("quantity",quantity.getText().toString())
                        .addFormDataPart("cost",price.getText().toString())
                        .addFormDataPart("fileToUpload","item.jpeg", RequestBody.create(MEDIA_TYPE_JPEG,
                                new File(sharedPref.getString("photo",null))))
                        .build();
                ApiClient.addPhoto(requestBody).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res= response.body().string();
                        Log.i("ItemDetails",res);
                        Intent i= new Intent(ItemDetails.this,Items.class);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        ItemObj itemObj= new ItemObj(name.getText().toString(),
                                Integer.parseInt(quantity.getText().toString()),
                                Double.parseDouble(price.getText().toString()));

                        Gson gson = new Gson();
                        String json = sharedPref.getString("itemList", null);
                        Type type = new TypeToken<ArrayList<ItemObj>>() {}.getType();
                        ArrayList<ItemObj> arrayList = gson.fromJson(json, type);
                        if(arrayList==null)
                        {
                            arrayList= new ArrayList<ItemObj>();
                        }
                        arrayList.add(itemObj);
                        Gson gson1 = new Gson();
                        String json1 = gson1.toJson(arrayList);
                        editor.putString("itemList", json1);
                        editor.commit();

                        startActivity(i);
                    }
                });
            }
        });
    }
}
