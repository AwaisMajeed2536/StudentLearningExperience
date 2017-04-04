package iqra.shabeer.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
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
import iqra.shabeer.models.StudentAccountBO;

/**
 * Created by Awais on 2/21/2017.
 */

public class StudentLandingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView []surveyClickAbles = new TextView[5];
    private TextView []surveyStatus = new TextView[5];
    private TextView userNameDisplay;
    private FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    private Context mContext;
    StudentAccountBO userAccountData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_landing_activity);
        initialize();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HashMap<String,String>> list = (ArrayList<HashMap<String,String>>)dataSnapshot.getValue();
                setData(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void setData(List<HashMap<String,String>> list){
        int size = list.size();
        for (int i=0; i<size; i++){
            surveyClickAbles[i].setVisibility(View.VISIBLE);
            surveyClickAbles[i].setText(list.get(i).get("courseName"));
            surveyStatus[i].setVisibility(View.VISIBLE);
            String status = list.get(i).get("status");
            surveyStatus[i].setText(status);
            if (status.equals("open")) {
                surveyStatus[i].setTextColor(Color.parseColor("#00ff00"));
                surveyClickAbles[i].setTextColor(Color.parseColor("#0000ff"));
                surveyClickAbles[i].setOnClickListener(this);
            }
            else
                surveyStatus[i].setTextColor(Color.parseColor("#ff0000"));
        }
    }
    public void initialize(){
        userNameDisplay = (TextView) findViewById(R.id.user_name_display);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            userAccountData = bundle.getParcelable("userData");
            userNameDisplay.setText(userAccountData.getFName());
        }
        mContext = StudentLandingActivity.this;
        surveyClickAbles[0] = (TextView)findViewById(R.id.survey_one);
        surveyClickAbles[1] = (TextView)findViewById(R.id.survey_two);
        surveyClickAbles[2] = (TextView)findViewById(R.id.survey_three);
        surveyClickAbles[3] = (TextView)findViewById(R.id.survey_four);
        surveyClickAbles[4] = (TextView)findViewById(R.id.survey_five);

        surveyStatus[0] = (TextView) findViewById(R.id.survey_one_status);
        surveyStatus[1] = (TextView) findViewById(R.id.survey_two_status);
        surveyStatus[2] = (TextView) findViewById(R.id.survey_three_status);
        surveyStatus[3] = (TextView) findViewById(R.id.survey_four_status);
        surveyStatus[4] = (TextView) findViewById(R.id.survey_five_status);

        for(int i=0; i<5; i++){
            surveyClickAbles[i].setVisibility(View.INVISIBLE);
            surveyStatus[i].setVisibility(View.INVISIBLE);
        }

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/evaluationsList/programs/"
                + userAccountData.getProgram() + "/semester" + userAccountData.getSemester());
    }

    @Override
    public void onClick(View v) {
        String clickedSubject = ((AppCompatTextView) v).getText().toString().toLowerCase();
        Intent intent = new Intent(mContext,StudentEvalutionActivity.class);
        intent.putExtra("subjectName",clickedSubject);
        startActivity(intent);
    }
}
