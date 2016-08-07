package com.zhanchiff.draglayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.zhanchiff.draglayout.library.DragTopNewGroup;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements DragTopNewGroup.BelowTouchListener {

    private DragTopNewGroup DragLayout;
    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private ArrayList<Fragment> mFragments;

    private String[] mTitles = new String[]{"Listview", "GridView", "Scrollview"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DragLayout = (DragTopNewGroup) findViewById(R.id.drag_layout);
        DragLayout.setBelowListener(this);

        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOffscreenPageLimit(3);

    }

    private void initDatas() {

        mFragments = new ArrayList<>();
        mIndicator.setTitles(mTitles);

        ListviewFragment fragment = new ListviewFragment();
        mFragments.add(fragment);
        GridViewFragment fragment1 = new GridViewFragment();
        mFragments.add(fragment1);
        ScrollViewFragment fragment2 = new ScrollViewFragment();
        mFragments.add(fragment2);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initViews() {
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public boolean canViewPullDown() {
        ViewGroup mInnerScrollView;
        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter a = mViewPager.getAdapter();
        if (a instanceof FragmentPagerAdapter) {
            FragmentPagerAdapter fsAdapter = (FragmentPagerAdapter) a;
            Fragment item = (Fragment) fsAdapter.instantiateItem(mViewPager, currentItem);
            mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.list_view));
        } else {
            mInnerScrollView = null;
        }

        if (mInnerScrollView == null) {
            return false;
        }
        if (mInnerScrollView instanceof ScrollView) {
            if (mInnerScrollView.getScrollY() == 0) {
                return true;
            }
        } else if (mInnerScrollView instanceof AbsListView) {
            AbsListView lv = (AbsListView) mInnerScrollView;
            if (lv.getChildCount() <= 0) {//如果没有adapter
                return true;
            } else if (lv.getFirstVisiblePosition() < 0) { //如果可见的位置小于0
                return true;
            } else if (lv.getFirstVisiblePosition() == 0 && lv.getChildAt(0).getTop() >= lv.getPaddingTop()) { //如果可见的位置就是0，那么他的top位置要低于paddingtop
                return true;
            }
        } else if (mInnerScrollView.getScrollY() == 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canViewPullUP() {
        ViewGroup mInnerScrollView;
        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter a = mViewPager.getAdapter();
        if (a instanceof FragmentPagerAdapter) {
            FragmentPagerAdapter fsAdapter = (FragmentPagerAdapter) a;
            Fragment item = (Fragment) fsAdapter.instantiateItem(mViewPager, currentItem);
            mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.list_view));//这里要保证每个fragment都要有一个list_view的id
        } else {
            mInnerScrollView = null;
        }

        if (mInnerScrollView == null) {
            return true;
        }
        if (mInnerScrollView instanceof ScrollView) {
            if ((mInnerScrollView.getScrollY() + mInnerScrollView.getHeight()) >= mInnerScrollView.getChildAt(0).getMeasuredHeight()) {
                return true;
            }
        } else if (mInnerScrollView instanceof AbsListView) {
            AbsListView lv = (AbsListView) mInnerScrollView;
            if (lv.getChildCount() <= 0) {//如果没有adapter
                return true;
            } else if (lv.getLastVisiblePosition() >= lv.getAdapter().getCount()) { //如果可见的位置小于0
                return true;
            } else if (lv.getLastVisiblePosition() == lv.getAdapter().getCount() - 1) { //如果可见的位置就是0，那么他的top位置要低于paddingtop
                if (lv.getChildAt(lv.getChildCount() - 1).getBottom() <= lv.getMeasuredHeight() - lv.getPaddingBottom()) {
                    return true;
                }
            }
        } else {
            return true;
        }

        return false;
    }
}
