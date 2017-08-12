package iqra.shabeer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import iqra.shabeer.fragment.CandleStickChartFragment;
import iqra.shabeer.fragment.MMSGraphFragment;
import iqra.shabeer.fragment.QualitativeAnalysisFragment;
import iqra.shabeer.fragment.QuantitativeAnalysisFragment;
import iqra.shabeer.fragment.ScoreGraphFragment;

/**
 * Created by Iqra on 20/04/2017.
 */

public class GraphPagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_PAGES = 3;
    private Context context;

    public GraphPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new ScoreGraphFragment();
        else if (position == 1)
            return new MMSGraphFragment();
        else
            return new CandleStickChartFragment();
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }
}
