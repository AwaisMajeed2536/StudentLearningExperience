package iqra.shabeer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import iqra.shabeer.R;
import iqra.shabeer.adapter.AnalysisPagerAdapter;


/**
 * Created by Iqra on 4/5/2017.
 */

public class ViewAnalysisActivity extends AppCompatActivity {

    private ViewPager fragmentHolder;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_view_analysis);
        fragmentHolder = (ViewPager) findViewById(R.id.fragment_holder);
        adapter = new AnalysisPagerAdapter(getSupportFragmentManager(), this);
        fragmentHolder.setAdapter(adapter);
    }

//todo try generating pdf again, add optionMenu

}
