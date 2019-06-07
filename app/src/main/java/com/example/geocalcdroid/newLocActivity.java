package com.example.geocalcdroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class newLocActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    @BindView(R.id.date) TextView datePick;
    @BindView(R.id.endlocation) TextView endLocation;
    @BindView(R.id.startlocation) TextView startLocation;
    private static final String TAG = "NewLocation";

    int PLACE_START_CODE = 1;
    int PLACE_END_CODE = 2;
    private DateTime date;
    private DatePickerDialog dpDialog;
    private LocationLookup loc;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = findViewById(R.id.fab);

        loc = new LocationLookup();
    }
    @OnClick({R.id.startlocation})
    public void LocPressed(){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, PLACE_START_CODE);
    }

    @OnClick({R.id.endlocation})
    public void LocEndPressed(){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, PLACE_END_CODE);
    }



    @OnClick({R.id.dateInfo})
    public void dateClicked(){
        dpDialog.show();
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " +
                d.getDayOfMonth() + ", " + d.getYear();
    }

    @OnClick(R.id.fab)
    public void FABPressed() {
        Intent result = new Intent();
        Parcelable parcel = Parcels.wrap(loc);
        result.putExtra("LOC",parcel);
        setResult(MainActivity.NEW_LOC, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_START_CODE) {
            if (resultCode == RESULT_OK) {
                Place pl = Autocomplete.getPlaceFromIntent(data);
                startLocation.setText(pl.getName());
                loc.origLat = pl.getLatLng().latitude;
                loc.origLng = pl.getLatLng().longitude;

                Log.i(TAG, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status stat = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, "onActivityResult: ");
            } else if (requestCode == RESULT_CANCELED) {
                System.out.println("Cancelled by the user");
            }
        } else if (requestCode == PLACE_END_CODE) {
            if (resultCode == RESULT_OK) {
                Place pl = Autocomplete.getPlaceFromIntent(data);
                endLocation.setText(pl.getName());
                loc.endLat = pl.getLatLng().latitude;
                loc.endLng = pl.getLatLng().longitude;

                Log.i(TAG, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status stat = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, "onActivityResult: ");
            } else if (requestCode == RESULT_CANCELED) {
                System.out.println("Cancelled by the user");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new DateTime(year, month, dayOfMonth, 0, 0);
        datePick.setText(formatted(date));
    }
}
