
package com.example.musicplay;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class MainActivity extends FragmentActivity implements OnClickListener {
    TestFragmentAdapter mAdapter;
    ViewPager mPager;
    ImageButton button, next, play;
    ImageView player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_titles);
        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);
        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
      //  indicator.setOnCenterItemClickListener(this);
        button = (ImageButton) findViewById(R.id.min_button3);
        next = (ImageButton) findViewById(R.id.next);
        play = (ImageButton) findViewById(R.id.play);
        player = (ImageView) findViewById(R.id.player);
        button.setOnClickListener(this);
        play.setOnClickListener(this);
        player.setOnClickListener(this);
        next.setOnClickListener(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println();
        Log.i("xx", "屏幕改变");
        Toast.makeText(MainActivity.this, "百", 1).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.min_button3:
                mPager.setCurrentItem(mAdapter.getCount() - 1);
                break;
            case R.id.next:
                
                break;
            case R.id.play:
                //v.setBackgroundResource(R)
                break;
            case R.id.player:
                break;
            default:
                break;
        }
    }
}
