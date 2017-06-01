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

import java.util.ArrayList;

import iqra.shabeer.R;

public class CandleStickChartFragment extends Fragment {


    CandleStickChart candleStickChart;

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


        ArrayList<CandleEntry> entries = new ArrayList<>();

        entries.add(new CandleEntry(0, 4.62f, 2.02f, 2.70f, 4.13f));
        entries.add(new CandleEntry(1, 5.50f, 2.70f, 3.35f, 4.96f));
        entries.add(new CandleEntry(2, 5.25f, 3.02f, 3.50f, 4.50f));
        entries.add(new CandleEntry(3, 6f, 3.25f, 4.40f, 5.0f));
        entries.add(new CandleEntry(4, 7f, 0f, 4.2f, 5.3f));

        //how to add data.......
        //entries.add(new CandleEntry(indexNo , max value in each ques, min value in each ques, Q1, Q3);
        CandleDataSet dataset = new CandleDataSet(entries, "# of Calls");//ye na aaye...

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Question 1");
        labels.add("Question 2");
        labels.add("Question 3");
        labels.add("Question 4");
        labels.add("Question 5");


        CandleData data = new CandleData(dataset);
        candleStickChart.setData(data);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        //candleStickChart.setDescription("Candle Stick");
        candleStickChart.animateY(5000);
    }
}
