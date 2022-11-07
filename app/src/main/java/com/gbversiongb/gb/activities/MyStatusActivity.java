package com.gbversiongb.gb.activities;

import static com.gbversiongb.gb.modules.AdController.LoadFBInterstitial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.fragments.MyPhotos;
import com.gbversiongb.gb.fragments.MyVideos;
import com.gbversiongb.gb.modules.AdController;
import com.gbversiongb.gb.modules.Utils;

public class MyStatusActivity extends AppCompatActivity {

    ImageView backIV;

    TabLayout tabLayout;
    ViewPager viewPager;
    String[] tabs;
    LinearLayout bannerBox;

    private com.facebook.ads.AdView faceBookBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.loadLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);


        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> finish());

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabs = new String[2];
        tabs[0] = getResources().getString(R.string.photos);
        tabs[1] = getResources().getString(R.string.videos);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabViewUn(i));
        }
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabView(tab.getPosition()));

                LoadFBInterstitial(MyStatusActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabViewUn(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bannerBox = findViewById(R.id.bannerBox);
        AdController.loadAdFifty(this, bannerBox);
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    private void setupTabIcons() {
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView txt = v.findViewById(R.id.tab);
        txt.setText(tabs[0]);
        txt.setTextColor(getResources().getColor(R.color.tab_txt_press));
        txt.setBackgroundResource(R.drawable.press_tab);
        FrameLayout.LayoutParams tabp = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 438 / 1080,
                getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        txt.setLayoutParams(tabp);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(null);
        tab.setCustomView(v);
    }

    public View getTabView(int pos) {
        View v = LayoutInflater.from(MyStatusActivity.this).inflate(R.layout.custom_tab, null);
        TextView txt = v.findViewById(R.id.tab);
        txt.setText(tabs[pos]);
        txt.setTextColor(getResources().getColor(R.color.tab_txt_press));
        txt.setBackgroundResource(R.drawable.press_tab);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 438 / 1080,
                getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        txt.setLayoutParams(tab);
        return v;
    }

    public View getTabViewUn(int pos) {
        View v = LayoutInflater.from(MyStatusActivity.this).inflate(R.layout.custom_tab, null);
        TextView txt = v.findViewById(R.id.tab);
        txt.setText(tabs[pos]);
        txt.setTextColor(getResources().getColor(R.color.tab_txt_unpress));
        txt.setBackgroundResource(R.drawable.unpress_tab);
        FrameLayout.LayoutParams tab = new FrameLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels * 438 / 1080,
                getResources().getDisplayMetrics().heightPixels * 140 / 1920);
        txt.setLayoutParams(tab);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new MyPhotos(), "Photos");
        adapter.addFragment(new MyVideos(), "Videos");

        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }
}
