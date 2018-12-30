package com.joeribv.joerisgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends AppCompatActivity {
    private SeekBar xBar;
    private SeekBar yBar;
    private String name;
    private int temp = 0;

    // to do, add setting button and add xsens/ysens to prefs!
    // create highscore activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xBar = (SeekBar) findViewById(R.id.seekBar);
        yBar = (SeekBar) findViewById(R.id.seekBar2);
        xBar.setProgress((int)(xBar.getMax()/2));
        yBar.setProgress((int)(yBar.getMax()/2));
        Constants.X_SENS = xBar.getProgress();
        Constants.Y_SENS = yBar.getProgress();
        xBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar,int progresValue,boolean fromUser){
                Constants.X_SENS = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        yBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar,int progresValue,boolean fromUser){
                Constants.Y_SENS = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        /* name settings */
        if (prefs.getString("name", "").isEmpty()) {
            changeName();
        }
        if (prefs.getString("level","").isEmpty()){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("level",Integer.toString(temp));
            editor.apply();
        }
    }
    public void hardGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        String difficulty = "hard";
        intent.putExtra("difficulty",difficulty);
        Constants.CUR_DIFF = difficulty;
        startActivity(intent);
    }
    public void extremeGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        String difficulty = "extreme";
        intent.putExtra("difficulty",difficulty);
        Constants.CUR_DIFF = difficulty;
        startActivity(intent);
    }
    public void godGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        String difficulty = "god";
        intent.putExtra("difficulty",difficulty);
        Constants.CUR_DIFF = difficulty;
        startActivity(intent);
    }
    public void highscoreActivity(View view){
        Intent intent = new Intent(this,HighscoreActivity.class);
        startActivity(intent);
    }

    public void changeName(){
        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name (nChar<15)");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                if(name.length()>15) {
                    return;
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name",name);
                editor.apply();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

}
