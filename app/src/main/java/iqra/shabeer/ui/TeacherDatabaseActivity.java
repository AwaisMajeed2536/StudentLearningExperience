package iqra.shabeer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import iqra.shabeer.R;
import iqra.shabeer.adapter.CustomListAdapter;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.TeacherAccountBO;

/**
 * Created by Iqra on 07-Jul-17.
 */

public class TeacherDatabaseActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef,newRef;
    private Context mContext;
    private EditText teacherId;
    private EditText teacherDes;
    private EditText teacherPass;
    private EditText teacherDept;
    private EditText teacherName;
    private Button addBtn;
    private Button loadBtn;
    private Button deltBtn;
    private Button updtBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReferenceFromUrl
                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/teacher/");
        setContentView(R.layout.teacher_database_activity);
       initialize();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_data: {
                String id = teacherId.getText().toString();
                String name = teacherName.getText().toString();
                String des = teacherDes.getText().toString();
                String pas = teacherPass.getText().toString();
                String dept=teacherDept.getText().toString();
                List course = null;
                if(id.length()==0||name.length()==0||pas.length()==0||des.length()==0||dept.length()==0) {
                    Toast.makeText(mContext,"Enter all fields to add data",Toast.LENGTH_SHORT).show();
                }
                else {
                    mRef.child(id).setValue(new TeacherAccountBO(name, dept, des, pas, course));
                    Toast.makeText(mContext, "Data Added!", Toast.LENGTH_SHORT).show();
                    teacherId.setText("");
                    teacherName.setText("");
                    teacherPass.setText("");
                    teacherDept.setText("");
                    teacherDes.setText("");
                }
                break;
            }
            case R.id.load_data: {
                final ArrayList names = new ArrayList<>();
                final ArrayList designation = new ArrayList<>();
                final ArrayList password = new ArrayList<>();
                final ArrayList department = new ArrayList<>();
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
                             TeacherAccountBO teacherData = iterator.next().getValue(TeacherAccountBO.class);
                             names.add(teacherData.getFName());
                             department.add(teacherData.getDepartment());
                             designation.add(teacherData.getDesignation());
                             password.add(teacherData.getPassword());

                             ((CustomListAdapter)(((ListView)findViewById(R.id.teacherList)).getAdapter())).notifyDataSetChanged();

                              }
                     }
                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                     });
                ((ListView)findViewById(R.id.teacherList)).setAdapter(new CustomListAdapter(names,designation,department,password,this));
               break;
            }
            case R.id.delete_data:{
                final String id = teacherId.getText().toString();
                final String name = teacherName.getText().toString();
                final String des = teacherDes.getText().toString();
                final String pas = teacherPass.getText().toString();
                final String dept=teacherDept.getText().toString();
                if(id.length()==0)
                {
                    Toast.makeText(mContext,"please enter id",Toast.LENGTH_SHORT).show();
                }
                else if((!(id.length()==0))&&(!(name.length()==0)||!(pas.length()==0)||(!(des.length()==0))||(!(dept.length()==0))))
                {
                    Toast.makeText(mContext,"Deletion can be done on teacher id",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newRef=mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/userAccounts/teacher/"+id);
                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() == null){
                                UtilHelper.dismissWaitDialog();
                                Toast.makeText(mContext,"Data not available",Toast.LENGTH_SHORT).show();
                                teacherId.setText("");
                                teacherName.setText("");
                                teacherPass.setText("");
                                teacherDept.setText("");
                                teacherDes.setText("");
                                return;

                            }
                            else
                            {
                                mRef.child(id).removeValue();
                                Toast.makeText(mContext,"Data deleted",Toast.LENGTH_SHORT).show();
                                teacherId.setText("");
                                teacherName.setText("");
                                teacherPass.setText("");
                                teacherDept.setText("");
                                teacherDes.setText("");
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
                final String id = teacherId.getText().toString();
                final String name = teacherName.getText().toString();
                final String des = teacherDes.getText().toString();
                final String pas = teacherPass.getText().toString();
                final String dept=teacherDept.getText().toString();
                if(id.length()==0)
                {
                    Toast.makeText(mContext,"please enter id",Toast.LENGTH_SHORT).show();
                }
                else if(name.length()==0&&pas.length()==0&&des.length()==0&&dept.length()==0){
                    Toast.makeText(mContext,"Enter in any field to update data",Toast.LENGTH_SHORT).show();
                  }
                else
                {
                    newRef=mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/userAccounts/teacher/"+id);
                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UtilHelper.dismissWaitDialog();
                            if(dataSnapshot.getValue() == null) {
                                Toast.makeText(mContext,"Data not available",Toast.LENGTH_SHORT).show();
                                teacherId.setText("");
                                teacherName.setText("");
                                teacherPass.setText("");
                                teacherDept.setText("");
                                teacherDes.setText("");
                                return;
                            }
                            Map<String, Object> userUpdates = new HashMap<String, Object>();
                                if(!(name.length()==0)){

                                    userUpdates.put(id+"/fName", name);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    teacherId.setText("");
                                    teacherName.setText("");
                                    teacherPass.setText("");
                                    teacherDept.setText("");
                                    teacherDes.setText("");
                                }
                                else if (!(pas.length()==0)){
                                    userUpdates.put(id+"/password", pas);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    teacherId.setText("");
                                    teacherName.setText("");
                                    teacherPass.setText("");
                                    teacherDept.setText("");
                                    teacherDes.setText("");
                                }
                                else if (!(des.length()==0)){
                                    userUpdates.put(id+"/designation", des);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    teacherId.setText("");
                                    teacherName.setText("");
                                    teacherPass.setText("");
                                    teacherDept.setText("");
                                    teacherDes.setText("");
                                }
                                else if (!(dept.length()==0)){
                                    userUpdates.put(id+"/department", dept);
                                    mRef.updateChildren(userUpdates);
                                    Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                    teacherId.setText("");
                                    teacherName.setText("");
                                    teacherPass.setText("");
                                    teacherDept.setText("");
                                    teacherDes.setText("");
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
        mContext=TeacherDatabaseActivity.this;
          addBtn = (Button) findViewById(R.id.add_data);
          loadBtn = (Button) findViewById(R.id.load_data);
          deltBtn = (Button) findViewById(R.id.delete_data);
          updtBtn =(Button) findViewById(R.id.update_data);
        addBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        deltBtn.setOnClickListener(this);
        updtBtn.setOnClickListener(this);
        teacherName = (EditText)findViewById(R.id.teacherid);
        teacherId = (EditText)findViewById(R.id.teacher_key);
        teacherDes = (EditText)findViewById(R.id.teacher_designation);
        teacherPass = (EditText)findViewById(R.id.teacher_password);
        teacherDept = (EditText)findViewById(R.id.teacher_department);
    }
}

