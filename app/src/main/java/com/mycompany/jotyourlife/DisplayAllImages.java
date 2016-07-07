package com.mycompany.jotyourlife;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class DisplayAllImages extends AppCompatActivity {

    DatabaseHelper helper=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_images);

        Intent intent = getIntent();
        String uid = intent.getExtras().getString("UID");


        helper= new DatabaseHelper(this);
        Cursor model = helper.getAllEntries(uid);
        startManagingCursor(model);
        model.moveToFirst();
        String uri = helper.getMePhotoUrl(model);

        Uri myUri = Uri.parse(uri);

        ImageView myImage = (ImageView) findViewById(R.id.myImage);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() ,myUri);
            if(bitmap==null){
                Log.d("bmp","null");
            }else{
                Log.d("bmp","not null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Bitmap.cre
        Bitmap bmp=Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);
        myImage.setImageBitmap(bmp);
        Toast.makeText(this, "URI:" + uri , Toast.LENGTH_LONG).show();
    }
}
