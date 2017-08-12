package iqra.shabeer.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import iqra.shabeer.R;

/**
 * Created by Iqra on 09-Jul-17.
 */

public class StudentListAdapter extends BaseAdapter{
    
    private ArrayList<String> names;
    private ArrayList<String> program;
    private ArrayList<String> passwords;
    private ArrayList<Long> semester;

    private AppCompatActivity activity;
    private int x=0;


    public StudentListAdapter(ArrayList<String> names, ArrayList<String> program,
                             ArrayList<Long> semester, ArrayList<String> passwords, AppCompatActivity activity){
        this.names=names;
        this.program=program;
        this.semester=semester;
        this.passwords=passwords;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.student_data_list
                ,viewGroup,false);
        ((TextView)view.findViewById(R.id.student_prog)).setText(program.get(i));
        ((TextView)view.findViewById(R.id.stud_id)).setText(names.get(i));
        ((TextView)view.findViewById(R.id.student_pass)).setText((passwords.get(i)));
        ((TextView)view.findViewById(R.id.student_sem)).setText(Long.toString(semester.get(i)));

        return view;
    }
}