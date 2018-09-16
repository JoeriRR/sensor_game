package com.joeribv.joerisgame;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class DataBaseManager {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int dataLength = 50;
    public DataBaseManager(){

    }
    public void updateDatabase(int score){
        // check whether player is in top 50 else dont update database.
        DatabaseReference root = database.getReference();
        DatabaseReference newChild = root.push();
        DatabaseReference gameScore = newChild.child("gameScore");
        DatabaseReference name = newChild.child("name");

        gameScore.setValue(Integer.toString(score));
        name.setValue(Constants.CURRENT_CONTEXT.getSharedPreferences("prefs", MODE_PRIVATE).getString("name", ""));

    }

    public void onChildAdded(DataSnapshot dataSnapshot, String a, ArrayList<DataBaseSorter> sortList){
        String value = dataSnapshot.child("name").getValue(String.class);
        String score = dataSnapshot.child("gameScore").getValue(String.class);
        if (value == null || score == null) {
            return;
        }
        DataBaseSorter temp = new DataBaseSorter(value,Integer.parseInt(score));
        sortList.add(temp);
        Collections.sort(sortList);
    }



}