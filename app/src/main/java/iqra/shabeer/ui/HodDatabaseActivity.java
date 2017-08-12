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
import java.util.List;
import java.util.Map;

import iqra.shabeer.R;
import iqra.shabeer.adapter.CustomListAdapter;
import iqra.shabeer.adapter.HodListAdapter;
import iqra.shabeer.helper.UtilHelper;
import iqra.shabeer.models.HodAccountBO;

/**
 * Created by Iqra on 07-Aug-17.
 */

public class HodDatabaseActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef,newRef;
    private Context mContext;
    private EditText hodId;
    private EditText hodPass;
    private EditText hodDept;
    private EditText hodName;
    private Button addBtn;
    private Button loadBtn;
    private Button deltBtn;
    private Button updtBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReferenceFromUrl
                ("https://student-evaluation-system.firebaseio.com/root/userAccounts/hod/");
        setContentView(R.layout.hod_database_activity);
        initialize();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_data: {
                String id = hodId.getText().toString();
                String name = hodName.getText().toString();
                String pas = hodPass.getText().toString();
                String dept=hodDept.getText().toString();
                List course = null;
                if(id.length()==0||name.length()==0||pas.length()==0||dept.length()==0) {
                    Toast.makeText(mContext,"Enter all fields to add data",Toast.LENGTH_SHORT).show();
                }
                else {
                    mRef.child(id).setValue(new HodAccountBO(name, dept , pas));
                    Toast.makeText(mContext, "Data Added!", Toast.LENGTH_SHORT).show();
                    hodId.setText("");
                    hodName.setText("");
                    hodPass.setText("");
                    hodDept.setText("");
                }
                break;
            }
            case R.id.load_data: {
                final ArrayList names = new ArrayList<>();
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
                            HodAccountBO hodData = iterator.next().getValue(HodAccountBO.class);
                            names.add(hodData.getFName());
                            department.add(hodData.getDepartment());
                            password.add(hodData.getPassword());

                            ((HodListAdapter)(((ListView)findViewById(R.id.hodList)).getAdapter())).notifyDataSetChanged();

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ((ListView)findViewById(R.id.hodList)).setAdapter(new HodListAdapter(names,department,password,this));
                break;
            }
            case R.id.delete_data:{
                final String id = hodId.getText().toString();
                final String name = hodName.getText().toString();
                final String pas = hodPass.getText().toString();
                final String dept=hodDept.getText().toString();
                if(id.length()==0)
                {
                    Toast.makeText(mContext,"please enter id",Toast.LENGTH_SHORT).show();
                }
                else if((!(id.length()==0))&&(!(name.length()==0)||!(pas.length()==0)||(!(dept.length()==0))))
                {
                    Toast.makeText(mContext,"Deletion can be done on hod id",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newRef=mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/userAccounts/hod/"+id);
                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() == null){
                                UtilHelper.dismissWaitDialog();
                                Toast.makeText(mContext,"Data not available",Toast.LENGTH_SHORT).show();
                                hodId.setText("");
                                hodName.setText("");
                                hodPass.setText("");
                                hodDept.setText("");
                                return;

                            }
                            else
                            {
                                mRef.child(id).removeValue();
                                Toast.makeText(mContext,"Data deleted",Toast.LENGTH_SHORT).show();
                                hodId.setText("");
                                hodName.setText("");
                                hodPass.setText("");
                                hodDept.setText("");
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
                final String id = hodId.getText().toString();
                final String name = hodName.getText().toString();
                final String pas = hodPass.getText().toString();
                final String dept=hodDept.getText().toString();
                if(id.length()==0)
                {
                    Toast.makeText(mContext,"please enter id",Toast.LENGTH_SHORT).show();
                }
                else if(name.length()==0&&pas.length()==0&&dept.length()==0){
                    Toast.makeText(mContext,"Enter in any field to update data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newRef=mDatabase.getReferenceFromUrl("https://student-evaluation-system.firebaseio.com/root/userAccounts/hod/"+id);
                    newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UtilHelper.dismissWaitDialog();
                            if(dataSnapshot.getValue() == null) {
                                Toast.makeText(mContext,"Data not available",Toast.LENGTH_SHORT).show();
                                hodId.setText("");
                                hodName.setText("");
                                hodPass.setText("");
                                hodDept.setText("");
                                return;
                            }
                            Map<String, Object> userUpdates = new HashMap<String, Object>();
                            if(!(name.length()==0)){

                                userUpdates.put(id+"/fName", name);
                                mRef.updateChildren(userUpdates);
                                Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                hodId.setText("");
                                hodName.setText("");
                                hodPass.setText("");
                                hodDept.setText("");
                            }
                            else if (!(pas.length()==0)){
                                userUpdates.put(id+"/password", pas);
                                mRef.updateChildren(userUpdates);
                                Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                hodId.setText("");
                                hodName.setText("");
                                hodPass.setText("");
                                hodDept.setText("");
                            
                            }
                            else if (!(dept.length()==0)){
                                userUpdates.put(id+"/department", dept);
                                mRef.updateChildren(userUpdates);
                                Toast.makeText(mContext,"Data updated",Toast.LENGTH_SHORT).show();
                                hodId.setText("");
                                hodName.setText("");
                                hodPass.setText("");
                                hodDept.setText("");
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
        mContext=HodDatabaseActivity.this;
        addBtn = (Button) findViewById(R.id.add_data);
        loadBtn = (Button) findViewById(R.id.load_data);
        deltBtn = (Button) findViewById(R.id.delete_data);
        updtBtn =(Button) findViewById(R.id.update_data);
        addBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        deltBtn.setOnClickListener(this);
        updtBtn.setOnClickListener(this);
        hodName = (EditText)findViewById(R.id.hod_id);
        hodId = (EditText)findViewById(R.id.hod_key);
        hodPass = (EditText)findViewById(R.id.hod_password);
        hodDept = (EditText)findViewById(R.id.hod_department);
    }
}

