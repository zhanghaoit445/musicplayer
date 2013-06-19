package com.example.musicplay;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    
    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    protected static final String[] CONTENT = new String[] { "专辑", "歌曲","艺术家", "流派","最近播放" ,"search"};
    private int mCount = CONTENT.length;
    @Override
    public Fragment getItem(int position) {
    //    return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        return TestFragment.newInstance(position); 
    }
    @Override
    public int getCount() {
        return mCount;
    }
    @Override
    public CharSequence getPageTitle(int position) {
      return TestFragmentAdapter.CONTENT[position % CONTENT.length];
    }
    @Override
    public int getIconResId(int index) {
      return index;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}