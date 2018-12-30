package com.joeribv.joerisgame;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity {
    private ArrayList<DataBaseSorter> hardList = new ArrayList<>();
    private ArrayList<DataBaseSorter> extremeList = new ArrayList<>();
    private ArrayList<DataBaseSorter> godList = new ArrayList<>();

    private highScore_Tab hardTab = new highScore_Tab();
    private highScore_Tab extremeTab = new highScore_Tab();
    private highScore_Tab godTab = new highScore_Tab();

    DataBaseManager dataBaseManager;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private RecyclerView.Adapter hAdapter, eAdapter, gAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseManager = new DataBaseManager();
        setContentView(R.layout.activity_highscore2);

        hAdapter = new MyAdapter(hardList);
        eAdapter = new MyAdapter(extremeList);
        gAdapter = new MyAdapter(godList);

        hardTab.setAdapter(hAdapter);
        extremeTab.setAdapter(eAdapter);
        godTab.setAdapter(gAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        final ViewPager viewPager = findViewById(R.id.paper);
        tabLayout.setupWithViewPager(viewPager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(hardTab, "Hard");
        pagerAdapter.addFragment(extremeTab, "Extreme");
        pagerAdapter.addFragment(godTab, "God");
        viewPager.setAdapter(pagerAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataBaseManager.onChildAdded(dataSnapshot,s,hardList,extremeList,godList);
                Constants.MIN_SCORE = 0;
                Constants.DATASIZE = 100;
                hAdapter.notifyDataSetChanged();
                eAdapter.notifyDataSetChanged();
                gAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                dataBaseManager.onChildAdded(dataSnapshot,s,hardList,extremeList,godList);
                Constants.MIN_SCORE = 0;
                Constants.DATASIZE = 100;
                hAdapter.notifyDataSetChanged();
                eAdapter.notifyDataSetChanged();
                gAdapter.notifyDataSetChanged();
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
}
