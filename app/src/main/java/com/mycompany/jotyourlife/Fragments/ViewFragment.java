package com.mycompany.jotyourlife.Fragments;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.jotyourlife.DatabaseHelper;
import com.mycompany.jotyourlife.R;
import com.mycompany.jotyourlife._;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shristi on 2/14/2016.
 */
public class ViewFragment extends Fragment {

    DatabaseHelper helper=null;
    Cursor model;

    TextView txtDayOfMonth;
    TextView txtDate;
    TextView txtDayTime;
    TextView txtHeading;
    TextView txtContent;

    ImageView imgSavedPic;


    String itemId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_page_design , container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        helper = new DatabaseHelper(getActivity());


        //----------
        txtDayOfMonth=(TextView) getActivity().findViewById(R.id.txtDayOfMonth);;
        txtDate=(TextView) getActivity().findViewById(R.id.txtDate);;
        txtDayTime=(TextView) getActivity().findViewById(R.id.txtDayTime);;
        txtHeading=(TextView) getActivity().findViewById(R.id.txtHeading);;
        txtContent=(TextView) getActivity().findViewById(R.id.txtContent);;

        imgSavedPic=(ImageView) getActivity().findViewById(R.id.imgSavedPic);
        //-----------


        Bundle bundle = this.getArguments();
        itemId = bundle.getString("itemId");
        Log.d("MYAPP1", "item id set from fragment " + itemId);
        changeData(itemId);
    }

//    public void setItemId(String id) {
//        itemId = id;
//        Log.d("MYAPP1", "item id set");
//    }

    public void changeData (String itemId) {
        if (helper == null) {
            helper = new DatabaseHelper(getActivity());
        }

        if(txtDayOfMonth == null|| txtDate==null ||txtDayTime==null ) {
            txtDayOfMonth=(TextView) getActivity().findViewById(R.id.txtDayOfMonth);;
            txtDate=(TextView) getActivity().findViewById(R.id.txtDate);;
            txtDayTime=(TextView) getActivity().findViewById(R.id.txtDayTime);;
            txtHeading=(TextView) getActivity().findViewById(R.id.txtHeading);;
            txtContent=(TextView) getActivity().findViewById(R.id.txtContent);;

            imgSavedPic=(ImageView) getActivity().findViewById(R.id.imgSavedPic);
        }

        model = helper.getParticularEntry(itemId);
        model.moveToFirst();
        String journal = helper.getMeJournalEntry(model);
        String heading = helper.getMeHeading(model);
        String uri = helper.getMePhotoUrl(model);
        String db_date = helper.getMeDate(model);
        String strDayTime = helper.getMeFormatterTime(model);
        String strDate = helper.getMeFormatterDate(model);
        int strDayOfMonth = helper.getMeDayOfMonth(model);


        Uri myUri = Uri.parse(uri);
        _.l("DAY OF MONTH: " + strDayOfMonth);
        txtDayOfMonth.setText(strDayOfMonth + "");
        txtHeading.setText(heading);
        txtContent.setText(journal);
        txtDayTime.setText(strDayTime);
        txtDate.setText(strDate);

        //txt_time.setText (db_date);

        if(uri == null || uri == "") {
            Log.d("URL" , "URL IS EMPTY");

            imgSavedPic.setImageResource(R.drawable.noimage);

        } else {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , myUri);
                Bitmap bmp = Bitmap.createScaledBitmap(bitmap ,1000,500 ,false);
                imgSavedPic.setImageBitmap(bmp);

                Log.d("IMAGE" , "IMAGE OBTAINED");

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IMAGE", "IMAGE not accessed from media store ");

            }

        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory

                .decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
