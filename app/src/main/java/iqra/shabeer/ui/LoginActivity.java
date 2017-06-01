package iqra.shabeer.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import iqra.shabeer.models.StudentAccountBO;
import iqra.shabeer.models.TeacherAccountBO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner loginType;
    private Context mContext;
    private String selectedLoginType;
    private EditText email,password;
    private Button loginButton;
    private TextView signUp,forgotPassword;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initialize();
        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

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
        switch (v.getId()){
            case R.id.login_button:
                String id = email.getText().toString();
                switch (selectedLoginType){
                    case "Student":
                        mRef = mDatabase.getReferenceFromUrl
                                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/student/"+id);
                        getData(mRef,"student");
                        break;
                    case "Teacher":
                        mRef = mDatabase.getReferenceFromUrl
                                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/teacher/"+id);
                        getData(mRef,"teacher");
                        break;
                    case "HOD":
                        mRef = mDatabase.getReferenceFromUrl
                                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/hod/"+id);
                        getData(mRef,"hod");
                        break;
                }
                break;
            case R.id.sign_up:
                //Todo signup
                break;
            case R.id.forgot_password:
                //todo forgot request
                break;
            default:
                break;
        }
    }
    public void getData(DatabaseReference databaseReference, final String accountType){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pswrd = password.getText().toString();
                if (accountType.equals("student")) {
                    StudentAccountBO studentData = dataSnapshot.getValue(StudentAccountBO.class);
                    String temp = null;
                    if (studentData != null)
                        temp = studentData.getPassword();
                    if (temp.equals(pswrd)) {
                        Intent intent = new Intent(mContext, StudentLandingActivity.class);
                        intent.putExtra("userData", studentData);
                        startActivity(intent);
                    } else
                        Toast.makeText(mContext, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
                } else if(accountType.equals("teacher")){
                    TeacherAccountBO teacherData = dataSnapshot.getValue(TeacherAccountBO.class);
                    String temp = null;
                    if (teacherData != null)
                        temp = teacherData.getPassword().toString();
                    if (!TextUtils.isEmpty(temp) && temp.equals(pswrd)){
                        Intent intent = new Intent(mContext, TeacherLandingActivity.class);
                        intent.putExtra("teacherData",teacherData);
                        startActivity(intent);
                    } else
                        Toast.makeText(mContext, "Username or password is incorrect!",Toast.LENGTH_LONG).show();
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
        signUp = (TextView) findViewById(R.id.sign_up);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        mDatabase = FirebaseDatabase.getInstance();
    }

}