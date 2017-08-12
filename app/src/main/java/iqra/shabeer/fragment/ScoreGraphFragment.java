package iqra.shabeer.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
/**
 * Created by Iqra on 21/04/2017.
 */

public class ScoreGraphFragment extends Fragment {

    protected BarChart mChart;
    private DatabaseReference evalRef;
    private ArrayList<ArrayList<Long>> dataList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score_graph, container, false);
       setHasOptionsMenu(true);
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
                mChart.getXAxis().setAxisMinimum(0.0f);
                mChart.getXAxis().setAxisMaximum(dataList.size());
                mChart.groupBars(0f, 0f, 0.1f);
                mChart.invalidate();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BarData getBarData() {
        List<BarEntry> SA = new ArrayList<>();
        List<BarEntry> A = new ArrayList<>();
        List<BarEntry> N = new ArrayList<>();
        List<BarEntry> DA = new ArrayList<>();
        List<BarEntry> SD = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ArrayList<Long> queData = dataList.get(i);
            SA.add(new BarEntry(i, queData.get(0)));
            A.add(new BarEntry(i, queData.get(1)));
            N.add(new BarEntry(i, queData.get(2)));
            DA.add(new BarEntry(i, queData.get(3)));
            SD.add(new BarEntry(i, queData.get(4)));
        }
        BarDataSet saDataSet = new BarDataSet(SA, "SA");
        saDataSet.setColor(Color.rgb(0, 255, 0));
        BarDataSet aDataSet = new BarDataSet(A, "A");
        saDataSet.setColor(Color.rgb(104, 241, 175));
        BarDataSet nDataSet = new BarDataSet(N, "N");
        nDataSet.setColor(Color.rgb(242, 247, 158));
        BarDataSet daDataSet = new BarDataSet(DA, "DA");
        daDataSet.setColor(Color.rgb(255, 102, 0));
        BarDataSet sdDataSet = new BarDataSet(SD, "SD");
        sdDataSet.setColor(Color.rgb(255, 0, 0));
        return new BarData(saDataSet, aDataSet, nDataSet, daDataSet, sdDataSet);
    }

    private void initView(View view) {
        mChart = (BarChart) view.findViewById(R.id.analysis_bar_chart);
        mChart.getDescription().setEnabled(false);

        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.animateY(5000);

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
        xAxis.setCenterAxisLabels(true);xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        //mChart.getAxisRight().setEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_graph, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mChart.saveToGallery("Score" + System.currentTimeMillis(), 50)) {
            Toast.makeText(getActivity(), "Saving SUCCESSFUL!",
                    Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity(), "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show();
        return super.onOptionsItemSelected(item);
    }

    }
