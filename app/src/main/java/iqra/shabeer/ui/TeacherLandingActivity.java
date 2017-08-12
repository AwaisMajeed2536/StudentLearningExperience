package iqra.shabeer.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.fragment.ScoreGraphFragment;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.TeacherAccountBO;
import iqra.shabeer.utilities.Constants;

import static android.R.id.list;

/**
 * Created by Iqra on 2/26/2017.
 */

public class TeacherLandingActivity extends AppCompatActivity implements View.OnClickListener {
    private TeacherAccountBO teacherAccountData;
    private Context mContext;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private TextView teacherName;
    private TextView teacherDesignation;
    private TextView teacherDepartment;
    private LinearLayout listOfCoursesLL;
    private Button generateSurveyButton;
    private Button viewAnalysisReportButton;
    private Button viewGraphButton;
    public static final String COURSE_PASS_KEY = "course_pass_key";
    public String[] array;
    private HashMap<String, List<String>> emailList = new HashMap<String, List<String>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_landing_activity);
        initialize();
        mDatabase = FirebaseDatabase.getInstance();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.send_email, menu);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RadioGroup rg = (RadioGroup) listOfCoursesLL.findViewById(0 + 189464);
        int selectedCourse = rg.getCheckedRadioButtonId();
        if (selectedCourse == -1) {
            UtilHelper.showAlertDialog(this, "Select an option", "");
        }
        else {
            if (item.getItemId() == R.id.reminder_emails) {

                Log.i("Send email", "");
              //  String[] TO = {"iqrashabbir176@gmail.com"};
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                RadioButton rb = (RadioButton) findViewById(selectedCourse);
                final String selectedCourseName = rb.getText().toString();
                mRef = mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/studentCourse/");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        emailList = (HashMap<String, List<String>>) dataSnapshot.getValue();
                        List<String> emails = emailList.get(selectedCourseName);
                        Constants.EMAIL_COUNT = emails.size();

                        array = new String[emails.size()];
                        int i=0;
                        for(String s: emails){
                            array[i++] = s;
                        }
                        for(i = 0; i < array.length; i++)
                        {
                            Log.i("String is", (String)array[i]);
                            //String[] str = (String[])to[i];
                            emailIntent.putExtra(Intent.EXTRA_EMAIL,",'" +array[i] + "'");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");



                //emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{String.valueOf(array)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reminder Emails");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Your survey has been created for course " + selectedCourseName + ".Please give your feedback");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(TeacherLandingActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
