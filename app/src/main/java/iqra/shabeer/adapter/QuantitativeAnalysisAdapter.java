package iqra.shabeer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.models.QuantitativeAnalysisModel;

/**
 * Created by Devprovider on 4/11/2017.
 */

public class QuantitativeAnalysisAdapter extends BaseAdapter {

    private List<QuantitativeAnalysisModel> dataList = new ArrayList<>();
    private Context mContext;

    public QuantitativeAnalysisAdapter( Context mContext ,List<QuantitativeAnalysisModel> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.quantitative_analysis_list_item, viewGroup, false);
        }
        TextView questionNo = (TextView) view.findViewById(R.id.question_number_textview);
        TextView qeustion = (TextView) view.findViewById(R.id.question_edittext);
        TextView scoreCount = (TextView) view.findViewById(R.id.score_count_edit_text);
        TextView mean = (TextView) view.findViewById(R.id.mean_textview);
        TextView median = (TextView) view.findViewById(R.id.median_textview);
        TextView stdDev = (TextView) view.findViewById(R.id.std_dev_textview);

        ProgressBar sa = (ProgressBar) view.findViewById(R.id.sa_progressbar);
        ProgressBar a = (ProgressBar) view.findViewById(R.id.a_progressbar);
        ProgressBar n = (ProgressBar) view.findViewById(R.id.n_progressbar);
        ProgressBar da = (ProgressBar) view.findViewById(R.id.da_progressbar);
        ProgressBar sd = (ProgressBar) view.findViewById(R.id.sd_progressbar);

        QuantitativeAnalysisModel obj = dataList.get(position);

        questionNo.setText(obj.getQuestionNumber());
        qeustion.setText(obj.getQuestion());
        int [] scoreCnt = obj.getScoreCount();

        sa.setProgress(scoreCnt[0]);
        a.setProgress(scoreCnt[1]);
        n.setProgress(scoreCnt[2]);
        da.setProgress(scoreCnt[3]);
        sd.setProgress(scoreCnt[4]);

        scoreCount.setText(getScoreCount(scoreCnt));
        mean.setText(String.format("%.2f",obj.getMean()));
        median.setText(String.valueOf(obj.getMedian()));
        stdDev.setText(String.format("%.2f",obj.getStdDev()));
        return view;
    }

    private String getScoreCount(int[] scoreCount){
        String scoreC = "";
        for(int i=0; i < scoreCount.length; i++){
            scoreC = scoreC + scoreCount[i] + " ";
        }
        return scoreC;
    }

}
