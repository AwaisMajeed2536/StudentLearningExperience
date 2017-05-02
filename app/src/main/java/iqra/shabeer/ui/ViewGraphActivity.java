package iqra.shabeer.ui;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import iqra.shabeer.R;
import iqra.shabeer.adapter.AnalysisPagerAdapter;
import iqra.shabeer.adapter.GraphPagerAdapter;

public class ViewGraphActivity extends AppCompatActivity {
    private ViewPager fragmentHolder;
    private GraphPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_view_graph);
        fragmentHolder = (ViewPager) findViewById(R.id.fragment_holder);
        adapter = new GraphPagerAdapter(getSupportFragmentManager(), this);
        fragmentHolder.setAdapter(adapter);
    }
}
