package net.ddns.weissenterprises.mview;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddEntryDialogFragment.AddEntryDialogFragmentListener
{

    private static final String DB_LOG = "2017 9 22 565.0€2017 9 23 9.0€2017 9 23 17.0€2017 9 23 6.0€2017 9 24 -20.0€2017 9 24 7.25€2017 9 24 15.0€2017 9 24 3.91€2017 9 24 -7.25€2017 9 25 7.5€2017 9 26 11.25€2017 9 26 3.99€2017 9 28 6.21€2017 9 28 10.0€2017 9 30 7.0€2017 9 30 55.0€2017 10 2 7.0€2017 10 2 4.0€2017 10 3 9.0€2017 10 6 2.0€2017 10 6 6.0€2017 10 6 4.0€2017 10 6 7.5€2017 10 6 10.0€2017 10 6 7.5€2017 10 7 15.0€2017 10 7 4.0€2017 10 7 19.0€2017 10 10 16.0€2017 10 10 6.0€2017 10 10 10.0€2017 10 10 40.0€2017 10 11 8.31€2017 10 11 2.25€2017 10 12 6.3€2017 10 13 5.39€2017 10 13 12.0€2017 10 14 55.0€2017 10 14 20.0€2017 10 14 13.4€2017 10 14 15.5€2017 10 15 7.78€2017 10 15 3.0€2017 10 15 2.0€2017 10 16 7.5€2017 10 16 6.5€2017 10 16 2.66€2017 10 17 3.0€2017 10 17 2.5€2017 10 17 7.5€2017 10 17 2.0€2017 10 17 5.0€2017 10 18 7.5€2017 10 18 7.8€2017 10 18 140.0€2017 10 20 13.0€2017 10 20 4.71€2017 10 20 8.0€2017 10 20 3.0€2017 10 20 3.0€2017 10 20 5.28€2017 10 22 2.5€2017 10 22 9.0€2017 10 22 6.5€2017 10 23 5.76€2017 10 23 20.0€2017 10 24 16.0€2017 10 25 7.0";

    static public float calculateMPD(float moneySpent, float moneyPerMonth, String dateS){

        int daysLeft = getDaysLeftInMonth(dateS);

        float mpd = 1;

        mpd = (moneyPerMonth - moneySpent) / daysLeft;

        return mpd;
    }

    static private int getDaysLeftInMonth(String dateS) {

        int d = 1;

        int daysInMonth = getDaysInMonth(dateS);

        int cd = Integer.parseInt(dateS.substring(3,5));

        d = daysInMonth - cd + 1;

        return d;
    }

    static private int getDaysInMonth(String dateS) {

        int d = 1;

        int m = Integer.parseInt(dateS.substring(0,2));
        int y = Integer.parseInt(dateS.substring(6,10));

        switch (m){
            case 1:
                d = 31;
                break;
            case 2:
                if ((y - 2004) % 4 == 0){
                    d = 29;
                } else{
                    d = 28;
                }
                break;
            case 3:
                d = 31;
                break;
            case 4:
                d = 30;
                break;
            case 5:
                d = 31;
                break;
            case 6:
                d = 30;
                break;
            case 7:
                d = 31;
                break;
            case 8:
                d = 31;
                break;
            case 9:
                d = 30;
                break;
            case 10:
                d = 31;
                break;
            case 11:
                d = 30;
                break;
            case 12:
                d = 31;
                break;
            default:
                d = 31;
                break;
        }
        return d;
    }

    private void updateSharedPreferences(String dateS, Float val, Double money_per_month, SharedPreferences sharedPreferences){
        String lastEntry = sharedPreferences.getString(getString(R.string.saved_last_entry_key),getString(R.string.ZERO_DATE));
        boolean is_earlier = isEarlier(lastEntry, dateS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (is_earlier){
            editor.putString(getString(R.string.saved_last_entry_key), dateS);
        }

        float moneySpent = sharedPreferences.getFloat(getString(R.string.saved_money_spent_key),0);
        //float moneyPerMonth = sharedPreferences.getFloat(getString(R.string.saved_money_per_month_key),0);
        float moneySpentToday = sharedPreferences.getFloat(getString(R.string.saved_money_spent_today_key),0);


        if (money_per_month != null){
            editor.putFloat(getString(R.string.saved_money_per_month_key),money_per_month.floatValue());
        } else if(val != null){
            editor.putFloat(getString(R.string.saved_money_spent_key),moneySpent + val.floatValue());
            float newMoneySpentToday = moneySpentToday + val.floatValue();
            float mpd = sharedPreferences.getFloat(getString(R.string.saved_mpd_today_key), 20);

            editor.putFloat(getString(R.string.saved_money_spent_today_key), newMoneySpentToday);



            TextView MTView = (TextView)findViewById(R.id.MTView);

            if (mpd <= newMoneySpentToday){
                MTView.setTextColor(Color.RED);
            }

            MTView.setText(Float.toString(newMoneySpentToday));
        }

        editor.commit();
        //float mpd = calculateMPD(moneySpent, moneyPerMonth, dateS);
    }

    private boolean isEarlier(String lastEntry, String dateS) {

        boolean x = false;

        int date_s = 10000 * Integer.parseInt(dateS.substring(6,10))
                + 100 * Integer.parseInt(dateS.substring(0,2))
                + Integer.parseInt(dateS.substring(3,5));

        int last_entry = 10000 * Integer.parseInt(lastEntry.substring(6,10))
                + 100 * Integer.parseInt(lastEntry.substring(0,2))
                + Integer.parseInt(lastEntry.substring(3,5));

        if (last_entry < date_s){
            x = true;
        }
        /*
        boolean a = Integer.parseInt(dateS.substring(6,10))>= Integer.parseInt(lastEntry.substring(6,10));
        if (a){
            boolean b = Integer.parseInt(dateS.substring(0,2)) >= Integer.parseInt(lastEntry.substring(0,2));
            if(b){
                boolean c = Integer.parseInt(dateS.substring(3,5)) > Integer.parseInt(lastEntry.substring(3,5));
                if (c) {
                    x = true;
                }
            }
        }
        */
        return x;
    }

    @Override
    public boolean onDialogClick(String val, int factor) {

        boolean ret = false;


        if(!val.equals("")){
            ListView listView = (ListView) findViewById(R.id.ScrollList);
            MainActivity.CashFlowAdapter adapter = (MainActivity.CashFlowAdapter) listView.getAdapter();

            Date date = new Date();

            float valF = Float.parseFloat(val)*factor;
            valF = Math.round(valF*100f)/100f;

            int category = 0;

            CFData cfData = new CFData(valF, date.getTime(), category);

            adapter.insert( cfData, 0);

            Context context = getBaseContext();

            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    getString(R.string.preference_file_key),Context.MODE_PRIVATE
            );

            updateSharedPreferences(cfData.getDateS(), valF, null, sharedPreferences);

            new SaveCashFlowEntry().execute(new DCCFA(context, null, adapter));
            ret = true;
        }
        return ret;
    }


  /*  @Override
    public void onPositive(){

        ListView listView = (ListView) findViewById(R.id.ScrollList);
        MainActivity.CashFlowAdapter adapter = (MainActivity.CashFlowAdapter) listView.getAdapter();

        double val = Double.parseDouble((String)((TextView)findViewById(R.id.dialogEntryVal)).getText());

        adapter.insert(new CFData(val, (new SimpleDateFormat("MM/DD/YYYY")).format(new Date())), 0);
        adapter.notifyDataSetChanged();
        new SaveCashFlowEntry().execute(new DCCFA(this, new Date(), adapter));
    }

    @Override
    public void onNegative(){

    }

*/
    public static class SaveCashFlowEntry extends AsyncTask<DCCFA,Void, Void>{
        protected Void doInBackground(DCCFA... dccfa){
            Context context = dccfa[0].context;

            CashFlowAdapter cfa = dccfa[0].cfa;

            FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            CFData cfData = (CFData) cfa.getItem(0);
            int month = Integer.parseInt(cfData.getDateS().substring(0,2));
            int day = Integer.parseInt(cfData.getDateS().substring(3,5));
            int year = Integer.parseInt(cfData.getDateS().substring(6,10));
            long timestamp = cfData.getCurrentTimeMillis();

            float val = cfData.getVal();

            int category = cfData.getCategory();

            ContentValues values = new ContentValues();

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH,Integer.toString(month));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAY, Integer.toString(day));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR, Integer.toString(year));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VAL, Float.toString(val));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, Long.toString(timestamp));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, Integer.toString(category));

            long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);


            return null;
        }
    }

    private class LoadCashFlowEntries extends AsyncTask<DCCFA, Void, Cursor>{

        protected Cursor doInBackground(DCCFA... dccfa){
            Context context = dccfa[0].context;

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateS = dateFormat.format(dccfa[0].date);

            FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DAY,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VAL,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY
            };

            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH + " = ?"
                    + " AND " + FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR + " = ?";
            String[] selectionArgs = {dateS.substring(0,2), dateS.substring(6,10)};

            String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + " DESC";

            Cursor cursor = db.query(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

            return cursor;
        }

        protected void onPostExecute(Cursor cursor){
            ListView listView = (ListView) findViewById(R.id.ScrollList);
            ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();

            //int cursorCount = cursor.getCount();
            //int lastHigh = 0;

            //CFData[] cfData = new CFData[cursorCount];

            //int c = 0;

            while(cursor.moveToNext()){

                long dateMillis = cursor.getLong(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP)
                );

                float val = cursor.getFloat(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VAL)
                );
                val = Math.round(val*100f)/100f;

                int category = cursor.getInt(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY)
                );

                adapter.add(new CFData(val, dateMillis, category));

//                if(c > 0){
//                    if(isEarlier(cfData[c].getDateS(), cfData[c-1].)){
//                        for(int i = c - 1; i >= lastHigh; i --){
//                            adapter.add(cfData[i]);
//                        }
//                        lastHigh = c + 1;
//                    }
//                }

                //adapter.add(cfData[c]);

                //c++;

            }

            adapter.notifyDataSetChanged();

            cursor.close();

        }

        protected void onProgressUpdate(Integer progress){
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialog = new AddEntryDialogFragment();
                dialog.show(getSupportFragmentManager(),"NoticeDialogFragment");

                //AddEntryDialogFragment addEntryDialogFragment = new AddEntryDialogFragment();
                //addEntryDialogFragment.show(getSupportFragmentManager(), "entry_alert");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //updating shared preferences, getting mpd, resetting

        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key),Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();

        String lastEntry = sharedPreferences.getString(getString(R.string.saved_last_entry_key), "00/00/0000");
        String dateS = (new SimpleDateFormat("MM/dd/YYYY")).format(new Date());

        boolean is_earlier =  isEarlier(lastEntry, dateS);

        float mpd = 0;

        if (is_earlier){
            float moneySpent = sharedPreferences.getFloat(getString(R.string.saved_money_spent_key),0);
            float moneyPerMonth = sharedPreferences.getFloat(getString(R.string.saved_money_per_month_key),600);

            if((Integer.parseInt(lastEntry.substring(6,10)) * 100 + Integer.parseInt(lastEntry.substring(0,2))) < (Integer.parseInt(dateS.substring(6,10)) * 100 + Integer.parseInt(dateS.substring(0,2)))){
                moneySpent = 0;
                editor.putFloat(getString(R.string.saved_money_spent_key), 0);
            }

            mpd = calculateMPD(moneySpent, moneyPerMonth, dateS);

            mpd = Math.round(mpd*100.0F)/100.0F;

            editor.putFloat(getString(R.string.saved_mpd_today_key), mpd);
            editor.putString(getString(R.string.saved_last_entry_key),dateS);
            editor.putFloat(getString(R.string.saved_money_spent_today_key), 0);

            editor.commit();

        }else{
            mpd = sharedPreferences.getFloat(getString(R.string.saved_mpd_today_key), 20);

        }



        TextView textViewMpd = (TextView) findViewById(R.id.MPDView);
        textViewMpd.setText(" / " + Float.toString(mpd));

        float mst = sharedPreferences.getFloat(getString(R.string.saved_money_spent_today_key),0);
        mst = Math.round(mst*100F)/100F;

        TextView dateView = (TextView) findViewById(R.id.dateView);
        dateView.setText(getDateViewString());

        TextView textViewMT = (TextView) findViewById(R.id.MTView);
        textViewMT.setText(Float.toString(mst));
        if (mst >= mpd){
            textViewMT.setTextColor(Color.RED);
        }

        TextView textViewStatus = (TextView) findViewById(R.id.textViewStatus);

        float moneySpent = sharedPreferences.getFloat(getString(R.string.saved_money_spent_key),0);
        moneySpent = Math.round(moneySpent*100F)/100F;
        float moneyPerMonth = sharedPreferences.getFloat(getString(R.string.saved_money_per_month_key),600);
        String textViewStatusString = moneySpent + " / " + moneyPerMonth;

        textViewStatus.setText(textViewStatusString);



        ArrayList<CFData> arrayOfCFData = new ArrayList<CFData>();
        CashFlowAdapter adapter = new CashFlowAdapter(this, arrayOfCFData);
        ListView listView = (ListView) findViewById(R.id.ScrollList);
        listView.setAdapter(adapter);

        Date date = new Date();
        new LoadCashFlowEntries().execute(new DCCFA(this, date,null));


        //
        // Add old entries || only run once!!!
        //
/*
        String[] entries = DB_LOG.split("€");

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for(String e : entries){
            String[] entry = e.split(" ");

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.DATE, Integer.parseInt(entry[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(entry[1]) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(entry[0]));

            long timeMillis = calendar.getTimeInMillis();

            float valF = Float.parseFloat(entry[3]);

            CFData cfData = new CFData(valF, timeMillis, 0);
            adapter.insert( cfData, 0);
            adapter.notifyDataSetChanged();

            ContentValues values = new ContentValues();


            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH,entry[1]);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAY, entry[2]);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR, entry[0]);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VAL, entry[3]);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, timeMillis);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY, 0);

            long newRowId = db.insertOrThrow(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

            Log.i("DB_LOG", Long.toString(newRowId));
        }

*/
        //

        //printDB(this);
    }


    private void printDB(Context context) {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_DAY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH,
                FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR,
                FeedReaderContract.FeedEntry.COLUMN_NAME_VAL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        String log_message = "";

        while(cursor.moveToNext()){
            int month = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH)
            );

            int day = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DAY)
            );

            int year = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR)
            );
            float val = cursor.getFloat(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_VAL)
            );
            long timestamp = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP)
            );
            int category = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY)
            );

            log_message = log_message + year + " " + month + " " + day + " " + val + " " + timestamp + " " + category + "|";
        }

        Log.i("DB_LOG", log_message);
    }


    private String getDateViewString( ) {

        String dateString;

        String y;
        String m;
        String d;

        Date date = new Date();

        y = (new SimpleDateFormat("YYYY")).format(date);
        m = (new SimpleDateFormat("M")).format(date);
        d = (new SimpleDateFormat("dd")).format(date);

        m = completeMonth(m);


        d = addDaySuffix(d);

        dateString = d + " of " + m + ", " + y;


        return dateString;


    }

    private String completeMonth(String m) {

        switch (m){
            case "1":
                m = getString(R.string.january);
                break;
            case "2":
                m = getString(R.string.february);
                break;
            case "3":
                m = getString(R.string.march);
                break;
            case "4":
                m = getString(R.string.april);
                break;
            case "5":
                m = getString(R.string.may);
                break;
            case "6":
                m = getString(R.string.june);
                break;
            case "7":
                m = getString(R.string.july);
                break;
            case "8":
                m = getString(R.string.august);
                break;
            case "9":
                m = getString(R.string.september);
                break;
            case "10":
                m = getString(R.string.october);
                break;
            case "11":
                m = getString(R.string.november);
                break;
            case "12":
                m = getString(R.string.december);
                break;
            default:
                m = "month";
                break;
        }

        return m;
    }

    private String addDaySuffix(String d) {

        boolean x = Integer.parseInt(d) < 10;

        if (x)
            d = d.substring(1, d.length());


        switch (d){
            case "1":
                d = "1st";
                break;
            case "2":
                d = "2nd";
                break;
            case "3":
                d = "3rd";
                break;
            case "21":
                d += "st";
                break;
            case "22":
                d += "nd";
                break;
            case "23":
                d += "rd";
                break;
            case "31":
                d += "st";
                break;
            default:
                d += "th";
        }

        return d;
    }

    public class CashFlowAdapter extends ArrayAdapter<CFData> {

        public CashFlowAdapter(Context context, List<CFData> objects){
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            CFData cfData = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
            }

            TextView date = (TextView) convertView.findViewById(R.id.date_val);
            TextView m_val = (TextView) convertView.findViewById(R.id.m_val);

            date.setText(cfData.getDateS());
            if (cfData.getVal() > 0){
                m_val.setText("+" + Float.toString(cfData.getVal()) + " " + getString(R.string.currency));
            }else{
                m_val.setText(Float.toString(cfData.getVal()) + " " + getString(R.string.currency));
            }

            return convertView;
        }


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_stats){
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
