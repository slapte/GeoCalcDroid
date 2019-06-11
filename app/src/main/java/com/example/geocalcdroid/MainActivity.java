/*
Sanil Apte, Sean Driscoll
 */

package com.example.geocalcdroid;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.location.Location;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.content.Context;
import android.widget.Toolbar;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static final int unitsSelection = 1;
    public static int HISTORY_RESULT = 2;
    public static String distUnits = "Kilometers";
    public static String bearUnits = "Degrees";
    private float distanceBetween;
    private float bearingTo;
    private TextView dist;
    private TextView bearing;
    private EditText latP1;
    private EditText latP2;
    private EditText longP1;
    private EditText longP2;
    public final static int NEW_LOC = 146;
    DatabaseReference topRef;
    public static List<LocationLookup> allHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Places.initialize(getApplicationContext(), "AIzaSyBg7vY9zQVfbZMmN2F04-AJkv85gxW7Udo");

        latP1 = (EditText) findViewById(R.id.lat1);
        latP2 = (EditText) findViewById(R.id.lat2);
        longP1 = (EditText) findViewById(R.id.long1);
        longP2 = (EditText) findViewById(R.id.long2);
        Button searchBtn = (Button) findViewById(R.id.searchButton);
        allHistory = new ArrayList<LocationLookup>();



        Button clr = (Button) findViewById(R.id.clear);
        Button calc = (Button) findViewById(R.id.calc);

        dist = (TextView) findViewById(R.id.distText);
        bearing = (TextView) findViewById(R.id.bearingText);
        Toolbar toolbar = findViewById(R.id.toolbar);



        clr.setOnClickListener(v -> {
            latP1.setText("");
            latP2.setText("");
            longP1.setText("");
            longP2.setText("");
            dist.setText("Distance: ");
            bearing.setText("Bearing: ");

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        calc.setOnClickListener(v -> {
            doCalc();


            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        searchBtn.setOnClickListener(v -> {
            Intent newLoc = new Intent(MainActivity.this,newLocActivity.class);
            startActivityForResult(newLoc, NEW_LOC);
        });

    }

    private void doCalc() {
        if(latP1.getText().toString().equals("") || latP2.getText().toString().equals("") || longP1.getText().toString().equals("") || longP2.getText().toString().equals("")){

        }else {
            Location p1 = new Location("");
            p1.setLatitude(Double.parseDouble(latP1.getText().toString()));
            p1.setLongitude(Double.parseDouble(longP1.getText().toString()));
            Location p2 = new Location("");
            p2.setLatitude(Double.parseDouble(latP2.getText().toString()));
            p2.setLongitude(Double.parseDouble(longP2.getText().toString()));


            float distance = p1.distanceTo(p2);
            distance = distance / 1000;
            distance = Math.round(distance * (float) 100.0) / (float) 100.0;
            float bear = p1.bearingTo(p2);
            bear = Math.round(bear * (float) 100.0) / (float) 100.0;

            distanceBetween = distance;
            bearingTo = bear;


            calcUnits();

            LocationLookup entry = new LocationLookup();
            entry.setOrigLat(Double.parseDouble(latP1.getText().toString()));
            entry.setOrigLng(Double.parseDouble(longP1.getText().toString()));
            entry.setEndLat(Double.parseDouble(latP2.getText().toString()));
            entry.setEndLng(Double.parseDouble(longP2.getText().toString()));
            entry.setTimeStamp(null);
            topRef.push().setValue(entry);

        }
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " +
                d.getDayOfMonth() + ", " + d.getYear();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == unitsSelection){
            distUnits = data.getStringExtra("dist");
            bearUnits = data.getStringExtra("bear");
            calcUnits();
        }else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.latP1.setText(vals[0]);
            this.longP1.setText(vals[1]);
            this.latP2.setText(vals[2]);
            this.longP2.setText(vals[3]);
            doCalc();  // code that updates the calcs.
        }else if(resultCode == NEW_LOC) {
            if(data != null && data.hasExtra("LOC")){
                Parcelable par = data.getParcelableExtra("LOC");
                LocationLookup l = Parcels.unwrap(par);

                this.latP1.setText(Double.toString(l.getOrigLat()));
                this.latP2.setText(Double.toString(l.getEndLat()));
                this.longP1.setText(Double.toString(l.getOrigLng()));
                this.longP2.setText(Double.toString(l.getEndLng()));
                doCalc();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.settingsItem){
            Intent intent = new Intent(MainActivity.this, settingsActvity.class);
            startActivityForResult(intent, unitsSelection);
            return true;
        }else if(item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT );
            return true;
        }

        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
        //topRef.addValueEventListener(valEvListener);

    }

    @Override
    public void onPause(){
        super.onPause();
        topRef.removeEventListener(chEvListener);
    }

    protected void calcUnits(){
        if(!distUnits.equals("Kilometers")){
            float miles = Math.round(distanceBetween *  (float) 62.1371) / (float) 100.0;
            dist.setText("Distance: " + miles + " mi");
        }
        else{
            dist.setText("Distance: " + distanceBetween + " km");
        }

        if(!bearUnits.equals("Degrees")){
            float mils = Math.round(bearingTo * (float) 1777.777778) / (float) 100.0;
            bearing.setText("Bearing: " + mils + " mils");
        }
        else{
            bearing.setText("Bearing: " + bearingTo + " degrees");
        }
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup) dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup) dataSnapshot.getValue(LocationLookup.class);
            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();
            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };




}
