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
 * Created by Iqra on 08-Jul-17.
 */

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<String> names;
    private ArrayList<String> Designations;
    private ArrayList<String> passwords;
    private ArrayList<String> Department;

    private AppCompatActivity activity;
    private int x=0;


    public CustomListAdapter(ArrayList<String> names, ArrayList<String> Designations,
                             ArrayList<String> Department, ArrayList<String> passwords, AppCompatActivity activity){
        this.names=names;
        this.Designations=Designations;
        this.Department=Department;
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
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.teacher_data_list
                ,viewGroup,false);
        ((TextView)view.findViewById(R.id.teacher_des)).setText(Designations.get(i));
        ((TextView)view.findViewById(R.id.teac_id)).setText(names.get(i));
        ((TextView)view.findViewById(R.id.teacher_pass)).setText((passwords.get(i)));
        ((TextView)view.findViewById(R.id.teacher_dept)).setText(Department.get(i));

        return view;
    }
}