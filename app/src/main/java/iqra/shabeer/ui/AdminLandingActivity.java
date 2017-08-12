package iqra.shabeer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import iqra.shabeer.R;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.AdminAccountBO;

/**
 * Created by Iqra on 6/16/2017.
 */

public class AdminLandingActivity extends Activity implements View.OnClickListener {
    private AdminAccountBO adminAccountData;
    private Context mContext;
    private TextView adminName;
    private TextView AdminDesignation;
    private Button DB_teacher;
    private Button DB_student;
    private Button DB_hod;
    Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_landing_activity);

        initialize();
        if (bundle!=null) {
            adminAccountData = bundle.getParcelable("adminData");
            adminName.setText(adminAccountData.getFName());
            AdminDesignation.setText(adminAccountData.getDB_Admin());
        }
        }


    private void initialize() {
        mContext = AdminLandingActivity.this;
        bundle = getIntent().getExtras();
        adminName = (TextView) findViewById(R.id.admin_name_display);
        AdminDesignation = (TextView) findViewById(R.id.admin_designation_display);
        DB_teacher = (Button) findViewById(R.id.data_teacher);
        DB_student = (Button) findViewById(R.id.data_student);
        DB_hod = (Button) findViewById(R.id.data_hod);
        DB_teacher.setOnClickListener(this);
        DB_student.setOnClickListener(this);
        DB_hod.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_teacher:
                Intent intent;
                intent=new Intent(mContext , TeacherDatabaseActivity.class);
                startActivity(intent);
                break;
            case R.id.data_student:
                intent=new Intent(mContext , StudentDatabaseActivity.class);
                startActivity(intent);
                break;
            case R.id.data_hod:
                intent= new Intent(mContext , HodDatabaseActivity.class);
                startActivity(intent);
                break;
        }

    }
}
