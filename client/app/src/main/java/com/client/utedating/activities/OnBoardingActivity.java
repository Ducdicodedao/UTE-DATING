package com.client.utedating.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.client.utedating.R;
import com.client.utedating.utils.MySharedPreferences;

public class OnBoardingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ImageView[] dots;
    private int[] mResources;
    private String[] mTitle;
    private String[] mDesc;
    private ViewPager mViewPager;
    private CustomPagerAdapter mAdapter;
    private LinearLayout pagerIndicator;
    private int dotsCount = 0;
    private AppCompatButton buttonNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        mResources =new int[] {
            R.drawable.img_onboarding1,
                    R.drawable.img_onboarding2,
                    R.drawable.img_onboarding3,
        };
        mTitle = new String[]{
                getString(R.string.title1),
                getString(R.string.title2),
                getString(R.string.title3),
        };
        mDesc = new String[]{
                getString(R.string.desc1),
                getString(R.string.desc2),
                getString(R.string.desc3),
        };

        mViewPager = findViewById(R.id.viewpager);
        pagerIndicator = findViewById(R.id.viewPagerCountDots);
        buttonNext = findViewById(R.id.buttonNext);

        mAdapter = new CustomPagerAdapter(this, mResources);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
        setPageViewIndicator();


        buttonNext.setOnClickListener(v -> {
            int count = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(count + 1, true);
            if(count == 2){
                MySharedPreferences.setBooleanSharedPreference(OnBoardingActivity.this,"isFirstTime", false);
                Intent intent = new Intent(OnBoardingActivity.this, LoginGGActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.item_dot_inactive,
                    null
            ));
        }

        dots[position].setImageDrawable(ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.item_dot_active,
                null
        ));

        if (position == 2) {
            buttonNext.setText("Start");
        } else {
            buttonNext.setText("Next");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class CustomPagerAdapter extends PagerAdapter {
        private LayoutInflater mLayoutInflater;
        private int[] mResources;

        public CustomPagerAdapter(Context context, int[] resources) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResources = resources;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_onboarding, container, false);
            ((ImageView) itemView.findViewById(R.id.ivBanner)).setImageResource(mResources[position]);
            ((TextView) itemView.findViewById(R.id.tvTitle)).setText(mTitle[position]);
            ((TextView) itemView.findViewById(R.id.tvDesc)).setText(mDesc[position]);

            TextView txtViewTitle = itemView.findViewById(R.id.tvTitle);
            Shader shader = new LinearGradient(0,0,0,txtViewTitle.getLineHeight(),
                    getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT);
            txtViewTitle.getPaint().setShader(shader);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    private void setPageViewIndicator() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.item_dot_inactive,
                    null
            ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(7, 0, 7, 0);

            final int finalI = i;
            dots[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(finalI);
                    return true;
                }
            });

            pagerIndicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.item_dot_active,
                null
        ));
    }
}