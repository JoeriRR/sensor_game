package com.joeribv.joerisgame;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TableLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity implements highScore_Tab1.OnFragmentInteractionListener,highScore_Tab2.OnFragmentInteractionListener,highScore_Tab3.OnFragmentInteractionListener {
    private ArrayList<DataBaseSorter> hardList = new ArrayList<>();
    private ArrayList<DataBaseSorter> extremeList = new ArrayList<>();
    private ArrayList<DataBaseSorter> godList = new ArrayList<>();

    DataBaseManager dataBaseManager;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter hAdapter, eAdapter, gAdapter, curAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseManager = new DataBaseManager();
        setContentView(R.layout.activity_highscore2);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        /* initiatise database variables */
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        hAdapter = new MyAdapter(hardList);
        eAdapter = new MyAdapter(extremeList);
        gAdapter = new MyAdapter(godList);
        curAdapter = hAdapter;
        Constants.CUR_DIFF = "hard";
        mRecyclerView.setAdapter(curAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /* Viewpager settings */
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager)findViewById(R.id.paper);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){

            }
            });

    }
    @Override
    public void onResume() {
        super.onResume();
        if(Constants.CUR_DIFF.equals("hard")){curAdapter = hAdapter; mRecyclerView.swapAdapter(curAdapter,true);}
        else if(Constants.CUR_DIFF.equals("god")){curAdapter = gAdapter;mRecyclerView.swapAdapter(curAdapter,true);}
        else if(Constants.CUR_DIFF.equals("extreme")){curAdapter = eAdapter;mRecyclerView.swapAdapter(curAdapter,true);}
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dataBaseManager.onChildAdded(dataSnapshot,s,hardList,extremeList,godList);
                Constants.MIN_SCORE = 0;
                Constants.DATASIZE = 100;
                curAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                dataBaseManager.onChildAdded(dataSnapshot,s,hardList,extremeList,godList);
                Constants.MIN_SCORE = 0;
                Constants.DATASIZE = 100;
                curAdapter.notifyDataSetChanged();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
