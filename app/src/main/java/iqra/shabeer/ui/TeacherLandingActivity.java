package iqra.shabeer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.fragment.ScoreGraphFragment;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.TeacherAccountBO;

/**
 * Created by Awais on 2/26/2017.
 */

public class TeacherLandingActivity extends AppCompatActivity implements View.OnClickListener {
    private TeacherAccountBO teacherAccountData;
    private Context mContext;
    private TextView teacherName;
    private TextView teacherDesignation;
    private TextView teacherDepartment;
    private LinearLayout listOfCoursesLL;
    private Button generateSurveyButton;
    private Button viewAnalysisReportButton;
    private Button viewGraphButton;
    public static final String COURSE_PASS_KEY = "course_pass_key";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_landing_activity);
        initialize();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            teacherAccountData = bundle.getParcelable("teacherData");
        teacherName.setText(teacherAccountData.getFName());
        teacherDesignation.setText(teacherAccountData.getDesignation());
        teacherDepartment.setText(teacherAccountData.getDepartment());
        List<String> listOfCourses = teacherAccountData.getCourses();
        RadioGroup radioGroup = new RadioGroup(mContext);
        radioGroup.setId(0 + 189464);
        for (int i = 0; i < listOfCourses.size(); i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(listOfCourses.get(i));
            radioButton.setId(i + 189468);
            radioGroup.addView(radioButton);
        }
        listOfCoursesLL.addView(radioGroup);
    }

    private void initialize() {
        mContext = TeacherLandingActivity.this;
        teacherName = (TextView) findViewById(R.id.teacher_name_display);
        teacherDesignation = (TextView) findViewById(R.id.teacher_designation_display);
        teacherDepartment = (TextView) findViewById(R.id.teacher_department_display);
        listOfCoursesLL = (LinearLayout) findViewById(R.id.subject_radio_buttons_ll);
        generateSurveyButton = (Button) findViewById(R.id.generate_survey_button);
        viewAnalysisReportButton = (Button) findViewById(R.id.view_analysis_report_button);
        viewGraphButton = (Button) findViewById(R.id.view_graph_button);
        generateSurveyButton.setOnClickListener(this);
        viewAnalysisReportButton.setOnClickListener(this);
        viewGraphButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RadioGroup rg = (RadioGroup) listOfCoursesLL.findViewById(0 + 189464);
        int selectedCourse = rg.getCheckedRadioButtonId();
        switch (v.getId()) {
            case R.id.generate_survey_button:
                if (selectedCourse == -1) {
                    UtilHelper.showAlertDialog(this, "Select an option", "");
                } else {
                    RadioButton rb = (RadioButton) findViewById(selectedCourse);
                    String selectedCourseName = rb.getText().toString();
                    Intent intent = new Intent(mContext, GenerateSurveyActivity.class);
                    intent.putExtra(COURSE_PASS_KEY, selectedCourseName);
                    startActivity(intent);
                }
                break;
            case R.id.view_analysis_report_button:
                if (selectedCourse == -1) {
                    UtilHelper.showAlertDialog(this, "Select an option", "");
                } else {
                    startActivity(new Intent(TeacherLandingActivity.this, ViewAnalysisActivity.class));
                }
                break;
            case R.id.view_graph_button:
                if (selectedCourse == -1) {
                    UtilHelper.showAlertDialog(this, "Select an option", "");
                } else {
                    startActivity(new Intent(TeacherLandingActivity.this, ViewGraphActivity.class));
                }

        }
    }
}
