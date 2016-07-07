package com.mycompany.jotyourlife.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentManager;
//import android.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mycompany.jotyourlife.AddOns.MyDialogCallBackInterface;
import com.mycompany.jotyourlife.AddOns.SelectTimeFragment;
import com.mycompany.jotyourlife.DatabaseHelper;
import com.mycompany.jotyourlife.HelperClasses.JournalAdapter;
import com.mycompany.jotyourlife.R;
import com.mycompany.jotyourlife._;
import com.mycompany.jotyourlife.AddOns.SelectDateFragment;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;



public class AddFragment extends Fragment implements MyDialogCallBackInterface  {


    EditText txtJourney=null;
    EditText txtHeading=null;

    ImageButton btnAddImage=null;
    ImageButton btnSave=null;
    ImageButton btnGPS=null;
    ImageButton btnSetDate=null;
    ImageButton btnSetTime=null;

    ImageView testImage = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_OK = -1;
    static final int REQUEST_GALLERY =0;
    public static final int ADD_FRAGMENT_REQ_CODE =1;

    String img_url = "0";
    DatabaseHelper helper;

    String mCurrentPhotoPath = "";

    Context context = null;

    int date_x=-1;
    int month_x=-1;
    int year_x=-1;

    int minute_x = -1;
    int hour_x =-1;
    Calendar curCal=null;
    static final int DIALOG_ID = 0;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAddImage = (ImageButton) getActivity().findViewById(R.id.btnImage);
        btnSave = (ImageButton) getActivity().findViewById(R.id.btnOK);
        btnGPS = (ImageButton) getActivity().findViewById(R.id.btnLocation);
        btnSetDate=(ImageButton) getActivity().findViewById(R.id.btnDate);
        btnSetTime=(ImageButton) getActivity().findViewById(R.id.btnTime);
        testImage = (ImageView) getActivity().findViewById(R.id.testImage);



        txtJourney = (EditText) getActivity().findViewById(R.id.etNote);
        txtHeading = (EditText) getActivity().findViewById(R.id.etHeading);



        btnAddImage.setOnClickListener(myListener);
        btnSetDate.setOnClickListener(myListener);
        btnSetTime.setOnClickListener(myListener);

        btnSave.setOnClickListener(myListener);
        helper = new DatabaseHelper(getActivity());
        context = getActivity();

        Calendar cal = Calendar.getInstance();
        date_x = cal.get(Calendar.DATE);
        month_x = cal.get(Calendar.MONTH);
        year_x = cal.get(Calendar.YEAR);

        _.l("MEOW: " + date_x + " " + month_x + " " + year_x );

        minute_x = cal.get(Calendar.MINUTE);
        hour_x=cal.get(Calendar.HOUR_OF_DAY);

    }



    View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (v.getId()) {

                case R.id.btnLocation:
                    Toast.makeText(getActivity() , "Coming Soon" , Toast.LENGTH_SHORT).show();

                case R.id.btnImage:
                    retrievePicture();
                    Toast.makeText(getActivity(), "MEOW", Toast.LENGTH_LONG).show();
                    break;

                case R.id.btnDate:
                    _.l("AF: D:" + date_x + " M: " + month_x + " Y: " + year_x);
                    DialogFragment setDateFrag = SelectDateFragment.newInstance( date_x ,month_x , year_x);
                    setDateFrag.setTargetFragment(AddFragment.this,ADD_FRAGMENT_REQ_CODE );
                    setDateFrag.show(fragmentTransaction , "DATE");
                    break;

                case R.id.btnTime:

                    _.l("AF: M: " + hour_x + " H: " + minute_x);
                    DialogFragment setTimeFrag = SelectTimeFragment.newInstance(minute_x, hour_x);
                    setTimeFrag.setTargetFragment(AddFragment.this , ADD_FRAGMENT_REQ_CODE);
                    setTimeFrag.show(fragmentTransaction , "TIME");
                    break;

                case R.id.btnOK:
                    saveData();
                    exchangeFragment();
                    break;
            }
        }
    };


    private void saveData() {
        String str_lat, str_long;
        String txt_heading = txtHeading.getText().toString();
        String txt_journal = txtJourney.getText().toString();
        int user_id = readUIDFromPreferenceFile(getActivity());
        str_lat ="300"; str_long = ""+300;
        String db_date = databaseFriendlyDate(hour_x , minute_x ,date_x , month_x , year_x);
        _.l("DB DATE: " + db_date);
        helper.AddEntry(user_id, txt_heading, txt_journal, mCurrentPhotoPath, str_lat, str_long, db_date);
        Toast.makeText(getActivity(), "User Journal Saved", Toast.LENGTH_LONG).show();
        exchangeFragment();
    }

    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String SQL_DATE_TIME_FORMAT= "yyyy/MM/dd hh:mm:ss";


    String databaseFriendlyDate(int hour ,int minute , int day , int month , int year) {
        _.l("###########YEAR: "+ year);
        Date date = new Date(year-1900,month,day ,hour ,minute, 0);
        _.l("###########Date: "+ date.toString());
        SimpleDateFormat sdf = new SimpleDateFormat(SQL_DATE_TIME_FORMAT);

        String str_date = sdf.format(date);
        _.l("###########FoRMATTED DATE: " + str_date.toString());
        return str_date;

    }

    public int readUIDFromPreferenceFile(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.PREFERENCE_KEY_FILE), Context.MODE_PRIVATE);
        int defaultid = -1;
        int uid = sharedPref.getInt(getString(R.string.current_user_id), defaultid);
        _.l("#####" + uid);
        Toast.makeText(getActivity(), "AddFrag UID Pref: " + uid, Toast.LENGTH_SHORT).show();
        return uid;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.activity_add_fragment, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mi = menu.findItem(0).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void retrievePicture() {
        final CharSequence[] items= {"Take Photo" , "Choose From Gallery" , "Cancel"};

        final AlertDialog.Builder imageDialog = new AlertDialog.Builder(getActivity());
        imageDialog.setTitle("Add Photo!");
        imageDialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("MYAPP" , "" +which);

                if (items[which].equals("Take Photo")) {
                    _.l("Take Photo");
                    Intent  takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }

                    }

                } else if (items[which].equals("Choose From Gallery")) {
                    _.l("Gallery");
                    Intent pickIntent = new Intent(Intent.ACTION_PICK ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*"); // basically  means retrieve images of all extensions
                    startActivityForResult(pickIntent , REQUEST_GALLERY);

                } else {
                    _.l("Cancel");
                }
            }
        });
        imageDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    _.l("Image captured" + mCurrentPhotoPath);
                    break;
                case REQUEST_GALLERY:
                    _.l("Image picked");
                    if(data!=null && data.getData()!=null){
                        _.l(data.getData()+"");
                    }
                    Uri uri = data.getData();
                    mCurrentPhotoPath = data.getDataString();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , uri);
                        _.l("URL CAPTURED IN BITMAP");

                    } catch(IOException e) {
                        _.l("IOEXCEPTION SEEN WHILE PICKING IMAGE USING URL");
                        e.printStackTrace();
                    }

                    break;
                default:
                    _.l("Some Other Request");
            }
        }
    }


    @Override
    public void dateDataback(int date, int month, int year) {
        date_x = date;
        month_x = month;
        year_x = year;
    }

    @Override
    public void timeDataback(int hour, int minute) {
        hour_x = hour;
        minute_x = minute;
    }
    private void exchangeFragment() {
        JourneyFragment journeyFragment = new JourneyFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, journeyFragment, "JourneyFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}