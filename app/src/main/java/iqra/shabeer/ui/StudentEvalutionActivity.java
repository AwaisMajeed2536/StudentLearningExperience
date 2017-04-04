package iqra.shabeer.ui;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.models.EvaluationDataBO;

/**
 * Created by Awais on 2/21/2017.
 */

public class StudentEvalutionActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference getQuestionRef;
    DatabaseReference submitReportRef;
    LinearLayout parentLinearLayout;
    private Context mContext;
    private int numberOfQuestions;
    private String[] selectedOptionsArray;
    private String subjectName;
    private ArrayList<long[]> previousValues;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_evaluation_activity);
        mDatabase = FirebaseDatabase.getInstance();
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_ll);
        mContext = StudentEvalutionActivity.this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            subjectName = bundle.getString("subjectName").trim();
        getQuestionRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/evaluations/"
                + subjectName);
        submitReportRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/analysisData/"
                + subjectName);
        getQuestionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EvaluationDataBO obj = dataSnapshot.getValue(EvaluationDataBO.class);
                List<String> listOfQuestion = obj.getQuestions();
                numberOfQuestions = listOfQuestion.size();
                selectedOptionsArray = new String[numberOfQuestions];
                for (int i = 0; i < numberOfQuestions; i++) {
                    LinearLayout ll = new LinearLayout(mContext);
                    LinearLayout ll1 = new LinearLayout(mContext);
                    final RadioGroup rg = new RadioGroup(mContext);
                    rg.setId(i + 18946);
                    ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT));
                    ll1.setOrientation(LinearLayout.HORIZONTAL);
                    ll1.setGravity(Gravity.CENTER);
                    TextView question = new TextView(mContext);
                    question.setId(i);
                    question.setTextSize(18);
                    RadioButton rb1 = new RadioButton(mContext);
                    rb1.setText("1");
                    RadioButton rb2 = new RadioButton(mContext);
                    rb2.setText("2");
                    RadioButton rb3 = new RadioButton(mContext);
                    rb3.setText("3");
                    RadioButton rb4 = new RadioButton(mContext);
                    rb4.setText("4");
                    RadioButton rb5 = new RadioButton(mContext);
                    rb5.setText("5");
                    rg.setOrientation(LinearLayout.HORIZONTAL);
                    rg.addView(rb1);
                    rg.addView(rb2);
                    rg.addView(rb3);
                    rg.addView(rb4);
                    rg.addView(rb5);
                    ll1.addView(rg);
                    question.setText(listOfQuestion.get(i));
                    ll.addView(question);
                    ll.addView(ll1);
                    parentLinearLayout.addView(ll);
                }
                Button submitButton = new Button(mContext);
                submitButton.setText("submit");
                submitButton.setId(825 + 1);
                parentLinearLayout.addView(submitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < numberOfQuestions; i++) {
                            RadioGroup rg = (RadioGroup) parentLinearLayout.findViewById(i + 18946);
                            int selectedOption = rg.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton) rg.findViewById(selectedOption);
                            selectedOptionsArray[i] = rb.getText().toString();
                        }
                        submitReportRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<ArrayList<Long>> subjectReport = (ArrayList<ArrayList<Long>>)
                                            dataSnapshot.getValue();
                                    previousValues = new ArrayList<long[]>();
                                    for (int i = 0; i < subjectReport.size(); i++) {
                                        ArrayList<Long> questionReport = subjectReport.get(i);
                                        long[] counts = new long[5];
                                        for (int j = 0; j < questionReport.size(); j++) {
                                            counts[j] = questionReport.get(j);
                                        }
                                        previousValues.add(counts);
                                    }
                                    submitReport(selectedOptionsArray, previousValues);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void submitReport(String[] selectedOptionsArray, ArrayList<long[]> previousValues) {
        ArrayList<long[]> reportDataToBeSubmitted = addValuesToMakeReport(selectedOptionsArray, previousValues);
        for (int i = 0; i < reportDataToBeSubmitted.size(); i++) {
            long[] ans = reportDataToBeSubmitted.get(i);
            for (int j = 0; j < 5; j++) {
                if (ans[j] == 0)
                    continue;
                submitReportRef.child(String.valueOf(i)).child(String.valueOf(j)).setValue(ans[j]);
            }
        }
        finish();
    }

    private ArrayList<long[]> addValuesToMakeReport(String[] selectedOptionsArray, ArrayList<long[]> previousValues) {
        ArrayList<long[]> output = new ArrayList<>();
        for (int i = 0; i < previousValues.size(); i++) {
            long[] previousCounts = previousValues.get(i);
            long[] newCounts = new long[5];
            for (int j = 0; j < 5; j++) {
                if ((Integer.valueOf(selectedOptionsArray[i]) - 1) == j)
                    newCounts[j] = previousCounts[j] + 1;
                else
                    newCounts[j] = previousCounts[j];
            }
            output.add(newCounts);
        }
        return output;
    }
}
