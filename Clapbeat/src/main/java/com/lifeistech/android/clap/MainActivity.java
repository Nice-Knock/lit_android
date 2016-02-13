package com.lifeistech.android.clap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    ImageButton button;
    Clap clapInstance;
    Spinner spinner;
    int repeat = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (ImageButton)findViewById(R.id.imgBtn);
        spinner = (Spinner)findViewById((R.id.spinner));

        String[] strArray = new String[5];
        strArray[0] = "パンッ！";
        strArray[1] = "パンッパンッ！";
        strArray[2] = "パンッパンッパンッ！";
        strArray[3] = "4回";
        strArray[4] = "5回";

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemClickListener(this);
        clapInstance = new Clap(this.getApplicationContext());
    }
    public void onItemSelected(AdapterView<?> parent,View v,int pos,long id){
        repeat = pos + 1;
    }
    public void onNothingSelected(AdapterView<?> parent){}

    public void onClock(View v){
        //clapInstance.play();
        clapInstance.repeatPlay(repeat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
