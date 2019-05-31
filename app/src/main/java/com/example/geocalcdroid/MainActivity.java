/*
Sanil Apte, Sean Driscoll
 */

package com.example.geocalcdroid;

import android.content.Intent;
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

import com.example.geocalcdroid.dummy.HistoryContent;

import org.joda.time.DateTime;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latP1 = (EditText) findViewById(R.id.lat1);
        latP2 = (EditText) findViewById(R.id.lat2);
        longP1 = (EditText) findViewById(R.id.long1);
        longP2 = (EditText) findViewById(R.id.long2);



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

            //Remembers the history
            HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(latP1.getText().toString(),
                    longP1.getText().toString(), latP2.getText().toString(), longP2.getText().toString(), DateTime.now());
            HistoryContent.addItem(item);
        }
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

}
