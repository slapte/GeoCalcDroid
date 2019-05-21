package com.example.geocalcdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.location.Location;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.content.Context;


public class MainActivity extends AppCompatActivity {

    public static final int unitsSelection = 1;
    public static String distUnits = "Kilometers";
    public static String bearUnits = "Degrees";
    private float distanceBetween;
    private float bearingTo;
    private TextView dist;
    private TextView bearing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText latP1 = (EditText) findViewById(R.id.lat1);
        EditText latP2 = (EditText) findViewById(R.id.lat2);
        EditText longP1 = (EditText) findViewById(R.id.long1);
        EditText longP2 = (EditText) findViewById(R.id.long2);

        Button clr = (Button) findViewById(R.id.clear);
        Button calc = (Button) findViewById(R.id.calc);
        Button settings = (Button) findViewById(R.id.btnSettings);

        dist = (TextView) findViewById(R.id.distText);
        bearing = (TextView) findViewById(R.id.bearingText);



        clr.setOnClickListener(v -> {
            latP1.setText("");
            latP2.setText("");
            longP1.setText("");
            longP2.setText("");

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        calc.setOnClickListener(v -> {
            Location p1 = new Location("");
            p1.setLatitude(Double.parseDouble(latP1.getText().toString()));
            p1.setLongitude(Double.parseDouble(longP1.getText().toString()));
            Location p2 = new Location("");
            p2.setLatitude(Double.parseDouble(latP2.getText().toString()));
            p2.setLongitude(Double.parseDouble(longP2.getText().toString()));


            float distance = p1.distanceTo(p2);
            distance = distance / 1000;
            distance = Math.round(distance  * (float) 100.0) / (float) 100.0;
            float bear = p1.bearingTo(p2);
            bear = Math.round(bear  * (float) 100.0) / (float) 100.0;

            distanceBetween = distance;
            bearingTo = bear;


            calcUnits();


            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settingsActvity.class);
                startActivityForResult(intent, unitsSelection);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == unitsSelection){
            distUnits = data.getStringExtra("dist");
            bearUnits = data.getStringExtra("bear");
            calcUnits();

        }
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
