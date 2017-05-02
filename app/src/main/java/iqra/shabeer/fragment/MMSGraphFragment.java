package iqra.shabeer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.helper.UtilHelper;

/**
 * Created by Devprovider on 02/05/2017.
 */

public class MMSGraphFragment extends Fragment {

    protected BarChart mChart;
    private DatabaseReference evalRef;
    private ArrayList<ArrayList<Long>> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score_graph, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        evalRef = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://student-evaluation-system.firebaseio.com/root/analysisData/quantitative/" + "dbd");
        evalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList = (ArrayList<ArrayList<Long>>) dataSnapshot.getValue();
                BarData barData = getBarData();
                barData.setValueFormatter(new LargeValueFormatter());
                mChart.setData(barData);

                mChart.getBarData().setBarWidth(0.1f);
                mChart.getXAxis().setAxisMinimum(1.0f);
                mChart.getXAxis().setAxisMaximum(dataList.size() + 1.0f);
                mChart.groupBars(0f, 0f, 0.235f);
                mChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BarData getBarData() {
        List<BarEntry> mean = new ArrayList<>();
        List<BarEntry> med = new ArrayList<>();
        List<BarEntry> stdDev = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ArrayList<Long> queData = dataList.get(i);
            double meanValue = UtilHelper.findMean(queData);
            mean.add(new BarEntry(i, (float) meanValue));
            med.add(new BarEntry(i, 2.3f));
            stdDev.add(new BarEntry(i, (float) UtilHelper.findStdDev(meanValue, queData)));
        }
        BarDataSet saDataSet = new BarDataSet(mean, "Mean");
        saDataSet.setColor(Color.rgb(0, 255, 0));
        BarDataSet aDataSet = new BarDataSet(med, "Median");
        saDataSet.setColor(Color.rgb(104, 241, 175));
        BarDataSet nDataSet = new BarDataSet(stdDev, "Std Deviation");
        nDataSet.setColor(Color.rgb(242, 247, 158));
        return new BarData(saDataSet, aDataSet, nDataSet);
    }


    private void initView(View view) {
        mChart = (BarChart) view.findViewById(R.id.analysis_bar_chart);
        mChart.getDescription().setEnabled(false);

        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
    }
}
