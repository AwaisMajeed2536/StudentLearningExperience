package iqra.shabeer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import iqra.shabeer.R;
import iqra.shabeer.adapter.StudentListAdapter;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.StudentAccountBO;

/**
 * Created by Iqra on 09-Jul-17.
 */
public class StudentDatabaseActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef,newRef;
    private Context mContext;
    private EditText studentId;
    private EditText studentSem;
    private EditText studentPass;
    private EditText studentProg;
    private EditText studentName;
    private Button addBtn;
    private Button loadBtn;
    private Button deltBtn;
    private Button updtBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReferenceFromUrl
                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/student/");
        setContentView(R.layout.student_database_activity);
        initialize();
    }

    @Override
    public void onClick(View v) {
        final String id = studentId.getText().toString();
        final String name = studentName.getText().toString();
        final String prog = studentProg.getText().toString();
        final String pas = studentPass.getText().toString();
        final String sem= studentSem.getText().toString();
        switch (v.getId()) {

            case R.id.add_data: {
               if(id.length()==0||name.length()==0||pas.length()==0||prog.length()==0||sem.equals("")) {
                    Toast.makeText(mContext,"Enter all fields to add data",Toast.LENGTH_SHORT).show();
                }
                else {
                    mRef.child(id).setValue(new StudentAccountBO(name, pas ,prog,Long.parseLong(sem)));
                    Toast.makeText(mContext, "Data Added!", Toast.LENGTH_SHORT).show();
                    studentId.setText("");
                    studentName.setText("");
                    studentPass.setText("");
                    studentProg.setText("");
                    studentSem.setText("");
                }
                break;
            }
            case R.id.load_data: {
                final ArrayList names = new ArrayList<>();
                final ArrayList semester = new ArrayList<>();
                final ArrayList password = new ArrayList<>();
                final ArrayList program = new ArrayList<>();
                mRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        UtilHelper.dismissWaitDialog();
                        if(dataSnapshot.getValue() == null){
                            return;
                        }
                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                        while (iterator.hasNext()){
                            StudentAccountBO studentData = iterator.next().getValue(StudentAccountBO.class);
                            names.add(studentData.getFName());
                            program.add(studentData.getProgram());
                            semester.add(studentData.getSemester());
                            password.add(studentData.getPassword());

                            ((StudentListAdapter)(((ListView)findViewById(R.id.studentList)).getAdapter())).notifyDataSetChanged();

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ((ListView)findViewById(R.id.studentList)).setAdapter(new StudentListAdapter(names,program,semester,password,this));
                break;
            }
            case R.id.delete_data:{
                if(id.length()==0)
                {
                    Toast.makeText(mContext,"please enter id",Toast.LENGTH_SHORT).show();
                }
                else if((!(id.length()==0))&&(!(name.length()==0)||!(pas.length()==0)||(!(sem.equals("")))||(!(prog.length()==0))))
                {
                    Toast.makeText(mContext,"Deletion can be done on student id",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newRef=mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/userAccounts/student/"+id);
                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UtilHelper.dismissWaitDialog();
                            if(dataSnapshot.getValue() == null){
                                Toast.makeText(mContext,"Data not available",Toast.LENGTH_SHORT).show();
                                studentId.setText("");
                                studentName.setText("");
                                studentPass.setText("");
                                studentProg.setText("");
                                studentSem.setText("");
                                return;
                            }
                            else
                            {
                                mRef.child(id).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                break;
            }
            case R.id.update_data:{
                if(id.length()==0)
                {
                    Toast.makeText(mContext,"please enter id",Toast.LENGTH_SHORT).show();
                }
                else if(name.length()==0&&pas.length()==0&&sem.equals("")&&prog.length()==0){
                    Toast.makeText(mContext,"Enter in any field to update data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newRef=mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/userAccounts/student/"+id);

                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UtilHelper.dismissWaitDialog();
                            if(dataSnapshot.getValue() == null){

                                Toast.makeText(mContext,"Data not available",Toast.LENGTH_SHORT).show();
                                studentId.setText("");
                                studentName.setText("");
                                studentPass.setText("");
                                studentProg.setText("");
                                studentSem.setText("");
                                return;
                            }
                            else{
                                Map<String, Object> userUpdates = new HashMap<String, Object>();
                                if(!(name.length()==0)){

                                    userUpdates.put(id+"/fName", name);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    studentId.setText("");
                                    studentName.setText("");
                                    studentPass.setText("");
                                    studentProg.setText("");
                                    studentSem.setText("");
                                }
                                else if (!(pas.length()==0)){
                                    userUpdates.put(id+"/password", pas);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    studentId.setText("");
                                    studentName.setText("");
                                    studentPass.setText("");
                                    studentProg.setText("");
                                    studentSem.setText("");
                                }
                                else if (!(sem.equals(""))){
                                    userUpdates.put(id+"/semester", sem);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    studentId.setText("");
                                    studentName.setText("");
                                    studentPass.setText("");
                                    studentProg.setText("");
                                    studentSem.setText("");
                                }
                                else if (!(prog.length()==0)){
                                    userUpdates.put(id+"/program", prog);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    studentId.setText("");
                                    studentName.setText("");
                                    studentPass.setText("");
                                    studentProg.setText("");
                                    studentSem.setText("");
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                break;
            }
        }
    }
    public void initialize(){
        mContext=StudentDatabaseActivity.this;
        addBtn = (Button) findViewById(R.id.add_data);
        loadBtn = (Button) findViewById(R.id.load_data);
        deltBtn = (Button) findViewById(R.id.delete_data);
        updtBtn =(Button) findViewById(R.id.update_data);
        addBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        deltBtn.setOnClickListener(this);
        updtBtn.setOnClickListener(this);
        studentName = (EditText)findViewById(R.id.studentid);
        studentId = (EditText)findViewById(R.id.student_key);
        studentProg = (EditText)findViewById(R.id.student_program);
        studentPass = (EditText)findViewById(R.id.student_password);
        studentSem = (EditText)findViewById(R.id.student_semester);
    }
}

