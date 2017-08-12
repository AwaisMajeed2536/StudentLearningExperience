package iqra.shabeer.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import iqra.shabeer.R;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.AdminAccountBO;
import iqra.shabeer.models.HodAccountBO;
import iqra.shabeer.models.StudentAccountBO;
import iqra.shabeer.models.TeacherAccountBO;

/**
 * Created by Iqra on 2/21/2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private Spinner loginType;
    private Context mContext;
    private String selectedLoginType;
    private EditText email,password;
    private Button loginButton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initialize();
        String userType = UtilHelper.getLoggedInUserType(this);
        if(!TextUtils.isEmpty(userType) && userType.equalsIgnoreCase("student")){
            Intent intent = new Intent(mContext, StudentLandingActivity.class);
            intent.putExtra("userData", UtilHelper.getLoggedInStudent());
            startActivity(intent);
        }
        else if(!TextUtils.isEmpty(userType) && userType.equalsIgnoreCase("teacher")){
            Intent intent = new Intent(mContext, TeacherLandingActivity.class);
            intent.putExtra("teacherData",UtilHelper.getLoggedInTeahcer());
            startActivity(intent);
        }
        else if(!TextUtils.isEmpty(userType) && userType.equalsIgnoreCase("Admin")){
            Intent intent = new Intent(mContext, AdminLandingActivity.class);
            intent.putExtra("adminData",UtilHelper.getLoggedInAdmin());
            startActivity(intent);
        }
        else if(!TextUtils.isEmpty(userType) && userType.equalsIgnoreCase("Hod")){
            //Intent intent = new Intent(mContext, LoginActivity.class);
          //  intent.putExtra("HodData",UtilHelper.getLoggedInHod());
            //startActivity(intent);
        }
        loginButton.setOnClickListener(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.login_options,android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loginType.setAdapter(spinnerAdapter);
        loginType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLoginType = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLoginType = "";
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                UtilHelper.showWaitDialog(LoginActivity.this, "Checking Credentials");
                String id = email.getText().toString().trim();
                switch (selectedLoginType) {
                    case "Student":
                        mRef = mDatabase.getReferenceFromUrl
                                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/student/" + id);
                        getData(mRef, "student");
                        break;
                    case "Teacher":
                        mRef = mDatabase.getReferenceFromUrl
                                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/teacher/" + id);
                        getData(mRef, "teacher");
                        break;
                    case "HOD":
                        mRef = mDatabase.getReferenceFromUrl
                                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/hod/" + id);
                        getData(mRef, "hod");
                        break;
                    case "Admin":
                        String pass = password.getText().toString();
                        String userN = "iqra";
                        String pas = "1234";
                        if (id.equals(userN) && pass.equals(pas)) {
                            Toast.makeText(mContext, "logged inn!", Toast.LENGTH_SHORT).show();
                            AdminAccountBO adminData = new AdminAccountBO();
                            adminData.setFName(id);
                            adminData.setPassword(pass);
                            adminData.getDB_Admin();
                            UtilHelper.createAdminLoginSession(LoginActivity.this, adminData);
                            UtilHelper.setLoggedInUserType(LoginActivity.this, "Admin");
                            UtilHelper.dismissWaitDialog();
                            Intent intent = new Intent(mContext, AdminLandingActivity.class);
                            intent.putExtra("adminData", adminData);
                            startActivity(intent);
                        } else{
                            Toast.makeText(mContext, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
                           // UtilHelper.dismissWaitDialog();
                        }

                }
                break;
        }
    }
    public void getData(DatabaseReference databaseReference, final String accountType){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UtilHelper.dismissWaitDialog();
                if(dataSnapshot.getValue() == null){
                    return;
                }
                String pswrd = password.getText().toString();
                if (accountType.equals("student")) {
                    StudentAccountBO studentData = dataSnapshot.getValue(StudentAccountBO.class);
                    UtilHelper.createStudentLoginSession(LoginActivity.this, studentData);
                    UtilHelper.setLoggedInUserType(LoginActivity.this, "student");
                    String temp = null;
                    if (studentData != null){
                        temp = studentData.getPassword();
                    if (temp.equals(pswrd)) {
                        Intent intent = new Intent(mContext, StudentLandingActivity.class);
                        intent.putExtra("userData", studentData);
                        startActivity(intent);
                    } else
                        Toast.makeText(mContext, "password is incorrect!", Toast.LENGTH_LONG).show();
                }else
                        Toast.makeText(mContext, "Username incorrect!", Toast.LENGTH_SHORT).show();
                } else if(accountType.equals("teacher")) {
                    TeacherAccountBO teacherData = dataSnapshot.getValue(TeacherAccountBO.class);
                    UtilHelper.createTeacherLoginSession(LoginActivity.this, teacherData);
                    UtilHelper.setLoggedInUserType(LoginActivity.this, "teacher");
                    String temp = null;
                    if (teacherData != null)
                        temp = teacherData.getPassword().toString();
                    if (!TextUtils.isEmpty(temp) && temp.equals(pswrd)) {
                        Intent intent = new Intent(mContext, TeacherLandingActivity.class);
                        intent.putExtra("teacherData", teacherData);
                        startActivity(intent);
                    } else {
                        UtilHelper.dismissWaitDialog();
                        Toast.makeText(mContext, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(accountType.equals("hod")) {
                    HodAccountBO hodData = dataSnapshot.getValue(HodAccountBO.class);
                    UtilHelper.createHodLoginSession(LoginActivity.this, hodData);
                    UtilHelper.setLoggedInUserType(LoginActivity.this, "Hod");
                    String temp = null;
                    if (hodData != null)
                        temp = hodData.getPassword().toString();
                    if (!TextUtils.isEmpty(temp) && temp.equals(pswrd)) {
                       // Intent intent = new Intent(mContext, LoginActivity.class);
                      //  intent.putExtra("HodData", hodData);
                      //  startActivity(intent);
                        Toast.makeText(mContext, "hod logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        UtilHelper.dismissWaitDialog();
                        Toast.makeText(mContext, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initialize(){
        mContext = LoginActivity.this;
        loginType = (Spinner) findViewById(R.id.login_type);
        email = (EditText) findViewById(R.id.email_address);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        mDatabase = FirebaseDatabase.getInstance();
    }

}