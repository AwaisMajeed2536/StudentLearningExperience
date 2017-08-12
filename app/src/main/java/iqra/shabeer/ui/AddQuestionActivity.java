package iqra.shabeer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import iqra.shabeer.R;
import iqra.shabeer.utilities.Constants;

/**
 * Created by Iqra on 3/28/2017.
 */

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    protected RadioGroup questionTypeRg;
    protected EditText questionEt;
    protected Button submitQuestionButton;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_question);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_question_button) {
            int checkedRB = questionTypeRg.getCheckedRadioButtonId();
            if (checkedRB == -1) {
                Toast.makeText(this, "Select question type first!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rb = (RadioButton) questionTypeRg.findViewById(checkedRB);
            mRef = FirebaseDatabase.getInstance().getReferenceFromUrl
                    ("https://student-evaluation-system.firebaseio.com/root/surveyQuestions/"
                            + rb.getText().toString().toLowerCase());
            if (rb.getText().toString().toLowerCase().equals("quantitative")) {
                mRef.child(String.valueOf(Constants.QNT_CHILD_COUNT)).setValue(questionEt.getText().toString());
                Toast.makeText(this, "Question Added!", Toast.LENGTH_SHORT).show();
            }
            else if(rb.getText().toString().toLowerCase().equals("qualitative")) {
                mRef.child(String.valueOf(Constants.QLT_CHILD_COUNT)).setValue(questionEt.getText().toString());
                Toast.makeText(this, "Question Added!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Failed to add question!", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this, GenerateSurveyActivity.class));
        }
    }

    private void initView() {
        questionTypeRg = (RadioGroup) findViewById(R.id.question_type_rg);
        questionTypeRg.setOnClickListener(AddQuestionActivity.this);
        questionEt = (EditText) findViewById(R.id.question_et);
        questionEt.setOnClickListener(AddQuestionActivity.this);
        submitQuestionButton = (Button) findViewById(R.id.submit_question_button);
        submitQuestionButton.setOnClickListener(AddQuestionActivity.this);
    }
}
