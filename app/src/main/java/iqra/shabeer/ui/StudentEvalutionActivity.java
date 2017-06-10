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
import android.widget.EditText;
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
import java.util.List;

import iqra.shabeer.R;

/**
 * Created by Awais on 2/21/2017.
 */

public class StudentEvalutionActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference getQuantitativeQueRef;
    DatabaseReference submitQauntReportRef;
    LinearLayout parentLinearLayout;
    private Context mContext;
    private int numberOfQuantitativeQuestions;
    private String[] selectedOptionsArray;
    private String subjectName;
    private ArrayList<long[]> previousValues;
    private DatabaseReference getQualitativeQueRef;
    private int numberOfQualitativeQuestions;
    private DatabaseReference submitQaultReportRef;
    private long qualtChildCount;

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
        countQualtChildren();
        getQuantitativeQueRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/evaluations/"
                + subjectName + "/quantitative/");
        getQualitativeQueRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/evaluations/"
                + subjectName + "/qualitative/");
        addQuantitativeQuestionsToView();

    }

    private void addQuantitativeQuestionsToView(){
        getQuantitativeQueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listOfQuestion = (List<String>) dataSnapshot.getValue();
                numberOfQuantitativeQuestions = listOfQuestion.size();
                selectedOptionsArray = new String[numberOfQuantitativeQuestions];
                for (int i = 0; i < numberOfQuantitativeQuestions; i++) {
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
                addQualitativeQuestionsToView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addQualitativeQuestionsToView(){
        getQualitativeQueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    return;
                }
                List<String> queList = (List<String>) dataSnapshot.getValue();
                numberOfQualitativeQuestions = queList.size();
                for(int i=0; i< queList.size(); i++){
                    LinearLayout linearLayout = new LinearLayout(mContext);

                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView textView = new TextView(mContext);
                    textView.setText(queList.get(i));
                    textView.setTextSize(18);
                    linearLayout.addView(textView);
                    EditText editText = new EditText(mContext);
                    editText.setId(2912 + i);
                    editText.setTextSize(18);
                    linearLayout.addView(editText);
                    parentLinearLayout.addView(linearLayout);
                }
                addSubmitButtonToView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addSubmitButtonToView(){
        Button submitButton = new Button(mContext);
        submitButton.setText("submit");
        submitButton.setId(825 + 1);
        parentLinearLayout.addView(submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < numberOfQuantitativeQuestions; i++) {
                    RadioGroup rg = (RadioGroup) parentLinearLayout.findViewById(i + 18946);
                    int selectedOption = rg.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) rg.findViewById(selectedOption);
                    selectedOptionsArray[i] = rb.getText().toString();
                }
                submitQauntReportRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/analysisData/quantitative/"
                        + subjectName);
                submitQauntReportRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<ArrayList<Long>> subjectReport = (ArrayList<ArrayList<Long>>)
                                dataSnapshot.getValue();
                        if (subjectReport.isEmpty())
                            return;
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

                submitQaultReportRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/analysisData/qualitative/"
                        + subjectName);

                List<String> ansList = new ArrayList<String>();
                for(int i=0; i<numberOfQualitativeQuestions; i++){
                    submitQaultReportRef.child(String.valueOf(i)).child(String.valueOf(qualtChildCount))
                            .setValue(((EditText)parentLinearLayout.findViewById(2912+i)).getText().toString());
                }

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
                submitQauntReportRef.child(String.valueOf(i)).child(String.valueOf(j)).setValue(ans[j]);
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

    private void countQualtChildren(){
        DatabaseReference tempRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/analysisData/qualitative/"
                + subjectName + "/0");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qualtChildCount = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
