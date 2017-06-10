package iqra.shabeer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import iqra.shabeer.utilities.Constants;

/**
 * Created by Awais on 2/26/2017.
 */

public class GenerateSurveyActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Context mContext;
    private LinearLayout surveyQuestionLL;
    private LinearLayout surveyParentLL;
    private HashMap<String, List<String>> questionsList = new HashMap<String, List<String>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_survey_activity);
        mContext = GenerateSurveyActivity.this;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/surveyQuestions");
        surveyQuestionLL = (LinearLayout) findViewById(R.id.survey_questions_list_ll);
        surveyParentLL = (LinearLayout) findViewById(R.id.survey_parent_ll);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionsList = (HashMap<String, List<String>>) dataSnapshot.getValue();
                generateView(questionsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void generateView(final HashMap<String, List<String>> list){
        List<String> quantitaveQuestion = new ArrayList<>();
        quantitaveQuestion = list.get("quantitative");
        Constants.QNT_CHILD_COUNT = quantitaveQuestion.size();
        List<String> qualitativeQestion = new ArrayList<>();
        qualitativeQestion = list.get("qualitative");
        Constants.QLT_CHILD_COUNT = qualitativeQestion.size();
        TextView quantitativeTv = new TextView(this);
        quantitativeTv.setText("Quantitative Questions");
        View hr1 = new View(this);
        ViewGroup.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                1);
        hr1.setBackground(ContextCompat.getDrawable(this, R.drawable.mid_blue_color));
        hr1.setLayoutParams(lp);
        surveyQuestionLL.addView(quantitativeTv);
        surveyQuestionLL.addView(hr1);
        for (int i=0; i<quantitaveQuestion.size(); i++){
            CheckBox cb = new CheckBox(mContext);
            cb.setText(quantitaveQuestion.get(i));
            cb.setId(0+i);
            surveyQuestionLL.addView(cb);
        }
        View hr2 = new View(this);
        hr2.setBackground(ContextCompat.getDrawable(this, R.drawable.mid_blue_color));
        hr2.setLayoutParams(lp);
        TextView qualitativeTv = new TextView(this);
        qualitativeTv.setText("Qualitative Questions");
        surveyQuestionLL.addView(qualitativeTv);
        surveyQuestionLL.addView(hr2);
        for (int i=0; i<qualitativeQestion.size(); i++){
            CheckBox cb = new CheckBox(mContext);
            cb.setText(qualitativeQestion.get(i));
            cb.setId(128+i);
            surveyQuestionLL.addView(cb);
        }

        setDateView();
        Button button = new Button(mContext);
        button.setText("Generate");
        surveyParentLL.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedQuestionList = new ArrayList<String>();
                String selectedCourse = getIntent().getStringExtra(TeacherLandingActivity.COURSE_PASS_KEY);
                for(int i=0; i<surveyQuestionLL.getChildCount(); i++){
                    CheckBox cb = (CheckBox)surveyQuestionLL.findViewById(0+i);
                    if(cb == null)
                        continue;
                    if (cb.isChecked())
                        selectedQuestionList.add(cb.getText().toString());
                }
                EvaluationDataBO obj = new EvaluationDataBO(selectedCourse,"sdfs",selectedQuestionList);
                DatabaseReference tempRef= mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/evaluations");
                tempRef.child(selectedCourse.toLowerCase()).setValue(obj);
            }
        });
    }

    private void setDateView(){
        String [] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        NumberPicker eDay = (NumberPicker)findViewById(R.id.end_day);
        eDay.setMinValue(1);
        eDay.setMaxValue(31);
        NumberPicker eMonth = (NumberPicker)findViewById(R.id.end_month);
        eMonth.setMinValue(0);
        eMonth.setMaxValue(11);
        eMonth.setDisplayedValues(monthNames);
        NumberPicker eYear = (NumberPicker)findViewById(R.id.end_year);
        eYear.setMinValue(2017);
        eYear.setMaxValue(2020);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.add_question_menu_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_question){
            startActivity(new Intent(this,AddQuestionActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
