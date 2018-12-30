package com.joeribv.joerisgame;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class DataBaseManager {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int dataLength = 50;
    public DataBaseManager(){

    }
    // to do updateDatabase met gamemodes.
    public void updateDatabase(int score,String difficulty){
        DatabaseReference root = database.getReference();
        DatabaseReference newChild = root.push();
        DatabaseReference gameScore = newChild.child("gameScore");
        DatabaseReference name = newChild.child("name");
        DatabaseReference gameMode = newChild.child("gameMode");

        // only update data when player is top 50 werkt volgens mij niet door min_score.
        if(Constants.DATASIZE <= dataLength || score > Constants.MIN_SCORE) {
            gameScore.setValue(Integer.toString(score));
            gameMode.setValue(difficulty);
            name.setValue(Constants.CURRENT_CONTEXT.getSharedPreferences("prefs", MODE_PRIVATE).getString("name", ""));
        }
    }
    public void onChildAdded(DataSnapshot dataSnapshot, String a, ArrayList<DataBaseSorter> hardList,ArrayList<DataBaseSorter> extremeList,ArrayList<DataBaseSorter> godList){
        String value = dataSnapshot.child("name").getValue(String.class);
        String score = dataSnapshot.child("gameScore").getValue(String.class);
        String difficulty = dataSnapshot.child("gameMode").getValue(String.class);
        if (value == null || score == null) {
            return;
        }
        if(difficulty == null){
            difficulty = "hard";
        }
        DataBaseSorter temp = new DataBaseSorter(value,Integer.parseInt(score),difficulty);
        if(difficulty.equals("hard")){
            hardList.add(temp);
            Collections.sort(hardList);
        }
        if(difficulty.equals("extreme")){
            extremeList.add(temp);
            Collections.sort(extremeList);
        }
        if(difficulty.equals("god")){
            godList.add(temp);
            Collections.sort(godList);
        }
    }



}
