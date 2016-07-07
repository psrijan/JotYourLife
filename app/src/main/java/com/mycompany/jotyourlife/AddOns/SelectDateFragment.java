package com.mycompany.jotyourlife.AddOns;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import com.mycompany.jotyourlife.Fragments.AddFragment;
import com.mycompany.jotyourlife._;

import java.util.Calendar;

/**
 * Created by Shristi on 3/24/2016.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int yy=-1, mm =-1, dd=-1;

    public static SelectDateFragment newInstance(int dday , int mmonth , int yyear) {
        SelectDateFragment selectDateFragment = new SelectDateFragment();
        Bundle args = new Bundle();
        args.putInt("dd" , dday);
        args.putInt("mm" , mmonth);
        args.putInt("yy", yyear);

        selectDateFragment.setArguments(args);

        return selectDateFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dd = getArguments().getInt("dd");
        mm = getArguments().getInt("mm");
        yy = getArguments().getInt("yy");
        _.l("SDF: Y: " + yy + " M: " + mm + " D: " + dd );

        final Calendar myCal = Calendar.getInstance();
        if(yy ==-1 && mm ==-1 && dd==-1) {
            yy = myCal.get(Calendar.YEAR);
            mm = myCal.get(Calendar.MONTH);
            dd = myCal.get(Calendar.DATE);
        }
        return new DatePickerDialog(getActivity() , this , yy  , mm , dd );
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        MyDialogCallBackInterface mHost = (MyDialogCallBackInterface) getTargetFragment();
        mHost.dateDataback(dayOfMonth ,monthOfYear , year);
    }

}
