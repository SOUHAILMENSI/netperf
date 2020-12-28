package tn.dev.netperf.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import tn.dev.netperf.Fragments.BrowsingFragment;
import tn.dev.netperf.Fragments.PingFragment;
import tn.dev.netperf.Fragments.ThroughputFragment;
import tn.dev.netperf.Fragments.YoutubeFragment;
import tn.dev.netperf.R;


public class TestActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Browsing");
        arrayList.add("YouTube");
        arrayList.add("Ping");
        arrayList.add("Speed");

        prepareViewpager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void prepareViewpager(ViewPager viewPager, ArrayList<String> arrayList) {

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        BrowsingFragment fragment = new BrowsingFragment();
        YoutubeFragment youtubeFragment = new YoutubeFragment();
        PingFragment pingFragment = new PingFragment();
        ThroughputFragment throughputFragment = new ThroughputFragment();


        adapter.addFragment(fragment, arrayList.get(0));
        adapter.addFragment(youtubeFragment, arrayList.get(1));
        adapter.addFragment(pingFragment, arrayList.get(2));
        adapter.addFragment(throughputFragment, arrayList.get(3));



        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {

        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {

            arrayList.add(title);
            fragmentList.add(fragment);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }
}