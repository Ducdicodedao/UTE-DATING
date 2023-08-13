package com.client.utedating.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.client.utedating.R;
import com.client.utedating.adapters.ViewPagerAdapter;
import com.client.utedating.fragments.AccountFragment;
import com.client.utedating.fragments.ChatFragment;
import com.client.utedating.fragments.LikedFragment;
import com.client.utedating.fragments.SwipeViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private Context mContext;
    private ViewPager viewPager;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);

        frameLayout = findViewById(R.id.frame_Layout);
        frameLayout.setVisibility(View.GONE);

        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new SwipeViewFragment());
        fragList.add(new LikedFragment());
        fragList.add(new ChatFragment());
        fragList.add(new AccountFragment());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        bnv.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //account
            case R.id.card:
                viewPager.setCurrentItem(0);
                break;
            case R.id.liked:
                viewPager.setCurrentItem(1);
                break;
            case R.id.chat:
                viewPager.setCurrentItem(2);
                break;
            case R.id.account:
                viewPager.setCurrentItem(3);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(frameLayout.getVisibility() == View.VISIBLE){
            frameLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "MainActivity onDestroy");
    }
}