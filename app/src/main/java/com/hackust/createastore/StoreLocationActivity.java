package com.hackust.createastore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.gms.location.places.ui.PlacePicker.getPlace;

/**
 * Created by Tavish on 22-Apr-17.
 */

public class StoreLocationActivity extends AppCompatActivity {
    int PLACE_PICKER_REQUEST = 1;
    Button map;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_location_layout);
        map=(Button)findViewById(R.id.mapBtn);
        next=(Button)findViewById(R.id.nextBtn);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try
                {
                    startActivityForResult(builder.build(StoreLocationActivity.this), PLACE_PICKER_REQUEST);
                }
                catch (GooglePlayServicesRepairableException| GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = getPlace(this,data);
                String toastMsg = String.format("Place: %s", place.getName());
                final SharedPreferences sharedPref = StoreLocationActivity.this.getSharedPreferences(
                        "userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("address", (String) place.getAddress());
                editor.commit();
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("pass",sharedPref.getString("password","samplePass"))
                            .addFormDataPart("address",sharedPref.getString("address","sampleAddr"))
                            .addFormDataPart("storeName",sharedPref.getString("storeName","sampleName"))
                            .addFormDataPart("user",sharedPref.getString("name","sampleUsername"))
                            .build();
                    ApiClient.addUser(requestBody).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call,final Response response){
                            try {
                                String res= response.body().string();
                                Log.i("Store Location Activity",res);
                                if(res.equals("SUCCESS"))
                                {
                                    next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i= new Intent(view.getContext(),Items.class);
                                            startActivity(i);
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
