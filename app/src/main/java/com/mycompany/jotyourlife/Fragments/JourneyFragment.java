package com.mycompany.jotyourlife.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.mycompany.jotyourlife.DatabaseHelper;
import com.mycompany.jotyourlife.HelperClasses.FragmentCommunicator;
import com.mycompany.jotyourlife.HelperClasses.JournalAdapter;
import com.mycompany.jotyourlife.HelperClasses.JournalHolder;
import com.mycompany.jotyourlife.R;
import com.mycompany.jotyourlife._;

/**
 * Created by Shristi on 1/25/2016.
 */
public class JourneyFragment extends Fragment {

    CursorAdapter cursorAdapter;
    DatabaseHelper helper;
    Cursor model = null;

    JournalHolder journalHolder=null;
    JournalAdapter journalAdapter=null;

    FragmentCommunicator comm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list=(ListView)getActivity().findViewById(R.id.list_items);
        helper = new DatabaseHelper(getActivity());

        Log.d("MYAPP" , "CHECK FOR ID: " + readUIDFromPreferenceFile(getActivity()));

        model = helper.getAllEntries(""+readUIDFromPreferenceFile(getActivity()));
        model.moveToFirst();
        _.l("COUNT: " + model.getCount() + "");


        getActivity().startManagingCursor(model);
        journalAdapter = new JournalAdapter(model, getActivity(), helper);
        list.setAdapter(journalAdapter);
        list.setOnItemClickListener(myList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.journey_fragment ,container , false );

    }

    public int readUIDFromPreferenceFile(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.PREFERENCE_KEY_FILE) ,Context.MODE_PRIVATE);
        int defaultid = -1;
        int uid= sharedPref.getInt(getString(R.string.current_user_id), defaultid);
//        Toast.makeText(getActivity(), "UID" + uid, Toast.LENGTH_SHORT).show();
        return uid;
    }

    AdapterView.OnItemClickListener myList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            model.moveToPosition(position);
            Log.d("MYAPP1" , "ahoi");
            String itemId = helper.getMeItemId(model);
            comm.respond(itemId);

        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        comm = (FragmentCommunicator)activity;
    }
}
