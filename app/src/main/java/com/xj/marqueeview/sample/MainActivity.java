package com.xj.marqueeview.sample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xj.marqueeview.sample.base.BaseFragmentPagerAdapter;
import com.xj.marqueeview.sample.iamgetext.ImageTextFragment;
import com.xj.marqueeview.sample.multitype.MultiTextFragment;
import com.xj.marqueeview.sample.simpletext.SimpleTextFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new SimpleTextFragment());
        mFragments.add(new ImageTextFragment());
        mFragments.add(new MultiTextFragment());
        mFragments.add(new AllTypeFragment());
        String[] titles = new String[]{"SimpleText","ImageText","MultiText","AllType"};
        BaseFragmentPagerAdapter<Fragment> pagerAdapter = new
                BaseFragmentPagerAdapter<>(getSupportFragmentManager(),titles,mFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }
}
