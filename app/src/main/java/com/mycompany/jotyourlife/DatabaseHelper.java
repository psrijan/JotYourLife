    package com.mycompany.jotyourlife;


    import java.text.ParsePosition;
    import java.util.Calendar;
    import java.util.Date;
    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.provider.BaseColumns;
    import android.util.Log;
    import android.widget.Toast;

    import com.mycompany.jotyourlife.Fragments.AddFragment;

    import java.sql.SQLException;

    /**
     * Created by Shristi on 1/22/2016.
     */
    public class DatabaseHelper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "volleyball.db";
        private static final int SCHEMA_VERSION = 8;
        private static final String LOGIN_TABLE = "loginTable";
        private static final String ITEM_TABLE = "itemTable";

        //columns in LOGINTABLE
        private static final String KEY_UID = "_uid";
        private static final String KEY_NAME = "name";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_PASS = "password";

        //columns in item table

        class MyItemTable {
            private static final String TABLE_ITEM = "itemTable";
            private static final String HEADING = "heading";
            private static final String KEY_ITEM_ID = "_id";
            private static final String KEY_USER_ID = "user_id";
            private static final String KEY_TEXT = "text";
            private static final String KEY_URL = "photo_URL";
            private static final String KEY_DATE = "date_time";
            private static final String KEY_LAT = "lati";
            private static final String KEY_LONG = "longi";


        }

        private static final String TABLE_ITEM = "itemTable";
        private static final String KEY_ITEM_ID = "_id";
        private static final String KEY_USER_ID = "user_id";
        private static final String KEY_TEXT = "text";
        private static final String KEY_URL = "photo_URL";
        private static final String KEY_DATE = "date_time";
        private static final String KEY_LAT = "lati";
        private static final String KEY_LONG = "longi";


        private SQLiteDatabase db;


        private static final String CREATE_LOGIN_TABLE = "CREATE TABLE loginTable ( " + KEY_UID + "  INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "name TEXT , " +
                "email TEXT NOT NULL , " +
                "password TEXT NOT NULL);";

        private static final String CREATE_USER_TABLE = "Create TABLE itemTable ( "
                + MyItemTable.KEY_ITEM_ID
                +  " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + " user_id INT  , "
                + MyItemTable.HEADING
                + " LONGTEXT , "
                + MyItemTable.KEY_TEXT
                + " LONGTEXT, "
                + " photo_URL VARCHAR(100), "
                + " date_time DATETIME, "
                + " lati VARCHAR(20), "
                + " longi VARCHAR(20) "
                + " ); ";


        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("MYAPP" , "Upgrade from version " + oldVersion + " to " + newVersion );

            db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);

            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_LOGIN_TABLE);
            db.execSQL(CREATE_USER_TABLE);
        }

        public void clearDB () {
            Log.d("MYAPP", "CLEARDB ");
            getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
            getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
            Log.d("MYAPP", "Database Successfull Cleared");
            getWritableDatabase().execSQL(CREATE_LOGIN_TABLE);
            getWritableDatabase().execSQL(CREATE_USER_TABLE);
            Log.d("MYAPP", "Database Created ");
        }

        public long AddEntry(int user_id , String heading , String jourText , String photoURL, String lati , String longi , String date ) {
//            SQLiteDatabase db = getWritableDatabase();
            //Log.d("MYAPP", "Database created ");

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_USER_ID, user_id);
            contentValues.put(MyItemTable.HEADING , heading);
            contentValues.put(KEY_TEXT, jourText);
            contentValues.put(KEY_URL, photoURL);
            contentValues.put(KEY_DATE,date );
            contentValues.put(KEY_LAT , lati);
            contentValues.put(KEY_LONG , longi);
            Log.d("MYAPP" , "ENTRY SAVED") ;

            return getWritableDatabase().insert(TABLE_ITEM, null, contentValues);
        }


        public long AddUser(String name, String email, String password) {
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, name);
            cv.put(KEY_EMAIL, email);
            cv.put(KEY_PASS, password);

            Log.d("MYAPP", "USER SAVED");
            return getWritableDatabase().insert(LOGIN_TABLE, null, cv);
        }

        public boolean Login (String email ,String password) {
            Cursor mCursor = getReadableDatabase().rawQuery("SELECT * FROM " +
                    LOGIN_TABLE +
                    " WHERE email =? AND password=?"
                    , new String[]{email, password});

            if(mCursor!=null) {
                if(mCursor.getCount()>0) {
                    Log.d("MYAPP" , "cursor not null");
                    return  true;
                }

            }
            Log.d("MYAPP", "cursor null") ;
            return false;
        }

        public Cursor getParticularEntry(String itemId) {
            //gets one particular entry of a user
            //when you click the list then this is called to fetch the particular item values in a new fragment
            return (getReadableDatabase().rawQuery("SELECT * FROM " +
                    ITEM_TABLE +
                            " WHERE " +
                            KEY_ITEM_ID +
                            " =? "
                    , new String[] {itemId}

            ));
        }

        public Cursor getAllEntries(String uid) {
            //Gets all Entries of a particular user
            return (getReadableDatabase().rawQuery("SELECT * " +
                    " FROM " +
                            ITEM_TABLE +
                            " WHERE " +
                            KEY_USER_ID +
                            " =? " ,
                    new String[]{uid}));
        }

        public Cursor getAll(String email) {
            return(getReadableDatabase()
                    .rawQuery("SELECT * " +
                            " FROM " +
                            LOGIN_TABLE +
                            " WHERE " +
                            KEY_EMAIL+
                            " =? "
                            ,new String[]{email}));
        }

        public String getEmail(Cursor c){
            return  c.getString(c.getColumnIndex(KEY_EMAIL));
        }
        public String getPassword (Cursor c) {
            return  c.getString(c.getColumnIndex(KEY_PASS));
        }
        public String getName (Cursor c) {
            return  c.getString(c.getColumnIndex(KEY_NAME));
        }
        public String getUID(Cursor c) { return  c.getString(c.getColumnIndex(KEY_UID)); }



        //methods to access second database

        public String getMeDate(Cursor c) {
            return c.getString(c.getColumnIndex(KEY_DATE));
        }
        public String getMeItemId (Cursor c) { return c.getString(c.getColumnIndex(KEY_ITEM_ID));}
        public String getMeJournalEntry(Cursor c) {
            return c.getString(c.getColumnIndex(KEY_TEXT));
        }
        public String getMeHeading(Cursor c ) {
            return c.getString(c.getColumnIndex(MyItemTable.HEADING));
        }

        public String getMePhotoUrl(Cursor c){
            return c.getString(c.getColumnIndex(KEY_URL));
        }

        public String getMeLatitude (Cursor c) { return c.getString(c.getColumnIndex(KEY_LAT));}
        public String getMeLongitude (Cursor c) { return c.getString (c.getColumnIndex(KEY_LONG));}


        public Date getDate(Cursor c) {
            String sql_date_string = getMeDate(c);
            SimpleDateFormat sdf = new SimpleDateFormat(AddFragment.SQL_DATE_TIME_FORMAT);
            ParsePosition parsePosition = new ParsePosition(0);
            Date date = sdf.parse(sql_date_string, parsePosition);
            return date;
        }

        public int getMeDayOfMonth(Cursor c) {
            Date myDate = getDate(c);
            SimpleDateFormat myFormat = new SimpleDateFormat("d");
            return Integer.parseInt(myFormat.format(myDate));
        }

        public String getMeFormatterDate(Cursor c){
            Date myDate = getDate(c);
            SimpleDateFormat dateFormat = new SimpleDateFormat( "MMM d,yyyy" );
            return dateFormat.format(myDate);
        }
//
        public String getMeFormatterTime (Cursor c ) {
            Date myDate = getDate(c);
            SimpleDateFormat timeFormat = new SimpleDateFormat ("E h:m a");
            return timeFormat.format(myDate);
        }

        public void doesNothing() {}



    }
