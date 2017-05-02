package iqra.shabeer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import iqra.shabeer.fragment.QualitativeAnalysisFragment;
import iqra.shabeer.fragment.QuantitativeAnalysisFragment;

/**
 * Created by Devprovider on 20/04/2017.
 */

public class AnalysisPagerAdapter extends FragmentPagerAdapter{

    private static final int TOTAL_PAGES = 2;
    private Context context;

    public AnalysisPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new QuantitativeAnalysisFragment();
        return new QualitativeAnalysisFragment();
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }
}
