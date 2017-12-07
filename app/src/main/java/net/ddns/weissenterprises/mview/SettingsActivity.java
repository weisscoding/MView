package net.ddns.weissenterprises.mview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();



        EditText mpm = (EditText) findViewById(R.id.editTextMPM);

        SharedPreferences sp = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        float MPM = sp.getFloat(getString(R.string.saved_money_per_month_key), 600);

        mpm.setText(Float.toString(MPM));

        mpm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                SharedPreferences sp = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();

                EditText editText = (EditText) findViewById(R.id.editTextMPM);

                String vText = editText.getText().toString();

                float new_mpm = Float.parseFloat(vText);

                editor.putFloat(getString(R.string.saved_money_per_month_key), new_mpm);

                float moneySpent = sp.getFloat(getString(R.string.saved_money_spent_key),0);

                String dateS = (new SimpleDateFormat("MM/dd/YYYY")).format(new Date());

                float mpd = MainActivity.calculateMPD(moneySpent, new_mpm, dateS);

                mpd = Math.round(mpd*100.0F)/100.0F;

                editor.putFloat(getString(R.string.saved_mpd_today_key), mpd);

                editor.apply();


                return false;
            }
        });

        EditText msm = (EditText) findViewById(R.id.editTextMSM);

        float MSM = sp.getFloat(getString(R.string.saved_money_spent_key), 0);

        msm.setText(Float.toString(MSM));

        msm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                SharedPreferences sp = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();

                EditText editText = (EditText) findViewById(R.id.editTextMSM);

                String vText = editText.getText().toString();

                float new_msm = Float.parseFloat(vText);

                editor.putFloat(getString(R.string.saved_money_spent_key), new_msm);

                float mpm = sp.getFloat(getString(R.string.saved_money_per_month_key),600);

                String dateS = (new SimpleDateFormat("MM/dd/YYYY")).format(new Date());

                float mpd = MainActivity.calculateMPD(new_msm, mpm, dateS);

                mpd = Math.round(mpd*100.0F)/100.0F;

                editor.putFloat(getString(R.string.saved_mpd_today_key), mpd);

                editor.apply();


                return false;
            }
        });

        Button button = (Button) findViewById(R.id.button_reset);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putFloat(getString(R.string.saved_money_per_month_key), 600);
                editor.putFloat(getString(R.string.saved_money_spent_key), 0);
                editor.putFloat(getString(R.string.saved_money_spent_today_key), 0);
                editor.putFloat(getString(R.string.saved_mpd_today_key), 20);
                editor.putString(getString(R.string.saved_last_entry_key), getString(R.string.ZERO_DATE));

                editor.commit();

                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getBaseContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                mDbHelper.onUpgrade(db, 0,0);

            }
        });

        EditText mst = (EditText) findViewById(R.id.editTextMST);

        float MST = sp.getFloat(getString(R.string.saved_money_spent_today_key), 0);

        mst.setText(Float.toString(MST));

        mst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                SharedPreferences sp = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();

                EditText editText = (EditText) findViewById(R.id.editTextMST);

                String vText = editText.getText().toString();

                float new_mst = Float.parseFloat(vText);

                editor.putFloat(getString(R.string.saved_money_spent_today_key), new_mst);

                editor.apply();

                return false;
            }
        });

    }
}
