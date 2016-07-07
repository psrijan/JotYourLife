package com.mycompany.jotyourlife.HelperClasses;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.TabActivity;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mycompany.jotyourlife.DatabaseHelper;
import com.mycompany.jotyourlife.R;

/**
 * Created by Shristi on 1/28/2016.
 */
public class JournalAdapter extends CursorAdapter {
    DatabaseHelper helper;

    public JournalAdapter(Cursor cursor , Context context , DatabaseHelper helper) {
        super(context, cursor , 0);
        this.helper = helper;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(context); // significance of context here
        View row = layoutInflater.inflate(R.layout.main_list_row, parent ,false);

        JournalHolder holder = new JournalHolder(row);
        row.setTag(holder);
        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        JournalHolder holder = (JournalHolder)view.getTag();
        holder.populateForm(cursor,helper );
    }
}
