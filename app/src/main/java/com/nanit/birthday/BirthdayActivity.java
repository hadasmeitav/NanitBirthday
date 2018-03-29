package com.nanit.birthday;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanit.birthday.utils.Preferences;
import com.nanit.birthday.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by hadas on 3/29/18.
 */

public class BirthdayActivity extends AppCompatActivity {

    private static final int MONTHS_IN_YEAR = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        setBackground();

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString(Preferences.Key.Name.name());
        ((TextView) findViewById(R.id.title)).setText(String.format(getString(R.string.title), name));

        setAge(Utils.deserializeDate(bundle.getString(Preferences.Key.Date.name())));

        Bitmap bitmap = bundle.getParcelable(MainActivity.BITMAP_SOURCE);
        if (bitmap != null) {
            ((ImageView) findViewById(R.id.photo)).setImageBitmap(bitmap);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_clear_black_36dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBackground() {
        int rollUi = new Random().nextInt(3);
        int resource = 0;
        switch (rollUi) {
            case (0):
                resource = R.mipmap.android_elephant_popup;
                break;
            case (1):
                resource = R.mipmap.android_fox_popup;
                break;
            case (2):
                resource = R.mipmap.android_pelican_popup;
                break;
        }

        findViewById(R.id.main_birthday_layout).setBackground(getResources().getDrawable(resource));
    }

    private int monthsBetweenDates(Date startDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(new Date(System.currentTimeMillis()));

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

    private void setAge(Date date) {
        int elapsed = monthsBetweenDates(date);
        ((TextView) findViewById(R.id.sub_title)).setText(getString(elapsed >= MONTHS_IN_YEAR ?
                R.string.sub_title_years : R.string.sub_title_months));
        elapsed /= elapsed >= MONTHS_IN_YEAR ? MONTHS_IN_YEAR : 1;

        int resource = 0;
        switch (elapsed) {
            case 1:
                resource = R.mipmap.resource_1;
                break;
            case 2:
                resource = R.mipmap.resource_2;
                break;
            case 3:
                resource = R.mipmap.resource_3;
                break;
            case 4:
                resource = R.mipmap.resource_4;
                break;
            case 5:
                resource = R.mipmap.resource_5;
                break;
            case 6:
                resource = R.mipmap.resource_6;
                break;
            case 7:
                resource = R.mipmap.resource_7;
                break;
            case 8:
                resource = R.mipmap.resource_8;
                break;
            case 9:
                resource = R.mipmap.resource_9;
                break;
            case 10:
                resource = R.mipmap.resource_10;
                break;
            case 11:
                resource = R.mipmap.resource_11;
                break;
            case 12:
                resource = R.mipmap.resource_12;
                break;
        }

        ((ImageView) findViewById(R.id.age)).setImageResource(resource);
    }
}
