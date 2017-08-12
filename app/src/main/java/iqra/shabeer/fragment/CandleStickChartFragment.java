package iqra.shabeer.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import iqra.shabeer.R;
import iqra.shabeer.helper.UtilHelper;

/**
 * Created by Iqra on 05/05/2017.
 */

public class CandleStickChartFragment extends Fragment {


    protected CandleStickChart candleStickChart;
    private DatabaseReference scoreRef;
    private ArrayList<ArrayList<Long>> scoreDataList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candle_stick_chart, container, false);
        initView(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void initView(View view) {
        candleStickChart = (CandleStickChart) view.findViewById(R.id.candle_stick_chart);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scoreRef = FirebaseDatabase.getInstance().getReferenceFromUrl(
                "https://student-evaluation-system.firebaseio.com/root/analysisData/quantitative/" + "dbd");

        scoreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreDataList = (ArrayList<ArrayList<Long>>) dataSnapshot.getValue();
                int i=0;
                ArrayList<CandleEntry> entries = new ArrayList<>();
                for (ArrayList<Long> singleQuestionScore : scoreDataList) {
                    Long[] array = new Long[singleQuestionScore.size()];
                    //entries.add(new CandleEntry(indexNo , max value in each ques, min value in each ques, Q1, Q3);
                    entries.add(new CandleEntry(i++,Collections.max(singleQuestionScore),Collections.min(singleQuestionScore),
                            UtilHelper.findQ1(singleQuestionScore.toArray(array)), UtilHelper.findQ3(singleQuestionScore.toArray(array))));
                }

                CandleDataSet dataSet = new CandleDataSet(entries, "Questions");
                dataSet.setDrawIcons(false);
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setColor(Color.rgb(80, 80, 80));
                dataSet.setShadowColor(Color.DKGRAY);
                dataSet.setShadowWidth(0.7f);
                dataSet.setIncreasingColor(Color.RED);
                dataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
               dataSet.setLabel("Questions");
                CandleData data = new CandleData(dataSet);
                candleStickChart.setData(data);
                candleStickChart.animateY(5000);
                candleStickChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_graph, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (candleStickChart.saveToGallery("Boxplot" + System.currentTimeMillis(), 50)) {
            Toast.makeText(getActivity(), "Saving SUCCESSFUL!",
                    Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity(), "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show();
        return super.onOptionsItemSelected(item);
    }

}
