package iqra.shabeer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.adapter.QuantitativeAnalysisAdapter;
import iqra.shabeer.models.QuantitativeAnalysisModel;


/**
 * Created by Devprovider on 4/5/2017.
 */

public class ViewAnalysisActivity extends AppCompatActivity {

    private ListView analysisTableListview;
    private QuantitativeAnalysisAdapter adapter;
    private List<QuantitativeAnalysisModel> dataList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_view_analysis);
        storeTempData();
        initView();
    }


    private void initView() {
        analysisTableListview = (ListView) findViewById(R.id.analysis_table_listview);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.analysis_list_header,null);
        analysisTableListview.addHeaderView(listHeader);
        adapter = new QuantitativeAnalysisAdapter(this, dataList);
        analysisTableListview.setAdapter(adapter);
    }

    private void storeTempData(){
        QuantitativeAnalysisModel model;
        for(int i=0; i<5; i++){
            model = new QuantitativeAnalysisModel();
            model.setQuestionNumber((i+1)+"");
            model.setQuestion("This is a simple question, generated for test purpose?");
            model.setScoreCount(new int[]{6,2,8,7,5});
            model.setMean(4.5);
            model.setMedian(5.3);
            model.setStdDev(0.6);
            dataList.add(model);
        }
    }
}
