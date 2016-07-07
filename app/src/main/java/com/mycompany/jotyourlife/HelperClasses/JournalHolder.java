package com.mycompany.jotyourlife.HelperClasses;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mycompany.jotyourlife.DatabaseHelper;
import com.mycompany.jotyourlife.Fragments.AddFragment;
import com.mycompany.jotyourlife.R;
import com.mycompany.jotyourlife._;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JournalHolder {
    private TextView dateTime;
    private  TextView journalText;
    private ImageView icon;
    private TextView urlText ;
    private TextView headingText;

    JournalHolder(View row) {
        headingText = (TextView) row.findViewById(R.id.txt_row_heading);
        dateTime = (TextView)row.findViewById(R.id.txt_row_dtime);
        urlText = (TextView)row.findViewById(R.id.txt_row_url);
        journalText =(TextView) row.findViewById(R.id.txt_row_preview);
        icon = (ImageView) row.findViewById(R.id.img_row_icon);
    }

    private Date DBstringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = null;
        try {
            stringDate = simpledateformat.parse(aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return stringDate;

    }

    public void populateForm(Cursor c, DatabaseHelper helper ) {
        String str_date =helper.getMeDate(c);
        _.l("SQL_DATE: " + str_date);
        Date date = DBstringToDate(str_date, AddFragment.SQL_DATE_TIME_FORMAT);
        _.l("DATE_AFTER_FORMATTING TO DATE: " + date);
        SimpleDateFormat sdf = new SimpleDateFormat("E dd-MMM-yyyy");

        dateTime.setText(sdf.format(date));
        journalText.setText(helper.getMeJournalEntry(c));
        urlText.setText(helper.getMePhotoUrl(c));
        headingText.setText(helper.getMeHeading(c));

        int day = date.getDay()+1;

        switch (day) {
            case 2:
                icon.setImageResource(R.drawable.img_mon);
                break;
            case 3:
                icon.setImageResource(R.drawable.img_tue);
                break;
            case 4:
                icon.setImageResource(R.drawable.img_wed);
                break;
            case 5:
                icon.setImageResource(R.drawable.img_thu);
                break;
            case 6:
                icon.setImageResource(R.drawable.img_fri);
                break;
            case 7:
                icon.setImageResource(R.drawable.img_sat);
                break;
            case 1:
                icon.setImageResource(R.drawable.img_sun);
                break;
            default:
                icon.setImageResource(R.drawable.img_mon);
                Log.d("MYAPP" , "WENT TO DEFAULT ") ;
        }
    }
};
