package com.hackust.createastore;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.security.AccessController.getContext;

/**
 * Created by Ishaan Batra on 4/22/2017.
 */

public class Items extends AppCompatActivity {
    FloatingActionButton mfab;
    ListView listView;
    ListAdapter mAdapter;
    private final int REQUEST_TAKE_PHOTO=2;
    private final int REQUEST_WRITE=3;
    private Uri photoUri;
    private String mCurrentPhotoPath;
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item0);
        final SharedPreferences sharedPref = getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        mfab = (FloatingActionButton) findViewById(R.id.add_fab);
        listView = (ListView) findViewById(R.id.listview_product_item0);

        Gson gson = new Gson();
        String json = sharedPref.getString("itemList", "[]");
        Type type = new TypeToken<ArrayList<ItemObj>>() {}.getType();
        ArrayList<ItemObj> arrayList = gson.fromJson(json, type);
        if(arrayList!=null)
        {
            Log.i("items","arraylist not null");
            item0 adapter = new item0(this, arrayList);
            listView.setAdapter(adapter);
        }
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Items.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Items.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE);
                }
                else
                {
                    takePhotoAndSend();
                }
            }
        });
    }
    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == this.RESULT_OK)
            {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(photoUri);
                this.sendBroadcast(mediaScanIntent);
                Bitmap bitmap = decodeBitmap(photoUri);
                Log.i("Items",mCurrentPhotoPath);
                final SharedPreferences sharedPref = getSharedPreferences(
                        "userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("photo",mCurrentPhotoPath);
                editor.apply();
                Log.i("Items","preparing to go to details");
                Intent i= new Intent(this,ItemDetails.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Failed to capture photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public Bitmap decodeBitmap(Uri uri)
    {
        int targetW = 640;
        int targetH = 640;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        try
        {
            BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri), null, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri), null, bmOptions);
            return bitmap;
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(Items.this,"File not found",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(Items.this,"Permission Granted", Toast.LENGTH_SHORT).show();
                    takePhotoAndSend();
                }
                else
                {
                    Toast.makeText(Items.this,"Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void takePhotoAndSend()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null)
        {
            File image = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                mCurrentPhotoPath = image.getAbsolutePath();
            } catch (IOException e)
            {
                Log.e("Items", "File Error");
            }
            if (image != null)
            {
                photoUri = FileProvider.getUriForFile(this,"com.hackust.createastore.fileprovider",image);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        final SharedPreferences sharedPref = getSharedPreferences(
                "userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = sharedPref.getString("itemList", "[]");
        Type type = new TypeToken<ArrayList<ItemObj>>() {}.getType();
        ArrayList<ItemObj> arrayList = gson.fromJson(json, type);
        if(arrayList!=null)
        {
            Log.i("Items","arraylist is not null");
            item0 adapter = new item0(this, arrayList);
            listView.setAdapter(adapter);
        }
        super.onResume();
    }
}
