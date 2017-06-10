package iqra.shabeer.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CandleStickChart;
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

public class CandleStickChartFragment extends Fragment {


    CandleStickChart candleStickChart;
    private DatabaseReference scoreRef;
    private ArrayList<ArrayList<Long>> scoreDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candle_stick_chart, container, false);
        initView(view);
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
                ArrayList<CandleEntry> entries = new ArrayList<>();
                for (ArrayList<Long> singleQuestionScore : scoreDataList) {
                    Long[] array = new Long[singleQuestionScore.size()];
                    entries.add(new CandleEntry(Collections.min(singleQuestionScore),
                            Collections.max(singleQuestionScore), UtilHelper.findMedian(singleQuestionScore.toArray(array)),
                            UtilHelper.findQ1(singleQuestionScore.toArray(array)), UtilHelper.findQ3(singleQuestionScore.toArray(array))));
                }
                CandleDataSet dataSet = new CandleDataSet(entries, "Students");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
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
}
