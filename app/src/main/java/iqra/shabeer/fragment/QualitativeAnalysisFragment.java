package iqra.shabeer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.adapter.QualitativeAnalysisAdapter;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.QualitativeAnalysisModel;

/**
 * Created by Devprovider on 20/04/2017.
 */

public class QualitativeAnalysisFragment extends Fragment {

    private ListView qualitativeListView;
    private List<QualitativeAnalysisModel> dataList = new ArrayList<>();
    private QualitativeAnalysisAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qualitative_analysis, container, false);
        storeTempData();
        UtilHelper.showWaitDialog(getActivity(), "Creating Report", "please wait...");
        qualitativeListView = (ListView) rootView.findViewById(R.id.qualitative_analysis_table_listview);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.analysis_list_header,null);
        ((TextView) header.findViewById(R.id.report_type)).setText("Qualitative Summary");
        ((LinearLayout) header.findViewById(R.id.table_header)).setVisibility(View.GONE);
        qualitativeListView.addHeaderView(header);
        adapter = new QualitativeAnalysisAdapter(getActivity(), dataList);
        qualitativeListView.setAdapter(adapter);
        UtilHelper.dismissWaitDialog();
    }

    private void storeTempData(){
        for(int i=0; i<7; i++){
            QualitativeAnalysisModel model = new QualitativeAnalysisModel();
            model.setQuestion("This is question number " + (i+1) +"generated for test purpose?");
            List<String> ans = new ArrayList<>();
            for(int j=0; j<6; j++) {
                ans.add("This is answer number " +(j+1) +"for question number" + (i+1));
            }
            model.setAnswerList(ans);
            dataList.add(model);
        }
    }
}
