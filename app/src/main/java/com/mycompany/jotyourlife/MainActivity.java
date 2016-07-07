package com.mycompany.jotyourlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.mycompany.jotyourlife.Fragments.AddFragment;
import com.mycompany.jotyourlife.Fragments.DrawerFragment;
import com.mycompany.jotyourlife.Fragments.HelpFragment;
import com.mycompany.jotyourlife.Fragments.InspirationFragment;
import com.mycompany.jotyourlife.Fragments.JourneyFragment;
import com.mycompany.jotyourlife.Fragments.MapsActivity;
import com.mycompany.jotyourlife.Fragments.SettingFragment;
import com.mycompany.jotyourlife.Fragments.ViewFragment;
import com.mycompany.jotyourlife.HelperClasses.FragmentCommunicator;

//preference api

public class MainActivity extends AppCompatActivity implements FragmentCommunicator {
    DrawerLayout drawerLayout;
    NavigationView navView;
//    FloatingActionButton myFAB;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Toolbar topToolbar =null;
    Button btnDatePicker , btnTimePicker;
    Class fragmentClass = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        String uid = extras.getString(LoginActivity.USERID);

        Log.d("MYAPP", "USER ID SAVED: " + uid);
        saveInPreferenceFile(this, Integer.parseInt(uid));

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        topToolbar = (Toolbar) findViewById(R.id.inc_toolbar);
        navView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);


        setSupportActionBar(topToolbar);
        //topToolbar.setLogo(R.mipmap.ic_launcher1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topToolbar.setLogoDescription(getResources().getString(R.string.logo_desc));
        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout!=null && drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


//        myFAB =(FloatingActionButton) findViewById(R.id.btn_FAB);
//        myFAB.setOnClickListener(myListener);
        setupDrawerContent(navView);


    }


//    View.OnClickListener myListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.btn_FAB) {
//                addFragmentLoader();
//            }
//        }
//    };



    private void saveInPreferenceFile(Context context , int userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.PREFERENCE_KEY_FILE), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Log.d("MYAPP", "SAVEDID");
        editor.putInt(getString(R.string.current_user_id),userId);
        editor.commit();
        readUIDFromPreferenceFile();
    }

    private int readUIDFromPreferenceFile(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.PREFERENCE_KEY_FILE), Context.MODE_PRIVATE);
        int defaultid = -1;
//        getResources().getInteger(R.string.current_user_id_default);
        int uid= sharedPref.getInt(getString(R.string.current_user_id), defaultid);
        //Toast.makeText(this, "UID" + uid, Toast.LENGTH_SHORT).show();
        return uid;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

    }



    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
//        Class fragmentClass = null;

        switch(menuItem.getItemId()){
            case R.id.drawer_add:
                fragmentClass = AddFragment.class;
                Log.d("MYAPP" , "ADD");
                break;
            case R.id.drawer_journey:
                fragmentClass = JourneyFragment.class;
                Log.d("MYAPP", "JOURNEY");
                break;

            case R.id.drawer_atlas:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                fragmentClass = JourneyFragment.class;
                break;
            case R.id.drawer_pictures:
                Intent intent = new Intent (this , DisplayAllImages.class);
                intent.putExtra("UID" ,""+readUIDFromPreferenceFile() );
                startActivity(intent);
                fragmentClass = JourneyFragment.class;
                break;
//            case R.id.drawer_ feedback:
//                fragmentClass =DrawerFragment.class;
//                Log.d("MYAPP", "FEEDBACK");
//                break;
//            case R.id.drawer_inspiration:
//                fragmentClass =InspirationFragment.class;
//                Log.d("MYAPP", "INSP");
//                break;
//            case R.id.drawer_help:
//                fragmentClass = HelpFragment.class;
//                Log.d("MYAPP", "HLEP");
//                break;
//            case R.id.drawer_setting:
//                fragmentClass = SettingFragment.class;
//                Log.d("MYAPP", "SETTING");
//                break;
            default:
                fragmentClass = JourneyFragment.class;
//                Log.d("MYAPP", "DEFAULT");
                break;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();

        drawerLayout.closeDrawers();
    }


    @Override
    public void respond(String itemId) {
        ViewFragment viewFragment = (ViewFragment) fragmentManager.findFragmentById(R.id.view_frag);
//        viewFragment.changeData(itemId);
//        Log.d("MYAPP1" , "AHOOOOI");

        if (viewFragment != null) {
//            Log.d("MYAPP1" , "not null");

            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            viewFragment.changeData(itemId);
        } else {
//            Log.d("MYAPP1" , "null");

            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ViewFragment newFragment = new ViewFragment();
            Bundle args = new Bundle();
            args.putString("itemId", itemId);
            newFragment.setArguments(args);
            //newFragment.setItemId(itemId);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.flContent, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

//        fragmentTransaction.replace(R.id.flContent, viewFragment);
//        fragmentTransaction.commit();
    }

    public void addFragmentLoader(){
        AddFragment fragment = new AddFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }
}