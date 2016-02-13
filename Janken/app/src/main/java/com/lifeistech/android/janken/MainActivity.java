package com.lifeistech.android.janken;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView cpu;
    ImageView player;
    TextView result;
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cpu = (ImageView)findViewById(R.id.cpu);
        player = (ImageView)findViewById(R.id.player);
        result = (TextView)findViewById(R.id.result);
    }
    public void goo(View v){
        player.setImageResource(R.drawable.goo);
        int random = r.nextInt(3);
        if(random == 0){
            cpu.setImageResource(R.drawable.goo);
            result.setText("draw");
        }else if(random == 1){
            cpu.setImageResource(R.drawable.choki);
            result.setText("You win!");
        }else{
            cpu.setImageResource(R.drawable.paa);
            result.setText("You lose");
        }
    }
    public void choki(View v){
        player.setImageResource(R.drawable.choki);
        int random = r.nextInt(3);
        if(random == 0){
            cpu.setImageResource(R.drawable.goo);
            result.setText("You lose");
        }else if(random == 1){
            cpu.setImageResource(R.drawable.choki);
            result.setText("draw");
        }else{
            cpu.setImageResource(R.drawable.paa);
            result.setText("You win!");
        }
    }
    public void paa(View v){
        player.setImageResource(R.drawable.paa);
        int random = r.nextInt(3);
        if(random == 0){
            cpu.setImageResource(R.drawable.goo);
            result.setText("You win!");
        }else if(random == 1){
            cpu.setImageResource(R.drawable.choki);
            result.setText("You lose");
        }else{
            cpu.setImageResource(R.drawable.paa);
            result.setText("draw");
        }
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
