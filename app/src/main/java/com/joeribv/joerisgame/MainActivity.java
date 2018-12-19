package com.joeribv.joerisgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private SeekBar xBar;
    private SeekBar yBar;
    private String name;
    private int temp = 0;
    private ArrayList<DataBaseSorter> sortList = new ArrayList<>();
    DataBaseManager dataBaseManager;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // to do, add setting button and add xsens/ysens to prefs!
    // create highscore activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseManager = new DataBaseManager();
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
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(sortList);
        mRecyclerView.setAdapter(mAdapter);

        if (prefs.getString("name", "").isEmpty()) {
            changeName();
        }
        if (prefs.getString("level","").isEmpty()){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("level",Integer.toString(temp));
            editor.apply();
        }
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataBaseManager.onChildAdded(dataSnapshot,s,sortList);
                Constants.MIN_SCORE = sortList.get(sortList.size()-1).getScore();
                Constants.DATASIZE = sortList.size();
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                dataBaseManager.onChildAdded(dataSnapshot,s,sortList);
                Constants.MIN_SCORE = sortList.get(sortList.size()-1).getScore();
                Constants.DATASIZE = sortList.size();
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void StartGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
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
