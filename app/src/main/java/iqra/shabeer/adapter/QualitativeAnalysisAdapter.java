package iqra.shabeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.models.QualitativeAnalysisModel;

/**
 * Created by Devprovider on 20/04/2017.
 */

public class QualitativeAnalysisAdapter extends BaseAdapter {

    private List<QualitativeAnalysisModel> dataList = new ArrayList<>();
    private Context context;

    public QualitativeAnalysisAdapter(Context context, List<QualitativeAnalysisModel> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.qualitative_analysis_list_item, viewGroup, false);
        TextView question = (TextView) view.findViewById(R.id.question_text_view);
        TextView answerList = (TextView) view.findViewById(R.id.answers_list_text_view);

        QualitativeAnalysisModel obj = dataList.get(i);

        question.setText(obj.getQuestion());
        String answer = getAnswerString(obj.getAnswerList());
        answerList.setText(answer);

        return view;
    }

    private String getAnswerString(List<String> answerList) {
        String ans = "";
        for(int i = 0; i< answerList.size(); i++){
            ans += "Student#" + (i+1)+ " " + answerList.get(i) + "\n\n";
        }
        return ans;
    }
}
