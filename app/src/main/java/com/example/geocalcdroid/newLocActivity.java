package com.example.geocalcdroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class newLocActivity extends AppCompatActivity /*implements DatePickerDialog.OnDateSetListener*/ {


    @BindView(R.id.date) TextView datePick;
    @BindView(R.id.endlocation) TextView endLocation;
    @BindView(R.id.startlocation) TextView startLocation;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private DateTime date;
    private DatePickerDialog dpDialog;
    private LocationLookup loc;
    FloatingActionButton fab = findViewById(R.id.fab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @OnClick({R.id.startlocation, R.id.endlocation})
    public void LocPressed(){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    }

    @OnClick({R.id.dateInfo})
    public void dateClicked(){
        dpDialog.show();
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " +
                d.getDayOfMonth() + ", " + d.getYear();
    }
    /*
    @OnClick(R.id.fab)
    public void FABPressed() {
        Intent result = new Intent();
        loc.origLat = jname.getText().toString();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        currentTrip.startDate = fmt.print(startDate);
        currentTrip.endDate = fmt.print(endDate);
        // add more code to initialize the rest of the fields
        Parcelable parcel = Parcels.wrap(currentTrip);
        result.putExtra("TRIP", parcel);
        setResult(RESULT_OK, result);
        finish();
    }
    */
/*
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        date = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        datePick.setText(formatted(date));

    }
    */

}
