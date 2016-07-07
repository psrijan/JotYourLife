package com.mycompany.jotyourlife.AddOns;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;

import com.mycompany.jotyourlife._;

import java.util.Calendar;

/**
 * Created by Shristi on 3/24/2016.
 */
public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    int hh=-1;
    int mm=-1;

    public static SelectTimeFragment newInstance(int minute, int hour ) {
        Bundle args = new Bundle();
        args.putInt("mm" , minute);
        args.putInt("hh", hour);

        SelectTimeFragment selectTimeFragment = new SelectTimeFragment();
        selectTimeFragment.setArguments(args);
        return  selectTimeFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();

        mm = getArguments().getInt("mm");
        hh = getArguments().getInt("hh");

        _.l("STF: M: " + mm + " H: " + hh);

        if(hh==-1 && mm==-1) {
            hh = cal.get(Calendar.HOUR);
            mm = cal.get(Calendar.MINUTE);
        }
        _.l("STF: M: " + mm + " H: " + hh);

        return new TimePickerDialog(getActivity() , this ,hh , mm , true);

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        MyDialogCallBackInterface mHost = (MyDialogCallBackInterface) getTargetFragment();
        mHost.timeDataback(hourOfDay, minute);
    }
}
